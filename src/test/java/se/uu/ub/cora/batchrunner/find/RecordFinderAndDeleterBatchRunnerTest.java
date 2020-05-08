package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.remove.RecordDeleterSpy;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.javaclient.CoraClientConfig;

public class RecordFinderAndDeleterBatchRunnerTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<RecordFinderAndDeleterBatchRunner> constructor = RecordFinderAndDeleterBatchRunner.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "someUserId", "someAppToken", "appTokenVerifierUrl",
				"http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.find.RecordFinderSpy",
				"se.uu.ub.cora.batchrunner.find.RecordsSeparatorSpy",
				"se.uu.ub.cora.batchrunner.remove.RecordDeleterSpy",
				"se.uu.ub.cora.batchrunner.CoraClientFactorySpy", "someType", "someId" };

		RecordFinderAndDeleterBatchRunner.main(args);

		RecordFinderSpy finderSpy = (RecordFinderSpy) RecordFinderAndDeleterBatchRunner.finder;
		assertCorrectFinder(finderSpy, args);

		RecordsSeparatorSpy separatorSpy = (RecordsSeparatorSpy) RecordFinderAndDeleterBatchRunner.separator;
		assertCorrectSeparator(args, finderSpy, separatorSpy);

		assertCorrectDeleter(args, separatorSpy);
	}

	private void assertCorrectFinder(RecordFinderSpy finderSpy, String[] args) {
		CoraClientConfig coraClientConfig = finderSpy.coraClientConfig;
		assertCorrectClientConfig(args, coraClientConfig);
		assertTrue(finderSpy.coraClientFactory instanceof CoraClientFactorySpy);

		assertTrue(finderSpy.findRecordsCalled);
		assertEquals(finderSpy.recordIdentifier.type, args[8]);
		assertEquals(finderSpy.recordIdentifier.id, args[9]);
	}

	private void assertCorrectClientConfig(String[] args, CoraClientConfig coraClientConfig) {
		assertEquals(coraClientConfig.userId, args[0]);
		assertEquals(coraClientConfig.appToken, args[1]);
		assertEquals(coraClientConfig.appTokenVerifierUrl, args[2]);
		assertEquals(coraClientConfig.coraUrl, args[3]);
	}

	private void assertCorrectSeparator(String[] args, RecordFinderSpy finderSpy,
			RecordsSeparatorSpy separatorSpy) {
		CoraClientConfig coraClientConfig = separatorSpy.coraClientConfig;
		assertCorrectClientConfig(args, coraClientConfig);
		assertTrue(separatorSpy.coraClientFactory instanceof CoraClientFactorySpy);
		assertTrue(separatorSpy.sortOutRecordsCalled);

		assertResultFromFinderIsSentToSeparator(finderSpy, separatorSpy);
	}

	private void assertResultFromFinderIsSentToSeparator(RecordFinderSpy finderSpy,
			RecordsSeparatorSpy separatorSpy) {
		List<RecordIdentifier> identifiersFromSeparator = separatorSpy.identifiers;
		List<RecordIdentifier> identifiersFromFinder = finderSpy.recordIdentifiers;
		for (int i = 0; i < identifiersFromSeparator.size(); i++) {
			assertEquals(identifiersFromSeparator.get(i), identifiersFromFinder.get(i));
		}
	}

	private void assertCorrectDeleter(String[] args, RecordsSeparatorSpy separatorSpy) {
		RecordDeleterSpy deleterSpy = (RecordDeleterSpy) RecordFinderAndDeleterBatchRunner.deleter;
		CoraClientConfig coraClientConfig = deleterSpy.coraClientConfig;
		assertCorrectClientConfig(args, coraClientConfig);
		assertTrue(deleterSpy.coraClientFactory instanceof CoraClientFactorySpy);

		assertTrue(deleterSpy.deleteByIdentifiersWasCalled);
		assertResultFromSeparatorIsSentToDeleter(separatorSpy, deleterSpy);
	}

	private void assertResultFromSeparatorIsSentToDeleter(RecordsSeparatorSpy separatorSpy,
			RecordDeleterSpy deleterSpy) {
		List<RecordIdentifier> recordIdentifiersFromSeparator = separatorSpy.resultHolder.recordIdentifiers;
		List<RecordIdentifier> identifiersFromDeleter = deleterSpy.identifiers;
		for (int i = 0; i < identifiersFromDeleter.size(); i++) {
			assertEquals(identifiersFromDeleter.get(i), recordIdentifiersFromSeparator.get(i));
		}
	}

}

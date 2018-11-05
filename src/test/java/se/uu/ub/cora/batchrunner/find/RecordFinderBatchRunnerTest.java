package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

import se.uu.ub.cora.client.CoraClientConfig;

public class RecordFinderBatchRunnerTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<RecordFinderBatchRunner> constructor = RecordFinderBatchRunner.class
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
				"se.uu.ub.cora.batchrunner.CoraClientFactorySpy", "someType", "someId" };

		RecordFinderBatchRunner.main(args);

		RecordFinderSpy finderSpy = (RecordFinderSpy) RecordFinderBatchRunner.finder;
		CoraClientConfig coraClientConfig = finderSpy.coraClientConfig;
		assertEquals(coraClientConfig.userId, args[0]);
		assertEquals(coraClientConfig.appToken, args[1]);
		assertEquals(coraClientConfig.appTokenVerifierUrl, args[2]);
		assertEquals(coraClientConfig.coraUrl, args[3]);

		assertTrue(finderSpy.findRecordsCalled);
		assertEquals(finderSpy.recordIdentifier.type, args[6]);
		assertEquals(finderSpy.recordIdentifier.id, args[7]);
	}

}

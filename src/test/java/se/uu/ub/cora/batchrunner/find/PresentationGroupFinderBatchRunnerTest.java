package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

import org.testng.annotations.Test;

public class PresentationGroupFinderBatchRunnerTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<PresentationGroupFinderBatchRunner> constructor = PresentationGroupFinderBatchRunner.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "se.uu.ub.cora.batchrunner.find.FinderSpy",
				"se.uu.ub.cora.batchrunner.find.PGroupFinderSpy",
				"http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy" };

		PresentationGroupFinderBatchRunner.main(args);

		FinderSpy recordTypeFinderSpy = (FinderSpy) PresentationGroupFinderBatchRunner.recordTypeFinder;
		assertTrue(recordTypeFinderSpy.findRecordsCalled);
		assertEquals(recordTypeFinderSpy.url,
				"http://localhost:8080/therest/rest/record/recordType");
		assertTrue(recordTypeFinderSpy.httpHandlerFactory instanceof HttpHandlerFactorySpy);

		List<String> returnedRecordTypes = recordTypeFinderSpy.ids;

		PGroupFinderSpy pGroupFinderSpy = (PGroupFinderSpy) PresentationGroupFinderBatchRunner.pGroupFinder;
		assertEquals(pGroupFinderSpy.recordTypeNames, returnedRecordTypes);
		assertTrue(pGroupFinderSpy.findRecordsCalled);
		assertTrue(pGroupFinderSpy.httpHandlerFactory instanceof HttpHandlerFactorySpy);
		assertEquals(pGroupFinderSpy.url,
				"http://localhost:8080/therest/rest/record/presentationGroup");

	}

	@Test
	public void testMainMethodNoRecordsFound() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "se.uu.ub.cora.batchrunner.find.FinderNoRecordsSpy",
				"se.uu.ub.cora.batchrunner.find.PGroupFinderSpy",
				"http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy" };

		PresentationGroupFinderBatchRunner.main(args);
		FinderNoRecordsSpy finderSpy = (FinderNoRecordsSpy) PresentationGroupFinderBatchRunner.recordTypeFinder;
		assertTrue(finderSpy.findRecordCalled);
		assertEquals(finderSpy.url, "http://localhost:8080/therest/rest/record/recordType");
	}

}

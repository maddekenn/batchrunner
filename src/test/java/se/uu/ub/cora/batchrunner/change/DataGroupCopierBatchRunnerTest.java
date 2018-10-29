package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.FinderSpy;

public class DataGroupCopierBatchRunnerTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<DataGroupCopierBatchRunner> constructor = DataGroupCopierBatchRunner.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "se.uu.ub.cora.batchrunner.change.DataCopierSpy",
				"http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy",
				"se.uu.ub.cora.batchrunner.find.FinderSpy" };

		DataGroupCopierBatchRunner.main(args);
		FinderSpy finderSpy = (FinderSpy) DataGroupCopierBatchRunner.finder;
		assertTrue(finderSpy.findRecordsCalled);
		assertEquals(finderSpy.url, "http://localhost:8080/therest/rest/record/recordType");

		DataCopierSpy dataCopierSpy = (DataCopierSpy) DataGroupCopierBatchRunner.dataCopier;
		assertEquals(dataCopierSpy.types.get(0), "presentationGroup");
		assertEquals(dataCopierSpy.ids.get(0), "someIdFormPGroup");
		assertEquals(dataCopierSpy.newIds.get(0), "someIdPGroup");
		assertEquals(dataCopierSpy.types.get(1), "presentationGroup");
		assertEquals(dataCopierSpy.ids.get(1), "someIdFormNewPGroup");
		assertEquals(dataCopierSpy.newIds.get(1), "someIdNewPGroup");
		assertEquals(dataCopierSpy.types.get(2), "presentationGroup");
		assertEquals(dataCopierSpy.ids.get(2), "someIdViewPGroup");
		assertEquals(dataCopierSpy.newIds.get(2), "someIdOutputPGroup");

	}

	@Test
	public void testMainMethodNoRecordsFound() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "se.uu.ub.cora.batchrunner.change.DataCopierSpy",
				"http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy",
				"se.uu.ub.cora.batchrunner.find.FinderSpy" };

		DataGroupCopierBatchRunner.main(args);
		FinderSpy finderSpy = (FinderSpy) DataGroupCopierBatchRunner.finder;
		assertTrue(finderSpy.findRecordsCalled);
		assertEquals(finderSpy.url, "http://localhost:8080/therest/rest/record/recordType");

	}

}

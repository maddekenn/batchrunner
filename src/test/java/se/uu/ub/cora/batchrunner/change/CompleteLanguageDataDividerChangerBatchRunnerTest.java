package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.batchrunner.find.HttpHandlerSpy;

public class CompleteLanguageDataDividerChangerBatchRunnerTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<CompleteLanguageDataDividerChangerBatchRunner> constructor = CompleteLanguageDataDividerChangerBatchRunner.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "se.uu.ub.cora.batchrunner.change.DataUpdaterSpy",
				"http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy",
				"se.uu.ub.cora.batchrunner.find.FinderSpy", "someNewDataDivider" };

		CompleteLanguageDataDividerChangerBatchRunner.main(args);

		DataUpdaterSpy dataUpdater = (DataUpdaterSpy) CompleteLanguageDataDividerChangerBatchRunner.dataUpdater;
		HttpHandlerFactorySpy httpHandlerFactory = (HttpHandlerFactorySpy) CompleteLanguageDataDividerChangerBatchRunner.httpHandlerFactory;

		HttpHandlerSpy httpHandlerReadItemCollection = httpHandlerFactory.httpHandlerSpies.get(0);
		assertEquals(httpHandlerReadItemCollection.requestMethod, "GET");
		assertTrue(httpHandlerReadItemCollection.urlString
				.endsWith("/metadataItemCollection/completeLanguageCollection"));

		assertEquals(dataUpdater.types.size(), 2);
		assertEquals(dataUpdater.types.get(1), "genericCollectionItem");
		assertEquals(dataUpdater.types.get(0), "genericCollectionItem");

		assertEquals(dataUpdater.recordIds.size(), 2);
		assertEquals(dataUpdater.recordIds.get(0), "svItem");
		assertEquals(dataUpdater.recordIds.get(1), "enItem");

		assertEquals(dataUpdater.dataDividers.size(), 2);
		assertEquals(dataUpdater.dataDividers.get(0), "someNewDataDivider");
		assertEquals(dataUpdater.dataDividers.get(1), "someNewDataDivider");
		// assertTrue(httpHandlerUpdateItem.urlString
		// .endsWith("/metadataItemCollection/completeLanguageCollection"));

		// FinderSpy finderSpy = (FinderSpy) DataGroupCopierBatchRunner.finder;
		// assertTrue(finderSpy.findRecordsCalled);
		// assertEquals(finderSpy.url,
		// "http://localhost:8080/therest/rest/record/recordType");

		DataUpdaterSpy dataUpdaterSpy = (DataUpdaterSpy) CompleteLanguageDataDividerChangerBatchRunner.dataUpdater;
		// assertEquals(dataUpdaterSpy.types.get(0), "presentationGroup");
		// assertEquals(dataCopierSpy.ids.get(0), "someIdFormPGroup");
		// assertEquals(dataCopierSpy.newIds.get(0), "someIdPGroup");
		// assertEquals(dataCopierSpy.types.get(1), "presentationGroup");
		// assertEquals(dataCopierSpy.ids.get(1), "someIdFormNewPGroup");
		// assertEquals(dataCopierSpy.newIds.get(1), "someIdNewPGroup");
		// assertEquals(dataCopierSpy.types.get(2), "presentationGroup");
		// assertEquals(dataCopierSpy.ids.get(2), "someIdViewPGroup");
		// assertEquals(dataCopierSpy.newIds.get(2), "someIdOutputPGroup");

	}

}

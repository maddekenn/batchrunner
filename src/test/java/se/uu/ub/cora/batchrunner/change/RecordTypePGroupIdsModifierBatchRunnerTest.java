package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.FinderSpy;

public class RecordTypePGroupIdsModifierBatchRunnerTest {

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
		String args[] = new String[] { "http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy",
				"se.uu.ub.cora.batchrunner.find.FinderSpy",
				"se.uu.ub.cora.batchrunner.change.ModifierSpy" };

		RecordTypePGroupIdsModifierBatchRunner.main(args);
		FinderSpy finderSpy = (FinderSpy) RecordTypePGroupIdsModifierBatchRunner.finder;
		assertTrue(finderSpy.findRecordsCalled);
		assertEquals(finderSpy.url, "http://localhost:8080/therest/rest/record/recordType");

		ModifierSpy modifierSpy = (ModifierSpy) RecordTypePGroupIdsModifierBatchRunner.modifier;
		assertEquals(modifierSpy.recordTypes.get(0), "someId");

	}

	@Test
	public void testMainMethodErrorWhenModifyingFound()
			throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
			InstantiationException, IllegalAccessException {

		String args[] = new String[] { "http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy",
				"se.uu.ub.cora.batchrunner.find.FinderSpy",
				"se.uu.ub.cora.batchrunner.change.ModifierWithErrorSpy" };

		RecordTypePGroupIdsModifierBatchRunner.main(args);
		FinderSpy finderSpy = (FinderSpy) RecordTypePGroupIdsModifierBatchRunner.finder;
		assertTrue(finderSpy.findRecordsCalled);
		assertEquals(finderSpy.url, "http://localhost:8080/therest/rest/record/recordType");

	}

}

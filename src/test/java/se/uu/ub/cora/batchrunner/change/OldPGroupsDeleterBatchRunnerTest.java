package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.FinderSpy;
import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;

public class OldPGroupsDeleterBatchRunnerTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<OldPGroupsDeleterBatchRunner> constructor = OldPGroupsDeleterBatchRunner.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "se.uu.ub.cora.batchrunner.change.DeleterSpy",
				"http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy",
				"se.uu.ub.cora.batchrunner.find.FinderSpy" };

		OldPGroupsDeleterBatchRunner.main(args);
		FinderSpy finderSpy = (FinderSpy) OldPGroupsDeleterBatchRunner.finder;
		DeleterSpy deleterSpy = (DeleterSpy) OldPGroupsDeleterBatchRunner.deleter;

		assertTrue(finderSpy.findRecordsCalled);

		assertTrue(deleterSpy.deletePGroupsWasCalled);
		assertEquals(deleterSpy.groupIdsSentToDelete.size(), 3);
		assertEquals(deleterSpy.url, "http://localhost:8080/therest/rest/record/");
		assertTrue(deleterSpy.httpHandlerFactory instanceof HttpHandlerFactorySpy);
		assertEquals(deleterSpy.groupIdsSentToDelete.get(0), "someIdFormPGroup");
		assertEquals(deleterSpy.groupIdsSentToDelete.get(1), "someIdFormNewPGroup");
		assertEquals(deleterSpy.groupIdsSentToDelete.get(2), "someIdViewPGroup");

	}

}

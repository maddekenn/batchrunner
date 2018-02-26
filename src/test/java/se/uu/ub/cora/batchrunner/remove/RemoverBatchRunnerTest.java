package se.uu.ub.cora.batchrunner.remove;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

public class RemoverBatchRunnerTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<RemoverBatchRunner> constructor = RemoverBatchRunner.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "se.uu.ub.cora.batchrunner.remove.RemoverSpy",
				"se.uu.ub.cora.batchrunner.remove.PresentationsFinderSpy",
				"http://localhost:8080/therest/rest/record/presentationGroup",
				"se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy" };

		RemoverBatchRunner.main(args);
		RemoverSpy removerSpy = (RemoverSpy) RemoverBatchRunner.remover;
		assertTrue(removerSpy.removeRecordsWasCalled);
		assertEquals(removerSpy.urlString,
				"http://localhost:8080/therest/rest/record/presentationGroup");
		assertEquals(removerSpy.finder.getClass().getName(),
				"se.uu.ub.cora.batchrunner.remove.PresentationsFinderSpy");
	}

	@Test
	public void testMainMethodNoRecordsFound() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "se.uu.ub.cora.batchrunner.remove.RemoverNoRecordsFoundSpy",
				"se.uu.ub.cora.batchrunner.remove.PresentationsFinderSpy",
				"http://localhost:8080/therest/rest/record/presentationGroup",
				"se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy" };

		RemoverBatchRunner.main(args);
		RemoverNoRecordsFoundSpy removerSpy = (RemoverNoRecordsFoundSpy) RemoverBatchRunner.remover;
		assertTrue(removerSpy.removeRecordsWasCalled);
		assertEquals(removerSpy.urlString,
				"http://localhost:8080/therest/rest/record/presentationGroup");
		;
	}

}

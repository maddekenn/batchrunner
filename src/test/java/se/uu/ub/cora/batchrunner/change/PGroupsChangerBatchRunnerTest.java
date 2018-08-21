package se.uu.ub.cora.batchrunner.change;

import org.testng.annotations.Test;
import se.uu.ub.cora.batchrunner.find.FinderBatchRunner;
import se.uu.ub.cora.batchrunner.find.FinderNoRecordsSpy;
import se.uu.ub.cora.batchrunner.find.FinderSpy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class PGroupsChangerBatchRunnerTest {

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Constructor<PGroupsChangerBatchRunner> constructor = PGroupsChangerBatchRunner.class
                .getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        String args[] = new String[] {
                "se.uu.ub.cora.batchrunner.find.FinderSpy", "http://localhost:8080/therest/rest/record/",
                "se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy", "se.uu.ub.cora.batchrunner.change.ModifierSpy"};

        PGroupsChangerBatchRunner.main(args);
        FinderSpy finderSpy = (FinderSpy) PGroupsChangerBatchRunner.finder;
        ModifierSpy modifierSpy = (ModifierSpy) PGroupsChangerBatchRunner.modifier;

        assertTrue(finderSpy.findRecordsCalled);
        assertEquals(finderSpy.url, "http://localhost:8080/therest/rest/record/recordType");
        assertEquals(modifierSpy.recordTypes.get(0), "someId");

    }

    @Test
    public void testMainMethodNoRecordsFound() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        String args[] = new String[] {
                "se.uu.ub.cora.batchrunner.find.FinderNoRecordsSpy", "http://localhost:8080/therest/rest/record/",
                "se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy", "se.uu.ub.cora.batchrunner.change.ModifierSpy"};

        PGroupsChangerBatchRunner.main(args);
        FinderNoRecordsSpy finderSpy = (FinderNoRecordsSpy) PGroupsChangerBatchRunner.finder;
        assertTrue(finderSpy.findRecordCalled);
        assertEquals(finderSpy.url, "http://localhost:8080/therest/rest/record/recordType");
    }

}

package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

public class FinderBatchRunnerTest {

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Constructor<FinderBatchRunner> constructor = FinderBatchRunner.class
                .getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        String args[] = new String[] { "/home/madde/workspace/find/",
                "se.uu.ub.cora.batchrunner.find.FinderSpy", "http://localhost:8080/therest/rest/record/recordType", "se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy"};

        FinderBatchRunner.main(args);
        FinderSpy finderSpy = (FinderSpy) FinderBatchRunner.finder;
        assertTrue(finderSpy.findRecordsCalled);
        assertEquals(finderSpy.url, "http://localhost:8080/therest/rest/record/recordType");
    }

    @Test
    public void testMainMethodNoRecordsFound() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        String args[] = new String[] { "/home/madde/workspace/find/",
                "se.uu.ub.cora.batchrunner.find.FinderNoRecordsSpy", "http://localhost:8080/therest/rest/record/recordType", "se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy"};

        FinderBatchRunner.main(args);
        FinderNoRecordsSpy finderSpy = (FinderNoRecordsSpy) FinderBatchRunner.finder;
        assertTrue(finderSpy.findRecordCalled);
        assertEquals(finderSpy.url, "http://localhost:8080/therest/rest/record/recordType");
    }

}

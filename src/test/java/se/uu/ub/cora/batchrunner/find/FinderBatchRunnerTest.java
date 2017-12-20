package se.uu.ub.cora.batchrunner.find;

import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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
        //TODO: skicka även in httphandlerspy
        String args[] = new String[] { "/home/madde/workspace/find/",
                "se.uu.ub.cora.batchrunner.find.FinderSpy", "http://localhost:8080/therest/rest/record/recordType"};

        FinderBatchRunner.main(args);
        FinderSpy finderSpy = (FinderSpy) FinderBatchRunner.finder;
        assertTrue(finderSpy.findRecordCalled);
        assertEquals(finderSpy.url, "http://localhost:8080/therest/rest/record/recordType");
    }

}

package se.uu.ub.cora.batchrunner.index;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.batchrunner.find.RecordFinder;

import java.util.ArrayList;
import java.util.List;

public class DataIndexerTest {

    private HttpHandlerFactorySpy httpHandlerFactorySpy;
    private DataIndexer indexer;

    @BeforeMethod
    public void setUp() {
        indexer = new DataIndexer();
        httpHandlerFactorySpy = new HttpHandlerFactorySpy();
        indexer.setHttpHandlerFactory(httpHandlerFactorySpy);

    }

    @Test
    public void testInit(){
        List<String> idsToIndex = new ArrayList<>();
        indexer.indexData(idsToIndex);

    }
}

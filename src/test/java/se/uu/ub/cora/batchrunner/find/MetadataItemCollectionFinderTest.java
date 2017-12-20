package se.uu.ub.cora.batchrunner.find;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.httphandler.HttpHandler;

import java.util.Collection;

import static org.testng.Assert.assertEquals;

public class MetadataItemCollectionFinderTest {
    private RecordStorageSpy recordStorage;
    private MetadataItemCollectionFinder finder;

    //TODO: tanken är ju egentligen att vi ska gå via theRest och inte direkt mot basicstorage

    @BeforeMethod
    public void setUp() {
        finder = new MetadataItemCollectionFinder();
//        recordStorage = new RecordStorageSpy();
//        finder.setRecordStorage(recordStorage);
        HttpHandler httpHandlerSpy = new HttpHandlerSpy();
        finder.setHttpHandler(httpHandlerSpy);

    }

    @Test
    public void testFindCollectionsWithOnlyOneItem(){
        Collection<DataGroup> collectionsWithOnlyOneItem = finder.findRecords();
        assertEquals(collectionsWithOnlyOneItem.size(), 1);
    }
}

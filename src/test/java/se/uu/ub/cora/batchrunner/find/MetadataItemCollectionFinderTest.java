package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.Collection;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class MetadataItemCollectionFinderTest {
	private RecordStorageSpy recordStorage;
	private MetadataItemCollectionFinder finder;

	// TODO: tanken är ju egentligen att vi ska gå via theRest och inte direkt
	// mot basicstorage

	@BeforeMethod
	public void setUp() {
		finder = new MetadataItemCollectionFinder();
		// recordStorage = new RecordStorageSpy();
		// finder.setRecordStorage(recordStorage);
		HttpHandlerFactory httpHandlerFactorySpy = new HttpHandlerFactorySpy();
		finder.setHttpHandlerFactory(httpHandlerFactorySpy);
		finder.setUrlString("http://localhost:8080/");

	}

	@Test
	public void testFindCollectionsWithOnlyOneItem() {
		Collection<String> collectionsWithOnlyOneItem = finder.findRecords();
		assertEquals(collectionsWithOnlyOneItem.size(), 1);
	}
}

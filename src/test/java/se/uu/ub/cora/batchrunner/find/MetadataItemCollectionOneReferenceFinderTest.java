package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MetadataItemCollectionOneReferenceFinderTest {
	private MetadataItemCollectionOneReferenceFinder finder;

	@BeforeMethod
	public void setUp() {
		finder = new MetadataItemCollectionOneReferenceFinder();
		HttpHandlerFactorySpy httpHandlerFactorySpy = new HttpHandlerFactorySpy();
		finder.setHttpHandlerFactory(httpHandlerFactorySpy);
		finder.setUrlString("http://localhost:8080/metadataItemCollection");

	}

	@Test
	public void testFindCollectionsWithOnlyOneItem() {
		List<String> collectionsWithOnlyOneItem = (List<String>) finder.findRecords();
		assertEquals(collectionsWithOnlyOneItem.size(), 1);
		assertEquals(collectionsWithOnlyOneItem.get(0), "searchTermTypeCollection");
	}
}

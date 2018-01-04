package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class MetadataItemCollectionFinderTest {
	private MetadataItemCollectionFinder finder;

	@BeforeMethod
	public void setUp() {
		finder = new MetadataItemCollectionFinder();
		HttpHandlerFactory httpHandlerFactorySpy = new HttpHandlerFactorySpy();
		finder.setHttpHandlerFactory(httpHandlerFactorySpy);
		finder.setUrlString("http://localhost:8080/");

	}

	@Test
	public void testFindCollectionsWithOnlyOneItem() {
		List<String> collectionsWithOnlyOneItem = (List<String>) finder.findRecords();
		assertEquals(collectionsWithOnlyOneItem.size(), 1);
		assertEquals(collectionsWithOnlyOneItem.get(0), "searchTermTypeCollection");
	}
}

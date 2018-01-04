package se.uu.ub.cora.batchrunner.find;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class RecordFinderTest {
	private RecordFinder finder;

	@BeforeMethod
	public void setUp() {
		finder = new RecordFinder();
		HttpHandlerFactory httpHandlerFactorySpy = new HttpHandlerFactorySpy();
		finder.setHttpHandlerFactory(httpHandlerFactorySpy);
		finder.setUrlString("http://localhost:8080/");

	}

	@Test
	public void testFindAllOfRecordType() {
		List<String> collectionsWithOnlyOneItem = (List<String>) finder.findRecordsByRecordType("text");
		assertEquals(collectionsWithOnlyOneItem.size(), 1);
		assertEquals(collectionsWithOnlyOneItem.get(0), "someText");
	}
}

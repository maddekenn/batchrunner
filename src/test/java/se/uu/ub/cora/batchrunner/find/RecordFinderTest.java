package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.json.parser.JsonParseException;

public class RecordFinderTest {
	private RecordFinderImp finder;
	private HttpHandlerFactorySpy httpHandlerFactorySpy;

	@BeforeMethod
	public void setUp() {
		finder = new RecordFinderImp();
		httpHandlerFactorySpy = new HttpHandlerFactorySpy();
		finder.setHttpHandlerFactory(httpHandlerFactorySpy);

	}

	@Test
	public void testFindAllOfRecordTypeText() {
		finder.setUrlString("http://localhost:8080/text");
		List<String> ids = (List<String>) finder.findRecords();
		assertEquals(ids.size(), 1);
		assertEquals(ids.get(0), "nameTypeCollectionText");
	}

	@Test
	public void testFindAllOfRecordTypeMetadataItemCollection() {
		finder.setUrlString("http://localhost:8080/metadataItemCollection");
		List<String> ids = (List<String>) finder.findRecords();
		assertEquals(ids.size(), 3);
		assertEquals(ids.get(0), "searchTermTypeCollection");
	}

	@Test(expectedExceptions = JsonParseException.class)
	public void testFindAllOfNonExistingRecordType() {
		finder.setUrlString("http://localhost:8080/nonExisting");
		List<String> ids = (List<String>) finder.findRecords();
	}
}

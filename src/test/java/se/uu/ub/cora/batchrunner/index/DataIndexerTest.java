package se.uu.ub.cora.batchrunner.index;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.batchrunner.find.HttpHandlerSpy;

public class DataIndexerTest {

	private HttpHandlerFactorySpy httpHandlerFactorySpy;
	private DataIndexer indexer;

	@BeforeMethod
	public void setUp() {
		httpHandlerFactorySpy = new HttpHandlerFactorySpy();
		String url = "http://localhost:8080/therest/rest/record/workOrder";
		indexer = DataIndexer.withBaseURLHttpHandlerFactoryAndAuthToken(url, httpHandlerFactorySpy,
				"someToken");

	}

	@Test
	public void testHttpHandlerProperties() {
		List<String> idsToIndex = new ArrayList<>();
		idsToIndex.add("firstText");
		idsToIndex.add("secondText");
		indexer.indexDataWithRecordTypeAndIdsToIndex("someRecordType", idsToIndex);
		HttpHandlerSpy httpHandlerSpy = httpHandlerFactorySpy.httpHandlerSpy;
		assertEquals(httpHandlerSpy.requestMethod, "POST");
		assertEquals(httpHandlerSpy.requestProperties.get("Accept"),
				"application/vnd.uub.record+json");
		assertEquals(httpHandlerSpy.requestProperties.get("Content-Type"),
				"application/vnd.uub.record+json");
		assertEquals(httpHandlerSpy.requestProperties.size(), 2);
		assertEquals(httpHandlerFactorySpy.urlString,
				"http://localhost:8080/therest/rest/record/workOrder?authToken=someToken");

	}

	@Test
	public void testSentJson() {
		List<String> idsToIndex = new ArrayList<>();
		idsToIndex.add("firstText");
		idsToIndex.add("secondText");
		indexer.indexDataWithRecordTypeAndIdsToIndex("someRecordType", idsToIndex);
		HttpHandlerSpy httpHandlerSpy = httpHandlerFactorySpy.httpHandlerSpy;
		assertEquals(httpHandlerSpy.outputStrings.get(0),
				"{\"name\":\"workOrder\",\"children\":[{\"name\":\"recordType\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"someRecordType\"}]},{\"name\":\"recordId\",\"value\":\"firstText\"},{\"name\":\"type\",\"value\":\"index\"}]}");
		assertEquals(httpHandlerSpy.outputStrings.get(1),
				"{\"name\":\"workOrder\",\"children\":[{\"name\":\"recordType\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"someRecordType\"}]},{\"name\":\"recordId\",\"value\":\"secondText\"},{\"name\":\"type\",\"value\":\"index\"}]}");

	}

	@Test
	public void testResponse() {
		List<String> idsToIndex = new ArrayList<>();
		idsToIndex.add("firstText");
		idsToIndex.add("secondText");
		indexer.indexDataWithRecordTypeAndIdsToIndex("someRecordType", idsToIndex);
		HttpHandlerSpy httpHandlerSpy = httpHandlerFactorySpy.httpHandlerSpy;

	}
}

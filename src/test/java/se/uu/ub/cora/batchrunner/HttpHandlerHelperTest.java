package se.uu.ub.cora.batchrunner;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.batchrunner.find.HttpHandlerSpy;

public class HttpHandlerHelperTest {
	private HttpHandlerFactorySpy httpHandlerFactory;
	private String url;
	private HttpHandlerHelper helper;

	@BeforeMethod
	public void setUp() {
		httpHandlerFactory = new HttpHandlerFactorySpy();
		url = "http://someTestUrl/";
		helper = HttpHandlerHelper.usingURLAndHttpHandlerFactory(url, httpHandlerFactory);
	}

	@Test
	public void init() {
		assertTrue(helper.getHttpHandler() instanceof HttpHandlerFactorySpy);
		assertEquals(helper.getUrl(), url);
	}

	@Test
	public void testReadRecord() {
		String readRecord = helper.readRecord("someType", "someId");

		HttpHandlerSpy httpHandlerSpy = httpHandlerFactory.httpHandlerSpies.get(0);
		assertEquals(httpHandlerSpy.requestMethod, "GET");
		assertEquals(httpHandlerSpy.urlString, "http://someTestUrl/someType/someId");
		assertEquals(readRecord, "Record with id someId returned from spy");

	}

	@Test(expectedExceptions = RecordNotFoundException.class, expectedExceptionsMessageRegExp = "Unable to read record with type: someType and id: someId")
	public void testReadRecordRecordNotFound() {
		httpHandlerFactory.setResponseCode(404);
		HttpHandlerHelper helper = HttpHandlerHelper.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		helper.readRecord("someType", "someId");

	}

	@Test
	public void testCreateRecord() {
		httpHandlerFactory.setResponseCode(201);
		HttpHandlerHelper helper = HttpHandlerHelper.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		String someJson = "some Json";
		String messageFromCreate = helper.createRecord("someType", someJson);

		HttpHandlerSpy httpHandlerSpy = httpHandlerFactory.httpHandlerSpies.get(0);

		assertEquals(httpHandlerSpy.requestMethod, "POST");
		assertEquals(httpHandlerSpy.urlString, "http://someTestUrl/someType");
		assertEquals(httpHandlerSpy.requestProperties.get("Accept"),
				"application/vnd.uub.record+json");
		assertEquals(httpHandlerSpy.requestProperties.get("Content-Type"),
				"application/vnd.uub.record+json");
		assertEquals(httpHandlerSpy.outputString, "some Json");
		assertEquals(messageFromCreate, "201 Ok: some Json");

	}

	@Test
	public void testCreateRecordWithError() {
		httpHandlerFactory.setResponseCode(409);
		HttpHandlerHelper helper = HttpHandlerHelper.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		String someJson = "some Json";
		String messageFromCreate = helper.createRecord("someType", someJson);

		assertEquals(messageFromCreate, "409 some error text from spy Error creating: some Json");

	}

	@Test
	public void testUpdateRecord() {
		httpHandlerFactory.setResponseCode(200);
		HttpHandlerHelper helper = HttpHandlerHelper.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		String someJson = "some Json";
		String recordId = "someRecordId";
		String messageFromCreate = helper.updateRecord("someType", recordId, someJson);

		HttpHandlerSpy httpHandlerSpy = httpHandlerFactory.httpHandlerSpies.get(0);

		assertEquals(httpHandlerSpy.requestMethod, "POST");
		assertEquals(httpHandlerSpy.urlString, "http://someTestUrl/someType/someRecordId");
		assertEquals(httpHandlerSpy.requestProperties.get("Accept"),
				"application/vnd.uub.record+json");
		assertEquals(httpHandlerSpy.requestProperties.get("Content-Type"),
				"application/vnd.uub.record+json");
		assertEquals(httpHandlerSpy.outputString, "some Json");
		assertEquals(messageFromCreate, "200 Ok: some Json");

	}

}

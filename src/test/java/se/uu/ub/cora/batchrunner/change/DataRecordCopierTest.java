package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.batchrunner.find.HttpHandlerSpy;

public class DataRecordCopierTest {

	private String url;
	private HttpHandlerFactorySpy httpHandlerFactory;

	@BeforeMethod
	public void setUp() {
		httpHandlerFactory = new HttpHandlerFactorySpy();
		url = "http://someTestUrl/";
	}

	@Test
	public void init() {
		DataCopier copier = DataRecordCopier.usingURLAndHttpHandlerFactory(url, httpHandlerFactory);
		assertTrue(copier.getHttpHandler() instanceof HttpHandlerFactorySpy);
		assertEquals(copier.getUrl(), url);

	}

	@Test
	public void testCopyTypeFromIdToIdOkOnCreate() {
		httpHandlerFactory.setResponseCode(201);
		DataRecordCopier copier = DataRecordCopier.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		String id = "myRecordTypeFormPGroup";
		String newId = "myRecordTypePGroup";
		String newJson = copier.copyTypeFromIdToNewId("presentationGroup", id, newId);

		assertHttpHandlerIsCorrectForReadPGroupByIndexTypeAndId(0, "presentationGroup",
				"myRecordTypeFormPGroup");
		assertTrue(copier.jsonCopier instanceof DataGroupJsonCopier);
		String expectedJson = "{\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataGroup\"},{\"name\":\"linkedRecordId\",\"value\":\"myRecordTypeGroup\"}],\"name\":\"presentationOf\"},{\"children\":[{\"repeatId\":\"3\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"coraText\"},{\"name\":\"linkedRecordId\",\"value\":\"formOfProjectGrantCollectionVarText\"}],\"name\":\"ref\",\"attributes\":{\"type\":\"text\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"},{\"repeatId\":\"0\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"presentationCollectionVar\"},{\"name\":\"linkedRecordId\",\"value\":\"formOfProjectGrantPCollVar\"}],\"name\":\"ref\",\"attributes\":{\"type\":\"presentation\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"},{\"repeatId\":\"4\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"coraText\"},{\"name\":\"linkedRecordId\",\"value\":\"projectTitlesGroupText\"}],\"name\":\"ref\",\"attributes\":{\"type\":\"text\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"},{\"repeatId\":\"1\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"presentationGroup\"},{\"name\":\"linkedRecordId\",\"value\":\"projectTitlesPGroup\"}],\"name\":\"ref\",\"attributes\":{\"type\":\"presentation\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"}],\"name\":\"childReferences\"},{\"name\":\"mode\",\"value\":\"input\"},{\"children\":[{\"name\":\"id\",\"value\":\"myRecordTypePGroup\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"diva\"}],\"name\":\"dataDivider\"}],\"name\":\"recordInfo\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pGroup\"}}";
		assertEquals(newJson, "201 Ok: " + expectedJson);

		assertHttpHandlerIsCorrectForCreateByIndexAndOutput(1, url + "presentationGroup",
				expectedJson);

	}

	private void assertHttpHandlerIsCorrectForReadPGroupByIndexTypeAndId(int index, String type,
			String id) {
		HttpHandlerSpy httpHandlerForPGroupRead = httpHandlerFactory.httpHandlerSpies.get(index);
		assertEquals(httpHandlerForPGroupRead.urlString, "http://someTestUrl/" + type + "/" + id);
		assertEquals(httpHandlerForPGroupRead.requestMethod, "GET");
	}

	private void assertHttpHandlerIsCorrectForCreateByIndexAndOutput(int index, String url,
			String output) {
		HttpHandlerSpy httpHandlerForCreate = httpHandlerFactory.httpHandlerSpies.get(index);

		assertEquals(httpHandlerForCreate.urlString, url);
		assertEquals(httpHandlerForCreate.requestMethod, "POST");
		assertEquals(httpHandlerForCreate.requestProperties.get("Accept"),
				"application/vnd.uub.record+json");
		assertEquals(httpHandlerForCreate.requestProperties.get("Content-Type"),
				"application/vnd.uub.record+json");
		assertEquals(httpHandlerForCreate.outputString, output);
	}

	@Test
	public void testCopyTypeFromIdToIdNotOkOnCreate() {
		httpHandlerFactory.setResponseCode(409);
		DataRecordCopier copier = DataRecordCopier.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		String id = "myRecordTypeFormPGroup";
		String newId = "myRecordTypePGroup";
		String newJson = copier.copyTypeFromIdToNewId("presentationGroup", id, newId);

		assertHttpHandlerIsCorrectForReadPGroupByIndexTypeAndId(0, "presentationGroup",
				"myRecordTypeFormPGroup");
		assertTrue(copier.jsonCopier instanceof DataGroupJsonCopier);
		String expectedJson = "{\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataGroup\"},{\"name\":\"linkedRecordId\",\"value\":\"myRecordTypeGroup\"}],\"name\":\"presentationOf\"},{\"children\":[{\"repeatId\":\"3\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"coraText\"},{\"name\":\"linkedRecordId\",\"value\":\"formOfProjectGrantCollectionVarText\"}],\"name\":\"ref\",\"attributes\":{\"type\":\"text\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"},{\"repeatId\":\"0\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"presentationCollectionVar\"},{\"name\":\"linkedRecordId\",\"value\":\"formOfProjectGrantPCollVar\"}],\"name\":\"ref\",\"attributes\":{\"type\":\"presentation\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"},{\"repeatId\":\"4\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"coraText\"},{\"name\":\"linkedRecordId\",\"value\":\"projectTitlesGroupText\"}],\"name\":\"ref\",\"attributes\":{\"type\":\"text\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"},{\"repeatId\":\"1\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"presentationGroup\"},{\"name\":\"linkedRecordId\",\"value\":\"projectTitlesPGroup\"}],\"name\":\"ref\",\"attributes\":{\"type\":\"presentation\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"}],\"name\":\"childReferences\"},{\"name\":\"mode\",\"value\":\"input\"},{\"children\":[{\"name\":\"id\",\"value\":\"myRecordTypePGroup\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"diva\"}],\"name\":\"dataDivider\"}],\"name\":\"recordInfo\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pGroup\"}}";
		assertEquals(newJson, "409 some error text from spy Error creating: " + expectedJson);

		assertHttpHandlerIsCorrectForCreateByIndexAndOutput(1, url + "presentationGroup",
				expectedJson);

	}

	@Test
	public void testCopyTypeFromIdToIdNotOkOnRead() {
		httpHandlerFactory.setResponseCode(404);
		DataRecordCopier copier = DataRecordCopier.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		String id = "myRecordTypeFormPGroup";
		String newId = "myRecordTypePGroup";
		String newJson = copier.copyTypeFromIdToNewId("presentationGroup", id, newId);
		assertEquals(newJson,
				"404 Unable to read record with type: presentationGroup and id: myRecordTypeFormPGroup myRecordTypeFormPGroup");
	}

}

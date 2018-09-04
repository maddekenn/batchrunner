package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.batchrunner.find.HttpHandlerSpy;

public class DataGroupCopierTest {

	private String url;
	private HttpHandlerFactorySpy httpHandlerFactory;

	@BeforeMethod
	public void setUp() {
		httpHandlerFactory = new HttpHandlerFactorySpy();
		url = "http://someTestUrl/";
	}

	@Test
	public void init() {
		DataCopier copier = DataGroupCopier.usingURLAndHttpHandlerFactory(url, httpHandlerFactory);
		assertTrue(copier.getHttpHandler() instanceof HttpHandlerFactorySpy);
		assertEquals(copier.getUrl(), url);

	}

	@Test
	public void testCopyOk() {
		DataGroupCopier copier = DataGroupCopier.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		String id = "myRecordTypeFormPGroup";
		String newId = "myRecordTypePGroup";
		String newJson = copier.copyTypeFromIdToNewId("presentationGroup", id, newId);

		// assert att read på gamla id:t är gjort
		assertHttpHandlerIsCorrectForReadPGroupByIndexTypeAndId(0, "presentationGroup",
				"myRecordTypeFormPGroup");
		// assert att DataGroupJsonCopier har anropats
		assertTrue(copier.jsonCopier instanceof DataGroupJsonCopier);
		String expectedJson = "{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"myRecordTypeFormPGroup\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationGroup\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"diva\"}],\"name\":\"dataDivider\"},{\"name\":\"tsCreated\",\"value\":\"2018-06-08 08:16:59.837\"},{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"updatedBy\"},{\"name\":\"tsUpdated\",\"value\":\"2018-06-08 08:16:59.837\"}],\"name\":\"updated\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataGroup\"},{\"name\":\"linkedRecordId\",\"value\":\"myRecordTypeGroup\"}],\"name\":\"presentationOf\"},{\"children\":[{\"repeatId\":\"3\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"coraText\"},{\"name\":\"linkedRecordId\",\"value\":\"formOfProjectGrantCollectionVarText\"}],\"name\":\"ref\",\"attributes\":{\"type\":\"text\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"},{\"repeatId\":\"0\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"presentationCollectionVar\"},{\"name\":\"linkedRecordId\",\"value\":\"formOfProjectGrantPCollVar\"}],\"name\":\"ref\",\"attributes\":{\"type\":\"presentation\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"},{\"repeatId\":\"4\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"coraText\"},{\"name\":\"linkedRecordId\",\"value\":\"projectTitlesGroupText\"}],\"name\":\"ref\",\"attributes\":{\"type\":\"text\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"},{\"repeatId\":\"1\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"presentationGroup\"},{\"name\":\"linkedRecordId\",\"value\":\"projectTitlesPGroup\"}],\"name\":\"ref\",\"attributes\":{\"type\":\"presentation\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"}],\"name\":\"childReferences\"},{\"name\":\"mode\",\"value\":\"input\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pGroup\"}}";
		assertEquals(newJson, "some json");
		// public DataGroupJsonCopier i DataGroupCopier??

		// assert json skickad till create

	}

	private void assertHttpHandlerIsCorrectForReadPGroupByIndexTypeAndId(int index, String type,
			String id) {
		HttpHandlerSpy httpHandlerForPGroupRead = httpHandlerFactory.httpHandlerSpies.get(index);
		assertEquals(httpHandlerForPGroupRead.urlString, "http://someTestUrl/" + type + "/" + id);
		assertEquals(httpHandlerForPGroupRead.requestMethod, "GET");
	}

}

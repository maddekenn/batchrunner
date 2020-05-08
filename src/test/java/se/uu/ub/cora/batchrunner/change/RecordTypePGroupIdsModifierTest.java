package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.batchrunner.find.HttpHandlerSpy;
import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverter;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactory;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactoryImp;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactory;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactoryImp;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataRecordConverter;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataRecordConverterImp;
import se.uu.ub.cora.json.builder.JsonBuilderFactory;
import se.uu.ub.cora.json.builder.org.OrgJsonBuilderFactoryAdapter;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public class RecordTypePGroupIdsModifierTest {

	private String url;
	private HttpHandlerFactorySpy httpHandlerFactory;
	private RecordTypePGroupIdsModifier modifier;

	@BeforeMethod
	public void setUp() {
		url = "http://someTestUrl/";
		httpHandlerFactory = new HttpHandlerFactorySpy();
		modifier = RecordTypePGroupIdsModifier.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
	}

	@Test
	public void testInit() {
		assertEquals(modifier.getHttpHandlerFactory(), httpHandlerFactory);
		assertEquals(modifier.getUrl(), url);
	}

	@Test
	public void testModifyIdsOfPGroupsInRecordType() {
		httpHandlerFactory.setResponseCode(200);
		List<String> messages = modifier.modifyData("myRecordType");
		HttpHandlerSpy httpHandlerForRecordTypeRead = httpHandlerFactory.httpHandlerSpies.get(0);

		assertHttpHandlerIsCorrectForReadRecordType("myRecordType");

		String correctRecordTypeJsonAfterUpdate = composeJsonFromUpdatedRecordType(
				httpHandlerForRecordTypeRead, "myRecordType");
		assertHttpHandlerIsCorrectForCreateByIndexAndOutput(1,
				"http://someTestUrl/recordType/myRecordType", correctRecordTypeJsonAfterUpdate);

		assertEquals(messages.size(), 0);

	}

	private void assertHttpHandlerIsCorrectForCreateByIndexAndOutput(int index, String url,
			String output) {
		HttpHandlerSpy httpHandlerForPGroupCreate = httpHandlerFactory.httpHandlerSpies.get(index);

		assertEquals(httpHandlerForPGroupCreate.urlString, url);
		assertEquals(httpHandlerForPGroupCreate.requestMethod, "POST");
		assertEquals(httpHandlerForPGroupCreate.requestProperties.get("Accept"),
				"application/vnd.uub.record+json");
		assertEquals(httpHandlerForPGroupCreate.requestProperties.get("Content-Type"),
				"application/vnd.uub.record+json");
		assertEquals(httpHandlerForPGroupCreate.outputString, output);
	}

	private String composeJsonFromUpdatedRecordType(HttpHandlerSpy httpHandlerForRecordTypeRead,
			String recordTypeId) {
		ClientDataGroup pGroupAsDataGroup = getRecordTypeDataGroup(httpHandlerForRecordTypeRead);

		replaceLinkUsingDataGroupNameInDataAndNewId(pGroupAsDataGroup, "presentationFormId",
				recordTypeId + "PGroup");
		replaceLinkUsingDataGroupNameInDataAndNewId(pGroupAsDataGroup, "newPresentationFormId",
				recordTypeId + "NewPGroup");
		replaceLinkUsingDataGroupNameInDataAndNewId(pGroupAsDataGroup, "presentationViewId",
				recordTypeId + "OutputPGroup");
		return getDataGroupAsJson(pGroupAsDataGroup);
	}

	private ClientDataGroup getRecordTypeDataGroup(HttpHandlerSpy httpHandlerForRecordTypeRead) {
		String recordTypeResponse = httpHandlerForRecordTypeRead.getResponseText();
		ClientDataRecord pGroupAsDataRecord = getPGroupJsonAsClientDataRecord(recordTypeResponse);
		ClientDataGroup pGroupAsDataGroup = pGroupAsDataRecord.getClientDataGroup();
		return pGroupAsDataGroup;
	}

	private void replaceLinkUsingDataGroupNameInDataAndNewId(ClientDataGroup pGroupAsDataGroup,
			String nameInData, String presentationId) {
		ClientDataGroup presentationFormId = pGroupAsDataGroup
				.getFirstGroupWithNameInData(nameInData);
		presentationFormId.removeFirstChildWithNameInData("linkedRecordId");
		presentationFormId.addChild(
				ClientDataAtomic.withNameInDataAndValue("linkedRecordId", presentationId));
	}

	private void assertHttpHandlerIsCorrectForReadRecordType(String recordTypeId) {
		HttpHandlerSpy httpHandlerForRecordTypeRead = httpHandlerFactory.httpHandlerSpies.get(0);

		assertEquals(httpHandlerForRecordTypeRead.requestMethod, "GET");
		assertEquals(httpHandlerForRecordTypeRead.urlString,
				"http://someTestUrl/recordType/" + recordTypeId);
	}

	private String getDataGroupAsJson(ClientDataGroup dataGroup) {
		DataToJsonConverterFactory jsonConverterFactory = new DataToJsonConverterFactoryImp();
		JsonBuilderFactory factory = new OrgJsonBuilderFactoryAdapter();
		DataToJsonConverter forClientDataElement = jsonConverterFactory
				.createForClientDataElementIncludingActionLinks(factory, dataGroup, false);
		return forClientDataElement.toJson();
	}

	private ClientDataRecord getPGroupJsonAsClientDataRecord(String readPresentationGroup) {
		JsonObject recordJsonObject = createJsonObjectFromResponseText(readPresentationGroup);

		JsonToDataConverterFactory recordConverterFactory = new JsonToDataConverterFactoryImp();
		JsonToDataRecordConverter converter = JsonToDataRecordConverterImp
				.usingConverterFactory(recordConverterFactory);
		return (ClientDataRecord) converter.toInstance(recordJsonObject);
	}

	private JsonObject createJsonObjectFromResponseText(String responseText) {
		JsonParser jsonParser = new OrgJsonParser();
		JsonValue jsonValue = jsonParser.parseString(responseText);
		return (JsonObject) jsonValue;
	}

	@Test
	public void testModifyIdsOfPGroupsInRecordTypeAlreadyNewId() {

		List<String> messages = modifier.modifyData("myNewStructuredRecordType");
		assertHttpHandlerIsCorrectForReadRecordType("myNewStructuredRecordType");
		assertEquals(messages.size(), 1);
		String message = messages.get(0);
		assertEquals(message,
				"myNewStructuredRecordType does not have default id for presentationFormId");

		assertEquals(httpHandlerFactory.httpHandlerSpies.size(), 1);

	}

	@Test
	public void testModifyIdsOfPGroupsInRecordTypeUnableToUpdate() {
		httpHandlerFactory.setResponseCode(401);
		List<String> messages = modifier.modifyData("myRecordType");

		assertHttpHandlerIsCorrectForReadRecordType("myRecordType");
		assertEquals(messages.size(), 1);
		String message = messages.get(0);
		assertTrue(message.startsWith("401 some error text from spy"));
	}
}

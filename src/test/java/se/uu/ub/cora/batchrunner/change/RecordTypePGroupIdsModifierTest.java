package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.batchrunner.find.HttpHandlerSpy;
import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverter;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactoryImp;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactory;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactoryImp;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataRecordConverter;
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
	public void testModifyPGroupsDoesNotExist() {
		httpHandlerFactory.setResponseCode(404);
		modifier.modifyData("myRecordType");
		HttpHandlerSpy httpHandlerForRecordTypeRead = httpHandlerFactory.httpHandlerSpies.get(0);

		assertHttpHandlerIsCorrectForReadRecordType("myRecordType");

		// FormPGroup
		assertHttpHandlerIsCorrectForReadPGroupByIndexAndId(1, "myRecordTypePGroup");
		assertHttpHandlerIsCorrectForReadPGroupByIndexAndId(2, "myRecordTypeFormPGroup");

		// FormNewPGroup
		// assertHttpHandlerIsCorrectForReadPGroupByIndexAndId(3,
		// "myRecordTypeNewPGroup");
		// assertHttpHandlerIsCorrectForReadPGroupByIndexAndId(4,
		// "myRecordTypeFormNewPGroup");

		// ViewPGroup
		// assertHttpHandlerIsCorrectForReadPGroupByIndexAndId(1, "myRecordTypePGroup");
		// assertHttpHandlerIsCorrectForReadPGroupByIndexAndId(2,
		// "myRecordTypeFormPGroup");

		assertHttpHandlerIsCorrectForCreateByIndexAndOutput(3,
				"http://someTestUrl/presentationGroup/",
				getExpectedJsonBasedOnFormerPGroupUsingIndexAndId(2, "myRecordTypePGroup"));
		String correctRecordTypeJsonAfterUpdate = composeJsonFromUpdatedRecordType(
				httpHandlerForRecordTypeRead, "myRecordTypePGroup");

		assertHttpHandlerIsCorrectForCreateByIndexAndOutput(4,
				"http://someTestUrl/recordType/myRecordType", correctRecordTypeJsonAfterUpdate);

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
			String presentationId) {
		String recordTypeResponse = httpHandlerForRecordTypeRead.getResponseText();
		ClientDataRecord pGroupAsDataRecord = getPGroupJsonAsClientDataRecord(recordTypeResponse);
		ClientDataGroup pGroupAsDataGroup = pGroupAsDataRecord.getClientDataGroup();

		ClientDataGroup presentationFormId = pGroupAsDataGroup
				.getFirstGroupWithNameInData("presentationFormId");
		presentationFormId.removeFirstChildWithNameInData("linkedRecordId");
		presentationFormId.addChild(
				ClientDataAtomic.withNameInDataAndValue("linkedRecordId", presentationId));
		return getDataGroupAsJson(pGroupAsDataGroup);
	}

	private String getExpectedJsonBasedOnFormerPGroupUsingIndexAndId(int index, String id) {
		HttpHandlerSpy httpHandlerForPFormGroupRead = httpHandlerFactory.httpHandlerSpies
				.get(index);

		String readPresentationGroup = httpHandlerForPFormGroupRead.getResponseText();

		ClientDataRecord pGroupAsDataRecord = getPGroupJsonAsClientDataRecord(
				readPresentationGroup);
		ClientDataGroup pGroupAsDataGroup = pGroupAsDataRecord.getClientDataGroup();
		ClientDataGroup recordInfoToCopy = pGroupAsDataGroup
				.getFirstGroupWithNameInData("recordInfo");
		ClientDataGroup dataDivider = recordInfoToCopy.getFirstGroupWithNameInData("dataDivider");
		pGroupAsDataGroup.removeFirstChildWithNameInData("recordInfo");

		ClientDataGroup newRecordInfo = ClientDataGroup.withNameInData("recordInfo");
		newRecordInfo.addChild(ClientDataAtomic.withNameInDataAndValue("id", id));
		newRecordInfo.addChild(dataDivider);
		pGroupAsDataGroup.addChild(newRecordInfo);

		String pGroupJsonWithNewRecordInfo = getDataGroupAsJson(pGroupAsDataGroup);
		return pGroupJsonWithNewRecordInfo;
	}

	private void assertHttpHandlerIsCorrectForReadPGroupByIndexAndId(int index, String id) {
		HttpHandlerSpy httpHandlerForPGroupRead = httpHandlerFactory.httpHandlerSpies.get(index);
		assertEquals(httpHandlerForPGroupRead.urlString,
				"http://someTestUrl/presentationGroup/" + id);
		assertEquals(httpHandlerForPGroupRead.requestMethod, "GET");
	}

	private void assertHttpHandlerIsCorrectForReadRecordType(String recordTypeId) {
		HttpHandlerSpy httpHandlerForRecordTypeRead = httpHandlerFactory.httpHandlerSpies.get(0);

		assertEquals(httpHandlerForRecordTypeRead.requestMethod, "GET");
		assertEquals(httpHandlerForRecordTypeRead.urlString,
				"http://someTestUrl/recordType/" + recordTypeId);
	}

	private String getDataGroupAsJson(ClientDataGroup dataGroup) {
		DataToJsonConverterFactoryImp jsonConverterFactory = new DataToJsonConverterFactoryImp();
		JsonBuilderFactory factory = new OrgJsonBuilderFactoryAdapter();
		DataToJsonConverter forClientDataElement = jsonConverterFactory
				.createForClientDataElement(factory, dataGroup);
		return forClientDataElement.toJson();
	}

	private ClientDataRecord getPGroupJsonAsClientDataRecord(String readPresentationGroup) {
		JsonObject recordJsonObject = createJsonObjectFromResponseText(readPresentationGroup);

		JsonToDataConverterFactory recordConverterFactory = new JsonToDataConverterFactoryImp();
		JsonToDataRecordConverter converter = JsonToDataRecordConverter
				.forJsonObjectUsingConverterFactory(recordJsonObject, recordConverterFactory);
		return converter.toInstance();
	}

	private JsonObject createJsonObjectFromResponseText(String responseText) {
		JsonParser jsonParser = new OrgJsonParser();
		JsonValue jsonValue = jsonParser.parseString(responseText);
		return (JsonObject) jsonValue;
	}

	@Test
	public void testModifyPGroupsWhenPGroupDoesExist() {
		modifier.modifyData("myOtherRecordType");
		HttpHandlerSpy httpHandlerForRecordTypeRead = httpHandlerFactory.httpHandlerSpies.get(0);

		assertHttpHandlerIsCorrectForReadRecordType("myOtherRecordType");

		// FormPGroup
		assertHttpHandlerIsCorrectForReadPGroupByIndexAndId(1, "myOtherRecordTypePGroup");

		HttpHandlerSpy httpHandlerForPGroupDelete = httpHandlerFactory.httpHandlerSpies.get(2);
		String url = "http://someTestUrl/presentationGroup/" + "myOtherRecordTypePGroup";
		assertEquals(httpHandlerForPGroupDelete.urlString, url);
		assertEquals(httpHandlerForPGroupDelete.requestMethod, "DELETE");
		assertTrue(httpHandlerForPGroupDelete.deleteWasCalled);

		assertHttpHandlerIsCorrectForReadPGroupByIndexAndId(3, "myOtherRecordTypeFormPGroup");

		// FormNewPGroup
		// assertHttpHandlerIsCorrectForReadPGroupByIndexAndId(3,
		// "myRecordTypeNewPGroup");
		// assertHttpHandlerIsCorrectForReadPGroupByIndexAndId(4,
		// "myRecordTypeFormNewPGroup");

		// ViewPGroup
		// assertHttpHandlerIsCorrectForReadPGroupByIndexAndId(1, "myRecordTypePGroup");
		// assertHttpHandlerIsCorrectForReadPGroupByIndexAndId(2,
		// "myRecordTypeFormPGroup");

		assertHttpHandlerIsCorrectForCreateByIndexAndOutput(4,
				"http://someTestUrl/presentationGroup/",
				getExpectedJsonBasedOnFormerPGroupUsingIndexAndId(3, "myOtherRecordTypePGroup"));
		String correctRecordTypeJsonAfterUpdate = composeJsonFromUpdatedRecordType(
				httpHandlerForRecordTypeRead, "myOtherRecordTypePGroup");
		//
		assertHttpHandlerIsCorrectForCreateByIndexAndOutput(5,
				"http://someTestUrl/recordType/myOtherRecordType",
				correctRecordTypeJsonAfterUpdate);

	}

	// TODO: hantera också de fall när FormPGroup osv inte finns
}

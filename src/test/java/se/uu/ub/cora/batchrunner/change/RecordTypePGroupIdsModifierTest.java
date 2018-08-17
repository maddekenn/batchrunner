package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;

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
		modifier.modifyData("myRecordType");
		assertHttpHandlerIsCorrectForReadRecordType();

		assertHttpHandlerIsCorrectForReadPresentationGroupByIndexAndId(1, "myRecordTypePGroup");
		assertHttpHandlerIsCorrectForReadPresentationGroupByIndexAndId(2, "myRecordTypeFormPGroup");

		String pGroupJsonWithNewRecordInfo = getExpectedJsonBasedOnFormerPGroupWithNewRecordInfo();

		HttpHandlerSpy httpHandlerForPGroupCreate = httpHandlerFactory.httpHandlerSpies.get(3);

		assertEquals(httpHandlerForPGroupCreate.urlString, "http://someTestUrl/presentationGroup/");
		assertEquals(httpHandlerForPGroupCreate.requestMethod, "POST");
		assertEquals(httpHandlerForPGroupCreate.requestProperties.get("Accept"),
				"application/vnd.uub.record+json");
		assertEquals(httpHandlerForPGroupCreate.requestProperties.get("Content-Type"),
				"application/vnd.uub.record+json");
		assertEquals(httpHandlerForPGroupCreate.outputString, pGroupJsonWithNewRecordInfo);

	}

	private String getExpectedJsonBasedOnFormerPGroupWithNewRecordInfo() {
		HttpHandlerSpy httpHandlerForPFormGroupRead = httpHandlerFactory.httpHandlerSpies.get(2);

		String readPresentationGroup = httpHandlerForPFormGroupRead.getResponseText();

		ClientDataRecord pGroupAsDataRecord = getPGroupJsonAsClientDataRecord(
				readPresentationGroup);
		ClientDataGroup pGroupAsDataGroup = pGroupAsDataRecord.getClientDataGroup();
		ClientDataGroup recordInfoToCopy = pGroupAsDataGroup
				.getFirstGroupWithNameInData("recordInfo");
		ClientDataGroup dataDivider = recordInfoToCopy.getFirstGroupWithNameInData("dataDivider");
		pGroupAsDataGroup.removeFirstChildWithNameInData("recordInfo");

		ClientDataGroup newRecordInfo = ClientDataGroup.withNameInData("recordInfo");
		newRecordInfo.addChild(ClientDataAtomic.withNameInDataAndValue("id", "myRecordTypePGroup"));
		newRecordInfo.addChild(dataDivider);
		pGroupAsDataGroup.addChild(newRecordInfo);

		String pGroupJsonWithNewRecordInfo = getDataGroupAsJson(pGroupAsDataGroup);
		return pGroupJsonWithNewRecordInfo;
	}

	private void assertHttpHandlerIsCorrectForReadPresentationGroupByIndexAndId(int index,
			String id) {
		HttpHandlerSpy httpHandlerForPGroupRead = httpHandlerFactory.httpHandlerSpies.get(index);
		assertEquals(httpHandlerForPGroupRead.urlString,
				"http://someTestUrl/presentationGroup/" + id);
		assertEquals(httpHandlerForPGroupRead.requestMethod, "GET");
	}

	private void assertHttpHandlerIsCorrectForReadRecordType() {
		HttpHandlerSpy httpHandlerForRecordTypeRead = httpHandlerFactory.httpHandlerSpies.get(0);

		assertEquals(httpHandlerForRecordTypeRead.requestMethod, "GET");
		assertEquals(httpHandlerForRecordTypeRead.urlString,
				"http://someTestUrl/recordType/myRecordType");
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
}

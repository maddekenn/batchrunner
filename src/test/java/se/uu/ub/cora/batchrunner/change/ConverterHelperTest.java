package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public class ConverterHelperTest {

	public String jsonToConvert = "{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"aTestSystemNoLinks\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"system\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8081/therest/rest/record/recordType/system\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8081/therest/rest/record/systemOneUser/141414\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"bibsys\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8081/therest/rest/record/system/bibsys\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8081/therest/rest/record/systemOneUser/141414\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"updatedBy\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:54:07.0\"}],\"name\":\"updated\"}],\"name\":\"recordInfo\"},{\"name\":\"systemName\",\"value\":\"system\"}],\"name\":\"system\"},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8081/therest/rest/record/system/aTestSystem\",\"accept\":\"application/vnd.uub.record+json\"},\"update\":{\"requestMethod\":\"POST\",\"rel\":\"update\",\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"http://localhost:8081/therest/rest/record/system/aTestSystem\",\"accept\":\"application/vnd.uub.record+json\"}}}}";

	@Test
	public void testJsonStringAsDataRecord() {
		ClientDataRecord jsonAsClientDataRecord = ConverterHelper
				.getJsonStringAsClientDataRecord(jsonToConvert);
		ClientDataGroup clientDataGroup = jsonAsClientDataRecord.getClientDataGroup();
		ClientDataGroup recordInfo = clientDataGroup.getFirstGroupWithNameInData("recordInfo");
		ClientDataGroup type = recordInfo.getFirstGroupWithNameInData("type");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "aTestSystemNoLinks");
		assertEquals(type.getFirstAtomicValueWithNameInData("linkedRecordId"), "system");
	}

	@Test
	public void testJsonObjectAsDataRecord() {
		JsonParser jsonParser = new OrgJsonParser();
		JsonObject jsonValue = jsonParser.parseStringAsObject(jsonToConvert);

		ClientDataRecord jsonAsClientDataRecord = ConverterHelper
				.getJsonObjectAsClientDataRecord(jsonValue);
		ClientDataGroup clientDataGroup = jsonAsClientDataRecord.getClientDataGroup();
		ClientDataGroup recordInfo = clientDataGroup.getFirstGroupWithNameInData("recordInfo");
		ClientDataGroup type = recordInfo.getFirstGroupWithNameInData("type");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "aTestSystemNoLinks");
		assertEquals(type.getFirstAtomicValueWithNameInData("linkedRecordId"), "system");
	}

	@Test
	public void testDataGroupToJson() {
		ClientDataGroup dataGroup = createDataGroup();

		DataToJsonConverterFactorySpy jsonConverterFactory = new DataToJsonConverterFactorySpy();
		String json = ConverterHelper.getDataGroupAsJsonUsingConverterFactory(dataGroup,
				jsonConverterFactory);
		assertEquals(json, "{\"name\":\"someDataGroup\"}");
		assertEquals(jsonConverterFactory.clientDataElement, dataGroup);
	}

	private ClientDataGroup createDataGroup() {
		ClientDataGroup dataGroup = ClientDataGroup.withNameInData("someDataGroup");
		ClientDataGroup recordInfo = ClientDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(ClientDataAtomic.withNameInDataAndValue("id", "someId"));
		dataGroup.addChild(recordInfo);
		return dataGroup;
	}

}

package se.uu.ub.cora.batchrunner;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.change.IteratorSpy;
import se.uu.ub.cora.batchrunner.change.JsonArraySpy;
import se.uu.ub.cora.batchrunner.change.JsonObjectSpy;
import se.uu.ub.cora.batchrunner.change.JsonParserSpy;
import se.uu.ub.cora.batchrunner.change.JsonStringSpy;
import se.uu.ub.cora.clientdata.ClientDataList;

public class JsonToClientDataTest {

	public String jsonListToConvert = "{\"dataList\":{\"fromNo\":\"0\",\"data\":[{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"someSearchTerm\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"searchTerm\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/recordType/searchTerm\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/systemOneUser/141414\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/system/cora\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01T00:00:00.000000Z\"},{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/systemOneUser/141414\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"updatedBy\"},{\"name\":\"tsUpdated\",\"value\":\"2017-12-20T11:14:56.557000Z\"}],\"name\":\"updated\"}],\"name\":\"recordInfo\"},{\"name\":\"searchTermType\",\"value\":\"linkedData\"}],\"name\":\"searchTerm\"},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/searchTerm/someSearchTerm\",\"accept\":\"application/vnd.uub.record+json\"},\"update\":{\"requestMethod\":\"POST\",\"rel\":\"update\",\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/searchTerm/someSearchTerm\",\"accept\":\"application/vnd.uub.record+json\"},\"index\":{\"requestMethod\":\"POST\",\"rel\":\"index\",\"body\":{\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"searchTerm\"}],\"name\":\"recordType\"},{\"name\":\"recordId\",\"value\":\"someSearchTerm\"},{\"name\":\"type\",\"value\":\"index\"}],\"name\":\"workOrder\"},\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/workOrder/\",\"accept\":\"application/vnd.uub.record+json\"},\"delete\":{\"requestMethod\":\"DELETE\",\"rel\":\"delete\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/searchTerm/someSearchTerm\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"recordIdSearchTerm\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"searchTerm\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/recordType/searchTerm\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/systemOneUser/141414\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/system/cora\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01T00:00:00.000000Z\"},{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/systemOneUser/141414\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"updatedBy\"},{\"name\":\"tsUpdated\",\"value\":\"2017-12-20T11:15:39.879000Z\"}],\"name\":\"updated\"}],\"name\":\"recordInfo\"},{\"name\":\"searchTermType\",\"value\":\"final\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"text\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/recordType/text\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"searchInRecordType\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"collectIndexTerm\"},{\"name\":\"linkedRecordId\",\"value\":\"recordIdCollectIndexTerm\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/collectIndexTerm/recordIdCollectIndexTerm\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"indexTerm\"}],\"name\":\"searchTerm\"},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/searchTerm/recordIdSearchTerm\",\"accept\":\"application/vnd.uub.record+json\"},\"update\":{\"requestMethod\":\"POST\",\"rel\":\"update\",\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/searchTerm/recordIdSearchTerm\",\"accept\":\"application/vnd.uub.record+json\"},\"index\":{\"requestMethod\":\"POST\",\"rel\":\"index\",\"body\":{\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"searchTerm\"}],\"name\":\"recordType\"},{\"name\":\"recordId\",\"value\":\"recordIdSearchTerm\"},{\"name\":\"type\",\"value\":\"index\"}],\"name\":\"workOrder\"},\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/workOrder/\",\"accept\":\"application/vnd.uub.record+json\"},\"delete\":{\"requestMethod\":\"DELETE\",\"rel\":\"delete\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/searchTerm/recordIdSearchTerm\"}}}}],\"totalNo\":\"2\",\"containDataOfType\":\"searchTerm\",\"toNo\":\"2\"}}";
	private JsonParserSpy jsonParser;
	private JsonToClientDataImp jsonToClientData;
	private JsonToDataRecordConverterSpy dataRecordConverter;

	@BeforeMethod
	public void setUp() {
		jsonParser = new JsonParserSpy();
		jsonToClientData = JsonToClientDataImp.usingJsonParser(jsonParser);
	}

	@Test
	public void testInit() {
		assertSame(jsonToClientData.getJsonParser(), jsonParser);
	}

	@Test
	public void testJsonStringAsDataRecordList() {
		dataRecordConverter = new JsonToDataRecordConverterSpy();
		ClientDataList dataRecordList = jsonToClientData
				.getJsonStringAsClientDataRecordList(dataRecordConverter, jsonListToConvert);
		assertSame(jsonToClientData.getjsonToDataRecordConverter(), dataRecordConverter);

		assertEquals(jsonParser.jsonStringsSentToParser.get(0), jsonListToConvert);

		JsonObjectSpy listObject = jsonParser.jsonObjectSpies.get(0);
		assertEquals(listObject.getValueKeys.get(0), "dataList");

		JsonObjectSpy dataListObject = listObject.getValueObjectsReturned.get(0);
		assertVariablesInListAreParsedFromJson(dataRecordList, dataListObject);
		assertEquals(dataListObject.getValueKeys.get(4), "data");

		List<JsonObjectSpy> returnedJsonObjects = getReturnedObjectsFromSpy(dataListObject);

		assertConvertedRecordIsAddedToList(returnedJsonObjects, dataRecordList, 0);
		assertConvertedRecordIsAddedToList(returnedJsonObjects, dataRecordList, 1);

	}

	private void assertVariablesInListAreParsedFromJson(ClientDataList dataRecordList,
			JsonObjectSpy dataListObject) {
		checkContainDataOfTypeIsParsedFromJson(dataRecordList, dataListObject);
		checkFromNoIsParsedFromJson(dataRecordList, dataListObject);
		checkToNoIsParsedFromJson(dataRecordList, dataListObject);
		checkTotalNoIsParsedFromJson(dataRecordList, dataListObject);
	}

	private void checkContainDataOfTypeIsParsedFromJson(ClientDataList dataRecordList,
			JsonObjectSpy dataListObject) {
		assertEquals(dataListObject.getValueKeys.get(0), "containDataOfType");
		JsonStringSpy jsonStringSpy = dataListObject.getValueStringsReturned.get(0);
		assertEquals(dataRecordList.getContainDataOfType(), jsonStringSpy.value);
	}

	private void checkFromNoIsParsedFromJson(ClientDataList dataRecordList,
			JsonObjectSpy dataListObject) {
		assertEquals(dataListObject.getValueKeys.get(1), "fromNo");
		JsonStringSpy jsonStringSpy = dataListObject.getValueStringsReturned.get(1);
		assertEquals(dataRecordList.getFromNo(), jsonStringSpy.value);
	}

	private void checkToNoIsParsedFromJson(ClientDataList dataRecordList,
			JsonObjectSpy dataListObject) {
		assertEquals(dataListObject.getValueKeys.get(2), "toNo");
		JsonStringSpy jsonStringSpy = dataListObject.getValueStringsReturned.get(2);
		assertEquals(dataRecordList.getToNo(), jsonStringSpy.value);
	}

	private void checkTotalNoIsParsedFromJson(ClientDataList dataRecordList,
			JsonObjectSpy dataListObject) {
		assertEquals(dataListObject.getValueKeys.get(3), "totalNo");
		JsonStringSpy jsonStringSpy = dataListObject.getValueStringsReturned.get(3);
		assertEquals(dataRecordList.getTotalNo(), jsonStringSpy.value);
	}

	private void assertConvertedRecordIsAddedToList(List<JsonObjectSpy> returnedJsonObjects,
			ClientDataList dataRecordList, int index) {
		JsonObjectSpy record = returnedJsonObjects.get(index);
		assertSame(dataRecordConverter.jsonObjects.get(index), record);
		assertSame(dataRecordConverter.returnedRecords.get(index),
				dataRecordList.getDataList().get(index));
	}

	private List<JsonObjectSpy> getReturnedObjectsFromSpy(JsonObjectSpy dataListObject) {
		JsonArraySpy dataArray = dataListObject.getValueArraysReturned.get(0);
		IteratorSpy iteratorSpy = dataArray.iteratorSpy;

		List<JsonObjectSpy> returnedJsonObjects = iteratorSpy.returnedJsonObjects;
		return returnedJsonObjects;
	}
}

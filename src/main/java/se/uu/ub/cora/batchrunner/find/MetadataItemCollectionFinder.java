package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonString;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public class MetadataItemCollectionFinder implements Finder {

	private static final String CHILDREN = "children";
	private String urlString;
	private HttpHandlerFactory httpHandlerFactory;


	@Override
	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	@Override
	public Collection<String> findRecords() {
		HttpHandler httpHandler = httpHandlerFactory.factor(urlString);
		httpHandler.setRequestMethod("GET");
		String responseText = httpHandler.getResponseText();

		List<String> ids = new ArrayList<>();
		JsonArray data = getDataFromListOfRecords(responseText);
		for (JsonValue value : data) {
			findRecordsWithOneItemReference(ids, value);
		}

		return ids;
	}

	private void findRecordsWithOneItemReference(List<String> ids, JsonValue value) {
		JsonArray children = extractChildrenFromRecordData(value);
		int numberOfItemReferences = getNumberOfItemReferences(children);

		String recordId = getIdFromRecordInfo(children);
		possiblyAddIdToFoundRecords(ids, recordId, numberOfItemReferences);
	}

	private JsonArray extractChildrenFromRecordData(JsonValue value) {
		JsonObject recordData = getDataPartOfRecord(value);

		return recordData.getValueAsJsonArray(CHILDREN);
	}

	private JsonObject getDataPartOfRecord(JsonValue value) {
		JsonObject record = ((JsonObject) value).getValueAsJsonObject("record");
		return record.getValueAsJsonObject("data");
	}

	private int getNumberOfItemReferences(JsonArray children) {
		int numberOfItemReferences = 0;
		JsonObject collectionItemReferences = (JsonObject) findChildWithName("collectionItemReferences", children);
		if(collectionItemReferences != null) {
			numberOfItemReferences = countNumberOfItemReferences(collectionItemReferences);
		}
		return numberOfItemReferences;
	}

	private String getIdFromRecordInfo(JsonArray children) {
		JsonObject recordInfo = (JsonObject) findChildWithName("recordInfo", children);
		return getIdFromRecordInfo(recordInfo);
	}

	private JsonValue findChildWithName(String nameToFind, JsonArray children){
		for (JsonValue child : children) {
			JsonObject objectChild = (JsonObject) child;
			String name = extractNameFromObject(objectChild);
			if(name.equals(nameToFind)){
				return objectChild;
			}

		}
		return null;
	}

	private String extractNameFromObject(JsonObject objectChild) {
		return objectChild.getValueAsJsonString("name").getStringValue();
	}

	private void possiblyAddIdToFoundRecords(List<String> ids, String recordId, int numberOfItemReferences) {
		if (numberOfItemReferences == 1) {
            ids.add(recordId);
        }
	}

	private String getIdFromRecordInfo(JsonObject objectChild) {
		String recordId = "";
		for (JsonValue recordInfoChild : objectChild.getValueAsJsonArray(CHILDREN)) {
            JsonObject recordInfoObjectChild = (JsonObject) recordInfoChild;
            String name = extractNameFromObject(recordInfoObjectChild);
			if ("id".equals(name)) {
				recordId = extractValueFromObject(recordInfoObjectChild);
            }
        }
		return recordId;
	}


	private String extractValueFromObject(JsonObject recordInfoObjectChild) {
		JsonString nameValue = recordInfoObjectChild
                .getValueAsJsonString("value");
		return nameValue.getStringValue();
	}

	private int countNumberOfItemReferences(JsonObject objectChild) {
		int numberOfItemReferences = 0;
		for (JsonValue itemRefChild : objectChild.getValueAsJsonArray(CHILDREN)) {
            JsonObject itemRefObjectChild = (JsonObject) itemRefChild;
            String itemRefChildName = extractNameFromObject(itemRefObjectChild);
            if ("ref".equals(itemRefChildName)) {
                numberOfItemReferences++;
            }
        }
		return numberOfItemReferences;
	}


	private JsonArray getDataFromListOfRecords(String responseText) {
		JsonParser jsonParser = new OrgJsonParser();
		JsonObject jsonValue = jsonParser.parseStringAsObject(responseText);
		JsonObject dataList = (JsonObject) jsonValue.getValue("dataList");
		return dataList.getValueAsJsonArray("data");
	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactory) {
		this.httpHandlerFactory = httpHandlerFactory;

	}
}

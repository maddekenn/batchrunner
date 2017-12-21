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
		int responseCode = httpHandler.getResponseCode();
		String responseText = httpHandler.getResponseText();

		List<String> ids = new ArrayList<>();
		JsonArray data = getDataFromListOfRecords(responseText);
		for (JsonValue value : data) {
			findRecordsWithOneItemReference(ids, value);
		}

		return ids;
	}

	private void findRecordsWithOneItemReference(List<String> ids, JsonValue value) {
		JsonObject recordData = getDataPartOfRecord(value);

		JsonArray children = recordData.getValueAsJsonArray("children");
		int numberOfItemReferences = 0;
		numberOfItemReferences = getNumberOfItemReferences(children, numberOfItemReferences);
		JsonObject recordInfo = (JsonObject) findChildWithName("recordInfo", children);
		String recordId = getIdFromRecordInfo(recordInfo);
		possiblyAddIdToFoundRecords(ids, recordId, numberOfItemReferences);
	}

	private int getNumberOfItemReferences(JsonArray children, int numberOfItemReferences) {
		JsonObject collectionItemReferences = (JsonObject) findChildWithName("collectionItemReferences", children);
		if(collectionItemReferences != null) {
			numberOfItemReferences = countNumberOfItemReferences(collectionItemReferences);
		}
		return numberOfItemReferences;
	}


	private JsonObject getDataPartOfRecord(JsonValue value) {
		JsonObject record = ((JsonObject) value).getValueAsJsonObject("record");
		JsonObject recordData = record.getValueAsJsonObject("data");
		return recordData;
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
		for (JsonValue recordInfoChild : objectChild.getValueAsJsonArray("children")) {
            JsonObject recordInfoObjectChild = (JsonObject) recordInfoChild;
            String name = extractNameFromObject(recordInfoObjectChild);
            if (name.equals("id")) {
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
		for (JsonValue itemRefChild : objectChild.getValueAsJsonArray("children")) {
            JsonObject itemRefObjectChild = (JsonObject) itemRefChild;
            String itemRefChildName = extractNameFromObject(itemRefObjectChild);
            if (itemRefChildName.equals("ref")) {
                numberOfItemReferences++;
            }
        }
		return numberOfItemReferences;
	}


	private JsonArray getDataFromListOfRecords(String responseText) {
		JsonParser jsonParser = new OrgJsonParser();
		JsonObject jsonValue = jsonParser.parseStringAsObject(responseText);
		JsonObject dataList = (JsonObject) jsonValue.getValue("dataList");
		JsonArray data = dataList.getValueAsJsonArray("data");
		return data;
	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactory) {
		this.httpHandlerFactory = httpHandlerFactory;

	}
}

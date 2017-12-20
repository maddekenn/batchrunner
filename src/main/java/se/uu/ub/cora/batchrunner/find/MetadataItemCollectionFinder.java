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
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class MetadataItemCollectionFinder implements Finder {

	private RecordStorage recordStorage;
	private String urlString;
	private HttpHandlerFactory httpHandlerFactory;

	@Override
	public void setRecordStorage(RecordStorage recordStorage) {
		this.recordStorage = recordStorage;
	}

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
			JsonObject recordData = getDataPartOFRecord(value);

			JsonArray children = recordData.getValueAsJsonArray("children");
			String recordId = "";
			int numberOfItemReferences = 0;
			for (JsonValue child : children) {
				JsonObject objectChild = (JsonObject) child;
				JsonString name = objectChild.getValueAsJsonString("name");

				if ("collectionItemReferences".equals(name.getStringValue())) {
					for (JsonValue itemRefChild : objectChild.getValueAsJsonArray("children")) {
						JsonObject itemRefObjectChild = (JsonObject) itemRefChild;
						JsonString itemRefChildName = itemRefObjectChild
								.getValueAsJsonString("name");
						if (itemRefChildName.getStringValue().equals("ref")) {
							numberOfItemReferences++;
						}
					}
				}

				else if (name.getStringValue().equals("recordInfo")) {
					for (JsonValue recordInfoChild : objectChild.getValueAsJsonArray("children")) {
						JsonObject recordInfoObjectChild = (JsonObject) recordInfoChild;
						JsonString name2 = recordInfoObjectChild.getValueAsJsonString("name");
						if (name2.getStringValue().equals("id")) {
							JsonString nameValue = recordInfoObjectChild
									.getValueAsJsonString("value");
							recordId = nameValue.getStringValue();

						}
					}
				}
			}
			if (numberOfItemReferences == 1) {
				ids.add(recordId);
			}

		}

		return ids;
	}

	private JsonObject getDataPartOFRecord(JsonValue value) {
		JsonObject record = ((JsonObject) value).getValueAsJsonObject("record");
		JsonObject recordData = record.getValueAsJsonObject("data");
		return recordData;
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

package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonValue;

public class MetadataItemCollectionOneReferenceFinder extends MetadataFinder implements Finder {

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

		return getRecordsWithOneItemReferenceFromResponse(responseText);

	}

	private List<String> getRecordsWithOneItemReferenceFromResponse(String responseText) {
		List<String> ids = new ArrayList<>();
		JsonArray data = getDataFromListOfRecords(responseText);
		for (JsonValue value : data) {
			getRecordsWithOneItemReference(ids, value);
		}
		return ids;
	}

	private void getRecordsWithOneItemReference(List<String> ids, JsonValue value) {
		JsonArray children = extractChildrenFromRecordData(value);
		int numberOfItemReferences = getNumberOfItemReferences(children);

		String recordId = getRecordIdentifierFromRecordInfo(children);
		possiblyAddIdToFoundRecords(ids, recordId, numberOfItemReferences);
	}

	private int getNumberOfItemReferences(JsonArray children) {
		int numberOfItemReferences = 0;
		JsonObject collectionItemReferences = (JsonObject) findChildWithName(
				"collectionItemReferences", children);
		if (collectionItemReferences != null) {
			numberOfItemReferences = countNumberOfItemReferences(collectionItemReferences);
		}
		return numberOfItemReferences;
	}

	private void possiblyAddIdToFoundRecords(List<String> ids, String recordId,
			int numberOfItemReferences) {
		if (numberOfItemReferences == 1) {
			ids.add(recordId);
		}
	}

	private int countNumberOfItemReferences(JsonObject objectChild) {
		int numberOfItemReferences = 0;
		for (JsonValue itemRefChild : objectChild.getValueAsJsonArray(CHILDREN)) {
			JsonObject itemRefObjectChild = (JsonObject) itemRefChild;
			numberOfItemReferences = countItemReferenceIfMatch(numberOfItemReferences,
					itemRefObjectChild);
		}
		return numberOfItemReferences;
	}

	private int countItemReferenceIfMatch(int numberOfItemReferences,
			JsonObject itemRefObjectChild) {
		String itemRefChildName = extractNameFromObject(itemRefObjectChild);
		if ("ref".equals(itemRefChildName)) {
			numberOfItemReferences++;
		}
		return numberOfItemReferences;
	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactory) {
		this.httpHandlerFactory = httpHandlerFactory;

	}

}

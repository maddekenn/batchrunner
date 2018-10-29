package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonValue;

public class UnusedRecordsFinder extends MetadataFinder implements Finder {

	private String urlString;
	private HttpHandlerFactory httpHandlerFactory;
	List<String> pVarsUsedInCode = Arrays.asList("linkedRecordIdPVar", "linkedRecordIdOutputPVar",
			"linkedRecordTypePVar", "linkedRecordTypeOutputPVar", "linkedRepeatIdPVar",
			"linkedRepeatIdOutputPVar");

	@Override
	public void setUrlString(String url) {
		urlString = url;
	}

	@Override
	public Collection<String> findRecords() {
		HttpHandler httpHandler = httpHandlerFactory.factor(urlString);
		httpHandler.setRequestMethod("GET");
		String responseText = httpHandler.getResponseText();
		return findAllUnusedPresentationGroupIds(responseText);
	}

	private Collection<String> findAllUnusedPresentationGroupIds(String responseText) {
		List<String> ids = new ArrayList<>();
		JsonArray data = getDataFromListOfRecords(responseText);
		for (JsonValue value : data) {
			possiblyAddPresentationGroupIdToFoundRecords(ids, value);
		}
		return ids;
	}

	private void possiblyAddPresentationGroupIdToFoundRecords(List<String> ids, JsonValue value) {
		JsonObject record = ((JsonObject) value).getValueAsJsonObject("record");
		if (recordHasNoReferenceFromOtherRecords(record) && isNotUsedInCode(value)) {
			addPresentationGroupIdToFoundRecords(ids, value);
		}
	}

	private boolean recordHasNoReferenceFromOtherRecords(JsonObject record) {
		JsonObject actionLinks = record.getValueAsJsonObject("actionLinks");
		return !actionLinks.containsKey("read_incoming_links");
	}

	private boolean isNotUsedInCode(JsonValue value) {
		JsonArray children = extractChildrenFromRecordData(value);
		String recordId = getRecordIdentifierFromRecordInfo(children);
		return !pVarsUsedInCode.contains(recordId);
	}

	private void addPresentationGroupIdToFoundRecords(List<String> ids, JsonValue value) {
		JsonArray children = extractChildrenFromRecordData(value);
		String recordId = getRecordIdentifierFromRecordInfo(children);
		ids.add(recordId);
	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactory) {
		this.httpHandlerFactory = httpHandlerFactory;
	}

}

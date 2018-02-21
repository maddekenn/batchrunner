package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonValue;

public class RecordFinder extends MetadataFinder implements Finder {

	private String urlString;
	private HttpHandlerFactory httpHandlerFactory;

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactory) {
		this.httpHandlerFactory = httpHandlerFactory;
	}

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
			JsonArray children = extractChildrenFromRecordData(value);
			String recordId = getIdFromRecordInfo(children);
			ids.add(recordId);
		}

		return ids;
	}
}

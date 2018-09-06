package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class DataGroupCopier implements DataCopier {

	private final String url;
	private final HttpHandlerFactory httpHandlerFactory;
	DataGroupJsonCopier jsonCopier;

	private DataGroupCopier(String url, HttpHandlerFactory httpHandlerFactory) {
		this.url = url;
		this.httpHandlerFactory = httpHandlerFactory;
	}

	public static DataGroupCopier usingURLAndHttpHandlerFactory(String url,
			HttpHandlerFactory httpHandlerFactory) {
		return new DataGroupCopier(url, httpHandlerFactory);
	}

	@Override
	public String copyTypeFromIdToNewId(String type, String id, String newId) {
		String readJson;
		try {
			readJson = readGroup(type, id);
		} catch (RecordNotFoundException e) {
			return "404 " + e.getMessage() + " " + id;
		}
		jsonCopier = new DataGroupJsonCopier();
		String newJson = jsonCopier.copyDataGroupAsJsonExcludeLinksUsingJsonAndNewId(readJson,
				newId);
		return createNewDataGroup(type, newJson);

	}

	private String createNewDataGroup(String type, String newJson) {
		HttpHandler httpHandler = createHttpHandlerForPostWithUrlAndJson(url + type);
		httpHandler.setOutput(newJson);
		int responseCode = httpHandler.getResponseCode();
		return constructMessage(newJson, httpHandler, responseCode);
	}

	private String constructMessage(String newJson, HttpHandler httpHandler, int responseCode) {
		if (responseCode != 201 && responseCode != 200) {
			return String.valueOf(responseCode) + " " + httpHandler.getErrorText()
					+ " Error creating: " + newJson;
		}
		return String.valueOf(responseCode) + " Ok creating: " + newJson;
	}

	private String readGroup(String type, String groupId) {
		HttpHandler httpHandler = httpHandlerFactory.factor(url + type + "/" + groupId);
		httpHandler.setRequestMethod("GET");
		if (httpHandler.getResponseCode() == 404) {
			throw new RecordNotFoundException("Unable to read dataGroup");
		}
		return httpHandler.getResponseText();

	}

	private HttpHandler createHttpHandlerForPostWithUrlAndJson(String urlString) {
		HttpHandler createHttpHandler = httpHandlerFactory.factor(urlString);
		createHttpHandler.setRequestMethod("POST");
		createHttpHandler.setRequestProperty("Accept", "application/vnd.uub.record+json");
		createHttpHandler.setRequestProperty("Content-Type", "application/vnd.uub.record+json");
		return createHttpHandler;

	}

	@Override
	public HttpHandlerFactory getHttpHandler() {
		return httpHandlerFactory;
	}

	@Override
	public String getUrl() {
		return url;
	}

}

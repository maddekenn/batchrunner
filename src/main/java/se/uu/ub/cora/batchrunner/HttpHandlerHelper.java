package se.uu.ub.cora.batchrunner;

import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class HttpHandlerHelper {
	private String url;
	private HttpHandlerFactory httpHandlerFactory;

	private HttpHandlerHelper(String url, HttpHandlerFactory httpHandlerFactory) {
		this.url = url;
		this.httpHandlerFactory = httpHandlerFactory;
	}

	public static HttpHandlerHelper usingURLAndHttpHandlerFactory(String url,
			HttpHandlerFactory httpHandlerFactory) {
		return new HttpHandlerHelper(url, httpHandlerFactory);
	}

	public String readRecord(String type, String recordId) {
		HttpHandler httpHandler = httpHandlerFactory.factor(url + type + "/" + recordId);
		httpHandler.setRequestMethod("GET");
		if (httpHandler.getResponseCode() == 404) {
			throw new RecordNotFoundException(
					"Unable to read record with type: " + type + " and id: " + recordId);
		}
		return httpHandler.getResponseText();

	}

	public String createRecord(String type, String newJson) {
		HttpHandler httpHandler = createHttpHandlerForPostWithUrlAndJson(url + type);
		httpHandler.setOutput(newJson);
		int responseCode = httpHandler.getResponseCode();
		return constructMessage(newJson, httpHandler, responseCode);
	}

	private HttpHandler createHttpHandlerForPostWithUrlAndJson(String urlString) {
		HttpHandler createHttpHandler = httpHandlerFactory.factor(urlString);
		createHttpHandler.setRequestMethod("POST");
		createHttpHandler.setRequestProperty("Accept", "application/vnd.uub.record+json");
		createHttpHandler.setRequestProperty("Content-Type", "application/vnd.uub.record+json");
		return createHttpHandler;

	}

	private String constructMessage(String newJson, HttpHandler httpHandler, int responseCode) {
		if (responseCode != 201) {
			return String.valueOf(responseCode) + " " + httpHandler.getErrorText()
					+ " Error creating: " + newJson;
		}
		return String.valueOf(responseCode) + " Ok creating: " + newJson;
	}

	HttpHandlerFactory getHttpHandler() {
		return httpHandlerFactory;
	}

	public String getUrl() {
		return url;
	}

}

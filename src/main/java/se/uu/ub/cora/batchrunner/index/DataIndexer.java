package se.uu.ub.cora.batchrunner.index;

import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class DataIndexer {
	private HttpHandlerFactory httpHandlerFactory;
	private String url;
	private String authToken;

	public DataIndexer(String url, HttpHandlerFactory httpHandlerFactory, String authToken) {
		this.url = url;
		this.httpHandlerFactory = httpHandlerFactory;
		this.authToken = authToken;
	}

	public void indexDataWithRecordTypeAndIdsToIndex(String recordType, List<String> idsToIndex) {
		url = url + "?authToken=" + authToken;
		HttpHandler httpHandler = httpHandlerFactory.factor(url);
		httpHandler.setRequestMethod("POST");
		httpHandler.setRequestProperty("Accept", "application/vnd.uub.record+json");
		httpHandler.setRequestProperty("Content-Type", "application/vnd.uub.record+json");
		for (String id : idsToIndex) {
			String json = "{\"name\":\"workOrder\",\"children\":[{\"name\":\"recordType\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\""
					+ recordType + "\"}]},{\"name\":\"recordId\",\"value\":\"" + id
					+ "\"},{\"name\":\"type\",\"value\":\"index\"}]}";
			httpHandler.setOutput(json);
			// TODO:write test for this code
			// Status statusCode =
			// Response.Status.fromStatusCode(httpHandler.getResponseCode());
			// if (statusCode.equals(Response.Status.CREATED)) {
			//
			// }
		}
	}

	public static DataIndexer withBaseURLHttpHandlerFactoryAndAuthToken(String url,
			HttpHandlerFactory httpHandlerFactory, String authToken) {
		return new DataIndexer(url, httpHandlerFactory, authToken);
	}
}

package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.batchrunner.HttpHandlerHelper;
import se.uu.ub.cora.batchrunner.RecordNotFoundException;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class DataRecordCopier implements DataCopier {

	private final String url;
	private final HttpHandlerFactory httpHandlerFactory;
	DataGroupJsonCopier jsonCopier;

	private DataRecordCopier(String url, HttpHandlerFactory httpHandlerFactory) {
		this.url = url;
		this.httpHandlerFactory = httpHandlerFactory;
	}

	public static DataRecordCopier usingURLAndHttpHandlerFactory(String url,
			HttpHandlerFactory httpHandlerFactory) {
		return new DataRecordCopier(url, httpHandlerFactory);
	}

	@Override
	public String copyTypeFromIdToNewId(String type, String id, String newId) {
		String readJson;
		try {
			HttpHandlerHelper httpHelper = HttpHandlerHelper.usingURLAndHttpHandlerFactory(url,
					httpHandlerFactory);
			readJson = httpHelper.readRecord(type, id);
		} catch (RecordNotFoundException e) {
			return "404 " + e.getMessage() + " " + id;
		}
		jsonCopier = new DataGroupJsonCopier();
		String newJson = jsonCopier.copyDataGroupAsJsonExcludeLinksUsingJsonAndNewId(readJson,
				newId);
		return createRecord(type, newJson);

	}

	private String createRecord(String type, String newJson) {
		HttpHandlerHelper httpHelper = HttpHandlerHelper.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		return httpHelper.createRecord(type, newJson);
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

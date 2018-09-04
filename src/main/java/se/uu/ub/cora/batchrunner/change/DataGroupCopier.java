package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

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
		String readJson = readGroup(type, id);
		jsonCopier = new DataGroupJsonCopier();
		return "";
	}

	private String readGroup(String type, String groupId) {
		HttpHandler httpHandler = httpHandlerFactory.factor(url + type + "/" + groupId);
		httpHandler.setRequestMethod("GET");
		return httpHandler.getResponseText();
		// return
		// copier.copyDataGroupAsJsonExcludeLinksUsingJsonAndNewId(pGroupToCopyJson,
		// newPGroupEnding);

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

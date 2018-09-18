package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class DataDividerUpdater {

	private String url;
	private HttpHandlerFactory httpHandlerFactory;

	private DataDividerUpdater(String url, HttpHandlerFactory httpHandlerFactory) {
		this.url = url;
		this.httpHandlerFactory = httpHandlerFactory;
	}

	public static DataDividerUpdater usingURLAndHttpHandlerFactory(String url,
			HttpHandlerFactory httpHandlerFactory) {
		return new DataDividerUpdater(url, httpHandlerFactory);
	}

	HttpHandlerFactory getHttpHandler() {
		return httpHandlerFactory;
	}

	public String getUrl() {
		return url;
	}

}

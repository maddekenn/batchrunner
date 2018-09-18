package se.uu.ub.cora.batchrunner;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class HttpHandlerHelper {
	private String url;
	private HttpHandlerFactory httpHandlerFactory;

	public static HttpHandlerHelper usingURLAndHttpHandlerFactory(String url,
			HttpHandlerFactory httpHandlerFactory) {
		return new HttpHandlerHelper(url, httpHandlerFactory);
	}

	private HttpHandlerHelper(String url, HttpHandlerFactory httpHandlerFactory) {
		this.url = url;
		this.httpHandlerFactory = httpHandlerFactory;
	}

	HttpHandlerFactory getHttpHandler() {
		return httpHandlerFactory;
	}

	public String getUrl() {
		return url;
	}

}

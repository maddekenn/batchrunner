package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class DataCopierSpy implements DataCopier {

	public List<String> types = new ArrayList<>();
	public List<String> ids = new ArrayList<>();
	public List<String> newIds = new ArrayList<>();

	private DataCopierSpy(String url, HttpHandlerFactory httpHandlerFactory) {
		// this.url = url;
		// this.httpHandlerFactory = httpHandlerFactory;
	}

	public static DataCopierSpy usingURLAndHttpHandlerFactory(String url,
			HttpHandlerFactory httpHandlerFactory) {
		return new DataCopierSpy(url, httpHandlerFactory);
	}

	@Override
	public String copyTypeFromIdToNewId(String type, String id, String newId) {
		types.add(type);
		ids.add(id);
		newIds.add(newId);
		return "a message";
	}

	@Override
	public HttpHandlerFactory getHttpHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

}

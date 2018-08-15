package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class FinderSpy implements Finder {
	public boolean findRecordsCalled = false;
	public String url;
	public List<String> ids;
	public HttpHandlerFactory httpHandlerFactory;

	@Override
	public void setUrlString(String url) {
		this.url = url;
	}

	@Override
	public Collection<String> findRecords() {
		findRecordsCalled = true;
		String id = "someId";
		ids = new ArrayList<>();
		ids.add(id);
		return ids;
	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactory) {
		this.httpHandlerFactory = httpHandlerFactory;
	}

}

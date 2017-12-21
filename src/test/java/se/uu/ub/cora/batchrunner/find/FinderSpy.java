package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class FinderSpy implements Finder {
	boolean findRecordCalled = false;
	public String url;


	@Override
	public void setUrlString(String url) {
		this.url = url;
	}

	@Override
	public Collection<String> findRecords() {
		findRecordCalled = true;
		String id = "someId";
		List<String> ids = new ArrayList<>();
		ids.add(id);
		return ids;
	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactorySpy) {
	}
}

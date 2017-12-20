package se.uu.ub.cora.batchrunner.find;

import java.util.Collection;
import java.util.Collections;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class FinderSpy implements Finder {
	boolean findRecordCalled = false;
	public String url;

	@Override
	public void setRecordStorage(RecordStorage recordStorage) {

	}

	@Override
	public void setUrlString(String url) {
		this.url = url;
	}

	@Override
	public Collection<String> findRecords() {
		findRecordCalled = true;
		return Collections.emptyList();
	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactorySpy) {
		// TODO Auto-generated method stub

	}
}

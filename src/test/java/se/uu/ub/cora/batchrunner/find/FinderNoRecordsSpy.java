package se.uu.ub.cora.batchrunner.find;

import java.util.Collection;
import java.util.Collections;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.javaclient.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class FinderNoRecordsSpy implements Finder {
	public boolean findRecordCalled = false;
	public String url;

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

	public Finder usingCoraClientFactoryAndClientConfig(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		// TODO Auto-generated method stub
		return null;
	}
}

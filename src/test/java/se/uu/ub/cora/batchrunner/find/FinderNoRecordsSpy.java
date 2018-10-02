package se.uu.ub.cora.batchrunner.find;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import se.uu.ub.cora.client.CoraClientConfig;
import se.uu.ub.cora.client.CoraClientFactory;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

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

	@Override
	public List<RecordIdentifier> findRecordsUsingRecordIdentifier(
			RecordIdentifier recordIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

	public Finder usingCoraClientFactoryAndClientConfig(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		// TODO Auto-generated method stub
		return null;
	}
}

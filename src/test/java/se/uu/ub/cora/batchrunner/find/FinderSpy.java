package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.javaclient.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class FinderSpy implements Finder {
	public boolean findRecordsCalled = false;
	public String url;
	public List<String> ids;
	public HttpHandlerFactory httpHandlerFactory;
	public CoraClientFactory coraClientFactory;
	public CoraClientConfig coraClientConfig;
	public RecordIdentifier recordIdentifier;

	public FinderSpy() {
	}

	public FinderSpy(CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

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

	public static Finder usingCoraClientFactoryAndClientConfig(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		return new FinderSpy(coraClientFactory, coraClientConfig);
	}

}

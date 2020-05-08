package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.javaclient.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class PGroupFinderSpy implements Finder {
	public boolean findRecordsCalled = false;
	public String url;
	public List<String> ids;
	public List<String> recordTypeNames;
	public HttpHandlerFactory httpHandlerFactory;

	public PGroupFinderSpy(List<String> recordTypeNames) {
		this.recordTypeNames = recordTypeNames;
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

	public static PGroupFinderSpy usingListOfRecordTypes(List<String> recordTypeNames) {
		return new PGroupFinderSpy(recordTypeNames);
	}

	public Finder usingCoraClientFactoryAndClientConfig(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		// TODO Auto-generated method stub
		return null;
	}
}

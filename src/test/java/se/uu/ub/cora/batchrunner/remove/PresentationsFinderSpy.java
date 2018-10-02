package se.uu.ub.cora.batchrunner.remove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.client.CoraClientConfig;
import se.uu.ub.cora.client.CoraClientFactory;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class PresentationsFinderSpy implements Finder {
	public boolean findRecordsCalled = false;
	public String urlString;

	@Override
	public void setUrlString(String urlString) {
		this.urlString = urlString;

	}

	@Override
	public Collection<String> findRecords() {
		findRecordsCalled = true;
		List<String> ids = new ArrayList<>();
		ids.add("unusedPresentation");
		return ids;
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

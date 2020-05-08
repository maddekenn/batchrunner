package se.uu.ub.cora.batchrunner.remove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.javaclient.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

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

	public Finder usingCoraClientFactoryAndClientConfig(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		// TODO Auto-generated method stub
		return null;
	}

}

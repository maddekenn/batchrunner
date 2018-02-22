package se.uu.ub.cora.batchrunner.remove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class PresentationGroupFinderSpy implements Finder {
	public boolean findRecordsCalled = false;

	@Override
	public void setUrlString(String url) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<String> findRecords() {
		findRecordsCalled = true;
		List<String> ids = new ArrayList<>();
		ids.add("unusedPresentationPGroup");
		return ids;
	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactorySpy) {
		// TODO Auto-generated method stub

	}

}

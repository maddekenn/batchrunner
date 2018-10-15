package se.uu.ub.cora.batchrunner.remove;

import java.util.Collection;
import java.util.Collections;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class RemoverNoRecordsFoundSpy implements RecordRemover {

	public boolean removeRecordsWasCalled;
	public String urlString;
	public Finder finder;

	@Override
	public void setUrlString(String urlString) {
		this.urlString = urlString;

	}

	@Override
	public Collection<String> removeRecordsFoundByFinder() {
		removeRecordsWasCalled = true;
		return Collections.emptyList();
	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactorySpy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFinder(Finder finder) {
		this.finder = finder;
	}

}

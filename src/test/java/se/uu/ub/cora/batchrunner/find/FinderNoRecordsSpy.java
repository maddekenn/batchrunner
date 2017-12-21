package se.uu.ub.cora.batchrunner.find;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FinderNoRecordsSpy implements Finder {
	boolean findRecordCalled = false;
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
}

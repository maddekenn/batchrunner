package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class DeleterSpy implements Deleter {

	public boolean deletePGroupsWasCalled = false;
	public List<String> groupIdsSentToDelete = new ArrayList<>();
	public String url;
	public HttpHandlerFactory httpHandlerFactory;

	@Override
	public List<String> deletePGroups(List<String> pGroupNames) {
		deletePGroupsWasCalled = true;
		groupIdsSentToDelete.addAll(pGroupNames);
		return Collections.emptyList();
	}

	@Override
	public void setUrlString(String url) {
		this.url = url;

	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactorySpy) {
		httpHandlerFactory = httpHandlerFactorySpy;

	}

}

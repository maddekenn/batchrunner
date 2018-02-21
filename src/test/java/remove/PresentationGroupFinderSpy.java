package remove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class PresentationGroupFinderSpy implements Finder {

	@Override
	public void setUrlString(String url) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<String> findRecords() {
		List<String> ids = new ArrayList<>();
		ids.add("unusedPresentationPGroup");
		return ids;
	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactorySpy) {
		// TODO Auto-generated method stub

	}

}

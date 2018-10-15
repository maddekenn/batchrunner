package se.uu.ub.cora.batchrunner.remove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class RecordRemoverImp implements RecordRemover {

	private HttpHandlerFactory httpHandlerFactory;
	private String urlString;
	private List<String> removedIds = new ArrayList<>();
	private Finder finder;

	@Override
	public List<String> removeRecordsFoundByFinder() {
		Collection<String> foundRecords = finder.findRecords();
		for (String id : foundRecords) {
			System.out.println(id);
			HttpHandler httpHandler = httpHandlerFactory.factor(urlString + "/" + id);
			httpHandler.setRequestMethod("DELETE");
			Status statusType = Response.Status.fromStatusCode(httpHandler.getResponseCode());
			if (statusType.equals(Response.Status.OK)) {
				removedIds.add(id);
			}
		}
		return removedIds;
	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactory) {
		this.httpHandlerFactory = httpHandlerFactory;
	}

	@Override
	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	@Override
	public void setFinder(Finder finder) {
		this.finder = finder;
	}

}

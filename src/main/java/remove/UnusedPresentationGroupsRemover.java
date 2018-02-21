package remove;

import java.util.Collection;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class UnusedPresentationGroupsRemover {

	private Finder presentationGroupFinder;
	private HttpHandlerFactory httpHandlerFactory;
	private String urlString;

	private UnusedPresentationGroupsRemover(Finder presentationGroupFinder) {
		this.presentationGroupFinder = presentationGroupFinder;
	}

	public static UnusedPresentationGroupsRemover usingFinder(Finder presentationGroupFinder) {
		return new UnusedPresentationGroupsRemover(presentationGroupFinder);
	}

	public void removeUnusedPresentationGroups() {
		Collection<String> foundRecords = presentationGroupFinder.findRecords();
		for (String id : foundRecords) {
			HttpHandler httpHandler = httpHandlerFactory.factor(urlString + "/" + id);
			httpHandler.setRequestMethod("DELETE");
		}
	}

	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactory) {
		this.httpHandlerFactory = httpHandlerFactory;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;

	}

}

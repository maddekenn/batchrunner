package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class PGroupDeleter implements Deleter {

	private String url;
	private HttpHandlerFactory httpHandlerFactory;

	public String getUrl() {
		return url;
	}

	public HttpHandlerFactory getHttpHandlerFactory() {
		return httpHandlerFactory;
	}

	@Override
	public List<String> deletePGroups(List<String> pGroupNames) {
		List<String> errorMessages = new ArrayList<>();
		for (String pGroupName : pGroupNames) {
			System.out.println("pGroupName :" + pGroupName + " ");
			int response = deletePresentationGroup(pGroupName);
			possiblyAddErrorMessage(errorMessages, pGroupName, response);
		}
		return errorMessages;
	}

	private int deletePresentationGroup(String pGroupEnding) {
		HttpHandler pGroupDeleteHttpHandler = httpHandlerFactory
				.factor(url + "presentationGroup/" + pGroupEnding);
		pGroupDeleteHttpHandler.setRequestMethod("DELETE");
		return pGroupDeleteHttpHandler.getResponseCode();
	}

	private void possiblyAddErrorMessage(List<String> errorMessages, String pGroupName,
			int response) {
		if (response == 405) {
			errorMessages.add("Unable to delete pGroup: " + pGroupName);
		}
	}

	@Override
	public void setUrlString(String url) {
		this.url = url;

	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactory) {
		this.httpHandlerFactory = httpHandlerFactory;

	}

}

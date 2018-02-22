package se.uu.ub.cora.batchrunner.remove;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.batchrunner.find.HttpHandlerSpy;
import se.uu.ub.cora.batchrunner.remove.UnusedPresentationGroupsRemover;

public class UnusedPresentationGroupsRemoverTest {
	UnusedPresentationGroupsRemover remover;
	HttpHandlerFactorySpy httpHandlerFactory;
	PresentationGroupFinderSpy presentationGroupFinder;
	private String urlString;

	@BeforeMethod
	public void setUp() {
		httpHandlerFactory = new HttpHandlerFactorySpy();
		presentationGroupFinder = new PresentationGroupFinderSpy();
		urlString = "http://localhost:8080/presentationGroup";

		remover = new UnusedPresentationGroupsRemover();
		remover.setFinder(presentationGroupFinder);
		remover.setHttpHandlerFactory(httpHandlerFactory);
		remover.setUrlString(urlString);

	}

	@Test
	private void testRemoveUnusedPresentationGroups() {
		List<String> removedIds = remover.removeRecordsFoundByFinder();
		assertEquals(removedIds.size(), 1);

		HttpHandlerSpy httpHandlerSpy = httpHandlerFactory.httpHandlerSpy;
		assertEquals(httpHandlerSpy.requestMethod, "DELETE");
		assertEquals(httpHandlerSpy.urlString, urlString + "/unusedPresentationPGroup");
		assertEquals(httpHandlerSpy.requestProperties.size(), 0);
	}

	@Test
	private void testRemoveUnusedPresentationGroupsErrorResponseCode() {
		httpHandlerFactory.setResponseCode(401);
		List<String> removedIds = remover.removeRecordsFoundByFinder();
		assertEquals(removedIds.size(), 0);
	}

}

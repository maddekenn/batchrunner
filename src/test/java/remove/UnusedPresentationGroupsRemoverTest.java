package remove;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;

public class UnusedPresentationGroupsRemoverTest {
	UnusedPresentationGroupsRemover remover;
	HttpHandlerFactorySpy httpHandlerFactory;
	PresentationGroupFinderSpy presentationGroupFinder;

	@BeforeMethod
	public void setUp() {
		httpHandlerFactory = new HttpHandlerFactorySpy();
		httpHandlerFactory = new HttpHandlerFactorySpy();
		presentationGroupFinder = new PresentationGroupFinderSpy();
		// presentationGroupFinder.setHttpHandlerFactory(httpHandlerFactory);
		// presentationGroupFinder.setUrlString(urlString);

		// finder.setHttpHandlerFactory(httpHandlerFactorySpy);

	}

	@Test
	private void testInit() {
		String urlString = "http://localhost:8080/presentationGroup";

		remover = UnusedPresentationGroupsRemover.usingFinder(presentationGroupFinder);
		remover.setHttpHandlerFactory(httpHandlerFactory);
		remover.setUrlString(urlString);
		remover.removeUnusedPresentationGroups();
		assertEquals(httpHandlerFactory.httpHandlerSpy.requestMethod, "DELETE");
		assertEquals(httpHandlerFactory.httpHandlerSpy.urlString,
				urlString + "/unusedPresentationPGroup");
	}
}

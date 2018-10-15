package se.uu.ub.cora.batchrunner.remove;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.batchrunner.find.HttpHandlerSpy;

public class RecordRemoverTest {
	RecordRemoverImp remover;
	HttpHandlerFactorySpy httpHandlerFactory;
	PresentationsFinderSpy presentationsFinder;
	private String urlString;

	@BeforeMethod
	public void setUp() {
		httpHandlerFactory = new HttpHandlerFactorySpy();
		presentationsFinder = new PresentationsFinderSpy();
		urlString = "http://localhost:8080/presentationGroup";

		remover = new RecordRemoverImp();
		remover.setFinder(presentationsFinder);
		remover.setHttpHandlerFactory(httpHandlerFactory);
		remover.setUrlString(urlString);

	}

	@Test
	private void testRemoveUnusedPresentationGroups() {
		List<String> removedIds = remover.removeRecordsFoundByFinder();
		assertEquals(removedIds.size(), 1);

		HttpHandlerSpy httpHandlerSpy = httpHandlerFactory.httpHandlerSpy;
		assertEquals(httpHandlerSpy.requestMethod, "DELETE");
		assertEquals(httpHandlerSpy.urlString, urlString + "/unusedPresentation");
		assertEquals(httpHandlerSpy.requestProperties.size(), 0);
		assertEquals(httpHandlerSpy.urlString,
				"http://localhost:8080/presentationGroup/unusedPresentation");
	}

	@Test
	private void testRemoveUnusedPresentationGroupsErrorResponseCode() {
		httpHandlerFactory.setResponseCode(401);
		List<String> removedIds = remover.removeRecordsFoundByFinder();
		assertEquals(removedIds.size(), 0);
	}

	@Test
	private void testRemoveUnusedPresentationVars() {
		urlString = "http://localhost:8080/presentationVar";
		remover.setUrlString(urlString);

		List<String> removedIds = remover.removeRecordsFoundByFinder();
		assertEquals(removedIds.size(), 1);

		HttpHandlerSpy httpHandlerSpy = httpHandlerFactory.httpHandlerSpy;
		assertEquals(httpHandlerSpy.requestMethod, "DELETE");
		assertEquals(httpHandlerSpy.urlString, urlString + "/unusedPresentation");
		assertEquals(httpHandlerSpy.requestProperties.size(), 0);
		assertEquals(httpHandlerSpy.urlString,
				"http://localhost:8080/presentationVar/unusedPresentation");
	}

}

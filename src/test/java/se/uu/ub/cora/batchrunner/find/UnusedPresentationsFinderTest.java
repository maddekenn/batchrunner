package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UnusedPresentationsFinderTest {
	private UnusedPresentationsFinder finder;
	private HttpHandlerFactorySpy httpHandlerFactorySpy;

	@BeforeMethod
	public void setUp() {
		finder = new UnusedPresentationsFinder();
		httpHandlerFactorySpy = new HttpHandlerFactorySpy();
		finder.setHttpHandlerFactory(httpHandlerFactorySpy);

	}

	@Test
	public void testFindAllUnusedPresentationGroups() {
		finder.setUrlString("http://localhost:8080/presentationGroup");
		List<String> ids = (List<String>) finder.findRecords();
		assertEquals(httpHandlerFactorySpy.httpHandlerSpy.requestMethod, "GET");
		assertEquals(ids.size(), 1);
		assertEquals(ids.get(0), "presentationGroupWithoutLinksPGroup");
	}

	@Test
	public void testFindAllUnusedPresentationVars() {
		finder.setUrlString("http://localhost:8080/presentationVar");
		List<String> ids = (List<String>) finder.findRecords();
		assertEquals(httpHandlerFactorySpy.httpHandlerSpy.requestMethod, "GET");
		assertEquals(ids.size(), 1);
		assertEquals(ids.get(0), "idTextTextVarPVar");
	}

	@Test
	public void testFindAllUnusedPresentationCollectionVars() {
		finder.setUrlString("http://localhost:8080/presentationCollectionVar");
		List<String> ids = (List<String>) finder.findRecords();
		assertEquals(httpHandlerFactorySpy.httpHandlerSpy.requestMethod, "GET");
		assertEquals(ids.size(), 2);
		assertEquals(ids.get(0), "inputTypeCollectionVarPCollVar");
		assertEquals(ids.get(1), "uncertainPCollVar");
	}
}

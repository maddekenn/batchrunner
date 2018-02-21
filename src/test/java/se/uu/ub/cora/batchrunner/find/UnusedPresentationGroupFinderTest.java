package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UnusedPresentationGroupFinderTest {
	private UnusedPresentationGroupFinder finder;
	private HttpHandlerFactorySpy httpHandlerFactorySpy;

	@BeforeMethod
	public void setUp() {
		finder = new UnusedPresentationGroupFinder();
		httpHandlerFactorySpy = new HttpHandlerFactorySpy();
		finder.setHttpHandlerFactory(httpHandlerFactorySpy);

	}

	@Test
	public void testFindAllUnusedPresentationGroups() {
		finder.setUrlString("http://localhost:8080/presentationGroup");
		List<String> ids = (List<String>) finder.findRecords();
		assertEquals(httpHandlerFactorySpy.httpHandlerSpy.requestMethod, "GET");
		assertEquals(ids.size(), 1);
		assertEquals(ids.get(0), "collectTermListPGroup");
	}
}

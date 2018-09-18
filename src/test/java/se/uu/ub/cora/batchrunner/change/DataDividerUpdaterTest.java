package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;

public class DataDividerUpdaterTest {

	private String url;
	private HttpHandlerFactorySpy httpHandlerFactory;

	@BeforeMethod
	public void setUp() {
		httpHandlerFactory = new HttpHandlerFactorySpy();
		url = "http://someTestUrl/";
	}

	@Test
	public void init() {
		DataDividerUpdater updater = DataDividerUpdater.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		assertTrue(updater.getHttpHandler() instanceof HttpHandlerFactorySpy);
		assertEquals(updater.getUrl(), url);
	}
}

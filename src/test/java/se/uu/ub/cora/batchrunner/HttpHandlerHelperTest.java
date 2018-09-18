package se.uu.ub.cora.batchrunner;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class HttpHandlerHelperTest {
	private HttpHandlerFactory httpHandlerFactory;
	private String url;

	@BeforeMethod
	public void setUp() {
		httpHandlerFactory = new HttpHandlerFactorySpy();
		url = "http://someTestUrl/";
	}

	@Test
	public void init() {
		HttpHandlerHelper helper = HttpHandlerHelper.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		assertTrue(helper.getHttpHandler() instanceof HttpHandlerFactorySpy);
		assertEquals(helper.getUrl(), url);
	}

	@Test
	public void testReadGroup() {
		HttpHandlerHelper helper = HttpHandlerHelper.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);

	}

}

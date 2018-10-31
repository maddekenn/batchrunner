package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.CoraClientSpy;
import se.uu.ub.cora.batchrunner.change.CoraClientConfigSpy;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class SystemRelatedElementsFinderTest {

	private CoraClientFactorySpy coraClientFactory;
	private CoraClientConfigSpy coraClientConfig;

	@BeforeMethod
	public void setUp() {
		coraClientFactory = new CoraClientFactorySpy("someUserId", "someAppToken");
		coraClientConfig = new CoraClientConfigSpy("someUserId", "someAppToken",
				"someAppTokenVerifierUrl", "someCoraUrl");

	}

	@Test
	public void testInit() {
		RecordIdentifier recordIdentifier = RecordIdentifier.usingTypeAndId("system",
				"aTestSystem");

		RecordFinder finder = SystemRelatedElementsFinder
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		List<RecordIdentifier> foundRecords = finder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);

		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.recordType, "system");
		assertEquals(coraClientSpy.recordId, "aTestSystem");

	}
}

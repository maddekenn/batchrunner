package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.change.RecordDeleter;
import se.uu.ub.cora.batchrunner.change.RecordDeleterImp;

public class RecordDeleterTest {
	private CoraClientFactorySpy coraClientFactory;
	private CoraClientConfigSpy coraClientConfig;

	@BeforeMethod
	public void setUp() {
		coraClientConfig = new CoraClientConfigSpy("someUserId", "someAppToken",
				"someAppTokenVerifierUrl", "someCoraUrl");
		coraClientFactory = new CoraClientFactorySpy("someUserId", "someAppToken");
	}

	@Test
	public void testDelete() {
		RecordDeleter recordDeleter = RecordDeleterImp
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		String recordType = "metadataItemCollection";
		String recordId = "languageCollection";
		String answer = recordDeleter.deleteRecordByTypeAndId(recordType, recordId);
		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.recordType, "metadataItemCollection");
		assertEquals(coraClientSpy.recordId, "languageCollection");
		assertEquals(answer, "OK");
	}
}

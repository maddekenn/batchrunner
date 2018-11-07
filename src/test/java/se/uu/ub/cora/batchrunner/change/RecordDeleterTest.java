package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.CoraClientSpy;
import se.uu.ub.cora.batchrunner.remove.RecordDeleterImp;
import se.uu.ub.cora.clientdata.RecordIdentifier;

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
		assertEquals(coraClientSpy.recordTypes.get(0), "metadataItemCollection");
		assertEquals(coraClientSpy.recordIds.get(0), "languageCollection");
		assertEquals(answer, "OK");
	}

	@Test
	public void testDeleteListOfIdentifiers() {
		RecordDeleter recordDeleter = RecordDeleterImp
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		List<RecordIdentifier> identifiers = new ArrayList<>();
		identifiers.add(
				RecordIdentifier.usingTypeAndId("metadataItemCollection", "languageCollection"));
		identifiers.add(
				RecordIdentifier.usingTypeAndId("metadataItemCollection", "countryCollection"));

		List<String> answers = recordDeleter.deleteByRecordIdentifiers(identifiers);
		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.recordTypes.get(0), "metadataItemCollection");
		assertEquals(coraClientSpy.recordIds.get(0), "languageCollection");
		assertEquals(answers.get(0), "OK");

		CoraClientSpy coraClientSpy1 = coraClientFactory.factoredClientSpies.get(1);
		assertEquals(coraClientSpy1.recordTypes.get(0), "metadataItemCollection");
		assertEquals(coraClientSpy1.recordIds.get(0), "countryCollection");
		assertEquals(answers.get(1), "OK");
	}
}

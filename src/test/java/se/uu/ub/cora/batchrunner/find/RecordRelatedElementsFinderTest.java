package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.CoraClientSpy;
import se.uu.ub.cora.batchrunner.change.CoraClientConfigSpy;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class RecordRelatedElementsFinderTest {

	private CoraClientFactorySpy coraClientFactory;
	private CoraClientConfigSpy coraClientConfig;

	@BeforeMethod
	public void setUp() {
		coraClientFactory = new CoraClientFactorySpy("someUserId", "someAppToken");
		coraClientConfig = new CoraClientConfigSpy("someUserId", "someAppToken",
				"someAppTokenVerifierUrl", "someCoraUrl");

	}

	@Test
	public void testNoIncomingLinks() {
		RecordIdentifier recordIdentifier = RecordIdentifier.usingTypeAndId("system",
				"aTestSystemNoLinks");

		RecordFinder finder = RecordRelatedElementsFinder
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		List<RecordIdentifier> foundRecords = finder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);

		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.recordTypes.get(0), "system");
		assertEquals(coraClientSpy.recordIds.get(0), "aTestSystemNoLinks");
		assertEquals(coraClientSpy.calledMethods.get(0), "read");

		assertEquals(coraClientSpy.calledMethods.size(), 1);

		assertEquals(foundRecords.size(), 0);
	}

	@Test
	public void testWithIncomingLinks() {
		RecordIdentifier recordIdentifier = RecordIdentifier.usingTypeAndId("system",
				"aTestSystemWithLinks");

		RecordFinder finder = RecordRelatedElementsFinder
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		List<RecordIdentifier> foundRecords = finder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);

		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.recordTypes.get(0), "system");
		assertEquals(coraClientSpy.recordIds.get(0), "aTestSystemWithLinks");
		assertEquals(coraClientSpy.calledMethods.get(0), "read");

		assertEquals(coraClientSpy.recordTypes.get(1), "system");
		assertEquals(coraClientSpy.recordIds.get(1), "aTestSystemWithLinks");
		assertEquals(coraClientSpy.calledMethods.get(1), "readIncomingLinks");
		assertEquals(coraClientSpy.calledMethods.size(), 2);

		assertEquals(foundRecords.size(), 1);
		RecordIdentifier result = foundRecords.get(0);
		assertEquals(result.type, "metadataGroup");
		assertEquals(result.id, "collectionItemReferenceGroup");
	}
}

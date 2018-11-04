package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.CoraClientSpy;
import se.uu.ub.cora.batchrunner.change.CoraClientConfigSpy;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class AllOfARecordTypeFinderTest {
	private AllOfARecordTypeFinder finder;
	private CoraClientFactorySpy coraClientFactory;
	private CoraClientConfigSpy coraClientConfig;

	@BeforeMethod
	public void setUp() {
		coraClientFactory = new CoraClientFactorySpy("someUserId", "someAppToken");
		coraClientConfig = new CoraClientConfigSpy("someUserId", "someAppToken",
				"someAppTokenVerifierUrl", "someCoraUrl");
		finder = (AllOfARecordTypeFinder) AllOfARecordTypeFinder
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

	}

	@Test
	public void testFindAllOfRecordType() {
		RecordIdentifier recordIdentifier = RecordIdentifier.usingTypeAndId("workOrder", null);

		List<RecordIdentifier> foundRecords = finder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);

		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.recordTypes.get(0), "workOrder");
		assertEquals(foundRecords.size(), 1);
		RecordIdentifier createdRecordIdentifier = foundRecords.get(0);
		assertEquals(createdRecordIdentifier.type, "workOrder");
		assertEquals(createdRecordIdentifier.id, "workOrder:3638403025511700");
	}
}

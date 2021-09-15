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
		RecordIdentifier recordIdentifier = RecordIdentifier.usingTypeAndId("demo", null);

		List<RecordIdentifier> foundRecords = finder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);

		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.recordTypes.get(0), "demo");
		assertEquals(foundRecords.size(), 2);
		assertCorrectRecordIdentifier(foundRecords, 0, "asdf");
		assertCorrectRecordIdentifier(foundRecords, 1, "sdfsdf");
	}

	private void assertCorrectRecordIdentifier(List<RecordIdentifier> foundRecords, int index,
			String id) {
		RecordIdentifier createdRecordIdentifier = foundRecords.get(index);
		assertEquals(createdRecordIdentifier.type, "demo");
		assertEquals(createdRecordIdentifier.id, id);
	}
}

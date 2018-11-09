package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.CoraClientSpy;
import se.uu.ub.cora.batchrunner.ResultHolder;
import se.uu.ub.cora.batchrunner.change.CoraClientConfigSpy;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class RecordsToDeleteSorterTest {

	public List<RecordIdentifier> recordIdentifiers = new ArrayList<>();
	private CoraClientFactorySpy coraClientFactory;
	private CoraClientConfigSpy coraClientConfig;

	@BeforeMethod
	public void beforeMethod() {
		recordIdentifiers = new ArrayList<>();
		RecordIdentifier toKeep = RecordIdentifier.usingTypeAndId("someType", "someIdToKeep");
		RecordIdentifier toDelete = RecordIdentifier.usingTypeAndId("someType", "someIdToDelete");
		RecordIdentifier parseError = RecordIdentifier.usingTypeAndId("someType",
				"someParseErrorId");
		RecordIdentifier coraClientError = RecordIdentifier.usingTypeAndId("someType",
				"someCoraCliencErrorId");
		recordIdentifiers.add(toKeep);
		recordIdentifiers.add(toDelete);
		recordIdentifiers.add(parseError);
		recordIdentifiers.add(coraClientError);
		coraClientFactory = new CoraClientFactorySpy("someUserId", "someAppToken");
		coraClientConfig = new CoraClientConfigSpy("someUserId", "someAppToken",
				"someAppTokenVerifierUrl", "someCoraUrl");

	}

	@Test
	public void testInit() {
		RecordsSeparator chooser = RecordsToDeleteSeparator
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		ResultHolder resultHolder = chooser.sortOutRecordIdentifiers(recordIdentifiers);
		List<RecordIdentifier> result = resultHolder.recordIdentifiers;

		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.calledMethods.get(0), "read");
		assertEquals(coraClientSpy.recordTypes.get(0), "someType");
		assertEquals(coraClientSpy.recordIds.get(0), "someIdToKeep");

		assertEquals(coraClientSpy.calledMethods.get(1), "read");
		assertEquals(coraClientSpy.recordTypes.get(1), "someType");
		assertEquals(coraClientSpy.recordIds.get(1), "someIdToDelete");

		assertEquals(coraClientSpy.calledMethods.get(2), "read");
		assertEquals(coraClientSpy.recordTypes.get(2), "someType");
		assertEquals(coraClientSpy.recordIds.get(2), "someParseErrorId");

		assertEquals(result.size(), 1);
	}

	@Test
	public void testUnableToParseJsonCheckError() {
		RecordsSeparator chooser = RecordsToDeleteSeparator
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		ResultHolder resultHolder = chooser.sortOutRecordIdentifiers(recordIdentifiers);
		List<RecordIdentifier> result = resultHolder.recordIdentifiers;
		assertEquals(result.size(), 1);

		List<String> errorMessages = resultHolder.messages;
		assertEquals(errorMessages.get(0),
				"Error parsing json for type: someType and id: someParseErrorId");
		assertEquals(errorMessages.size(), 2);

	}

	@Test
	public void testCoraClientError() {
		RecordsSeparator chooser = RecordsToDeleteSeparator
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		ResultHolder resultHolder = chooser.sortOutRecordIdentifiers(recordIdentifiers);
		List<RecordIdentifier> result = resultHolder.recordIdentifiers;
		assertEquals(result.size(), 1);

		List<String> errorMessages = resultHolder.messages;
		assertEquals(errorMessages.get(1), "some coraClientException from spy");
		assertEquals(errorMessages.size(), 2);

	}
}

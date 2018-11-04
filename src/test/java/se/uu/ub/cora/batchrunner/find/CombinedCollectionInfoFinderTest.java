package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.CoraClientSpy;
import se.uu.ub.cora.batchrunner.change.CoraClientConfigSpy;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class CombinedCollectionInfoFinderTest {
	private RecordFinder combinedFinder;
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
		combinedFinder = CombinedCollectionInfoFinder
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		RecordIdentifier recordIdentifier = RecordIdentifier
				.usingTypeAndId("metadataItemCollection", "completeLanguageCollection");

		List<RecordIdentifier> records = combinedFinder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);
		CoraClientSpy coraClientReadCollectionSpy = coraClientFactory.factoredClientSpies.get(1);
		assertEquals(coraClientReadCollectionSpy.recordTypes.get(0), "metadataItemCollection");
		assertEquals(coraClientReadCollectionSpy.recordIds.get(0), "completeLanguageCollection");

		CoraClientSpy coraClientReadItemSpy = coraClientFactory.factoredClientSpies.get(3);
		assertEquals(coraClientReadItemSpy.recordTypes.get(0), "genericCollectionItem");
		assertEquals(coraClientReadItemSpy.recordIds.get(0), "svItem");

		CoraClientSpy coraClientReadItemSpy2 = coraClientFactory.factoredClientSpies.get(4);
		assertEquals(coraClientReadItemSpy2.recordTypes.get(0), "genericCollectionItem");
		assertEquals(coraClientReadItemSpy2.recordIds.get(0), "enItem");

		assertEquals(records.size(), 9);
		assertCorrectRecordIdentifier(records, 0, "metadataItemCollection",
				"completeLanguageCollection");

		assertCorrectRecordIdentifier(records, 1, "genericCollectionItem", "svItem");
		assertCorrectRecordIdentifier(records, 2, "genericCollectionItem", "enItem");

		assertCorrectRecordIdentifierForText(records, 3, "languageCollectionText");
		assertCorrectRecordIdentifierForText(records, 4, "languageCollectionDefText");
		assertCorrectRecordIdentifierForText(records, 5, "svItemText");
		assertCorrectRecordIdentifierForText(records, 6, "svItemDefText");
		assertCorrectRecordIdentifierForText(records, 7, "enItemText");
		assertCorrectRecordIdentifierForText(records, 8, "enItemDefText");

	}

	private void assertCorrectRecordIdentifier(List<RecordIdentifier> items, int index,
			String recordType, String itemId) {
		RecordIdentifier firstItemIdentifier = items.get(index);
		assertEquals(firstItemIdentifier.type, recordType);
		assertEquals(firstItemIdentifier.id, itemId);
	}

	private void assertCorrectRecordIdentifierForText(List<RecordIdentifier> items, int index,
			String itemId) {
		assertCorrectRecordIdentifier(items, index, "coraText", itemId);
	}
}

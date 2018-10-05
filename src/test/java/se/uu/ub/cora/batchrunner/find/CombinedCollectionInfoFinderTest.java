package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.change.CoraClientConfigSpy;
import se.uu.ub.cora.batchrunner.change.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.change.CoraClientSpy;
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

		List<RecordIdentifier> items = combinedFinder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);
		CoraClientSpy coraClientReadCollectionSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientReadCollectionSpy.recordType, "metadataItemCollection");
		assertEquals(coraClientReadCollectionSpy.recordId, "completeLanguageCollection");

		CoraClientSpy coraClientReadItemSpy = coraClientFactory.factoredClientSpies.get(2);
		assertEquals(coraClientReadItemSpy.recordType, "genericCollectionItem");
		assertEquals(coraClientReadItemSpy.recordId, "svItem");

		CoraClientSpy coraClientReadItemSpy2 = coraClientFactory.factoredClientSpies.get(3);
		assertEquals(coraClientReadItemSpy2.recordType, "genericCollectionItem");
		assertEquals(coraClientReadItemSpy2.recordId, "enItem");

		assertEquals(items.size(), 9);
		RecordIdentifier firstItemIdentifier = items.get(0);
		assertEquals(firstItemIdentifier.type, "metadataItemCollection");
		assertEquals(firstItemIdentifier.id, "completeLanguageCollection");
		RecordIdentifier secondItemIdentifier = items.get(1);
		assertEquals(secondItemIdentifier.type, "genericCollectionItem");
		assertEquals(secondItemIdentifier.id, "svItem");
		RecordIdentifier thirdItemIdentifier = items.get(2);
		assertEquals(thirdItemIdentifier.type, "genericCollectionItem");
		assertEquals(thirdItemIdentifier.id, "enItem");

		assertCorrectRecordIdentifier(items, "languageCollectionText", 3);
		assertCorrectRecordIdentifier(items, "languageCollectionDefText", 4);
		assertCorrectRecordIdentifier(items, "svItemText", 5);
		assertCorrectRecordIdentifier(items, "svItemDefText", 6);
		assertCorrectRecordIdentifier(items, "enItemText", 7);
		assertCorrectRecordIdentifier(items, "enItemDefText", 8);

	}

	private void assertCorrectRecordIdentifier(List<RecordIdentifier> items, String itemId,
			int index) {
		RecordIdentifier firstItemIdentifier = items.get(index);
		assertEquals(firstItemIdentifier.type, "coraText");
		assertEquals(firstItemIdentifier.id, itemId);
	}
}

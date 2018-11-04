package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.CoraClientSpy;
import se.uu.ub.cora.batchrunner.change.CoraClientConfigSpy;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class TextsConnectedToItemCollectionFinderTest {
	private RecordFinder finder;
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
		finder = TextsConnectedToItemCollectionFinder
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		RecordIdentifier recordIdentifier = RecordIdentifier
				.usingTypeAndId("metadataItemCollection", "completeLanguageCollection");

		List<RecordIdentifier> items = finder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);
		CoraClientSpy coraClientReadCollectionSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientReadCollectionSpy.recordTypes.get(0), "metadataItemCollection");
		assertEquals(coraClientReadCollectionSpy.recordIds.get(0), "completeLanguageCollection");

		CoraClientSpy coraClientReadItemSpy = coraClientFactory.factoredClientSpies.get(2);
		assertEquals(coraClientReadItemSpy.recordTypes.get(0), "genericCollectionItem");
		assertEquals(coraClientReadItemSpy.recordIds.get(0), "svItem");

		CoraClientSpy coraClientReadItemSpy2 = coraClientFactory.factoredClientSpies.get(3);
		assertEquals(coraClientReadItemSpy2.recordTypes.get(0), "genericCollectionItem");
		assertEquals(coraClientReadItemSpy2.recordIds.get(0), "enItem");

		assertEquals(items.size(), 6);
		assertCorrectRecordIdentifier(items, "languageCollectionText", 0);
		assertCorrectRecordIdentifier(items, "languageCollectionDefText", 1);
		assertCorrectRecordIdentifier(items, "svItemText", 2);
		assertCorrectRecordIdentifier(items, "svItemDefText", 3);
		assertCorrectRecordIdentifier(items, "enItemText", 4);
		assertCorrectRecordIdentifier(items, "enItemDefText", 5);
	}

	private void assertCorrectRecordIdentifier(List<RecordIdentifier> items, String itemId,
			int index) {
		RecordIdentifier firstItemIdentifier = items.get(index);
		assertEquals(firstItemIdentifier.type, "coraText");
		assertEquals(firstItemIdentifier.id, itemId);
	}
}

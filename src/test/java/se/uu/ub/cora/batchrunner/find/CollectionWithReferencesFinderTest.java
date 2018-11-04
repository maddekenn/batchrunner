package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.CoraClientSpy;
import se.uu.ub.cora.batchrunner.change.CoraClientConfigSpy;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class CollectionWithReferencesFinderTest {
	private RecordFinder finder;
	private CoraClientFactorySpy coraClientFactory;
	private CoraClientConfigSpy coraClientConfig;

	@BeforeMethod
	public void setUp() {
		coraClientConfig = new CoraClientConfigSpy("someUserId", "someAppToken",
				"someAppTokenVerifierUrl", "someCoraUrl");
		coraClientFactory = new CoraClientFactorySpy("someUserId", "someAppToken");
	}

	@Test
	public void testFindItemsForACollection() {
		finder = CollectionWithReferencesFinder
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		RecordIdentifier recordIdentifier = RecordIdentifier
				.usingTypeAndId("metadataItemCollection", "completeLanguageCollection");

		List<RecordIdentifier> items = finder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);
		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.recordTypes.get(0), "metadataItemCollection");
		assertEquals(coraClientSpy.recordIds.get(0), "completeLanguageCollection");

		assertEquals(items.size(), 3);
		assertEquals(items.get(0).type, "metadataItemCollection");
		assertEquals(items.get(0).id, "completeLanguageCollection");
		assertEquals(items.get(1).type, "genericCollectionItem");
		assertEquals(items.get(1).id, "svItem");
		assertEquals(items.get(2).type, "genericCollectionItem");
		assertEquals(items.get(2).id, "enItem");

	}
}

package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.change.CoraClientConfigSpy;
import se.uu.ub.cora.batchrunner.change.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.change.CoraClientSpy;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class ReferencedItemsFinderTest {
	private Finder finder;
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
		finder = ReferencedItemsFinder.usingCoraClientFactoryAndClientConfig(coraClientFactory,
				coraClientConfig);

		RecordIdentifier recordIdentifier = RecordIdentifier
				.usingTypeAndId("metadataItemCollection", "completeLanguageCollection");

		List<RecordIdentifier> items = finder.findRecordsUsingRecordIdentifier(recordIdentifier);
		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.recordType, "metadataItemCollection");
		assertEquals(coraClientSpy.recordId, "completeLanguageCollection");

		assertEquals(items.size(), 2);
		assertEquals(items.get(0).type, "genericCollectionItem");
		assertEquals(items.get(0).id, "svItem");
		assertEquals(items.get(1).type, "genericCollectionItem");
		assertEquals(items.get(1).id, "enItem");

	}
}

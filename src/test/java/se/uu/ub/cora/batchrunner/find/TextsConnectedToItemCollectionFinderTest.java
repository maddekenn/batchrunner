package se.uu.ub.cora.batchrunner.find;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.change.CoraClientConfigSpy;
import se.uu.ub.cora.batchrunner.change.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.change.CoraClientSpy;
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

		List<RecordIdentifier> items = finder.findRecordsUsingRecordIdentifier(recordIdentifier);
		CoraClientSpy coraClientReadCollectionSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientReadCollectionSpy.recordType, "metadataItemCollection");
		assertEquals(coraClientReadCollectionSpy.recordId, "completeLanguageCollection");

		CoraClientSpy coraClientReadItemSpy = coraClientFactory.factoredClientSpies.get(1);
		assertEquals(coraClientReadItemSpy.recordType, "genericCollectionItem");
		assertEquals(coraClientReadItemSpy.recordId, "svItem");

		CoraClientSpy coraClientReadItemSpy2 = coraClientFactory.factoredClientSpies.get(2);
		assertEquals(coraClientReadItemSpy2.recordType, "genericCollectionItem");
		assertEquals(coraClientReadItemSpy2.recordId, "enItem");

		assertEquals(items.size(), 4);
	}
}

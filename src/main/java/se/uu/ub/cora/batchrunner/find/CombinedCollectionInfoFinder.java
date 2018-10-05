package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.client.CoraClientConfig;
import se.uu.ub.cora.client.CoraClientFactory;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class CombinedCollectionInfoFinder implements RecordFinder {

	private CoraClientFactory coraClientFactory;
	private CoraClientConfig coraClientConfig;

	public CombinedCollectionInfoFinder(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	@Override
	public List<RecordIdentifier> findRecordsRelatedToRecordIdentifier(
			RecordIdentifier recordIdentifier) {
		List<RecordIdentifier> result = new ArrayList<>();
		RecordFinder textFinder = TextsConnectedToItemCollectionFinder
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);
		List<RecordIdentifier> texts = textFinder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);

		RecordFinder refsFinder = CollectionWithReferencesFinder
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);
		List<RecordIdentifier> items = refsFinder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);
		result.addAll(items);
		result.addAll(texts);
		return result;
	}

	public static RecordFinder usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		return new CombinedCollectionInfoFinder(coraClientFactory, coraClientConfig);
	}

}

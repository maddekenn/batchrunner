package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.javaclient.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class CombinedCollectionInfoFinder implements RecordFinder {

	private CoraClientFactory coraClientFactory;
	private CoraClientConfig coraClientConfig;

	public CombinedCollectionInfoFinder(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	public static RecordFinder usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		return new CombinedCollectionInfoFinder(coraClientFactory, coraClientConfig);
	}

	@Override
	public List<RecordIdentifier> findRecordsRelatedToRecordIdentifier(
			RecordIdentifier recordIdentifier) {
		List<RecordIdentifier> result = new ArrayList<>();
		findItemsAndAddToResult(recordIdentifier, result);
		findTextsAndAddToResult(recordIdentifier, result);

		return result;
	}

	private void findItemsAndAddToResult(RecordIdentifier recordIdentifier,
			List<RecordIdentifier> result) {
		RecordFinder refsFinder = CollectionWithReferencesFinder
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);
		List<RecordIdentifier> items = refsFinder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);
		result.addAll(items);
	}

	private void findTextsAndAddToResult(RecordIdentifier recordIdentifier, List<RecordIdentifier> result) {
		RecordFinder textFinder = TextsConnectedToItemCollectionFinder
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);
		List<RecordIdentifier> texts = textFinder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);
		result.addAll(texts);
	}

}

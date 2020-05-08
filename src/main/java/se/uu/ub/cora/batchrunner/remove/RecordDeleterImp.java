package se.uu.ub.cora.batchrunner.remove;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.change.RecordDeleter;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.javaclient.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClient;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class RecordDeleterImp implements RecordDeleter {

	private CoraClientFactory coraClientFactory;
	private CoraClientConfig coraClientConfig;

	public RecordDeleterImp(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	public static RecordDeleter usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		return new RecordDeleterImp(coraClientFactory, coraClientConfig);
	}

	@Override
	public String deleteRecordByTypeAndId(String recordType, String recordId) {
		CoraClient coraClient = coraClientFactory.factor(coraClientConfig.userId,
				coraClientConfig.appToken);
		return coraClient.delete(recordType, recordId);
	}

	@Override
	public List<String> deleteByRecordIdentifiers(List<RecordIdentifier> identifiers) {
		List<String> answers = new ArrayList<>();
		for (RecordIdentifier recordIdentifier : identifiers) {
			String answer = deleteRecordByTypeAndId(recordIdentifier.type, recordIdentifier.id);
			answers.add(answer);
		}
		return answers;
	}

}

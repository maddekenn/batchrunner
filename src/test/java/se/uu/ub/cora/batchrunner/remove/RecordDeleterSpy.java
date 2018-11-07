package se.uu.ub.cora.batchrunner.remove;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.change.RecordDeleter;
import se.uu.ub.cora.client.CoraClientConfig;
import se.uu.ub.cora.client.CoraClientException;
import se.uu.ub.cora.client.CoraClientFactory;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class RecordDeleterSpy implements RecordDeleter {

	public List<String> types = new ArrayList<>();
	public List<String> recordIds = new ArrayList<>();
	public CoraClientFactory coraClientFactory;
	public CoraClientConfig coraClientConfig;
	public List<RecordIdentifier> identifiers;
	public boolean deleteByIdentifiersWasCalled = false;

	public RecordDeleterSpy(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	public static RecordDeleter usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		return new RecordDeleterSpy(coraClientFactory, coraClientConfig);
	}

	@Override
	public String deleteRecordByTypeAndId(String recordType, String recordId) {
		types.add(recordType);
		recordIds.add(recordId);
		if ("errorItem".equals(recordId)) {
			throw new CoraClientException("Error from DataUpdaterSpy");
		}
		return "OK";
	}

	@Override
	public List<String> deleteByRecordIdentifiers(List<RecordIdentifier> recordIdentifiers) {
		deleteByIdentifiersWasCalled = true;
		identifiers = new ArrayList<>();
		identifiers.addAll(recordIdentifiers);

		List<String> messages = new ArrayList<>();
		messages.add("seom message from spy");
		return messages;
	}

}

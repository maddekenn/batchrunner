package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.client.CoraClientConfig;
import se.uu.ub.cora.client.CoraClientException;
import se.uu.ub.cora.client.CoraClientFactory;

public class RecordDeleterSpy implements RecordDeleter {

	public List<String> types = new ArrayList<>();
	public List<String> recordIds = new ArrayList<>();

	public RecordDeleterSpy(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		// TODO Auto-generated constructor stub
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

}

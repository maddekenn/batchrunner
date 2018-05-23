package se.uu.ub.cora.batchrunner.convert;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.client.CoraClient;

public class CoraClientSpy implements CoraClient {

	List<String> createdRecordTypes = new ArrayList<>();
	List<String> jsonStrings = new ArrayList<>();

	@Override
	public String create(String recordType, String json) {
		createdRecordTypes.add(recordType);
		jsonStrings.add(json);
		return null;
	}

}

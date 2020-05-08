package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.ResultHolder;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.javaclient.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class RecordsSeparatorSpy implements RecordsSeparator {

	public CoraClientFactory coraClientFactory;
	public CoraClientConfig coraClientConfig;
	public List<RecordIdentifier> identifiers = new ArrayList<>();
	public boolean sortOutRecordsCalled = false;
	public ResultHolder resultHolder;

	public RecordsSeparatorSpy(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	public static RecordsSeparator usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		return new RecordsSeparatorSpy(coraClientFactory, coraClientConfig);
	}

	@Override
	public ResultHolder sortOutRecordIdentifiers(List<RecordIdentifier> recordIdentifiers) {
		sortOutRecordsCalled = true;
		identifiers = new ArrayList<>();
		identifiers.addAll(recordIdentifiers);

		resultHolder = new ResultHolder();
		resultHolder.addRecordIdentifier(recordIdentifiers.get(0));
		resultHolder.addMessage("some message from spy");
		return resultHolder;
	}

}

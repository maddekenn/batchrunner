package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.javaclient.cora.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class RecordFinderSpy implements RecordFinder {
	public boolean findRecordsCalled = false;
	public String url;
	public List<String> ids;
	public HttpHandlerFactory httpHandlerFactory;
	public CoraClientFactory coraClientFactory;
	public CoraClientConfig coraClientConfig;
	public RecordIdentifier recordIdentifier;
	public List<RecordIdentifier> recordIdentifiers;
	public String authToken;

	public RecordFinderSpy() {
	}

	public RecordFinderSpy(CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	public RecordFinderSpy(CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig,
			String authToken) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
		this.authToken = authToken;
	}

	@Override
	public List<RecordIdentifier> findRecordsRelatedToRecordIdentifier(
			RecordIdentifier recordIdentifier) {
		findRecordsCalled = true;
		this.recordIdentifier = recordIdentifier;
		recordIdentifiers = new ArrayList<>();
		recordIdentifiers.add(RecordIdentifier.usingTypeAndId("genericCollectionItem", "svItem"));
		recordIdentifiers.add(RecordIdentifier.usingTypeAndId("genericCollectionItem", "enItem"));
		if ("errorInItemCollection".equals(recordIdentifier.id)) {
			recordIdentifiers
					.add(RecordIdentifier.usingTypeAndId("genericCollectionItem", "errorItem"));
		}
		return recordIdentifiers;
	}

	public static RecordFinder usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		return new RecordFinderSpy(coraClientFactory, coraClientConfig);
	}

	public static RecordFinder usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig,
			String authToken) {
		return new RecordFinderSpy(coraClientFactory, coraClientConfig, authToken);
	}

}

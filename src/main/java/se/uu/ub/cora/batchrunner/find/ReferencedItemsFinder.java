package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.batchrunner.change.ConverterHelper;
import se.uu.ub.cora.client.CoraClient;
import se.uu.ub.cora.client.CoraClientConfig;
import se.uu.ub.cora.client.CoraClientFactory;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class ReferencedItemsFinder implements Finder {

	private CoraClientFactory coraClientFactory;
	private CoraClientConfig coraClientConfig;

	public ReferencedItemsFinder(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	public static Finder usingURLAndHttpHandlerFactory(String url,
			HttpHandlerFactory httpHandlerFactorySpy) {
		return null;
	}

	public static Finder usingCoraClientFactoryAndClientConfig(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		return new ReferencedItemsFinder(coraClientFactory, coraClientConfig);
	}

	@Override
	public void setUrlString(String url) {

	}

	@Override
	public Collection<String> findRecords() {
		return null;
	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactorySpy) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<RecordIdentifier> findRecordsUsingRecordIdentifier(
			RecordIdentifier recordIdentifier) {
		List<RecordIdentifier> foundRecords = new ArrayList<>();
		List<ClientDataGroup> refs = getAllRefsFromCollection(recordIdentifier);
		for (ClientDataGroup ref : refs) {
			String itemType = ref.getFirstAtomicValueWithNameInData("linkedRecordType");
			String itemId = ref.getFirstAtomicValueWithNameInData("linkedRecordId");
			foundRecords.add(RecordIdentifier.usingTypeAndId(itemType, itemId));
		}
		return foundRecords;
	}

	private List<ClientDataGroup> getAllRefsFromCollection(RecordIdentifier recordIdentifier) {
		ClientDataRecord clientDataRecord = readRecordUsingRecordIdentifier(recordIdentifier);
		ClientDataGroup clientDataGroup = clientDataRecord.getClientDataGroup();
		ClientDataGroup collectionItemReferences = clientDataGroup
				.getFirstGroupWithNameInData("collectionItemReferences");
		return collectionItemReferences.getAllGroupsWithNameInData("ref");
	}

	private ClientDataRecord readRecordUsingRecordIdentifier(RecordIdentifier recordIdentifier) {
		CoraClient coraClient = coraClientFactory.factor(coraClientConfig.userId,
				coraClientConfig.appToken);
		String readRecord = coraClient.read(recordIdentifier.type, recordIdentifier.id);
		return ConverterHelper.getJsonAsClientDataRecord(readRecord);
	}

}

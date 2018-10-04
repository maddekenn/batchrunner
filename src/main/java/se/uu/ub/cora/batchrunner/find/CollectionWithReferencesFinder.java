package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.change.ConverterHelper;
import se.uu.ub.cora.client.CoraClient;
import se.uu.ub.cora.client.CoraClientConfig;
import se.uu.ub.cora.client.CoraClientFactory;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class CollectionWithReferencesFinder implements RecordFinder {

	private CoraClientFactory coraClientFactory;
	private CoraClientConfig coraClientConfig;
	private CoraClient coraClient;

	public CollectionWithReferencesFinder(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	public static RecordFinder usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		return new CollectionWithReferencesFinder(coraClientFactory, coraClientConfig);
	}

	@Override
	public List<RecordIdentifier> findRecordsRelatedToRecordIdentifier(
			RecordIdentifier recordIdentifier) {
		coraClient = coraClientFactory.factor(coraClientConfig.userId, coraClientConfig.appToken);
		List<RecordIdentifier> foundRecords = new ArrayList<>();
		ClientDataRecord clientDataRecord = readRecordUsingRecordIdentifier(recordIdentifier);
		List<ClientDataGroup> refs = getAllRefsFromCollection(clientDataRecord);
		for (ClientDataGroup ref : refs) {
			String itemType = ref.getFirstAtomicValueWithNameInData("linkedRecordType");
			String itemId = ref.getFirstAtomicValueWithNameInData("linkedRecordId");
			foundRecords.add(RecordIdentifier.usingTypeAndId(itemType, itemId));
		}
		return foundRecords;
	}

	private List<ClientDataGroup> getAllRefsFromCollection(ClientDataRecord clientDataRecord) {
		ClientDataGroup clientDataGroup = clientDataRecord.getClientDataGroup();
		ClientDataGroup collectionItemReferences = clientDataGroup
				.getFirstGroupWithNameInData("collectionItemReferences");
		return collectionItemReferences.getAllGroupsWithNameInData("ref");
	}

	private ClientDataRecord readRecordUsingRecordIdentifier(RecordIdentifier recordIdentifier) {
		String readRecord = coraClient.read(recordIdentifier.type, recordIdentifier.id);
		return ConverterHelper.getJsonAsClientDataRecord(readRecord);
	}

}

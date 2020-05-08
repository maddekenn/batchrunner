package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.change.ConverterHelper;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.javaclient.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClient;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

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
		foundRecords
				.add(RecordIdentifier.usingTypeAndId(recordIdentifier.type, recordIdentifier.id));

		ClientDataRecord clientDataRecord = readRecordUsingRecordIdentifier(recordIdentifier);
		addRefsToFoundRecords(foundRecords, clientDataRecord);
		return foundRecords;
	}

	private ClientDataRecord readRecordUsingRecordIdentifier(RecordIdentifier recordIdentifier) {
		String readRecord = coraClient.read(recordIdentifier.type, recordIdentifier.id);
		return ConverterHelper.getJsonStringAsClientDataRecord(readRecord);
	}

	private void addRefsToFoundRecords(List<RecordIdentifier> foundRecords,
			ClientDataRecord clientDataRecord) {
		List<ClientDataGroup> refs = getAllRefsFromCollection(clientDataRecord);
		for (ClientDataGroup ref : refs) {
			addRefToFoundRecords(ref, foundRecords);
		}
	}

	private List<ClientDataGroup> getAllRefsFromCollection(ClientDataRecord clientDataRecord) {
		ClientDataGroup clientDataGroup = clientDataRecord.getClientDataGroup();
		ClientDataGroup collectionItemReferences = clientDataGroup
				.getFirstGroupWithNameInData("collectionItemReferences");
		return collectionItemReferences.getAllGroupsWithNameInData("ref");
	}

	private void addRefToFoundRecords(ClientDataGroup ref, List<RecordIdentifier> foundRecords) {
		String itemType = ref.getFirstAtomicValueWithNameInData("linkedRecordType");
		String itemId = ref.getFirstAtomicValueWithNameInData("linkedRecordId");
		foundRecords.add(RecordIdentifier.usingTypeAndId(itemType, itemId));
	}

}

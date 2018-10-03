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

public class TextsConnectedToItemCollectionFinder implements RecordFinder {

	private CoraClientFactory coraClientFactory;
	private CoraClientConfig coraClientConfig;

	public TextsConnectedToItemCollectionFinder(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	@Override
	public List<RecordIdentifier> findRecordsUsingRecordIdentifier(
			RecordIdentifier recordIdentifier) {
		RecordFinder itemFinder = ReferencedItemsFinder
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);
		List<RecordIdentifier> items = itemFinder
				.findRecordsUsingRecordIdentifier(recordIdentifier);

		List<RecordIdentifier> result = new ArrayList<>();
		for (RecordIdentifier item : items) {
			String readItem = readItemUsingRecordIdentifier(item);
			addText(result, readItem);
			addDefText(result, readItem);
		}

		return result;
	}

	private void addDefText(List<RecordIdentifier> result, String readItem) {
		RecordIdentifier defTextIdentifier = getTextAsRecordIdentifier(readItem, "defTextId");
		result.add(defTextIdentifier);
	}

	private void addText(List<RecordIdentifier> result, String readItem) {
		RecordIdentifier textIdentifier = getTextAsRecordIdentifier(readItem, "textId");
		result.add(textIdentifier);
	}

	private String readItemUsingRecordIdentifier(RecordIdentifier item) {
		String itemId = item.id;
		String itemType = item.type;
		CoraClient coraClient = coraClientFactory.factor(coraClientConfig.userId,
				coraClientConfig.appToken);
		String readItem = coraClient.read(itemType, itemId);
		return readItem;
	}

	private RecordIdentifier getTextAsRecordIdentifier(String readItem, String textNameInData) {
		ClientDataGroup dataGroup = getJsonAsClientDataGroup(readItem);
		ClientDataGroup textIdGroup = dataGroup.getFirstGroupWithNameInData(textNameInData);
		String textType = textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType");
		String textId = textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
		RecordIdentifier itemIdentifier = RecordIdentifier.usingTypeAndId(textType, textId);
		return itemIdentifier;
	}

	private ClientDataGroup getJsonAsClientDataGroup(String json) {
		ClientDataRecord pGroupClientDataRecord = ConverterHelper.getJsonAsClientDataRecord(json);
		return pGroupClientDataRecord.getClientDataGroup();
	}

	public static RecordFinder usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		// TODO Auto-generated method stub
		return new TextsConnectedToItemCollectionFinder(coraClientFactory, coraClientConfig);
	}

}

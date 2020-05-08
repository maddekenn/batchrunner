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

public class TextsConnectedToItemCollectionFinder implements RecordFinder {

	private CoraClientFactory coraClientFactory;
	private CoraClientConfig coraClientConfig;

	public TextsConnectedToItemCollectionFinder(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	public static RecordFinder usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		return new TextsConnectedToItemCollectionFinder(coraClientFactory, coraClientConfig);
	}

	@Override
	public List<RecordIdentifier> findRecordsRelatedToRecordIdentifier(
			RecordIdentifier recordIdentifier) {
		List<RecordIdentifier> items = findItems(recordIdentifier);
		return createRecordIdentifiersForTextsForCollectionAndItems(items);

	}

	private List<RecordIdentifier> findItems(RecordIdentifier recordIdentifier) {
		RecordFinder itemFinder = CollectionWithReferencesFinder
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);
		return itemFinder.findRecordsRelatedToRecordIdentifier(recordIdentifier);
	}

	private List<RecordIdentifier> createRecordIdentifiersForTextsForCollectionAndItems(
			List<RecordIdentifier> items) {
		List<RecordIdentifier> result = new ArrayList<>();
		for (RecordIdentifier item : items) {
			createRecordIdentifierForTextsForItem(result, item);
		}
		return result;
	}

	private void createRecordIdentifierForTextsForItem(List<RecordIdentifier> result,
			RecordIdentifier item) {
		String readItem = readItemUsingRecordIdentifier(item);
		addText(result, readItem);
		addDefText(result, readItem);
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
		return coraClient.read(itemType, itemId);
	}

	private RecordIdentifier getTextAsRecordIdentifier(String readItem, String textNameInData) {
		ClientDataGroup dataGroup = getJsonAsClientDataGroup(readItem);
		ClientDataGroup textIdGroup = dataGroup.getFirstGroupWithNameInData(textNameInData);
		String textType = textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType");
		String textId = textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
		return RecordIdentifier.usingTypeAndId(textType, textId);
	}

	private ClientDataGroup getJsonAsClientDataGroup(String json) {
		ClientDataRecord pGroupClientDataRecord = ConverterHelper.getJsonStringAsClientDataRecord(json);
		return pGroupClientDataRecord.getClientDataGroup();
	}

}

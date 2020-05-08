package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.uu.ub.cora.batchrunner.change.ConverterHelper;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.javaclient.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClient;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public class RecordRelatedElementsFinder implements RecordFinder {

	private CoraClientFactory coraClientFactory;
	private CoraClientConfig coraClientConfig;
	private CoraClient coraClient;

	public RecordRelatedElementsFinder(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;

	}

	public static RecordFinder usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		return new RecordRelatedElementsFinder(coraClientFactory, coraClientConfig);
	}

	@Override
	public List<RecordIdentifier> findRecordsRelatedToRecordIdentifier(
			RecordIdentifier recordIdentifier) {
		coraClient = coraClientFactory.factor(coraClientConfig.userId, coraClientConfig.appToken);
		ClientDataRecord readRecord = getRecordForRecordIdentifier(recordIdentifier);
		return possiblyHandleIncomingLinks(recordIdentifier, readRecord);
	}

	private List<RecordIdentifier> possiblyHandleIncomingLinks(RecordIdentifier recordIdentifier,
			ClientDataRecord readRecord) {
		if (readRecord.getActionLinks().containsKey("read_incoming_links")) {
			return handleIncomingLinks(recordIdentifier);
		}
		return Collections.emptyList();
	}

	private List<RecordIdentifier> handleIncomingLinks(RecordIdentifier recordIdentifier) {
		JsonArray dataFromListOfRecords = getIncomingLinks(recordIdentifier);
		List<RecordIdentifier> result = new ArrayList<>();
		for (JsonValue value : dataFromListOfRecords) {
			RecordIdentifier usingTypeAndId = convertJsonValueToRecordIdentifier(value);
			result.add(usingTypeAndId);
		}
		return result;
	}

	private JsonArray getIncomingLinks(RecordIdentifier recordIdentifier) {
		String readIncomingLinks = coraClient.readIncomingLinks(recordIdentifier.type,
				recordIdentifier.id);
		return getDataFromListOfRecords(readIncomingLinks);
	}

	protected JsonArray getDataFromListOfRecords(String responseText) {
		JsonParser jsonParser = new OrgJsonParser();
		JsonObject jsonValue = jsonParser.parseStringAsObject(responseText);
		JsonObject dataList = (JsonObject) jsonValue.getValue("dataList");
		return dataList.getValueAsJsonArray("data");
	}

	private ClientDataRecord getRecordForRecordIdentifier(RecordIdentifier recordIdentifier) {
		String jsonRecord = coraClient.read(recordIdentifier.type, recordIdentifier.id);

		return ConverterHelper.getJsonStringAsClientDataRecord(jsonRecord);
	}

	private RecordIdentifier convertJsonValueToRecordIdentifier(JsonValue value) {
		ClientDataGroup dataGroup = ConverterHelper
				.getJsonObjectAsClientDataGroup((JsonObject) value);
		ClientDataGroup from = dataGroup.getFirstGroupWithNameInData("from");
		String type = from.getFirstAtomicValueWithNameInData("linkedRecordType");
		String id = from.getFirstAtomicValueWithNameInData("linkedRecordId");
		return RecordIdentifier.usingTypeAndId(type, id);
	}

}

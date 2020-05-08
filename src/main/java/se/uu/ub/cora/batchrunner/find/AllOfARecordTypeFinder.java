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
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonValue;

public class AllOfARecordTypeFinder extends MetadataFinder implements RecordFinder {

	private CoraClientFactory coraClientFactory;
	private CoraClientConfig coraClientConfig;

	public AllOfARecordTypeFinder(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	public static RecordFinder usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		return new AllOfARecordTypeFinder(coraClientFactory, coraClientConfig);
	}

	@Override
	public List<RecordIdentifier> findRecordsRelatedToRecordIdentifier(
			RecordIdentifier recordIdentifier) {
		List<RecordIdentifier> foundRecords = new ArrayList<>();
		JsonArray json = findRecordsUsingRecordType(recordIdentifier.type);
		for (JsonValue value : json) {
			ClientDataGroup clientDataGroup = extractDataGroup(value);
			foundRecords.add(getRecordIdentifierFromDataGroup(clientDataGroup));
		}
		return foundRecords;
	}

	private JsonArray findRecordsUsingRecordType(String recordType) {
		CoraClient coraClient = coraClientFactory.factor(coraClientConfig.userId,
				coraClientConfig.appToken);
		String responseText = coraClient.readList(recordType);
		return getDataFromListOfRecords(responseText);
	}

	private ClientDataGroup extractDataGroup(JsonValue value) {
		String jsonString = getJsonValueAsString(value);
		ClientDataRecord pGroupClientDataRecord = ConverterHelper
				.getJsonStringAsClientDataRecord(jsonString);
		return pGroupClientDataRecord.getClientDataGroup();
	}

	private String getJsonValueAsString(JsonValue value) {
		JsonObject record = (JsonObject) value;
		return record.toJsonFormattedString();
	}

	private RecordIdentifier getRecordIdentifierFromDataGroup(ClientDataGroup dataGroup) {
		ClientDataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		String id = recordInfo.getFirstAtomicValueWithNameInData("id");
		String type = getRecordType(recordInfo);
		return RecordIdentifier.usingTypeAndId(type, id);
	}

	private String getRecordType(ClientDataGroup recordInfo) {
		ClientDataGroup typeGroup = recordInfo.getFirstGroupWithNameInData("type");
		return typeGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

}

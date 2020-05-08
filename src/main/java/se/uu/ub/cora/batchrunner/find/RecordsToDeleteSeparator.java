package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.ResultHolder;
import se.uu.ub.cora.batchrunner.change.ConverterHelper;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.javaclient.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClient;
import se.uu.ub.cora.javaclient.cora.CoraClientException;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;
import se.uu.ub.cora.json.parser.JsonParseException;

public class RecordsToDeleteSeparator implements RecordsSeparator {

	private CoraClientFactory coraClientFactory;
	private CoraClientConfig coraClientConfig;
	private ResultHolder resultHolder;

	public RecordsToDeleteSeparator(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	public static RecordsSeparator usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		return new RecordsToDeleteSeparator(coraClientFactory, coraClientConfig);
	}

	@Override
	public ResultHolder sortOutRecordIdentifiers(List<RecordIdentifier> recordIdentifiers) {
		resultHolder = new ResultHolder();
		CoraClient coraClient = coraClientFactory.factor(coraClientConfig.userId,
				coraClientConfig.appToken);

		List<RecordIdentifier> result = new ArrayList<>();
		for (RecordIdentifier identifier : recordIdentifiers) {
			possiblyAddIdentifierToResult(coraClient, result, identifier);
		}

		return resultHolder;
	}

	private void possiblyAddIdentifierToResult(CoraClient coraClient, List<RecordIdentifier> result,
			RecordIdentifier identifier) {
		ClientDataRecord record = readRecordUsingCoraClientAndIdentifier(coraClient, identifier);
		if (record != null && recordHasNoIncomingLinks(record)) {
			resultHolder.addRecordIdentifier(identifier);
		}
	}

	private ClientDataRecord readRecordUsingCoraClientAndIdentifier(CoraClient coraClient,
			RecordIdentifier identifier) {
		ClientDataRecord record = null;
		try {
			String readJson = coraClient.read(identifier.type, identifier.id);
			record = ConverterHelper.getJsonStringAsClientDataRecord(readJson);
		} catch (CoraClientException ce) {
			resultHolder.addMessage(ce.getMessage());
		} catch (JsonParseException e) {
			resultHolder.addMessage("Error parsing json for type: " + identifier.type + " and id: "
					+ identifier.id);
		}

		return record;
	}

	private boolean recordHasNoIncomingLinks(ClientDataRecord record) {
		return !record.getActionLinks().containsKey("read_incoming_links");
	}

}

package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.CoraClientSpy;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactory;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactoryImp;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataGroupConverter;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public class DataDividerUpdaterTest {

	private CoraClientFactorySpy coraClientFactory;
	private CoraClientConfigSpy coraClientConfig;

	@BeforeMethod
	public void setUp() {
		coraClientConfig = new CoraClientConfigSpy("someUserId", "someAppToken",
				"someAppTokenVerifierUrl", "someCoraUrl");
		coraClientFactory = new CoraClientFactorySpy("someUserId", "someAppToken");
	}

	@Test
	public void init() {
		DataUpdater updater = DataDividerUpdater
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);
		assertTrue(updater.getCoraClientFactory() instanceof CoraClientFactorySpy);
	}

	@Test
	public void testUpdateDataDivider() {
		DataUpdater updater = DataDividerUpdater
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		String updatedRecord = updater.updateDataDividerInRecordUsingTypeIdAndNewDivider(
				"metadataItemCollection", "languageCollection", "newDataDivider");

		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.recordTypes.get(0), "metadataItemCollection");
		assertEquals(coraClientSpy.recordIds.get(0), "languageCollection");

		String dataDivider = extractDataDividerFromUpdatedJson(updatedRecord);
		assertEquals(dataDivider, "newDataDivider");

	}

	private String extractDataDividerFromUpdatedJson(String outputString) {
		ClientDataGroup pGroupClientDataGroup = getJsonAsClientDataGroup(outputString);
		ClientDataGroup recordInfo = pGroupClientDataGroup
				.getFirstGroupWithNameInData("recordInfo");
		ClientDataGroup dataDividerGroup = recordInfo.getFirstGroupWithNameInData("dataDivider");
		String dataDivider = dataDividerGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
		return dataDivider;
	}

	private ClientDataGroup getJsonAsClientDataGroup(String outputString) {
		OrgJsonParser jsonParser = new OrgJsonParser();
		JsonValue jsonValue = jsonParser.parseString(outputString);
		JsonToDataConverterFactory converterFactory = new JsonToDataConverterFactoryImp();
		JsonToDataGroupConverter dataGroupConverter = JsonToDataGroupConverter
				.forJsonObjectUsingConverterFactory((JsonObject) jsonValue, converterFactory);

		return (ClientDataGroup) dataGroupConverter.toInstance();
	}

	@Test
	public void testUpdateDataDividerForListOfRecordIdentifiers() {
		DataUpdater updater = DataDividerUpdater
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		List<RecordIdentifier> recordIdentifiers = new ArrayList<>();
		recordIdentifiers.add(
				RecordIdentifier.usingTypeAndId("metadataItemCollection", "languageCollection"));
		recordIdentifiers.add(RecordIdentifier.usingTypeAndId("metadataGroup", "languageGroup"));

		List<String> updatedRecords = updater.updateDataDividerUsingRecordIdentifiersAndNewDivider(
				recordIdentifiers, "newDataDivider");

		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.recordTypes.get(0), "metadataItemCollection");
		assertEquals(coraClientSpy.recordIds.get(0), "languageCollection");

		String dataDivider = extractDataDividerFromUpdatedJson(updatedRecords.get(0));
		assertEquals(dataDivider, "newDataDivider");

		CoraClientSpy coraClientSpy2 = coraClientFactory.factoredClientSpies.get(1);
		assertEquals(coraClientSpy2.recordTypes.get(0), "metadataGroup");
		assertEquals(coraClientSpy2.recordIds.get(0), "languageGroup");

		String dataDivider2 = extractDataDividerFromUpdatedJson(updatedRecords.get(1));
		assertEquals(dataDivider2, "newDataDivider");

	}

	@Test
	public void testUpdateDataDividerUpdateException() {
		DataUpdater updater = DataDividerUpdater
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);
		String message = updater.updateDataDividerInRecordUsingTypeIdAndNewDivider("someRecordType",
				"someNonWorkingRecordId", "newDataDivider");
		assertEquals(message,
				"Unable to update json: {\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"enItem\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"newDataDivider\"}],\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"genericCollectionItem\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"createdBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01  00:00:00.0\"},{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"updatedBy\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:53:07.0\"}],\"name\":\"updated\"}],\"name\":\"recordInfo\"},{\"name\":\"nameInData\",\"value\":\"sv\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"coraText\"},{\"name\":\"linkedRecordId\",\"value\":\"enItemText\"}],\"name\":\"textId\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"coraText\"},{\"name\":\"linkedRecordId\",\"value\":\"enItemDefText\"}],\"name\":\"defTextId\"}],\"name\":\"metadata\",\"attributes\":{\"type\":\"collectionItem\"}} Error: Unable to fully parse json string");
	}
}

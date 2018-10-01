package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactory;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactoryImp;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataGroupConverter;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public class DataDividerUpdaterTest {

	private String url;
	private HttpHandlerFactorySpy httpHandlerFactory;
	private CoraClientFactorySpy coraClientFactory;
	private CoraClientConfigSpy coraClientConfig;

	@BeforeMethod
	public void setUp() {
		httpHandlerFactory = new HttpHandlerFactorySpy();
		coraClientConfig = new CoraClientConfigSpy("someUserId", "someAppToken",
				"someAppTokenVerifierUrl", "someCoraUrl");
		coraClientFactory = new CoraClientFactorySpy();
		url = "http://someTestUrl/";
	}

	@Test
	public void init() {
		DataDividerUpdater updater = DataDividerUpdater
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);
		assertTrue(updater.getCoraClientFactory() instanceof CoraClientFactorySpy);
	}

	@Test
	public void testUpdateDataDivider() {
		DataDividerUpdater updater = DataDividerUpdater
				.usingCoraClientFactoryAndClientConfig(coraClientFactory, coraClientConfig);

		String updatedRecord = updater.updateDataDividerInRecordUsingTypeIdAndNewDivider(
				"someRecordType", "someRecordId", "newDataDivider");

		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.recordType, "someRecordType");
		assertEquals(coraClientSpy.recordId, "someRecordId");

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
}

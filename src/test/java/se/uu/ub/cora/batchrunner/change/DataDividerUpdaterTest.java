package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.batchrunner.find.HttpHandlerSpy;
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

	@BeforeMethod
	public void setUp() {
		httpHandlerFactory = new HttpHandlerFactorySpy();
		url = "http://someTestUrl/";
	}

	@Test
	public void init() {
		DataDividerUpdater updater = DataDividerUpdater.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		assertTrue(updater.getHttpHandlerFactory() instanceof HttpHandlerFactorySpy);
		assertEquals(updater.getUrl(), url);
	}

	@Test
	public void testUpdateDataDivider() {
		DataUpdater updater = DataDividerUpdater.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		String message = updater.updateDataDividerInRecordUsingTypeIdAndNewDivider("someRecordType",
				"someRecordId", "newDataDivider");

		assertTrue(message.startsWith("200 Ok:"));

		HttpHandlerSpy httpHandlerReadSpy = httpHandlerFactory.httpHandlerSpies.get(0);
		assertEquals(httpHandlerReadSpy.requestMethod, "GET");
		assertTrue(httpHandlerReadSpy.urlString.endsWith("/someRecordType/someRecordId"));

		HttpHandlerSpy httpHandlerUpdateSpy = httpHandlerFactory.httpHandlerSpies.get(1);
		assertEquals(httpHandlerUpdateSpy.requestMethod, "POST");
		assertTrue(httpHandlerUpdateSpy.urlString.endsWith("/someRecordType/someRecordId"));

		String outputString = httpHandlerUpdateSpy.outputString;
		String dataDivider = extractDataDividerFromUpdatedJson(outputString);
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

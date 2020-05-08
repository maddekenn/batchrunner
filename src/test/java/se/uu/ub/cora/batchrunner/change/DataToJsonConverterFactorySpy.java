package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataElement;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverter;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactory;
import se.uu.ub.cora.json.builder.JsonBuilderFactory;

public class DataToJsonConverterFactorySpy implements DataToJsonConverterFactory {

	public ClientDataElement clientDataElement;
	public DataToJsonConverterSpy dataToJsonConverterSpy;

	@Override
	public DataToJsonConverter createForClientDataElement(JsonBuilderFactory factory,
			ClientDataElement clientDataElement) {
		this.clientDataElement = clientDataElement;
		dataToJsonConverterSpy = new DataToJsonConverterSpy(clientDataElement);
		return dataToJsonConverterSpy;
	}

	@Override
	public DataToJsonConverter createForClientDataElementIncludingActionLinks(
			JsonBuilderFactory factory, ClientDataElement clientDataElement,
			boolean includeActionLinks) {
		this.clientDataElement = clientDataElement;
		dataToJsonConverterSpy = new DataToJsonConverterSpy(clientDataElement);
		return dataToJsonConverterSpy;
	}

}

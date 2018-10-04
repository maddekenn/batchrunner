package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.client.CoraClientConfig;
import se.uu.ub.cora.client.CoraClientException;
import se.uu.ub.cora.client.CoraClientFactory;

public class DataUpdaterSpy implements DataUpdater {

	public String url;
	public HttpHandlerFactorySpy httpHandlerFactory;
	public List<String> types = new ArrayList<>();
	public List<String> recordIds = new ArrayList<>();
	public List<String> dataDividers = new ArrayList<>();
	public CoraClientFactory coraClientFactory;
	public CoraClientConfig coraClientConfig;

	public DataUpdaterSpy(CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	@Override
	public String updateDataDividerInRecordUsingTypeIdAndNewDivider(String type, String recordId,
			String newDataDivider) {
		types.add(type);
		recordIds.add(recordId);
		dataDividers.add(newDataDivider);
		if ("errorItem".equals(recordId)) {
			throw new CoraClientException("Error from DataUpdaterSpy");
		}
		return null;
	}

	public static DataUpdaterSpy usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		return new DataUpdaterSpy(coraClientFactory, coraClientConfig);
	}

	@Override
	public CoraClientFactory getCoraClientFactory() {
		// TODO Auto-generated method stub
		return null;
	}

}

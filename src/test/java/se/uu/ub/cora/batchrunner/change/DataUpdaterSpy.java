package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class DataUpdaterSpy implements DataUpdater {

	public String url;
	public HttpHandlerFactorySpy httpHandlerFactory;
	public List<String> types = new ArrayList<>();
	public List<String> recordIds = new ArrayList<>();
	public List<String> dataDividers = new ArrayList<>();

	public DataUpdaterSpy(String url, HttpHandlerFactory httpHandlerFactory) {
		this.url = url;
		this.httpHandlerFactory = (HttpHandlerFactorySpy) httpHandlerFactory;
	}

	@Override
	public String updateDataDividerInRecordUsingTypeIdAndNewDivider(String type, String recordId,
			String newDataDivider) {
		types.add(type);
		recordIds.add(recordId);
		dataDividers.add(newDataDivider);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpHandlerFactory getHttpHandlerFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	public static DataUpdaterSpy usingURLAndHttpHandlerFactory(String url,
			HttpHandlerFactory httpHandlerFactory) {
		return new DataUpdaterSpy(url, httpHandlerFactory);
	}

}

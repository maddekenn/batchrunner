package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.clientdata.ClientDataGroup;

public class DataChangerFactorySpy implements DataChangerFactory {

	public List<String> types = new ArrayList<>();
	public List<ClientDataGroup> dataGroups = new ArrayList<>();
	public DataChangerSpy dataChanger;
	public List<DataChangerSpy> factoredDataChangers = new ArrayList<>();

	@Override
	public DataChanger factor(String type, ClientDataGroup dataGroupToChange) {
		types.add(type);
		dataGroups.add(dataGroupToChange);
		dataChanger = new DataChangerSpy();
		factoredDataChangers.add(dataChanger);
		return dataChanger;
	}

}

package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataGroup;

public interface DataChangerFactory {

	DataChanger factor(String type, ClientDataGroup dataGroupToChange);

}

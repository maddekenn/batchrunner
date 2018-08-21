package se.uu.ub.cora.batchrunner.change;

import java.util.List;

public interface Modifier {
	List<String> modifyData(String recordTypeId);
}

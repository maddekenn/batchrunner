package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class ModifierSpy implements Modifier {
	public List<String> recordTypes = new ArrayList<>();

	@Override
	public List<String> modifyData(String recordTypeId) {
		recordTypes.add(recordTypeId);
		return Collections.emptyList();
	}

	public static ModifierSpy usingURLAndHttpHandlerFactory(String url,
			HttpHandlerFactory httpHandlerFactory) {

		return new ModifierSpy();
	}
}

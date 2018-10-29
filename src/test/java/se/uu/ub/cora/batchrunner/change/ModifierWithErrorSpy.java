package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class ModifierWithErrorSpy implements Modifier {
	public List<String> recordTypes = new ArrayList<>();

	@Override
	public List<String> modifyData(String recordTypeId) {
		List<String> errorMessages = new ArrayList<>();
		recordTypes.add(recordTypeId);
		errorMessages.add("some error from modifierSpy");
		return errorMessages;
	}

	public static ModifierWithErrorSpy usingURLAndHttpHandlerFactory(String url,
			HttpHandlerFactory httpHandlerFactory) {

		return new ModifierWithErrorSpy();
	}
}

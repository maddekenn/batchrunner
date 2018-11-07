package se.uu.ub.cora.batchrunner;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.clientdata.RecordIdentifier;

public class ResultHolder {
	public List<RecordIdentifier> recordIdentifiers = new ArrayList<>();
	public List<String> messages = new ArrayList<>();

	public void addRecordIdentifier(RecordIdentifier recordIdentifier) {
		recordIdentifiers.add(recordIdentifier);
	}

	public void addMessage(String message) {
		messages.add(message);
	}

}

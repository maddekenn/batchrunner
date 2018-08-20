package se.uu.ub.cora.batchrunner.change;

public class PresentationObject {

	String oldPGroupId;
	String newPGroupId;
	String presentationId;

	public PresentationObject(String oldPGroupId, String newPGroupId, String presentationId) {
		this.oldPGroupId = oldPGroupId;
		this.newPGroupId = newPGroupId;
		this.presentationId = presentationId;

	}

}

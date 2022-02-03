package se.uu.ub.cora.batchrunner;

public final class NotImplementedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public static NotImplementedException withMessage(String message) {
		return new NotImplementedException(message);
	}

	private NotImplementedException(String message) {
		super(message);
	}

}
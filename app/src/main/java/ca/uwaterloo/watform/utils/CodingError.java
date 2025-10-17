package ca.uwaterloo.watform.utils;

public class CodingError {

	// missing cases in the code
	public static String missingCase(String x) throws ErrorFatal {
		throw new ErrorFatal("missing case " + x);
	}

	// failed dynamic cast; incorrect assumption about object's runtime type
	public static ErrorFatal failedCast(String s) {
		return new ErrorFatal("Failed Dynamic Cast: " + s);
	}
}

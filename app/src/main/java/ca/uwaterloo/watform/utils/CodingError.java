package ca.uwaterloo.watform.utils;

public class CodingError {

	// missing cases in the code
	public static String missingCase(String x) throws ErrorFatal {
		throw new ErrorFatal("missing case " + x);
	}

}
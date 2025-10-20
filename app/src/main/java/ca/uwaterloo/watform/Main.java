package ca.uwaterloo.watform;

public class Main {
    public static void main(String[] args) throws Exception {

        final String dashToTLAPlus = "--tla";
        final String dashToAlloy = "--alloy";
        final String predicateAbstraction = "--pred";
        final String test = "--test";

        final String defaultMessage =
                """
		Possible arguments:
		%s
		%s
		%s"""
                        .formatted(dashToAlloy, dashToTLAPlus, predicateAbstraction, test);

        if (args.length == 0) {
            System.out.println(defaultMessage);
        } else {
            String[] passedArgs = new String[args.length - 1];
            for (int i = 1; i < args.length; i++) passedArgs[i - 1] = args[i];
            if (args[0].equals(dashToAlloy)) {
                ca.uwaterloo.watform.parser.Main.main(passedArgs);
            } else if (args[0].equals(dashToTLAPlus)) {
                ca.uwaterloo.watform.dashtotlaplus.Main.main(passedArgs);
            } else if (args[0].equals(predicateAbstraction)) {
                ca.uwaterloo.watform.predabstraction.Main.main(passedArgs);
            } else if (args[0].equals(test)) {
                ca.uwaterloo.watform.test.Main.main(passedArgs);
            } else System.out.println(defaultMessage);
        }
    }
}

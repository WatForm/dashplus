package ca.uwaterloo.watform.debugcli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DebugCli {
    private static final String PROMPT = "(aldb) ";
    private final DebugSimulationManager simulationManager;

    public DebugCli(DebugSimulationManager simulationManager) {
        this.simulationManager = simulationManager;
    }

    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line;
            try {
                System.out.print(PROMPT);
                line = reader.readLine();
                if (line == null) {
                    return;
                }
            } catch (IOException e) {
                System.out.println("Error reading input: " + e.getMessage());
                return;
            }

            String trimmed = line.trim();
            String[] input = trimmed.isEmpty() ? new String[] {} : trimmed.split("\\s+");

            DebugCommand command =
                    DebugCommandRegistry.commandForString(input.length > 0 ? input[0] : "");
            command.execute(input, simulationManager);
        }
    }
}

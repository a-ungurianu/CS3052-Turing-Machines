import tm.Utils;
import tm.description.State;
import tm.runner.MachineRunner;
import tm.runner.MachineState;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void showHelp() {
        System.out.println("Usage: ");
        System.out.println("runtm <desription-file> <input>");
    }


    public static void main(String... args) throws IOException {
        if(args.length != 2) {
            showHelp();
            return;
        }

        String descriptionPath = args[0];
        String inputPath = args[1];

        State startState = Utils.readMachine(descriptionPath);

        Scanner inputScanner = new Scanner(new FileInputStream(inputPath));
        String input = inputScanner.nextLine();

        MachineRunner runner = new MachineRunner(startState, input, true);

        MachineState run = runner.run();

        if(run != null) {
            System.out.println("The machine accepted the input.");
        }
        else {
            System.out.println("The machine did not accept the input.");
        }
        System.out.println("Number of transitions necessary: " + runner.getTransitionCount());
    }
}

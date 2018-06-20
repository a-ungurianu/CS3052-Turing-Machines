package profile;

import tm.Utils;
import tm.description.State;
import tm.runner.MachineRunner;

import java.util.Random;

public class ProfileAdditionCheck {
    final static Random RANDOM = new Random();

    static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    static String generateAdditionInput(int w) {
        int w0 = RANDOM.nextInt(w+1);
        int w1 = w - w0;
        return reverse(Integer.toString(w0, 2)) +
                '#' +
                reverse(Integer.toString(w1, 2)) +
                '#' +
                reverse(Integer.toString(w,2));

    }

    public static void main(String[] args) throws Exception {
        State startState = Utils.readMachine("check_add.tm");

        for(int w = 1; w < 1000; ++w) {
            long totalTransitions = 0;
            for(int trial = 0; trial < 100; ++trial) {
                MachineRunner machineRunner = new MachineRunner(startState, generateAdditionInput(w));
                if(machineRunner.run() == null) {
                    throw new Exception("Test input failed");
                }
                totalTransitions += machineRunner.getTransitionCount();
            }
            System.out.println("" + w + ", " + totalTransitions / 10);
        }
    }
}

package profile;

import tm.Utils;
import tm.description.State;
import tm.runner.MachineRunner;

import java.util.Random;

public class ProfileTwoPower {
    final static Random RANDOM = new Random();

    static boolean isPow2(int number) {
        return number > 0 && ((number & (number - 1)) == 0);
    }

    static String generateInput(int number) {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < number; ++i) {
            builder.append('1');
        }
        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        State startState = Utils.readMachine("power_of_two.tm");

        for(int length = 1; length < 1024; ++length) {
            long totalTransitions = 0;
            for(int trial = 0; trial < 1; ++trial) {
                MachineRunner machineRunner = new MachineRunner(startState, generateInput(length));
                if(isPow2(length) != (machineRunner.run() != null)) {
                    throw new Exception("Machine failed on " + length);
                }
                totalTransitions += machineRunner.getTransitionCount();
            }
            System.out.println("" + length + ", " + totalTransitions / 10);
        }
    }
}

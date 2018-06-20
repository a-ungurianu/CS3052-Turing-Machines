package profile;

import tm.Utils;
import tm.description.State;
import tm.runner.MachineRunner;

import java.util.Random;

public class ProfileParenthesis {
    final static Random RANDOM = new Random();

    static String generateParenthesis(int halfLength) {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < halfLength; ++i) {
            builder.append('[');
        }
        for(int i = 0; i < halfLength; ++i) {
            builder.append(']');
        }

        String half = builder.toString();

        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        State startState = Utils.readMachine("parenthesis.tm");

        for(int length = 1; length < 500; ++length) {
            long totalTransitions = 0;
            for(int trial = 0; trial < 10; ++trial) {
                MachineRunner machineRunner = new MachineRunner(startState, generateParenthesis(length));
                if(machineRunner.run() == null) {
                    throw new Exception("Test failed for length:" + (length*2));
                }
                totalTransitions += machineRunner.getTransitionCount();
            }
            System.out.println("" + length*2 + ", " + totalTransitions / 10);
        }
    }
}

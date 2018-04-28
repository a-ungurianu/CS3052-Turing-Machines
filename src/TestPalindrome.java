import java.io.IOException;
import java.util.Random;

public class TestPalindrome {
    final static Random RANDOM = new Random();

    static String generatePalindrome(int length) {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < length / 2; ++i) {
            builder.append(RANDOM.nextInt(3));
        }

        String half = builder.toString();

        if(length % 2 == 1) {
            builder.append(RANDOM.nextInt(3));
        }

        builder.append(new StringBuilder(half).reverse());

        return builder.toString();
    }

    public static void main(String[] args) throws IOException {
        State startState = Main.readMachine("palindrome.tm");

        for(int length = 1; length < 1000; ++length) {
            long totalTransitions = 0;
            for(int trial = 0; trial < 10; ++trial) {
                MachineRunner machineRunner = new MachineRunner(startState, generatePalindrome(length));
                machineRunner.run();
                totalTransitions += machineRunner.getTransitionCount();
            }
            System.out.println("" + length + ", " + totalTransitions / 10);
        }
    }
}

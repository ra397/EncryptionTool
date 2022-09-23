import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Key {
    private final int numKeys;

    // generate keys file
    public Key(int numKeys) {
        this.numKeys = numKeys;
        this.makeKeysFile(); // write private member variables to file
    }

    // helper function that generate a random integer between min and max
    private Integer generateRandomInteger(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    // make an arrayList of keys
    private ArrayList<Integer> makeKeysList(int numKeys) {
        ArrayList<Integer> keysList = new ArrayList<>();
        for (int i = 0; i < numKeys; i++) {
            keysList.add((generateRandomInteger(1, 25)));
        }
        return keysList;
    }

    // overwrite keys with new keys list
    private void makeKeysFile() {
        File keyFile = new File("keys.txt");
        try {
            FileWriter keyWriter = new FileWriter(keyFile, false);
            keyWriter.write(0 + "\n");
            keyWriter.write(String.valueOf(makeKeysList(this.numKeys)));
            keyWriter.close();
        } catch (IOException e) {
            System.out.println("Could not update file contents");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String numKeys;

        do {
            System.out.print("Enter the number of keys you want to generate: ");
            numKeys = scanner.nextLine();
        } while (!isInteger(numKeys));

        new Key(Integer.parseInt(numKeys));
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

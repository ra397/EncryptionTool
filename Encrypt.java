import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Encrypt {
    private final char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private final String keyFilePath;
    private int prevKeyIndex;
    private int currKeyIndex;
    private ArrayList<Integer> keysList;
    private static int id = 0;


    public Encrypt(String message, String keyFilePath) {
        this.keyFilePath = keyFilePath;

        readKeyFile(keyFilePath);
        String encryptedText = encryptMessage(message);
        generateEncryptedFile(encryptedText);
        updateKeysFile();

        id++;
    }


    private ArrayList<Integer> readKeysList(String data) {
        ArrayList<Integer> keyList = new ArrayList<>();
        for (Character c : data.toCharArray()) {
            if (Character.isDigit(c)) {
                keyList.add(Integer.parseInt(c.toString()));
            }
        }
        return keyList;
    }

    private void readKeyFile(String keyFilePath) {
        try {
            File file = new File(keyFilePath);
            try (FileReader fileReader = new FileReader(file)) {
                Scanner scanner = new Scanner(fileReader);
                this.currKeyIndex = Integer.parseInt(scanner.nextLine());
                this.keysList = readKeysList(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Could not read keys file");
            System.exit(0);
        }
    }

    private String encryptMessage(String message) {
        StringBuilder encryptedMessage = new StringBuilder();
        this.prevKeyIndex = this.currKeyIndex;
        for (Character c : message.toUpperCase().toCharArray()) {
            if (c.hashCode() >= 65 && c.hashCode() <= 90) {
                int newCharIndex = c.hashCode() - 65 + this.keysList.get(this.currKeyIndex);
                if (newCharIndex > 25) {
                    newCharIndex = newCharIndex % 26;
                }
                encryptedMessage.append(alphabet[newCharIndex]);
                this.currKeyIndex++;
            } else {
                encryptedMessage.append(c);
            }
        }
        return String.valueOf(encryptedMessage);
    }


    private void generateEncryptedFile(String encryptedMessage) {
        try {
            FileWriter messageWriter = new FileWriter("encryptedFile" + id + ".txt", false);
            messageWriter.write(String.format(this.prevKeyIndex + "%n" + encryptedMessage));
            messageWriter.close();
        } catch (IOException e) {
            System.out.println("Could not generate encrypted file.");
        }
    }

    private void updateKeysFile() {
        try {
            FileWriter messageWriter = new FileWriter(this.keyFilePath, false);
            messageWriter.write(String.valueOf(this.currKeyIndex) + '\n');
            messageWriter.write(String.valueOf(this.keysList));
            messageWriter.close();
        } catch (IOException e) {
            System.out.println("Could not update keys file");
        }
    }

    /*
     * DRIVER PROGRAM
     *  */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int choice = displayMain(scanner);
        String message;
        String keyFilePath;

        while (choice != 2) {
            System.out.print("Enter the path to the keys file: ");
            keyFilePath = scanner.nextLine();

            System.out.print("Enter the message you wish to encrypt: ");
            message = scanner.nextLine();

            new Encrypt(message, keyFilePath);

            choice = displayMain(scanner);
        }
    }

    public static Integer displayMain(Scanner scanner) {
        String choice;
        do {
            System.out.println("""
                    * ----------------------- *           
                    |   1- Encrypt a message  |  
                    |   2- Exit               |  
                    * ----------------------- *
                        """);
            System.out.print("Enter choice: ");
            choice = scanner.nextLine();
        } while (!isInteger(choice) || Integer.parseInt(choice) < 1 || Integer.parseInt(choice) > 2);
        return Integer.parseInt(choice);
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



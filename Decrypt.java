import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Decrypt {
    private final char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private final String keyFilePath;
    private ArrayList<Integer> keysList;
    private final String encryptedFilePath;
    private int encryptionIndex;
    private static int id = 0;

    public Decrypt(String encryptedFile, String keyFile) {
        this.encryptedFilePath = encryptedFile;
        this.keyFilePath = keyFile;

        readKeyFile(this.keyFilePath);
        String encryptedMessage = readEncryptedFile(this.encryptedFilePath);
        String decryptedMessage = decryptMessage(encryptedMessage);
        generateDecryptedFile(decryptedMessage);

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
                scanner.nextLine();
                this.keysList = readKeysList(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Could not read keys file");
            System.exit(0);
        }
    }

    private String readEncryptedFile(String encryptedFilePath) {
        String encryptedMessage = null;
        try {
            File file = new File(this.encryptedFilePath);
            try (FileReader fileReader = new FileReader(file)) {
                Scanner scanner = new Scanner(fileReader);
                this.encryptionIndex = Integer.parseInt(scanner.nextLine());
                encryptedMessage = scanner.nextLine();
            }
        } catch (IOException e) {
            System.out.println("Could not read keys file");
            System.exit(0);
        }
        return encryptedMessage;
    }

    private String decryptMessage(String message) {
        StringBuilder decryptedMessage = new StringBuilder();
        int keyIndex = this.encryptionIndex;
        for (Character c : message.toUpperCase().toCharArray()) {
            if (c.hashCode() >= 65 && c.hashCode() <= 90) {
                int newCharIndex = c.hashCode() - 65 - this.keysList.get(keyIndex);
                if (newCharIndex < 0) {
                    newCharIndex = newCharIndex + 26;
                }
                decryptedMessage.append(alphabet[newCharIndex]);
                keyIndex++;
            } else {
                decryptedMessage.append(c);
            }
        }
        return String.valueOf(decryptedMessage);
    }

    private void generateDecryptedFile(String decryptedMessage) {
        try {
            FileWriter messageWriter = new FileWriter("decryptedFile" + id + ".txt", false);
            messageWriter.write(decryptedMessage);
            messageWriter.close();
        } catch (IOException e) {
            System.out.println("Could not generate decrypted file.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int choice = displayMain(scanner);
        String encryptedFilePath;
        String keyFilePath;

        while (choice != 2) {
            System.out.print("Enter the path to the keys file: ");
            keyFilePath = scanner.nextLine();

            System.out.print("Enter the path to the encrypted file: ");
            encryptedFilePath = scanner.nextLine();

             new Decrypt(encryptedFilePath, keyFilePath);

            choice = displayMain(scanner);
        }
    }

    public static Integer displayMain(Scanner scanner) {
        String choice;
        do {
            System.out.println("""
                    * ----------------------- *           
                    |   1- Decrypt a message  |  
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

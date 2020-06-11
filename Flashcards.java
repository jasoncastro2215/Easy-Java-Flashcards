import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

public class Flashcards{
    final static Scanner scanner = new Scanner(System.in);
    static Map<String, String> map = new HashMap<>();
    static Map<String, Integer> hard = new HashMap<>();
    static String log = "";
    static String imports = "";//C:\Users\jason\Desktop\file.txt
    static String exports = "";
    static boolean firstImport = false;
    static boolean lastExports = false;
    public static void main(String[] args) {
        boolean is_exit = false;
        if (args.length == 2) {
            if (args[0].equals("-import")) {
                imports = args[1];
            } else if (args[0].equals("-export")) {
                exports = args[1];
            }
        } else if (args.length == 4) {
            if (args[0].equals("-import")) {
                imports = args[1];
                if (args[2].equals("-export")) {
                    exports = args[3];
                }
            } else if (args[0].equals("-export")) {
                exports = args[1];
                if (args[2].equals("-import")) {
                    imports = args[3];
                }
            }
        }
        imp();
        firstImport = true;
        while (!is_exit) {
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            addLog("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):\n");
            String input = scanner.nextLine();
            addLog(input+"\n");
            switch (input) {
                case "add":
                    add();
                    break;
                case "remove":
                    remove();
                    break;
                case "import":
                    imp();
                    break;
                case "export":
                    exp();
                    break;
                case "ask":
                    ask();
                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    addLog("Bye bye!");
                    lastExports = true;
                    exp();
                    is_exit = true;
                    break;
                case "log":
                    log();
                    break;
                case "hardest card":
                    hardest();
                    break;
                case "reset stats":
                    reset();
                    break;
            }
        }
    }
    public static void add() {
        System.out.println("The card:");
        addLog("The card:\n");
        String key = scanner.nextLine();
        addLog(key+"\n");
        if (map.containsKey(key)) {
            System.out.println("The card \"" + key + "\" already exists.\n");
            addLog("The card \"" + key + "\" already exists.\n\n");
        } else {
            System.out.println("The definition of the card:");
            addLog("The definition of the card:\n");
            String value = scanner.nextLine();
            addLog(value+"\n");
            if (map.containsValue(value)) {
                System.out.println("The definition \"" + value + "\" already exists.\n");
                addLog("The definition \"" + value + "\" already exists.\n\n");
            } else {
                map.put(key, value);
                System.out.println("The pair (\"" + key + "\":" + "\"" + value + "\") has been added.\n");
                addLog("The pair (\"" + key + "\":" + "\"" + value + "\") has been added.\n\n");
            }
        }
    }

    public static void remove() {
        System.out.println("The card:");
        addLog("The card:\n");
        String input = scanner.nextLine();
        addLog(input+"\n");

        if (map.containsKey(input)) {
            map.remove(input);
            hard.remove(input);
            System.out.println("The card has been removed.\n");
            addLog("The card has been removed.\n\n");
        } else {
            System.out.println("Can't remove \"" + input + "\": there is no such card.\n");
            addLog("Can't remove \"" + input + "\": there is no such card.\n\n");
        }
    }

    public static void imp() {
        String input = "";
        if (firstImport) {
            System.out.println("File name:");
            addLog("File name:\n");
            input = scanner.nextLine();
            addLog(input+"\n");
        } else {
            input = imports;
        }
        if (!input.isEmpty()) {
            File file = new File(input);
            int count = 0;
            try (Scanner importFile = new Scanner(file)) {
                while (importFile.hasNext()) {
                    String[] arr = importFile.nextLine().split("!@#");
                    map.put(arr[0], arr[1]);
                    if (Integer.parseInt(arr[2]) != 0) {
                        hard.put(arr[0], Integer.parseInt(arr[2]));
                    }
                    count++;
                }
                System.out.println(count + " cards have been loaded.\n");
                addLog(count + " cards have been loaded.\n\n");
            } catch (FileNotFoundException e) {
                System.out.println("File not found.\n");
                addLog("File not found.\n\n");
            }
        }
    }

    public static void exp() {
        String input = "";
        if (lastExports) {
            input = exports;
        } else {
            System.out.println("File name:");
            addLog("File name:\n");
            input = scanner.nextLine();
            addLog(input+"\n");
        }
        if (!input.isEmpty()) {
            File file = new File(input);
            try (PrintWriter printWriter = new PrintWriter(file)) {
                for (Entry set : map.entrySet()) {
                    if (hard.containsKey(set.getKey())) {
                        for (Entry set2 : hard.entrySet()) {
                            if (set.getKey().equals(set2.getKey())) {
                                printWriter.println(set.getKey() + "!@#" + set.getValue() + "!@#" + set2.getValue());
                                break;
                            }
                        }
                    } else {
                        printWriter.println(set.getKey()+"!@#"+set.getValue()+"!@#"+0);
                    }
                }
            } catch (IOException e) {
                System.out.print(e);
            }

            System.out.println(map.size() + " cards have been saved.\n");
            addLog(map.size() + " cards have been saved.\n\n");
        }
    }

    public static void ask() {
        System.out.println("How many times to ask?");
        addLog("How many times to ask?\n");
        int input = Integer.parseInt(scanner.nextLine());
        addLog(input+"\n");
        int count = 0;
        while(count < input) {
            for (Entry set : map.entrySet()) {
                System.out.println("Print the definition of \"" +
                        set.getKey() + "\":");
                addLog("Print the definition of \"" +
                        set.getKey() + "\":\n");
                String value = scanner.nextLine();
                addLog(value+"\n");
                if (value.equals(set.getValue())) {
                    System.out.println("Correct answer\n");
                    addLog("Correct answer\n\n");
                } else if (map.containsValue(value)) {
                    for (Entry set2 : map.entrySet()) {
                        if (value.equals(set2.getValue())) {
                            System.out.println("Wrong answer. The correct one is \""+set.getValue()+"\", " +
                                    "you've just written the definition of \""+set2.getKey()+"\"\n");
                            addLog("Wrong answer. The correct one is \""+set.getValue()+"\", " +
                                    "you've just written the definition of \""+set2.getKey()+"\"\n\n");
                            addHardest(set);
                        }
                    }
                } else {
                    System.out.println("Wrong answer. The correct one is \""
                            + set.getValue() + "\".\n");
                    addLog("Wrong answer. The correct one is \""
                            + set.getValue() + "\".\n\n");
                    addHardest(set);
                }
                ++count;
                if (count == input) {
                    break;
                }
            }
        }
    }

    public static void log() {
        System.out.println("File name:");
        addLog("File name:\n");
        String input = scanner.nextLine();
        addLog(input+"\n");
        File file = new File(input);

        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.print(log);
        } catch (IOException e) {
            System.out.print(e);
        }

        System.out.println("The log has been saved.\n");
    }

    public static void hardest() {
        int max = Integer.MIN_VALUE;
        Map<String, Integer> temp = new HashMap<>();
        temp.putAll(hard);

        for (Entry set : hard.entrySet()) {
            if (max < (Integer) set.getValue()) {
                max = (Integer) set.getValue();
            }
        }

        for (Entry set : hard.entrySet()) {
            if (max != (Integer) set.getValue()) {
                temp.remove(set.getKey());
            }
        }

        if (max == Integer.MIN_VALUE) {
            System.out.println("There are no cards with errors.\n");
            addLog("There are no cards with errors.\n\n");
        } else {
            String str = "The hardest card is ";
            int size = temp.size();
            int count = 1;
            for (Entry set : temp.entrySet()) {
                if (count == size) {
                    str += "\"" + set.getKey() + "\"";
                    break;
                }
                str +=  "\"" + set.getKey() + "\", ";
                count++;
            }
            str += ". You have " + max + " errors answering it.\n";
            System.out.println(str);
            addLog(str+"\n");
        }
        temp.clear();
    }

    public static void reset() {
        System.out.println("Card statistics has been reset.\n");
        addLog("Card statistics has been reset.\n\n");
        hard.clear();
    }

    public static void addLog(String str) {
        log += str;
    }

    public static void addHardest(Entry set) {
        if (hard.containsKey(set.getKey())) {
            for (Entry h : hard.entrySet()) {
                if (h.getKey().equals(set.getKey())) {
                    hard.put((String) h.getKey(),((Integer)h.getValue()+1));
                }
            }
        } else {
            hard.put((String) set.getKey(), 1);
        }
    }
}
//package jono.testing;

import java.io.*;
import java.util.*;

public class Main {
    private static int countLines = 0;
    private static String[] fileArray;
    public static boolean argumentsGiven;

    public static void main(String[] args) {

        String sentOdkUsername = "";
        String sentOdkPassword = "";
        argumentsGiven = false;

        if (args.length == 2){
            sentOdkUsername = args[0];
            sentOdkPassword = args[1];
            argumentsGiven = true;
        }
        else{
            System.out.println("WARNING: Incorrect number of arguments given.");
            System.out.println("WARNING: Arguments should be 'Username, Password'.");
            System.out.println("WARNING: Running program in test mode.\n");
        }

        //1. Read in the file
        String file = "collect.settings";
        Object[] collectedSettings = fileToObject(file);
        //Debug Output
        System.out.println("Number of objects : " + collectedSettings.length);

        //2. Inject the username and password into the file object
        List<Map<Object, Object>> mapsListFromFile = new ArrayList<Map<Object, Object>>();
        mapsListFromFile = setupODK(collectedSettings, sentOdkUsername, sentOdkPassword);

        //3. Print out a serialized file
        serializeFile("collect_new.settings", mapsListFromFile);

    }

    private static void deserializeFile(String fileName, boolean debugOutput) {
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            countLines = 0;
            Object obj = null;
            Object[] objArray = new Object[2];

            while ((obj = in.readObject()) != null) {
                if (debugOutput) {
                    System.out.println(obj);
                }
            }
            in.close();
            fileIn.close();

        } catch (java.io.EOFException i) {
            System.out.println("\nEnd of file reached.");
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println(ex);
        }
        if (debugOutput) {
            System.out.println("");
        }
        //System.out.println("\nThere are " + countLines + " line(s) in the file.");
    }

    private static void serializeFile(String fileName, List<Map<Object, Object>> contents) {
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            //Run through the maps list
            for (int i = 0; i < 2; i++) {
                // printMap(contents.get(i));
                // System.out.println("\n\n");
                objectOutputStream.writeObject(contents.get(i));
            }
        } catch (IOException e) {
            System.out.println("oops...");
            e.printStackTrace();
        }
    }

    private static Object[] fileToObject(String fileName) {
        Object[] tempArray = new Object[2];

        try {
            //Initialize the file
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            countLines = 0;

            Object obj = null;

            while ((obj = in.readObject()) != null) {
                //Store the object in the array
                tempArray[countLines] = obj;
                countLines++;
            }

            in.close();
            fileIn.close();

            System.out.println("Array returned!");

        } catch (IOException i) {
            //        	 System.out.println(i);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
        //        System.out.println("\nThere are " + countLines + " line(s) in the file.");
        return tempArray;
    }

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            // it.remove(); // avoids a ConcurrentModificationException
        }
    }

    private static StringBuilder getAlternateDataStream(String filename) {
        File ads = new File(filename);
        FileInputStream inputStream = null;
        StringBuilder sb = new StringBuilder("");
        try {
            inputStream = new FileInputStream(ads);
            System.out.println("Data Steam size in bytes : " + inputStream.available());
            int content;
            while ((content = inputStream.read()) != -1) {
                sb.append((char) content);
                System.out.print((char) content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return sb;
    }

    private static void streamToFile(String content, String filename) {
        try {
            OutputStream outputStream = new FileOutputStream(filename);
            Writer writer = new OutputStreamWriter(outputStream);
            writer.write(content);
            writer.close();
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    private static List<Map<Object, Object>> setupODK(Object[] currentSettings, String odkUsername, String odkPassword){

        // Create an array list of maps, mapsList
        List<Map<Object, Object>> newMapList = new ArrayList<Map<Object, Object>>();      //Creates a list of maps that are found in the 'collectedSettings' object

        //Runs through the array of objects  and adds to mapsList
        for (int i = 0; i < currentSettings.length; i++) {
            //noinspection unchecked
            Map<Object, Object> newMap = (Map) currentSettings[i];                                        //creates a map from the object
            newMapList.add(i, newMap);                                                    //adds each map to the list
        }

        // Update the file before serializing again
        // mapsList.get(0).put("username", "carl@test.com");
        newMapList.get(0).put("username", "test");
        newMapList.get(0).put("password", "t3stt3st");

        if (argumentsGiven){
            newMapList.get(0).put("username", odkUsername);
            newMapList.get(0).put("password", odkPassword);
        }

        newMapList.get(0).put("autosend_wifi", true);
        // mapsList.get(1).put("change_username", true);
        System.out.println("\n\nUPDATED  LIST:");

        for (int i = 0; i < currentSettings.length; i++) {
            printMap(newMapList.get(i));
            System.out.println("\n\n");
        }

        return newMapList;

    }
    private static void test(){

    }
}



        /*  TESTING :  OUTPUT  - can delete */
//        System.out.println("\n\nORIGINAL LIST:");
//        for (int i = 0; i < collectedSettings.length; i++) {
//            printMap(mapsList.get(i));
//            System.out.println("\n\n");
//        }
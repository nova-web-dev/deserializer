//package jono.testing;

import java.io.*;
import java.util.*;

public class Main {
    private static int countLines = 0;
    private static String[] fileArray;
    private static boolean USE_ZONE = false;

    public static void main(String[] args) {
        //file to parse
        String file = "collect.settings";


        //Creates an object from a file, and prints it to console
        deserializeFile(file, true);

        Object[] collectedSettings = fileToObject(file);
        System.out.println("Number of objects : " + collectedSettings.length);

        Map<Object, Object> newMap;

        // Create an array list of maps, mapsList
        List<Map<Object, Object>> mapsList = new ArrayList<Map<Object, Object>>();      //Creates a list of maps that are found in the 'collectedSettings' object

        //Runs through the array of objects  and adds to mapsList
        for (int i = 0; i < collectedSettings.length; i++) {
            //noinspection unchecked
            newMap = (Map) collectedSettings[i];                                        //creates a map from the object
            mapsList.add(i, newMap);                                                    //adds each map to the list
        }



//      Update the file before serializing again
//        mapsList.get(0).put("username", "carl@test.com");
        mapsList.get(0).put("username", "test");
        mapsList.get(0).put("password", "t3stt3st");
        mapsList.get(0).put("autosend_wifi", true);
//        mapsList.get(1).put("change_username", true);
        System.out.println("\n\nUPDATED  LIST:");

        for (int i = 0; i < collectedSettings.length; i++) {
            printMap(mapsList.get(i));
            System.out.println("\n\n");
        }

        serializeFile("collect_new.settings", mapsList);


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

        } catch (IOException i) {
            //System.out.println(i);
        } catch (ClassNotFoundException ex) {
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
                //                printMap(contents.get(i));
                //                System.out.println("\n\n");
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
}



        /*  TESTING :  OUTPUT  - can delete */
//        System.out.println("\n\nORIGINAL LIST:");
//        for (int i = 0; i < collectedSettings.length; i++) {
//            printMap(mapsList.get(i));
//            System.out.println("\n\n");
//        }
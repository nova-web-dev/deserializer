// package carltest;

import java.io.*;
import java.util.*;

public class Main {

    private static int countLines = 0;
    private static String[] fileArray;
    private static Object[] objArray;
    private static Object[] collectedSettings;

    public static void main(String[] args) {
        //Creates an object from a file, and prints it to console
        deserializeFile("collect.settings", false);
        collectedSettings = fileToObject("collect.settings");
//        System.out.println(collectedSettings[0]);
        Map<String, String> newMap = (Map) collectedSettings[0];
//        System.out.println(newMap.values());
        printMap(newMap);
    }

    private static void deserializeFile(String fileName, boolean debugOutput){
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            countLines = 0;

            Object obj  = null;
            objArray = new Object[2];

            while( (obj = in.readObject()) != null ) {
                if (debugOutput){
                    System.out.println(obj);

                }
            }

            in.close();
            fileIn.close();

        } catch (IOException i) {
//        	 System.out.println(i);
        }

        catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
        if (debugOutput){
            System.out.println("");
        }
//        System.out.println("\nThere are " + countLines + " line(s) in the file.");
    }

    private static void serializeFile(String fileName){

    }

    private static Object[] fileToObject (String fileName){
        Object [] tempArray = new Object[2];

        try {
            //Initialize the file
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            countLines = 0;

            Object obj  = null;



            while( (obj = in.readObject()) != null ) {

                //Store the object in the array
                tempArray[countLines] = obj;
                countLines++;
            }



            in.close();
            fileIn.close();

            System.out.println("Array returned!");

        } catch (IOException i) {
//        	 System.out.println(i);
        }

        catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
//        System.out.println("\nThere are " + countLines + " line(s) in the file.");
        return tempArray;
    }

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
}

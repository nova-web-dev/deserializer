//package jono.testing;

import java.io.*;
import java.util.*;

public class Main {
    private static int countLines = 0;
    private static String[] fileArray;
    private static boolean argumentsGiven;
    public static boolean DEBUG_MODE = true;
    private static boolean READ_FROM_FILE = false;
    private static String sentOdkUsername = "";
    private static String sentOdkPassword = "";
    private static Object [] stringFile = new Object[2];

    private static String[] file1Strings;
    private static String[] file1Booleans;
    private static String[] file1Long;

    private static String[] file2Strings;
    private static String[] file2Booleans;
    private static String[] file2Long;


    public static void main(String[] args) {

        //Neat method for evaluating command line arguments
        evaluateArguments(args);
        setupVariables();

        //1. Read the file into an array of objects
        String file = "collect.settings";
        Object [] collectedSettings = fileToObject(file);

        //1.1 Alternatively, read the string into an array of objects (specifically hashmaps)
        Object[] hardCodedSettings = stringArrayToObject(stringFile);

        //2. Initialize a list of maps (we will store two maps here)
        List<Map<Object, Object>> mapsListFromFile = new ArrayList<Map<Object, Object>>();

        //DEBUG - CHECK THE TYPES OF THE VARIABLES
//        typeChecker(collectedSettings);
        typeChecker(hardCodedSettings);


        if (READ_FROM_FILE){
//            2.1 Inject the username and password into the file object
            mapsListFromFile = setupODK(collectedSettings, sentOdkUsername, sentOdkPassword);
        } else{
//            2.2 Do this for hardcoded file instead
            mapsListFromFile = setupODK(hardCodedSettings, sentOdkUsername, sentOdkPassword);
        }



//

        //3. Print out a serialized file
        serializeFile("collect_new.settings", mapsListFromFile);

        //3.1 Alternatively, create a method to send a base64 version of serialized file as a string somewhere else

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
                    System.out.println(obj.getClass().getName());
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
        } catch (Exception ex){
            System.out.println("OTHER EXCEPTION: " + ex);
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

    private static Object[] stringArrayToObject(Object[] input){

        int timesToRun = input.length;
        Object[] tempObj = new Object[timesToRun];

        for (int i = 0; i < timesToRun; i++){
            tempObj[i] = convertStringToMap(input[i]);
            System.out.println(tempObj[i]);
        }

        return tempObj;
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

        // newMapList.get(1).put("change_username", true);
        System.out.println("\n\nUPDATED  LIST:");

        for (int i = 0; i < currentSettings.length; i++) {
            printMap(newMapList.get(i));
            System.out.println("\n\n");
        }

        return newMapList;

    }

    private static void typeChecker(Object[] checkMe){

        // Create an array list of maps, mapsList
        List<Map<Object, Object>> newMapList2 = new ArrayList<Map<Object, Object>>();      //Creates a list of maps that are found in the 'collectedSettings' object

        //Runs through the array of objects  and adds to mapsList
        for (int i = 0; i < checkMe.length; i++) {
            //noinspection unchecked
            Map<Object, Object> newMap = (Map) checkMe[i];                                        //creates a map from the object
            newMapList2.add(i, newMap);                                                    //adds each map to the list
        }

        for (int i = 0; i < checkMe.length; i++){
            System.out.println("");
            Iterator it = newMapList2.get(i).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue().getClass());
//                it.remove(); // avoids a ConcurrentModificationException
            }
            System.out.println("");
        }

    }

    private static Map<Object,Object> convertStringToMap(Object convertMe){
        String value = convertMe.toString();
        value = value.substring(1, value.length()-1);
        //remove curly brackets
        String[] keyValuePairs = value.split(",");              //split the string to create key-value pairs

        Map<Object,Object> map = new HashMap<>();

        for(int i = 0; i < keyValuePairs.length; i++)                        //iterate over the pairs
        {
            String[] entry = keyValuePairs[i].split("=");
            //split the pairs to get key and value
            if (entry.length == 1){
                //This will happen if there is a null value
                map.put(entry[0].trim(), convertToTrueType(entry[0].trim(), ""));
            }
            else{
//                map.put(entry[0].trim(), entry[1].trim());
                map.put(entry[0].trim(), convertToTrueType(entry[0].trim(), entry[1].trim()));
            }

            //add them to the hashmap and trim whitespaces
        }
        return map;
    }

    private static void evaluateArguments(String [] args){
        argumentsGiven = false;

        if (args.length == 2){
            sentOdkUsername = args[0];
            sentOdkPassword = args[1];
            argumentsGiven = true;
        }
        else if (args.length == 3){
            sentOdkUsername = args[0];
            sentOdkPassword = args[1];
            try {
                if (Integer.parseInt(args[2]) == 1){
                    READ_FROM_FILE = true;
                }
            } catch (Exception ex){
                //System.out.println("UNABLE TO PARSE ARGUMENTS!");
            }

            argumentsGiven = true;
        }
        else{
            System.out.println("WARNING: Incorrect number of arguments given.");
            System.out.println("WARNING: Arguments should be 'Username, Password'.");
            System.out.println("WARNING: Running program in test mode.\n");
        }
    }

    private static void setupVariables(){
        stringFile[0] = "{submission_url=/submission, lastVersion=1062, selected_google_account=, firstRun=false, font_size=21, map_basemap_behavior=streets, constraint_behavior=on_swipe, formlist_url=/formList, password=t3stt3st, protocol=odk_default, navigation=swipe, high_resolution=true, autosend_wifi=true, map_sdk_behavior=google_maps, autosend_network=false, server_url=https://abalobi-fisher.appspot.com, delete_send=false, default_completed=true, username=testcarlusername}";
        stringFile[1] = "{jump_to=true, show_map_sdk=true, show_splash_screen=true, form_processing_logic=-1, get_blank=true, change_protocol_settings=true, show_map_basemap=true, mark_as_finalized=true, edit_saved=true, change_password=true, access_settings=true, navigation=true, high_resolution=true, save_as=true, change_language=true, delete_saved=true, delete_after_send=true, default_to_finalized=true, change_font_size=true, change_server=true, constraint_behavior=true, autosend_wifi=true, autosend_network=true, send_finalized=true, change_google_account=true, save_mid=true, change_username=true}";

        file1Strings = new String[]{"submission_url", "selected_google_account", "font_size", "map_basemap_behavior", "constraint_behavior", "formlist_url", "password", "protocol", "navigation", "map_sdk_behavior", "server_url", "username"};
        file1Booleans = new String[]{"firstRun", "high_resolution", "autosend_wifi", "autosend_network", "delete_send", "default_completed"};
        file1Long = new String[]{"lastVersion"};

        file2Booleans = new String[]{"jump_to", "show_map_sdk", "show_splash_screen", "get_blank", "change_protocol_settings", "show_map_basemap", "mark_as_finalized", "edit_saved", "change_password", "access_settings", "navigation", "high_resolution", "save_as", "change_language", "delete_saved", "delete_after_send", "default_to_finalized", "change_font_size", "change_server", "constraint_behavior", "autosend_wifi", "autosend_network", "send_finalized", "change_google_account", "save_mid", "change_username"};
        file2Strings = new String[]{"form_processing_logic"};
    }

    private static Object convertToTrueType(String keyName, Object keyValue){
        //Compare to strings
        for (String file1String : file1Strings) {
            if (keyName.toString().equals(file1String)) {
                System.out.println("THIS IS A STRING!");
                return keyValue.toString();
            }
        }
        for (String file1Boolean : file1Booleans) {
            if (keyName.toString().equals(file1Boolean)) {
                System.out.println("THIS IS A BOOLEAN!");
                return Boolean.valueOf(keyValue.toString());
            }
        }
        for (String file1Longs : file1Long) {
            if (keyName.toString().equals(file1Longs)) {
                System.out.println("THIS IS A LONG!");
                return Long.parseLong(keyValue.toString());
            }
        }
        for (String file2String : file2Strings) {
            if (keyName.toString().equals(file2String)) {
                System.out.println("THIS IS A STRING 2!");
                return (keyValue.toString());
            }
        }
        for (String file2Boolean : file2Booleans) {
            if (keyName.toString().equals(file2Boolean)) {
                System.out.println("THIS IS A BOOLEAN 2!");
                return Boolean.valueOf(keyValue.toString());
            }
        }

        return keyValue;
    }


}



        /*  TESTING :  OUTPUT  - can delete */
//        System.out.println("\n\nORIGINAL LIST:");
//        for (int i = 0; i < collectedSettings.length; i++) {
//            printMap(mapsList.get(i));
//            System.out.println("\n\n");
//        }


/*

        Iterator it = newMapList.get(0).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue().getClass());
            it.remove(); // avoids a ConcurrentModificationException
        }
 */
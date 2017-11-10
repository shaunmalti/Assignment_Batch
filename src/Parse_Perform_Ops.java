import au.com.bytecode.opencsv.CSVReader;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * Created by shaunmarkham on 25/10/2017.
 */
class Parse_Perform_Ops {

    public static ArrayList<Tuple> Parse_Read() throws Exception {
        //read data from .csv files using the apache_commons CSVReader
        File folder = new File(System.getProperty("user.dir") + "/data/Subject_data");
        File[] listOfFiles = folder.listFiles();
        ArrayList<Tuple> Data = new ArrayList<>();
        List<String[]> myEntries = new ArrayList<>();
        String[] line;
        //read data into a list of type string array
        for (File listOfFile : listOfFiles) {
            CSVReader reader = new CSVReader(new FileReader(listOfFile), ',', '"', 1);
            while ((line = reader.readNext()) != null) {
                myEntries.add(line);
            }
        }

        //get important information from the myEntries list and create a new object Tuple from it
        for (int x = 5; x < myEntries.size(); x++) {
            Tuple test = new Tuple(myEntries.get(x)[0], myEntries.get(x)[9], myEntries.get(x)[4]);
            Data.add(test);
        }
        //return an arraylist of type object (Tuple)
        return Data;
    }

    public static void PrinterMethod(University_Info object, File file) throws IOException {
        //method to print titles (included University scores) to the input folder for use in mapreduce
        PrintWriter writer = new PrintWriter(new FileWriter(file, true));
        for (String aArray_Data : object.getWord_Score()) {
            writer.println(aArray_Data);
        }
        writer.close();
    }

    public static void Cleanup(String item) throws IOException{
        //moving output from mapreduce to a new folder and renaming according to assesment criteria
        //together with the removing of the output folder for the next mapreduce iteration
        new File(System.getProperty("user.dir") + "/legacy_output").mkdir();

        File newfile = new File(System.getProperty("user.dir") + "/legacy_output/part_" + item + ".txt");
        if (!newfile.exists()) {
            newfile.createNewFile();
        }
        Path from = Paths.get(System.getProperty("user.dir") + "/output/part-r-00000");
        Path to = Paths.get(System.getProperty("user.dir") + "/legacy_output/part_" + item + ".txt");
        Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
        File folder = new File(System.getProperty("user.dir") + "/output/");
        FileUtils.deleteDirectory(folder);
    }

    public static ArrayList<University_Info> Get_Uni_Info() throws Exception {
        //read data from .csv file (REF2014_Results) using the apache_commons CSVReader
        //creating only new University_Info objects on occurrence of 'Output' in the 'Profile' column
        CSVReader reader = new CSVReader(new FileReader(System.getProperty("user.dir") + "/data/REF2014_Results.csv"), ',', '"', 1);
        ArrayList<University_Info> Data = new ArrayList<>();
        @SuppressWarnings("unchecked") List<String[]> myEntries = reader.readAll();
        for (int x = 7; x < myEntries.size(); x++) {
            if (myEntries.get(x)[9].equals("Outputs")) {
                University_Info test = new University_Info(myEntries.get(x)[0], myEntries.get(x)[1], myEntries.get(x)[5],
                        myEntries.get(x)[11].replace("-", "0.0"), myEntries.get(x)[12].replace("-", "0.0"), myEntries.get(x)[13].replace("-", "0.0"),
                        myEntries.get(x)[14].replace("-", "0.0"));
                        //change "-" occurring in Star columns to 0s as that would create a mess further on
                        //in the program
                test.Calculate_Score();
                        //calculate an artificial score for each University department based on the
                        //percentage score of star occurrences
                Data.add(test);
            }
        }
        return Data;
    }

    public static ArrayList<University_Info> Tuple_Uni_Linker(ArrayList<University_Info> Uni_Info, ArrayList<Tuple> Info) {
        //for each paper append it to the corresponding University_Info object based on the common University ID and
        //assessment names in University_Info and Tuple classes
        for (Tuple aInfo : Info) {
            for (int y = 0; y < Uni_Info.size(); y++) {
                if (aInfo.getUni_ID().equals(Uni_Info.get(y).getID()) &&
                        aInfo.getAsses_name().equals(Uni_Info.get(y).getAsses_Name())) {
                    Uni_Info.get(y).addUni_Tuple(aInfo.getWords());
                }
            }
        }
        return Uni_Info;
    }

    public static Map<String, List<University_Info>>  ReturnMultiple(ArrayList<University_Info> Total_Dept_Score, int option) {
        //method to return a hashmap with key of type string (assessment name) and value being a list of University_Info
        //objects that contain a list of papers for that key
        Map<String, List<University_Info>> result = new HashMap<>();
        for (University_Info p : Total_Dept_Score) {
            List<University_Info> list = result.get(p.getAsses_Name());
            if (list == null) {
                list = new ArrayList<>();
                result.put(p.getAsses_Name(), list);
            }
            list.add(p);
        }
        for (String item : result.keySet()) {
            for (University_Info object : result.get(item)) {
                object.Calculate_Score();
            }
        }
        for (String items : result.keySet()) {
            Collections.sort(result.get(items));
            //sort the University_Info list for each key in the hashmap
            if (result.get(items) != null) {
                while (result.get(items).size() > 15) {
                    result.get(items).remove(0);
                    //keep only the top 15
                }
            }
        }
        return result;
    }

    public static void SortOutput() throws IOException{
        //get the output of the mapreduce, place in hashmap and sort based on value
        File output = new File(System.getProperty("user.dir") + "/output/part-r-00000");
        HashMap<String,Double> Data = new HashMap();
        try (BufferedReader br = new BufferedReader(new FileReader(output))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitline = line.split("\t");
                Data.put(splitline[0], Double.valueOf(splitline[1]));
            }
        }
        Map<String,Double> Sorted_Data = sortByValue(Data);
        int total_occ = 0;
        for (String aArray_Data : Sorted_Data.keySet()) {
            total_occ += Sorted_Data.get(aArray_Data);
        }
        PrintWriter writer = new PrintWriter(new FileWriter(output));
        for (String aArray_Data : Sorted_Data.keySet()) {
            writer.println(aArray_Data + "\t" + Sorted_Data.get(aArray_Data));
        }
        writer.close();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
        //method used to sort hashmap based on value in SortOutput() method
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static void getBestandWorst(ArrayList<University_Info> Total_Info_Scores) throws IOException{
        //method to split university data into two separate files, top 15 university titles for a specific subject
        //and the rest. This is to be used as training data for a text classifier, see further work section in document
        Collections.sort(Total_Info_Scores);
        ArrayList<University_Info> Worst = new ArrayList<>();
        ArrayList<University_Info> Best = new ArrayList<>();
        for (University_Info item : Total_Info_Scores) {
            if (item.Contains_Tuples() == true) {
                Worst.add(item);
                Best.add(item);
            }
        }

        java.util.Map<String, List<University_Info>> worstresult = new HashMap<>();
        java.util.Map<String, List<University_Info>> bestresult = new HashMap<>();
        for (University_Info p : Total_Info_Scores) {
            List<University_Info> worstlist = worstresult.get(p.getAsses_Name());
            List<University_Info> bestlist = bestresult.get(p.getAsses_Name());
            if (worstlist == null) {
                worstlist = new ArrayList<>();
                bestlist = new ArrayList<>();
                worstresult.put(p.getAsses_Name(), worstlist);
                bestresult.put(p.getAsses_Name(),bestlist);
            }
            worstlist.add(p);
            bestlist.add(p);
        }
        for (String items : worstresult.keySet()) {
            Collections.sort(worstresult.get(items));
            Collections.sort(bestresult.get(items));
            if (worstresult.get(items) != null) {
                while (worstresult.get(items).size() > 15) {
                    worstresult.get(items).remove(worstresult.get(items).size()-1);
                    bestresult.get(items).remove(0);
                }
            }
        }

        new File(System.getProperty("user.dir") + "/WorstHalf").mkdir();
        for (String item : worstresult.keySet()) {
            for (University_Info object : worstresult.get(item)) {
                if (object.Contains_Tuples() == true) {
                    File worstfile = new File(System.getProperty("user.dir") + "/WorstHalf/" + "Worst_" + object.getAsses_Name().substring(0,3) + ".txt");
                    if (!worstfile.exists()) {
                        worstfile.createNewFile();
                    }
                    Parse_Perform_Ops.PrinterMethod(object, worstfile);
                } else {
                    continue;
                }
            }
        }

        new File(System.getProperty("user.dir") + "/BestHalf").mkdir();
        for (String item : bestresult.keySet()) {
            for (University_Info object : bestresult.get(item)) {
                if (object.Contains_Tuples() == true) {
                    File bestfile = new File(System.getProperty("user.dir") + "/BestHalf/" + "Best15_" + object.getAsses_Name().substring(0,3) + ".txt");
                    if (!bestfile.exists()) {
                        bestfile.createNewFile();
                    }
                    Parse_Perform_Ops.PrinterMethod(object, bestfile);
                } else {
                    continue;
                }
            }
        }
    }

    public static java.util.Map<String, List<University_Info>> Append_Words(java.util.Map<String, List<University_Info>> MultiMap) {
        //method to append a score to each word based off of University position
        int x = 1;
        java.util.Map<String, List<University_Info>> Appended = MultiMap;
        for (String newit : Appended.keySet()) {
            if (Appended.get(newit).get(0).Contains_Tuples()==true) {
                x=1;
            }
            for (University_Info newobj : Appended.get(newit)) {
                if (newobj.Contains_Tuples() == true) {
                    newobj.Append_wordscore(String.valueOf(x));
                    x++;
                }
            }
        }
        return Appended;
    }

}

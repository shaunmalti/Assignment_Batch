import au.com.bytecode.opencsv.CSVReader;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
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
        File folder = new File(System.getProperty("user.dir") + "/data/Subject_data");
        File[] listOfFiles = folder.listFiles();
        ArrayList<Tuple> Data = new ArrayList<>();
        List<String[]> myEntries = new ArrayList<>();
        String[] line;
        for (File listOfFile : listOfFiles) {
            CSVReader reader = new CSVReader(new FileReader(listOfFile), ',', '"', 1);
            while ((line = reader.readNext()) != null) {
                myEntries.add(line);
            }
        }

        String[][] Info = new String[myEntries.size()][];
        for (int x = 5; x < myEntries.size(); x++) {
            Tuple test = new Tuple(myEntries.get(x)[0], myEntries.get(x)[9], myEntries.get(x)[4]);
            Data.add(test);
        }

        return Data;
    }

    public static void PrinterMethod(University_Info object, File file) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        file.delete();
        file.createNewFile();
        PrintWriter writer = new PrintWriter(new FileWriter(file, true));
        for (String aArray_Data : object.getUni_Tuples()) {
            writer.println(aArray_Data);
        }
        writer.close();
    }

    public static void Cleanup(String item) throws IOException{
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
        CSVReader reader = new CSVReader(new FileReader(System.getProperty("user.dir") + "/data/REF2014_Results.csv"), ',', '"', 1);
        ArrayList<University_Info> Data = new ArrayList<>();
        @SuppressWarnings("unchecked") List<String[]> myEntries = reader.readAll();
        for (int x = 7; x < myEntries.size(); x++) {
            if (myEntries.get(x)[9].equals("Outputs")) {
                University_Info test = new University_Info(myEntries.get(x)[0], myEntries.get(x)[1], myEntries.get(x)[5],
                        myEntries.get(x)[11].replace("-", "0.0"), myEntries.get(x)[12].replace("-", "0.0"), myEntries.get(x)[13].replace("-", "0.0"),
                        myEntries.get(x)[14].replace("-", "0.0"));
                test.Calculate_Score();
                Data.add(test);
            }
        }
        return Data;
    }

    public static ArrayList<University_Info> Tuple_Uni_Linker(ArrayList<University_Info> Uni_Info, ArrayList<Tuple> Info) {
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
        Map<String, List<University_Info>> result = new HashMap<>();
        for (University_Info p : Total_Dept_Score) {
            List<University_Info> list = result.get(p.getAsses_Name());
            if (list == null) {
                list = new ArrayList<>();
                result.put(p.getAsses_Name(), list);
            }
            list.add(p);
        }
        for (String items : result.keySet()) {
            Collections.sort(result.get(items));
            if (result.get(items) != null) {
                while (result.get(items).size() > 10) { //get top 10 unis for each category
                    result.get(items).remove(0);
                }
            }
        }
        return result;
    }

    public static void SortOutput() throws IOException{
        File output = new File(System.getProperty("user.dir") + "/output/part-r-00000");
        HashMap<String,Integer> Data = new HashMap();
        try (BufferedReader br = new BufferedReader(new FileReader(output))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitline = line.split("\t");
                Data.put(splitline[0], Integer.valueOf(splitline[1]));
            }
        }
        Map<String,Integer> Sorted_Data = sortByValue(Data);
        int total_occ = 0;
        for (String aArray_Data : Sorted_Data.keySet()) {
            total_occ += Sorted_Data.get(aArray_Data);
        }
        PrintWriter writer = new PrintWriter(new FileWriter(output));
        for (String aArray_Data : Sorted_Data.keySet()) {
            writer.println(aArray_Data + "\t" + Sorted_Data.get(aArray_Data) + "\t" + ((Double.valueOf(Sorted_Data.get(aArray_Data))/total_occ)*100) + "%");
        }
        writer.close();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
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
                while (worstresult.get(items).size() > 10) { //get top 10 unis for each category
                    worstresult.get(items).remove(worstresult.get(items).size()-1);
                    bestresult.get(items).remove(0);
                }
            }
        }

//        now print result
        new File(System.getProperty("user.dir") + "/WorstHalf").mkdir();
        for (String item : worstresult.keySet()) {
            for (University_Info object : worstresult.get(item)) {
                if (object.Contains_Tuples() == true) {
                    File worstfile = new File(System.getProperty("user.dir") + "/WorstHalf/" + "Worst10_" + object.getAsses_Name().substring(0,3) + ".txt");
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
                    File bestfile = new File(System.getProperty("user.dir") + "/BestHalf/" + "Best10_" + object.getAsses_Name().substring(0,3) + ".txt");
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

}

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.sun.xml.internal.fastinfoset.util.CharArray;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.swing.text.Document;
import javax.swing.text.html.parser.Parser;
import javax.xml.crypto.Data;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by shaunmarkham on 25/10/2017.
 */
public class Parser_Class_With_Tags {


    public static MyPair Gettags() throws FileNotFoundException, IOException{ //returns mypair class
        BufferedReader br = new BufferedReader(new FileReader(
                "/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/REF2014_Results.csv"));

        String line = "";
        ArrayList<String> Data = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (line.length() > 50) {
                Data.add(line);
            }
        }

        String[][] Indexes = new String[Data.size()][];
        HashMap<String,Integer> Uni_scores = new HashMap<>();
        for (int x = 0; x < Data.size();x++) {
            Indexes[x] = Data.get(x).toString().replace("-","0.0").split(",");
        }

        HashMap<String, String> PairsTable = new HashMap<>();
        for (int x = 0; x < Indexes.length; x++) {
            if (Indexes[x].length > 2 && Indexes[x][0].length() == 8) {
                PairsTable.put(Indexes[x][0], Indexes[x][1]);
            }
        }

        return new MyPair(PairsTable, Indexes);
    }

    public static void SplitMethod() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(
                "/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/mapreduceresult/part-r-00000"));

        String line = "";
        ArrayList<String> Ind_Desc_Arr = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            Ind_Desc_Arr.add(line);
        }

        String[][] Ind_Desc = new String[Ind_Desc_Arr.size()][];
        for (int x = 0; x < Ind_Desc_Arr.size();x++) {
            Ind_Desc[x] = Ind_Desc_Arr.get(x).toString().split("\t");
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static ArrayList<Tuple> Parse_Read() throws Exception
    {
        CSVReader reader = new CSVReader(new FileReader("/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/sample/9_9 - Physics.csv"), ',' , '"' , 1);
        ArrayList<Tuple> Data = new ArrayList<Tuple>();
        List<String[]> myEntries = reader.readAll();
        String[][] Info = new String[myEntries.size()][];
        for (int x=5;x<myEntries.size();x++) {
            Tuple test = new Tuple(myEntries.get(x)[0],myEntries.get(x)[9]);
            Data.add(test);
        }

        return Data;
    }

    public static void PrinterMethod(ArrayList<Tuple> Array_Data) throws FileNotFoundException, UnsupportedEncodingException{
        PrintWriter writer = new PrintWriter("/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/textfile/textfile.txt", "UTF-8");
        for (int x = 0; x < Array_Data.size();x++){
            writer.println(Array_Data.get(x).getWords());
        }
        writer.close();
    }

    public static ArrayList<University_Info> newgettags() throws Exception {
        CSVReader reader = new CSVReader(new FileReader("/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/REF2014_Results.csv"), ',', '"', 1);
        ArrayList<University_Info> Data = new ArrayList<University_Info>();
        List<String[]> myEntries = reader.readAll();
        for (int x = 7; x < myEntries.size(); x++) {
            if (myEntries.get(x)[9].toString().equals("Outputs")) {
                University_Info test = new University_Info(myEntries.get(x)[0], myEntries.get(x)[1], myEntries.get(x)[5],
                        myEntries.get(x)[11].replace("-","0.0"), myEntries.get(x)[12].replace("-","0.0"), myEntries.get(x)[13].replace("-","0.0"),
                        myEntries.get(x)[14].replace("-","0.0"));
                test.Calculate_Score();
                Data.add(test);
            }
        }
        return Data;
    }


    public static void main(String[] args) throws IOException,Exception{

//
// MyPair Gettags = Gettags(); //get indexes and names of unis
        ArrayList<University_Info> Uni_Info = newgettags();
        ArrayList<University_Info> Total_Dept_Score = newmethod(Uni_Info);
        SplitMethod(); //split reduced occurrences

//        String[][] Indexes = Gettags.getPair_Indexes();
//        Map<String,Double> Uni_ID_Scores = Uni_Scores(Indexes);

        ArrayList<Tuple> Info = new ArrayList<Tuple>();
        Info = Parse_Read();

        PrinterMethod(Info); //print titles to text file
    }


    public static ArrayList<University_Info> newmethod(ArrayList<University_Info> Uni_Info) {
        ArrayList<University_Info> Reduced_Uni_Info = new ArrayList<University_Info>();

        for (int x=0;x<Uni_Info.size();x++) {

        }

        return Uni_Info;
    }

}

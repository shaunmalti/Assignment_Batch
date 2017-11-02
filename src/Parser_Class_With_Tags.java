import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.sun.xml.internal.fastinfoset.util.CharArray;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.sql.rowset.Predicate;
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


    public static void SplitMethod() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(
                "/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/mapreduceresult/part-r-00000"));

        String line = "";
        ArrayList<String> Ind_Desc_Arr = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            Ind_Desc_Arr.add(line);
        }

        String[][] Ind_Desc = new String[Ind_Desc_Arr.size()][];
        for (int x = 0; x < Ind_Desc_Arr.size(); x++) {
            Ind_Desc[x] = Ind_Desc_Arr.get(x).toString().split("\t");
        }
    }


    public static ArrayList<Tuple> Parse_Read() throws Exception {
        File folder = new File("/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/sample/"); //TODO MAKE THIS GENERIC
        File[] listOfFiles = folder.listFiles();
        ArrayList<Tuple> Data = new ArrayList<Tuple>();
        List<String[]> myEntries = new ArrayList<String[]>();
        String[] line;
        for (int i = 0; i < listOfFiles.length; i++) {
            CSVReader reader = new CSVReader(new FileReader(listOfFiles[i]), ',', '"', 1);
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

    public static void PrinterMethod(ArrayList<Tuple> Array_Data) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/textfile/textfile.txt", "UTF-8"); //TODO MAKE THIS GENERIC
        for (int x = 0; x < Array_Data.size(); x++) {
            writer.println(Array_Data.get(x).getWords());
        }
        writer.close();
    }

    public static ArrayList<University_Info> Get_Uni_Info() throws Exception {
        CSVReader reader = new CSVReader(new FileReader("/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/REF2014_Results.csv"), ',', '"', 1);  //TODO MAKE THIS GENERIC
        ArrayList<University_Info> Data = new ArrayList<University_Info>();
        List<String[]> myEntries = reader.readAll();
        for (int x = 7; x < myEntries.size(); x++) {
            if (myEntries.get(x)[9].toString().equals("Outputs")) {
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
        for (int x = 0; x < Info.size(); x++) {
            for (int y = 0;y < Uni_Info.size(); y++) {
                if (Info.get(x).getUni_ID().equals(Uni_Info.get(y).getID()) &&
                        Info.get(x).getAsses_name().equals(Uni_Info.get(y).getAsses_Name())) {
                    Uni_Info.get(y).addUni_Tuple(Info.get(x).getWords());
                }
            }
        }

        return Uni_Info;
    }



}

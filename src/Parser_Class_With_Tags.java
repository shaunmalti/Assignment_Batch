import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.*;

/**
 * Created by shaunmarkham on 25/10/2017.
 */
class Parser_Class_With_Tags {

    public static ArrayList<Tuple> Parse_Read() throws Exception {
        File folder = new File("/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/sample/"); //TODO MAKE THIS GENERIC
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

    public static void PrinterMethod(ArrayList<Tuple> Array_Data) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/textfile/textfile.txt", "UTF-8"); //TODO MAKE THIS GENERIC
        for (Tuple aArray_Data : Array_Data) {
            writer.println(aArray_Data.getWords());
        }
        writer.close();
    }

    public static ArrayList<University_Info> Get_Uni_Info() throws Exception {
        CSVReader reader = new CSVReader(new FileReader("/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/REF2014_Results.csv"), ',', '"', 1);  //TODO MAKE THIS GENERIC
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



}

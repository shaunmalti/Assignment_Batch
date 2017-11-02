import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.*;

/**
 * Created by shaunmarkham on 25/10/2017.
 */
class Parse_Perform_Ops {

    public static ArrayList<Tuple> Parse_Read() throws Exception {
        File folder = new File(System.getProperty("user.dir") + "/data/Subject_data"); //TODO MAKE THIS GENERIC
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
        PrintWriter writer = new PrintWriter(System.getProperty("user.dir") + "/textfile/textfile.txt", "UTF-8"); //TODO MAKE THIS GENERIC
        for (Tuple aArray_Data : Array_Data) {
            writer.println(aArray_Data.getWords());
        }
        writer.close();
    }

    public static ArrayList<University_Info> Get_Uni_Info() throws Exception {
        CSVReader reader = new CSVReader(new FileReader(System.getProperty("user.dir") + "/data/REF2014_Results.csv"), ',', '"', 1);  //TODO MAKE THIS GENERIC
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

    public static Map<String, List<University_Info>>  ReturnMultiple(ArrayList<University_Info> Total_Dept_Score) {
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


}

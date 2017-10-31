import com.sun.xml.internal.fastinfoset.util.CharArray;

import javax.swing.text.Document;
import javax.swing.text.html.parser.Parser;
import javax.xml.crypto.Data;
import java.io.*;
import java.util.*;

/**
 * Created by shaunmarkham on 25/10/2017.
 */
public class Parser_Class_With_Tags {

    public static String[][] Parser() throws IOException{
        ArrayList<String> Data = new ArrayList<>();
        ArrayList<String> LostSentences = new ArrayList<>();
        String line = "";

        BufferedReader br = null;
        br = new BufferedReader(new FileReader(
                "/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/sample/8_8 - Chemistry.csv"));

        while ((line = br.readLine()) != null) {
            if (line.length() > 50) {
                Data.add(line);
            }
        }

        int skippedlines = 1;
        String[][] test = new String[Data.size()][];
        for (int x = 0; x < Data.size() ; x++) {
            test[x] = Data.get(x).split(","); // make check to append to [9] due to commas in title (check in 10 AND 11 if not null)
            if (test[x].length >= 12 && ((!test[x][10].isEmpty() && !test[x][11].toString().isEmpty())
                    || !(test[x][10].isEmpty() && !test[x][11].toString().isEmpty()))) {
                test[x][9] = test[x][9] + test[x][10] + test[x][11];
            }else if (test[x].length >= 11 && (!test[x][10].isEmpty() )) {
                test[x][9] = test[x][9] + test[x][10];
            }
            //CHANGE THIS FOR GEOGRAPHY TODO
            if (test[x].length < 2 ||
                    ((test[x][2].toString() != "B"
                            && (test[x][0].toString().length() > 8
                            && test[x][0].toString().length() != 0))
                            && x != 1))
            {
                LostSentences.add(test[x][0]);
                skippedlines++;
            } else if (LostSentences.size() > 0)  {
                for (int y = 0; y < skippedlines-1; y++) {
                    test[x-skippedlines][9] = test[x-skippedlines][9] + " " + LostSentences.get(0).toString();
                    LostSentences.remove(0);
                }
                skippedlines = 1;
            }
        }
        return test;
    }

    public static void PrinterMethod(String[][] Array) throws FileNotFoundException, UnsupportedEncodingException{
        PrintWriter writer = new PrintWriter("/Users/shaunmarkham/IdeaProjects/Batch_Test_CSV_Convert/textfile/textfile.txt", "UTF-8");
        for (int x = 0; x < Array.length;x++){
            //for every word append the code
            if (Array[x].length > 25 && Array[x][2].toCharArray()[0]=='B') { //TODO change
                //System.out.println(x);
                String lastword = Array[x][9].substring(Array[x][9].lastIndexOf(" ")+1);
                Array[x][9] = Array[x][9].toString().replace(":"," ").replace("."," ").replace("\""," ").replace("\"\""," ")
                        .replace("?"," ").replace("-"," ").replace("'"," ").replace(" ","_" + Array[x][0].toString() + " ").replace("\n"," ").replace("\r"," ")
                        .replace(Array[x][9].substring(Array[x][9].lastIndexOf(" ")+1), lastword + "_" + Array[x][0].toString());
                writer.println(Array[x][9].toString());
            }

        }
        writer.close();

    }

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
            Indexes[x] = Data.get(x).toString().replace("-","0.0").split(",");                             //TODO
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

    public static Map<String,Double> Uni_Scores(String[][] Indexes) {
        Map<String, Double> Scores_Uni = new TreeMap<>();
        Double Score = 0.0;
        for (int x = 0; x < Indexes.length; x++) {
            if (Indexes[x].length > 13 && Indexes[x][0].length() == 8) {
                if (Indexes[x][9].toString().equals("Outputs")) {
                    Score = (Double.valueOf(Indexes[x][11].toString()) * 10) +
                            (Double.valueOf(Indexes[x][12].toString()) * 5) +
                            (Double.valueOf(Indexes[x][13].toString()) * 2) +
                            (Double.valueOf(Indexes[x][14].toString()) * 1);
                    if (!Scores_Uni.containsKey((Indexes[x][0]))) {
                        Scores_Uni.put((Indexes[x][0]).toString(), Score);
                    } else {
                        Scores_Uni.put((Indexes[x][0]),
                                Scores_Uni.get((Indexes[x][0])) + Score);
                    }
                } else if (Indexes[x][10].toString().equals("Outputs")) {
                    Score = (Double.valueOf(Indexes[x][12].toString()) * 10) +
                            (Double.valueOf(Indexes[x][13].toString()) * 5) +
                            (Double.valueOf(Indexes[x][14].toString()) * 2) +
                            (Double.valueOf(Indexes[x][15].toString()) * 1);
                    if (!Scores_Uni.containsKey((Indexes[x][0]))) {
                        Scores_Uni.put((Indexes[x][0]), Score);
                    } else {
                        Scores_Uni.put((Indexes[x][0]),
                                Scores_Uni.get((Indexes[x][0])) + Score);
                    }
                } else if (Indexes[x][11].toString().equals("Outputs")) {
                    Score = (Double.valueOf(Indexes[x][13].toString()) * 10) +
                            (Double.valueOf(Indexes[x][14].toString()) * 5) +
                            (Double.valueOf(Indexes[x][15].toString()) * 2) +
                            (Double.valueOf(Indexes[x][16].toString()) * 1);
                    if (!Scores_Uni.containsKey((Indexes[x][0]))) {
                        Scores_Uni.put((Indexes[x][0]), Score);
                    } else {
                        Scores_Uni.put((Indexes[x][0]),
                                Scores_Uni.get((Indexes[x][0])) + Score);
                    }
                } else if (Indexes[x][12].toString().equals("Outputs")) {
                    Score = (Double.valueOf(Indexes[x][14].toString()) * 10) +
                            (Double.valueOf(Indexes[x][15].toString()) * 5) +
                            (Double.valueOf(Indexes[x][16].toString()) * 2) +
                            (Double.valueOf(Indexes[x][17].toString()) * 1);
                    if (!Scores_Uni.containsKey((Indexes[x][0]))) {
                        Scores_Uni.put((Indexes[x][0]), Score);
                    } else {
                        Scores_Uni.put((Indexes[x][0]),
                                Scores_Uni.get((Indexes[x][0])) + Score);
                    }
                } else if (Indexes[x][13].toString().equals("Outputs")) {
                    Score = (Double.valueOf(Indexes[x][15].toString()) * 10) +
                            (Double.valueOf(Indexes[x][16].toString()) * 5) +
                            (Double.valueOf(Indexes[x][17].toString()) * 2) +
                            (Double.valueOf(Indexes[x][18].toString()) * 1);
                    if (!Scores_Uni.containsKey((Indexes[x][0]))) {
                        Scores_Uni.put((Indexes[x][0]), Score);
                    } else {
                        Scores_Uni.put((Indexes[x][0]),
                                Scores_Uni.get((Indexes[x][0])) + Score);
                    }
                }
            }
        }
        Map<String,Double> Sorted_Scores_Uni = sortByValue(Scores_Uni);
        return Sorted_Scores_Uni;
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

    public static class MyPair {
        private HashMap<String,String> Pair_Hash;
        private String[][] Pair_Indexes;

        public MyPair(HashMap<String, String> pair_Hash, String[][] pair_Indexes) {
            Pair_Hash = pair_Hash;
            Pair_Indexes = pair_Indexes;
        }

        public HashMap<String, String> getPair_Hash() {
            return Pair_Hash;
        }

        public void setPair_Hash(HashMap<String, String> pair_Hash) {
            Pair_Hash = pair_Hash;
        }

        public String[][] getPair_Indexes() {
            return Pair_Indexes;
        }

        public void setPair_Indexes(String[][] pair_Indexes) {
            Pair_Indexes = pair_Indexes;
        }
    }

    public static void main(String[] args) throws IOException{
        String[][] DataArray = Parser(); //parse data
        PrinterMethod(DataArray); //print titles to text file

        MyPair Gettags = Gettags(); //get indexes and names of unis

        //SplitMethod(); //split reduced occurrances
        String[][] Indexes = Gettags.getPair_Indexes();
        Map<String,Double> Uni_ID_Scores = Uni_Scores(Indexes);

    }

}

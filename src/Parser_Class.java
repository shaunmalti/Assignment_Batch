import com.sun.xml.internal.fastinfoset.util.CharArray;

import javax.swing.text.html.parser.Parser;
import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shaunmarkham on 25/10/2017.
 */
public class Parser_Class {

    public static String[][] Parser() throws IOException{
        ArrayList<String> Data = new ArrayList<>();
        ArrayList<String> LostSentences = new ArrayList<>();
        String line = "";
        BufferedReader br = new BufferedReader(new FileReader(
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
//            System.out.println(x);
            if (test[x].length >= 12 && ((test[x][10] != null && test[x][11].toString() != "")
                    || (test[x][10] != null && test[x][11].toString() != null))) {
                test[x][9] = test[x][9] + test[x][10] + test[x][11];
            }else if (test[x].length >= 11 && (test[x][10] != null )) {
                test[x][9] = test[x][9] + test[x][10];
            }
            //fix code

            //System.out.println(x); //CHANGE THIS FOR GEOGRAPHY TODO
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
                    test[x-skippedlines][9] = test[x-skippedlines][9] + " " +LostSentences.get(0).toString();
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
                System.out.println(x);
                String lastword = Array[x][9].substring(Array[x][9].lastIndexOf(" ")+1);
                Array[x][9] = Array[x][9].toString().replace(":"," ").replace("."," ").replace("\""," ").replace("\"\""," ")
                        .replace("?"," ").replace("-"," ").replace("'"," ").replace(" ","_" + Array[x][0].toString() + " ").replace("\n"," ").replace("\r"," ")
                        .replace(Array[x][9].substring(Array[x][9].lastIndexOf(" ")+1), lastword + "_" + Array[x][0].toString());
//                for (int y = 0; y < chararray.length; y++) {
//                    if (chararray[y+1] == " ")

//                }
                writer.println(Array[x][9].toString());
            }

        }
        writer.close();

    }
    public static void main(String[] args) throws IOException{
        String[][] DataArray = Parser(); //parse data
        PrinterMethod(DataArray); //print titles to text file

    }


}

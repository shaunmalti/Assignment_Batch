import java.util.HashMap;

/**
 * Created by shaunmarkham on 01/11/2017.
 */
public class MyPair {


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

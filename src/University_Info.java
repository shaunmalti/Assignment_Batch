import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by shaunmarkham on 01/11/2017.
 */
public class University_Info implements java.io.Serializable, Comparator<University_Info>, Comparable<University_Info> {
    private String ID;
    private String Name;
    private String Asses_Name;
    private String Four_Star;
    private String Three_Star;
    private String Two_Star;
    private String One_Star;
    private Double Score;
    private ArrayList<String> Uni_Tuples = new ArrayList<>();

    public ArrayList<String> getUni_Tuples() {
        return Uni_Tuples;
    }

    public void addUni_Tuple(String TupleInfo) {
        Uni_Tuples.add(TupleInfo);
    }

    public University_Info(String ID, String name, String asses_Name, String four_Star, String three_Star, String two_Star, String  one_Star) {
        this.ID = ID;
        Name = name;
        Asses_Name = asses_Name;
        Four_Star = four_Star;
        Three_Star = three_Star;
        Two_Star = two_Star;
        One_Star = one_Star;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAsses_Name() {
        return Asses_Name;
    }

    public void setAsses_Name(String asses_Name) {
        Asses_Name = asses_Name;
    }

    public String getFour_Star() {
        return Four_Star;
    }

    public void setFour_Star(String four_Star) {
        Four_Star = four_Star;
    }

    public String getThree_Star() {
        return Three_Star;
    }

    public void setThree_Star(String three_Star) {
        Three_Star = three_Star;
    }

    public String getTwo_Star() {
        return Two_Star;
    }

    public void setTwo_Star(String two_Star) {
        Two_Star = two_Star;
    }

    public String getOne_Star() {
        return One_Star;
    }

    public void setOne_Star(String one_Star) {
        One_Star = one_Star;
    }

    public Double getScore() {
        return Score;
    }

    public void setScore(Double score) {
        Score = score;
    }

    public void Calculate_Score() {
        this.Score = (Double.valueOf(this.getFour_Star())*0.5)+(Double.valueOf(this.getThree_Star())*0.25)
                + (Double.valueOf(this.getTwo_Star())*0.125) + (Double.valueOf(this.getOne_Star())*0.0625);
    }

    @Override
    public int compareTo(University_Info d) {
        return (d.getScore()).compareTo(this.getScore());
    }

    @Override
    public int compare(University_Info d, University_Info d1) {
        return Double.compare(d1.getScore(),d.getScore());
    }


}

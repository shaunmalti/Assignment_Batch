/**
 * Created by shaunmarkham on 01/11/2017.
 */
public class University_Info {
    String ID;
    String Name;
    String Asses_Name;
    String Four_Star;
    String Three_Star;
    String Two_Star;
    String One_Star;
    Double Score;

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

    public void Calculate_Score() {
        this.Score = (Double.valueOf(this.getFour_Star())*10)+(Double.valueOf(this.getThree_Star())*5)
                + (Double.valueOf(this.getTwo_Star())*2) + (Double.valueOf(this.getOne_Star()));
    }
}

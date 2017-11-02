/**
 * Created by shaunmarkham on 01/11/2017.
 */
public class Tuple {
    String Uni_ID;
    String Title;
    String Asses_name;

    public Tuple(String uni_ID, String title, String asses_name) {
        Uni_ID = uni_ID;
        Title = title;
        Asses_name = asses_name;
    }

    public String getUni_ID() {
        return Uni_ID;
    }

    public void setUni_ID(String uni_ID) {
        Uni_ID = uni_ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getAsses_name() {
        return Asses_name;
    }

    public void setAsses_name(String asses_name) {
        Asses_name = asses_name;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getWords() {                                          //TODO CHANGE THIS IF I WANT THE UNIVERSITY ID APPENDED
        String sentence;
        String[] words = this.getTitle().split("\\s+");
//        for (int i = 0; i < words.length; i++) {
//            words[i] = words[i].replaceAll(" ", "_" + this.getUni_ID());
//        }
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
//            strBuilder.append(words[i] + "_" + this.getUni_ID() + " ");
            strBuilder.append(words[i] + " ");
        }
        String newString = strBuilder.toString();

        return newString;
//        return sentence;
    }
}
/**
 * Created by shaunmarkham on 01/11/2017.
 */
public class Tuple {
    String Uni_ID;
    String Title;

    public Tuple(String uni_ID, String title) {
        Uni_ID = uni_ID;
        Title = title;
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

    public void setTitle(String title) {
        Title = title;
    }

    public String getWords() {
        String sentence;
        String[] words = this.getTitle().split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll(" ", "_" + this.getUni_ID());
        }
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            strBuilder.append(words[i] + "_" + this.getUni_ID() + " ");
        }
        String newString = strBuilder.toString();

        return newString;
    }
}
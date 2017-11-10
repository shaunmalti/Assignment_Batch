/**
 * Created by shaunmarkham on 01/11/2017.
 */
class Tuple {
    private String Uni_ID;
    private String Title;
    private String Asses_name;

    public Tuple(String uni_ID, String title, String asses_name) {
        //class for university papers
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

    private String getTitle() {
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

    public String getWords() {
        //method that returns the words in the paper title
        String[] words = this.getTitle().split("\\s+");
        StringBuilder strBuilder = new StringBuilder();
        for (String word : words) {
            strBuilder.append(word + " ");
        }

        return strBuilder.toString();
    }
}
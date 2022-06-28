package nordis.nordisquiz;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuestClass extends AppCompatActivity {

    private String question;
    private String response;
    private String response2;
    private String response3;
    private String response4;
    ArrayList<QuestClass> mylist = new ArrayList<>();

    public QuestClass(String question, String response, String response2, String response3, String response4) {
        this.question = question;
        this.response = response;
        this.response2 = response2;
        this.response3 = response3;
        this.response4 = response4;
    }


    public void createQuestion(){
        
    }
}

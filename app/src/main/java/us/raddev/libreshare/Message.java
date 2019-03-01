package us.raddev.libreshare;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Message {
    private static int counter;
    public String Note;
    public Boolean isComplete;
    public int id;

    public Message (){
        Message.counter++;
        this.id = Message.counter;
    }

    public Message(String note){
        this();
        this.Note = note;
        isComplete = false;
    }

    public static void InitCounter(){
        Message.counter = 0;
    }
}

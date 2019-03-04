package us.raddev.libreshare;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Message {
    private static int counter;
    public String Note;
    public Boolean isComplete;
    public int id;

    public Message (){
        Log.d("MainActivity", "Message()");
        Message.counter++;
        this.id = Message.counter;
        Log.d("MainActivity", "Message.id : " + this.id);
    }

    public Message(String note){
        this();
        Log.d("MainActivity", "Message(String note)");
        this.Note = note;
        isComplete = false;
    }

    public static void InitCounter(){
        Message.counter = 0;
    }
}

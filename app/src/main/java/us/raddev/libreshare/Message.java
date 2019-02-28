package us.raddev.libreshare;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Message {
    public String Note;
    public Boolean isComplete;

    public Message (){

    }

    public Message(String note){
        this.Note = note;
        isComplete = false;
    }
}

package us.raddev.libreshare;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@IgnoreExtraProperties
public class Entry {
    public String get_id() {
        return _id;
    }
    public String title;
    public String created;
    public String updated;
    private String _id;
    private Context _context;

    public List<Message> get_allMessages() {
        return _allMessages;
    }

    public List<Message> _allMessages;

    public Entry(){
        Message.InitCounter();
        created = String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        updated = "";
    }

    public Entry(String note, String title){
        // Default constructor required for calls to DataSnapshot.getValue(Entry.class)
        this();
        this._id = Config.generateId();
        this.title = title;
        this._allMessages = new ArrayList<Message>();
        this._allMessages.add(new Message(note));
    }
}

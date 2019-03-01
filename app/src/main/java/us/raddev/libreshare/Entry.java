package us.raddev.libreshare;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@IgnoreExtraProperties
public class Entry {
    public String get_id() {
        return _id;
    }

    private String _id;
    private String _ownerId;
    private Context _context;

    public List<Message> get_allMessages() {
        return _allMessages;
    }

    public List<Message> _allMessages;

    public Entry(){
        Message.InitCounter();
    }

    public Entry(String note){
        // Default constructor required for calls to DataSnapshot.getValue(Entry.class)
        this();
        this._id = Config.generateId();
        this._allMessages = new ArrayList<Message>();
        this._allMessages.add(new Message(note));
    }

    public Entry(Context context, String ownerId) {
        this();
        this._context = context;
        this._ownerId = ownerId;
        this._id = Config.generateId();
    }

    public void initPreferences(){
        SharedPreferences configPrefs = _context.getApplicationContext().getSharedPreferences("ownerId", Context.MODE_PRIVATE);
        _ownerId = configPrefs.getString("ownerId", null);
    }
}

package us.raddev.libreshare;

import android.content.Context;
import android.content.SharedPreferences;

public class Entry {
    private String _id;
    private String ownerId;
    private Context _context;

    public String get_message() {
        return _message;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    private String _message;

    public Entry(Context context, String ownerId) {
        this._context = context;
        this.ownerId = ownerId;
        this._id = Config.generateId();
    }

    public void initPreferences(){
        SharedPreferences configPrefs = _context.getApplicationContext().getSharedPreferences("ownerId", Context.MODE_PRIVATE);
        ownerId = configPrefs.getString("ownerId", null);
    }
}

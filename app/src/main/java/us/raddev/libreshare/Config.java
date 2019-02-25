package us.raddev.libreshare;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.security.SecureRandom;

import static java.lang.Long.valueOf;

public class Config {
    public Config(Context context) {
        this.context = context;
        initPreferences();
    }
    private Context context;
    private String ownerId;
    private String userId;

    private void initPreferences(){
        SharedPreferences configPrefs =
                context.getApplicationContext().getSharedPreferences("ownerId", Context.MODE_PRIVATE);
        ownerId = configPrefs.getString("ownerId", null);
        configPrefs = context.getApplicationContext().getSharedPreferences("userId", Context.MODE_PRIVATE);
        userId = configPrefs.getString("userId", null);
        if (getUserId() == null) {
            userId = generateId();
            configPrefs =
                    context.getApplicationContext().getSharedPreferences("userId", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = configPrefs.edit();
            edit.putString("userId", getUserId());
            edit.commit();
            Log.d("MainActivity", "Committed: " + getUserId());
        }
        else {
            Log.d("MainActivity", "loaded userId : " + getUserId());
        }


    }

    public static String generateId(){
        // This method can be used by the Entry class also so it can generate a unique entryId
        // so it is public static.
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            new SecureRandom();
            byte allBytes[] = new byte[20];
            secureRandom.nextBytes(allBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : allBytes ){
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        }
        catch (java.security.NoSuchAlgorithmException nsa){
            return "Error : " + nsa.getMessage();
        }
    }

    public String getUserId() {
        return userId;
    }
}

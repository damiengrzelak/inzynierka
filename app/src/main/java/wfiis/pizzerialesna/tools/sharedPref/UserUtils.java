package wfiis.pizzerialesna.tools.sharedPref;

import android.content.SharedPreferences;

import com.inverce.mod.core.IM;

public class UserUtils {
    private static final String LOGIN_SESSION = "userLoginSession";
    private static final String USER_UID = "userUid";

    private SharedPreferences preferences = IM.application().getApplicationContext().getSharedPreferences("userLoginSession", 0);
    private SharedPreferences.Editor preferencesEditor = preferences.edit();

    private boolean isLogged;
    private String uid;


    public void saveSession(String uid, boolean isLogged) {
        this.isLogged = isLogged;
        this.uid = uid;
        preferencesEditor.putString(USER_UID, uid);
        preferencesEditor.putBoolean(LOGIN_SESSION, isLogged);
        preferencesEditor.commit();
    }

    public boolean isLogged(){
        getPreferences();
        return preferences.getBoolean(LOGIN_SESSION, isLogged);
    }

    public void logOut(){
        this.isLogged = false;
        preferencesEditor.putString(USER_UID, uid);
        preferencesEditor.putBoolean(LOGIN_SESSION, isLogged);
        preferencesEditor.commit();
    }
    public SharedPreferences getPreferences(){
        return IM.application().getApplicationContext().getSharedPreferences("userLoginSession", 0);
    }
}

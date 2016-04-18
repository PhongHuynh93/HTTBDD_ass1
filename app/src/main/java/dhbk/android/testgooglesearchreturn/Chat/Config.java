package dhbk.android.testgooglesearchreturn.Chat;

import android.content.Context;
import android.content.SharedPreferences;

import com.firebase.client.Firebase;

/**
 * Created by Jhordan on 24/07/15.
 */
public class Config {

    // TODO: change this to your own Firebase URL
    private static final String FIREBASE_URL = "https://test-fb-phong093.firebaseio.com";
    private static final String FIREBASE_CHILD = "chat";
    private static final String FIREBASE_MAIL = "email";
    public static final String USER_MAIL = "user_mail";

    private static final String PREFERENCE_MAIL = "pref_email";
    private static final String PREFERENCE_USER_MAIL = "pref_user_email";

    public static String getFirebaseMail() {
        return FIREBASE_MAIL;
    }

    public static void getFirebaseInitialize(Context context) {
        Firebase.setAndroidContext(context);
    }

    public static Firebase getFirebaseReference(){
       return new Firebase(FIREBASE_URL).child(FIREBASE_CHILD);
    }

    public static void setMail(Context context ,String mail){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_MAIL, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREFERENCE_USER_MAIL, mail).apply();
    }

    public static String getMail(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_MAIL, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREFERENCE_USER_MAIL, "soy_un_mail");
    }

}

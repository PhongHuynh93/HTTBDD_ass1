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

    public static void getFirebaseInitialize(Context context) {
        Firebase.setAndroidContext(context);
    }

    public static Firebase getFirebaseReference(){
       return new Firebase(FIREBASE_URL).child(FIREBASE_CHILD);
    }

}

package dhbk.android.testgooglesearchreturn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import dhbk.android.testgooglesearchreturn.R;

/**
 * Created by lhmhieu on 04-Apr-16.
 */
public class FacebookLoginActivity extends AppCompatActivity{
    private static final String TAG = FacebookLoginActivity.class.getName();
    CallbackManager callbackManager;
    LoginButton loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
//                Bundle params = new Bundle();
//                params.putString("fields", "id,email,gender,cover,picture.type(large)");
//                new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
//                        new GraphRequest.Callback() {
//                            @Override
//                            public void onCompleted(GraphResponse response) {
//                                if (response != null) {
//                                    try {
//                                        JSONObject data = response.getJSONObject();
//                                        if (data.has("picture")) {
//                                            String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
//                                            // set profile image to imageview using Picasso or Native methods
////                                            ImageView imageView = (ImageView) findViewById(R.id.imageView);
////                                            Picasso.with(getApplicationContext()).load(profilePicUrl).into(imageView);
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }).executeAsync();
//                Log.i(TAG, "onSuccess: ");
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}

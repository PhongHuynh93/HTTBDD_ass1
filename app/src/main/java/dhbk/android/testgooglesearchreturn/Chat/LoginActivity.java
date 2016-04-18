package dhbk.android.testgooglesearchreturn.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import dhbk.android.testgooglesearchreturn.R;

/**
 * Created by Jhordan on 24/07/15.
 */
public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getName();
    public static final String AUTHEN = "authen";



    private Firebase mFirebaseReference;
    TextView txtEmail, txtPassword;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Config.getFirebaseInitialize(this);
        mFirebaseReference = Config.getFirebaseReference();
        initializeView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAuthStateListener();
    }


    //    login
    private void initializeView() {

        ((FloatingActionButton) findViewById(R.id.button_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseLogin(txtEmail.getText().toString(), txtPassword.getText().toString());
            }
        });
        txtEmail = (TextView) findViewById(R.id.edit_txt_mail);
        txtPassword = (TextView) findViewById(R.id.edit_txt_pass);
    }


    private void getAuthStateListener() {
        mFirebaseReference.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                // user is logged in
                if (authData != null) {
                    mUsername = ((String) authData.getProviderData().get(Config.getFirebaseMail()));
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(Config.USER_MAIL, mUsername);
                    startActivity(intent);
                    Config.setMail(LoginActivity.this, mUsername);
                    Log.i(TAG, AUTHEN + "onAuthStateChanged: log in success");
                }
                // user is not logged in
                else {
                    Log.i(TAG, AUTHEN + "onAuthStateChanged: fail log in");
                    mUsername = null;
                }
            }
        });
    }

    private void firebaseLogin(final String email, final String password) {
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, null, getString(R.string.login_progress_dialog), true);
        mFirebaseReference.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            // dk thành công
            public void onSuccess(Map<String, Object> result) {
                progressDialog.dismiss();
                mFirebaseReference.authWithPassword(email, password, null);

                Toast.makeText(LoginActivity.this, "Successfully created user account with uid: " + result.get("uid"), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                progressDialog.dismiss();
                mFirebaseReference.authWithPassword(email, password, null);

                Toast.makeText(LoginActivity.this, "Error " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
//
//            @Override
//            public void onSuccess() {
//                mFirebaseReference.authWithPassword(email, password, null);
//            }
//
//            @Override
//            public void onError(FirebaseError firebaseError) {
//                mFirebaseReference.authWithPassword(email, password, null);
//                progressDialog.dismiss();
//            }
        });

    }
}

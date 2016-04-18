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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Config.getFirebaseInitialize(this);
        mFirebaseReference = Config.getFirebaseReference();
        initializeView();
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

    // when click button
    private void firebaseLogin(final String email, final String password) {
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, null, getString(R.string.login_progress_dialog), true);
        mFirebaseReference.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            // dk thành công-> đặt email, pass vào database
            public void onSuccess(Map<String, Object> result) {
                mFirebaseReference.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        // Authenticated successfully with payload authData
                        // go to main chat
                        progressDialog.dismiss();
                        Log.i(TAG,AUTHEN +  "onAuthenticated: Đăng kí thành công");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        progressDialog.dismiss();
                        // Authenticated failed with error firebaseError
                        Log.i(TAG, AUTHEN + " onAuthenticationError:  Đăng kí thất bại");

                    }
                });
            }

            // dk that bai, do trung email
            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                mFirebaseReference.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    // neu đánh trúng email
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        progressDialog.dismiss();
                        Log.i(TAG, AUTHEN + "onAuthenticated: Đăng nhập thành công");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        progressDialog.dismiss();
                        Log.i(TAG, AUTHEN + " onAuthenticationError: Đăng nhập thất bại");
                        Toast.makeText(LoginActivity.this, "Error " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

    }
}

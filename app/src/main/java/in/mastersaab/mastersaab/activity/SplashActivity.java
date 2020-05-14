package in.mastersaab.mastersaab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (firebaseAuth.getCurrentUser() == null) {
            startAuthenticationActivity();
            Log.d("auth", "null, current user:" + firebaseAuth.getCurrentUser());
        } else {
            startMainActivity();
            Log.d("auth", "not null" + firebaseAuth.getCurrentUser());
        }

    }

    private void startMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startAuthenticationActivity() {
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }

}

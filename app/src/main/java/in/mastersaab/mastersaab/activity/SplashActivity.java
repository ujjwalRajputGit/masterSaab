package in.mastersaab.mastersaab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import in.mastersaab.mastersaab.R;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(firebaseAuth.getCurrentUser() == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "please wait...", Toast.LENGTH_LONG);
            toast.show();
            Log.d("fireauth","user not logged in");
            firebaseAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Log.d("fireauth", "signInAnonymously:success");
                                startMainActivity();
                            } else {
                                Log.w("fireauth", "signInAnonymously:failure", task.getException());
                                Toast toast = Toast.makeText(getApplicationContext(), "make sure your internet is working...", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    });
        } else {
            startMainActivity();
        }

    }

    private void startMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}

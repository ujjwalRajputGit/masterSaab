package in.mastersaab.mastersaab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import in.mastersaab.mastersaab.R;
import in.mastersaab.mastersaab.fragment.authentication.EmailSignInFragment;
import in.mastersaab.mastersaab.fragment.authentication.PhoneAuthFragment;

public class AuthenticationActivity extends AppCompatActivity {


    private static final int RC_SIGN_IN = 6998;
    private static final String TAG = "google";
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;

    private static FragmentManager fragmentManager;
    private static FragmentTransaction fragmentTransaction;
    private EmailSignInFragment emailSignInFragment;
    private PhoneAuthFragment phoneAuthFragment;

    private Group allSignInGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        Button googleSignInButton = findViewById(R.id.google_signInButton);
        Button emailSignInButton = findViewById(R.id.email_signInButton);
        Button phoneSignInButton = findViewById(R.id.phone_signInButton);

        allSignInGroup = findViewById(R.id.allSignIn_group);

        firebaseAuth = FirebaseAuth.getInstance();

        fragmentManager = getSupportFragmentManager();
        emailSignInFragment = new EmailSignInFragment();
        phoneAuthFragment = new PhoneAuthFragment();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        Fragment fragment = fragmentManager.findFragmentById(R.id.authentication);
        if (fragment != null) {
            allSignInGroup.setVisibility(View.GONE);
        }

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton.setOnClickListener(view -> signIn("google"));

        emailSignInButton.setOnClickListener(view -> signIn("email"));

        phoneSignInButton.setOnClickListener(view -> signIn("phone"));

    }

    private void signIn(String signIn) {
        switch (signIn) {
            case "google":
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case "email":
                addFragment(emailSignInFragment);
                allSignInGroup.setVisibility(View.GONE);
                break;
            case "phone":
                addFragment(phoneAuthFragment);
                allSignInGroup.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
//                        Snackbar.make(findViewById(R.id.main), "Sign in Success", Snackbar.LENGTH_SHORT).show();
//                            updateUI(user);
                        startMainActivity();
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.authentication), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                    }

                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public static void addFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.authentication, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.authentication);
        if (fragment != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
            allSignInGroup.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }

    }
}

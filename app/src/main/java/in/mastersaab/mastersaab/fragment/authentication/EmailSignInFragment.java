package in.mastersaab.mastersaab.fragment.authentication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.mastersaab.mastersaab.R;
import in.mastersaab.mastersaab.activity.AuthenticationActivity;

public class EmailSignInFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private TextInputEditText passwordEditText;
    private TextInputEditText emailEditText;

    public EmailSignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_email_signin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        Button signInButton = view.findViewById(R.id.emailSignIn_FinalButton);
        emailEditText = view.findViewById(R.id.emailSignIn_EditText);
        passwordEditText = view.findViewById(R.id.passwordSignIn_EditText);
        TextView forgetPassword = view.findViewById(R.id.forgetPassword_textView);
        TextView createAccount = view.findViewById(R.id.createAccount_textView);

        EmailSignUpFragment emailSignUpFragment = new EmailSignUpFragment();

        signInButton.setOnClickListener(view1 -> signIn());
        createAccount.setOnClickListener(view1 -> AuthenticationActivity.addFragment(emailSignUpFragment));

    }

    private void signIn() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d("email log", "signInWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
//                            updateUI(user);
                    } else {
                        Log.w("Emaillog", "signInWithEmail:failure", task.getException());
                        Log.w("Emaillog", "signInWithEmail:failure" + task.getException().getMessage());
                        Log.w("Emaillog", "signInWithEmail:failure" + task.getException().getCause());
                        Log.w("Emaillog", "signInWithEmail:failure" + task.getException().getLocalizedMessage());
                        Log.w("Emaillog", "signInWithEmail:failure" + task.getException().getStackTrace());
                        Log.w("Emaillog", "signInWithEmail:failure" + task.getException().getClass());
//                            updateUI(null);
                    }

                });

    }

}

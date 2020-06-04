package in.mastersaab.mastersaab.fragment.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import in.mastersaab.mastersaab.R;
import in.mastersaab.mastersaab.activity.MainActivity;

public class EmailSignUpFragment extends Fragment {
    
    private TextInputEditText displayNameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText conformPasswordEditText;;
    private FirebaseAuth firebaseAuth;

    public EmailSignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_email_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        displayNameEditText = view.findViewById(R.id.displayNameSignUp_EditText);
        emailEditText = view.findViewById(R.id.emailSignUp_EditText);
        passwordEditText = view.findViewById(R.id.passwordSignUp_EditText);
        conformPasswordEditText = view.findViewById(R.id.conformPasswordSignUp_EditText);

        Button signUpButton = view.findViewById(R.id.emailSignUp_FinalButton);
        signUpButton.setOnClickListener(view1 -> signUp());
    }

    private void signUp() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), (OnCompleteListener<AuthResult>) task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "createUserWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateDisplayName(user);
//                            updateUI(user);
                    } else {
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
//                        updateUI(null);
                    }

                });
    }

    private void updateDisplayName(FirebaseUser user) {
        String displayName = displayNameEditText.getText().toString();

        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        user.updateProfile(profileChangeRequest)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "User profile updated.");
//                        startMainActivity();
                    }
                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}

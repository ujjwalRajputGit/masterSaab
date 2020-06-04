package in.mastersaab.mastersaab.fragment.authentication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import in.mastersaab.mastersaab.R;
import in.mastersaab.mastersaab.activity.MainActivity;

public class PhoneAuthFragment extends Fragment {

    private Button generateOTPBtn;
    private Button verifyOTPBtn;
    private EditText phoneNumber;
    private EditText countryCode;
    private EditText otp;
    private ProgressBar generateOTPProgress;
    private ProgressBar verifyOTPProgress;
    private TextView generateError;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Group verifyOTPGroup;
    private String verificationId;
    private FirebaseAuth firebaseAuth;
    private TextView resendOTP;
    private TextView changePhone;

    public PhoneAuthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        generateOTPBtn = view.findViewById(R.id.generateOTP_Button);
        verifyOTPBtn = view.findViewById(R.id.verifyOTP_button);
        phoneNumber = view.findViewById(R.id.phoneNo_EditText);
        countryCode = view.findViewById(R.id.countyCode_EditText);
        otp = view.findViewById(R.id.otp_EditText);
        generateOTPProgress = view.findViewById(R.id.generateOTP_Progress);
        verifyOTPProgress = view.findViewById(R.id.verifyOTP_Progress);
        generateError = view.findViewById(R.id.generate_error);
        verifyOTPGroup = view.findViewById(R.id.verifyOTP_group);
        resendOTP = view.findViewById(R.id.resendOTP);
        changePhone = view.findViewById(R.id.changePhone);

        generateOTPBtn.setOnClickListener(view1 -> generateOTP());
        verifyOTPBtn.setOnClickListener(view1 -> verifyOTP());
        resendOTP.setOnClickListener(view1 -> resendOTP());
        changePhone.setOnClickListener(view1 -> chnagePhoneNumber());

        firebaseAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                generateError.setText(R.string.verification_failed);
                generateError.setVisibility(View.VISIBLE);
                verifyOTPProgress.setVisibility(View.GONE);
                generateOTPProgress.setVisibility(view.GONE);
                verifyOTPGroup.setVisibility(View.GONE);
                generateOTPBtn.setEnabled(true);
                resendOTP.setVisibility(View.GONE);
                changePhone.setVisibility(View.GONE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                generateOTPProgress.setVisibility(View.GONE);
                verifyOTPGroup.setVisibility(View.VISIBLE);
                verificationId = s;
                new Handler().postDelayed(() -> {
                    resendOTP.setVisibility(View.VISIBLE);
                    changePhone.setVisibility(View.VISIBLE);
                },30000);
            }
        };
    }

    private void chnagePhoneNumber() {
        verifyOTPGroup.setVisibility(View.GONE);
        generateOTPBtn.setEnabled(true);
        resendOTP.setVisibility(View.GONE);
        changePhone.setVisibility(View.GONE);
        generateError.setVisibility(View.GONE);
    }

    private void resendOTP() {
        verifyOTPGroup.setVisibility(View.GONE);
        generateOTPBtn.setEnabled(true);
        resendOTP.setVisibility(View.GONE);
        changePhone.setVisibility(View.GONE);
        generateError.setVisibility(View.GONE);
    }

    private void verifyOTP() {
        String code = otp.getText().toString();
        if(code.isEmpty()) {
            generateError.setText(R.string.Please_enter_the_otp);
            generateError.setVisibility(View.VISIBLE);
        }else {
            verifyOTPProgress.setVisibility(View.VISIBLE);
            generateError.setVisibility(View.GONE);
            verifyOTPBtn.setEnabled(false);

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void generateOTP() {
        String country_code = countryCode.getText().toString();
        String phone_number = phoneNumber.getText().toString();
        String complete_phone_number = "+" + country_code + phone_number;

        if (country_code.isEmpty() || phone_number.isEmpty()) {
            generateError.setText(R.string.fill_the_form);
            generateError.setVisibility(View.VISIBLE);
        }else {
            generateOTPProgress.setVisibility(View.VISIBLE);
            generateOTPBtn.setEnabled(false);

            generateError.setVisibility(View.GONE);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    complete_phone_number,
                    60,
                    TimeUnit.SECONDS,
                    getActivity(),
                    mCallbacks
            );
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInWithCredential:success");
                        FirebaseUser user = task.getResult().getUser();
                        startMainActivity();
                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            generateError.setText(R.string.otp_invalid);
                        }else {
                            generateError.setText(task.getException().getLocalizedMessage());

                        }
                        generateError.setVisibility(View.VISIBLE);
                        verifyOTPProgress.setVisibility(View.GONE);
                        verifyOTPBtn.setEnabled(true);
                        changePhone.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}

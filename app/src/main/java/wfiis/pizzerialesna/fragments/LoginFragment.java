package wfiis.pizzerialesna.fragments;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.inverce.mod.core.IM;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.customViews.InputEditTextView;
import wfiis.pizzerialesna.customViews.TopToast;
import wfiis.pizzerialesna.firebase.CheckConnectionToFirebase;
import wfiis.pizzerialesna.tools.AppendMessage;
import wfiis.pizzerialesna.tools.sharedPref.UserUtils;
import wfiis.pizzerialesna.validation.Validator;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private InputEditTextView email;
    private InputEditTextView password;
    private Button loginButton;
    private TextView registerTextView;
    private TextView forgetPassword;

    private boolean isValid;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CheckConnectionToFirebase checkConnectionToFirebase = new CheckConnectionToFirebase();

    private UserUtils userUtils = new UserUtils();

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        assert getActions() != null;

        findViews(view);
        setListeners();

        //TO REMOVE ONLY FOR TESt
        email.setText("damiengrzelak@gmail.com");
        password.setText("haslotest");
        //

        getActions().topBar().showBackIcon(false);
        getActions().topBar().showMenuIcon(false);
        return view;
    }

    private void setListeners() {
        loginButton.setOnClickListener(this);
        registerTextView.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    private void findViews(View view) {
        email = view.findViewById(R.id.fragment_login_email);
        password = view.findViewById(R.id.fragment_login_password);
        loginButton = view.findViewById(R.id.fragment_login_login_button);
        registerTextView = view.findViewById(R.id.fragment_login_register_textView);
        forgetPassword = view.findViewById(R.id.fragment_login_register_remind_password);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fragment_login_login_button:
                validate();
                authUser(email.getText(), password.getText());
                break;
            case R.id.fragment_login_register_textView:
                getActions().navigateTo(RegisterFragment.newInstance(), true);
                break;
            case R.id.fragment_login_register_remind_password:
                if (checkEmail()) {
                    resetUserPassword(email.getText());
                }
                break;
        }
    }

    private boolean checkEmail() {
        return Validator.isEmailValid(email.getText());
    }

    private void validate() {
        if (Validator.isEmailValid(email.getText())) {
            email.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.email_incorect);
            email.setError(true);
        }
        if (Validator.isPasswordValid(password.getText())) {
            password.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.password_incorect);
            password.setError(true);
        }

        if (!email.isError() && !password.isError()) {
            isValid = true;
        } else {
            AppendMessage.showSuccessOrError();
            isValid = false;
        }
    }

    private boolean authUser(String email, String password) {

        if (isValid) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(IM.activity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                isValid = true;
                                userUtils.saveSession(task.getResult().getUser().getUid(), true);
                                getActions().navigateTo(HomeFragment.newInstance(), false);
                            } else {
                                AppendMessage.appendMessage(R.string.incorrect_sing_in_pass);
                                isValid = false;
                                AppendMessage.showSuccessOrError();
                            }
                        }
                    });
        }
        return isValid;
    }

    private void resetUserPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            TopToast.show(R.string.password_reset, TopToast.TYPE_SUCCESS, TopToast.DURATION_SHORT);
                        } else {
                            TopToast.show(R.string.acc_no_exist, TopToast.TYPE_ERROR, TopToast.DURATION_SHORT);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                TopToast.show(R.string.acc_no_exist, TopToast.TYPE_ERROR, TopToast.DURATION_SHORT);
            }
        });
    }
}

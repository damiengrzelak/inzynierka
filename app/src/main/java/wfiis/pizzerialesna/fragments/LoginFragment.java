package wfiis.pizzerialesna.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inverce.mod.core.Ui;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.customViews.InputEditTextView;
import wfiis.pizzerialesna.tools.AppendMessage;
import wfiis.pizzerialesna.tools.SpanUtils;
import wfiis.pizzerialesna.validation.Validator;

public class LoginFragment extends BaseFragment implements View.OnClickListener{

    private InputEditTextView email;
    private InputEditTextView password;
    private Button loginButton;
    private TextView registerTextView;

    private boolean isValid;

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

        getActions().topBar().showBackIcon(false);
        return view;
    }

    private void setListeners() {
        loginButton.setOnClickListener(this);
        registerTextView.setOnClickListener(this);
    }

    private void findViews(View view) {
        email = view.findViewById(R.id.fragment_login_email);
        password = view.findViewById(R.id.fragment_login_password);
        loginButton = view.findViewById(R.id.fragment_login_login_button);
        registerTextView = view.findViewById(R.id.fragment_login_register_textView);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fragment_login_login_button:
                validate();
                if (isValid) {
                    getActions().navigateTo(HomeFragment.newInstance(), false);
                }
                break;
            case R.id.fragment_login_register_textView:
                break;
        }
    }

    private void validate() {
        String error_text;
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
}

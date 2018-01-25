package wfiis.pizzerialesna.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inverce.mod.core.IM;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.customViews.InputEditTextView;
import wfiis.pizzerialesna.customViews.TopToast;
import wfiis.pizzerialesna.customViews.ZipCodeListener;
import wfiis.pizzerialesna.firebase.CheckConnectionToFirebase;
import wfiis.pizzerialesna.model.User;
import wfiis.pizzerialesna.tools.AppendMessage;
import wfiis.pizzerialesna.tools.Util;
import wfiis.pizzerialesna.validation.Validator;

public class RegisterFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    private InputEditTextView name;
    private InputEditTextView surname;
    private InputEditTextView email;
    private InputEditTextView password;
    private InputEditTextView phone;
    private InputEditTextView street;
    private InputEditTextView homeNr;
    private InputEditTextView city;
    private InputEditTextView zipCode;
    private Button register;

    private boolean isValid;
    private FirebaseAuth  mAuth = FirebaseAuth.getInstance();
    private int PHONELENGT = 9;
    private int ZIP_CODE_LENGTH = 6;

    private CheckConnectionToFirebase checkConnectionToFirebase = new CheckConnectionToFirebase();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        assert getActions() != null;

        findViews(view);
        prepareViews();
        setListeners();

        getActions().topBar().showBackIcon(false);
        return view;
    }

    private void setListeners() {
        register.setOnClickListener(this);
        zipCode.getEdit().addTextChangedListener(new ZipCodeListener(zipCode.getEdit()));
    }

    private void findViews(View view) {
        name = view.findViewById(R.id.fragment_register_name);
        surname = view.findViewById(R.id.fragment_register_surname);
        email = view.findViewById(R.id.fragment_register_email);
        password = view.findViewById(R.id.fragment_register_password);
        phone = view.findViewById(R.id.fragment_register_tel);
        street = view.findViewById(R.id.fragment_register_street);
        homeNr = view.findViewById(R.id.fragment_register_street_nr);
        city = view.findViewById(R.id.fragment_register_city);
        zipCode = view.findViewById(R.id.fragment_register_zip_code);
        register = view.findViewById(R.id.fragment_register_register_button);

        password.getEdit().setTransformationMethod(null);

    }

    public void prepareViews() {
        phone.getEdit().setFilters(new InputFilter[]{new InputFilter.LengthFilter(PHONELENGT)});

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fragment_register_register_button) {
            validate();
            register();
        }
    }

    private void register() {
            if (isValid) {
                mAuth.createUserWithEmailAndPassword(email.getText(), password.getText())
                        .addOnCompleteListener(IM.activity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = new User(
                                            name.getText(),
                                            surname.getText(),
                                            email.getText(),
                                            password.getText(),
                                            phone.getText(),
                                            street.getText(),
                                            homeNr.getText(),
                                            city.getText(),
                                            zipCode.getText());
                                    saveUserToDB(task.getResult().getUser().getUid(), user);
                                    getActions().navigateTo(HomeFragment.newInstance(), false);
                                    TopToast.show(R.string.register_success, TopToast.TYPE_SUCCESS, TopToast.DURATION_SHORT);
                                } else {
                                    AppendMessage.appendMessage(R.string.register_problem);
                                    isValid = false;
                                    AppendMessage.showSuccessOrError();
                                }

                            }
                        });
            }
    }

    private void validate() {
        isValid = true;
        if (Validator.firstNameIsValid(name.getText()) && !Util.nullOrEmpty(name.getText())) {
            name.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.name_incorect);
            name.setError(true);
            isValid = false;
        }
        if (Validator.lastNameIsValid(surname.getText()) && !Util.nullOrEmpty(surname.getText())) {
            surname.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.last_name_incorect);
            surname.setError(true);
            isValid = false;
        }
        if (Validator.isEmailValid(email.getText()) && !Util.nullOrEmpty(email.getText())) {
            email.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.email_incorect);
            email.setError(true);
            isValid = false;
            isValid = false;
        }
        if (Validator.isPasswordValid(password.getText()) && !Util.nullOrEmpty(password.getText())) {
            password.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.password_incorect);
            password.setError(true);
            isValid = false;
        }
        if (Validator.phoneNumberFormatIsValid(phone.getText()) && !Util.nullOrEmpty(phone.getText())) {
            phone.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.phone_incorect);
            phone.setError(true);
            isValid = false;
        }
        if (Validator.streetIsValid(street.getText()) && !Util.nullOrEmpty(street.getText())) {
            street.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.street_incorrect);
            street.setError(true);
            isValid = false;
        }
        if (Validator.isHouseNumberValid(homeNr.getText()) && !Util.nullOrEmpty(homeNr.getText())) {
            homeNr.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.street_nr_incorect);
            homeNr.setError(true);
            isValid = false;
        }
        if (Validator.cityIsValid(city.getText()) && !Util.nullOrEmpty(city.getText())) {
            city.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.city_incorrect);
            city.setError(true);
            isValid = false;
        }
        if (Validator.zipCodeIsValid(zipCode.getText()) && !Util.nullOrEmpty(zipCode.getText())) {
            zipCode.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.zip_code_incorrect);
            zipCode.setError(true);
            isValid = false;
        }

        if (!isValid) {
            AppendMessage.showSuccessOrError();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void saveUserToDB(String uid, User user){
        DatabaseReference usersRef = ref.child("users/"+uid);
        usersRef.setValue(user);
    }
}

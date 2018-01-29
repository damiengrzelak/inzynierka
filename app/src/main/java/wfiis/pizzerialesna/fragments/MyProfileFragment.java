package wfiis.pizzerialesna.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.customViews.InputEditTextView;
import wfiis.pizzerialesna.customViews.TopToast;
import wfiis.pizzerialesna.customViews.ZipCodeListener;
import wfiis.pizzerialesna.model.User;
import wfiis.pizzerialesna.tools.AppendMessage;
import wfiis.pizzerialesna.tools.UI;
import wfiis.pizzerialesna.tools.Util;
import wfiis.pizzerialesna.validation.Validator;

import static wfiis.pizzerialesna.Cfg.users_table;

public class MyProfileFragment extends BaseFragment implements View.OnClickListener {

    private InputEditTextView name;
    private InputEditTextView surname;
    private InputEditTextView email;
    private InputEditTextView phone;
    private InputEditTextView street;
    private InputEditTextView homeNr;
    private InputEditTextView city;
    private InputEditTextView zipCode;
    private CheckBox checkBox;

    private ImageView editDataBtn;
    private ImageView editPermissionBtn;

    private Button saveData;
    private Button savePermission;

    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private User currentUser = new User();

    private boolean isValid;

    public static MyProfileFragment newInstance() {
        return new MyProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        assert getActions() != null;

        UI.hideSoftInput(view);

        findViews(view);
        prepareViews();
        findUser();

        setListeners();


        getActions().topBar().showBackIcon(true);
        getActions().topBar().showMenuIcon(false);
        return view;
    }

    private void setListeners() {
        zipCode.getEdit().addTextChangedListener(new ZipCodeListener(zipCode.getEdit()));

        editDataBtn.setOnClickListener(this);
        editPermissionBtn.setOnClickListener(this);
        savePermission.setOnClickListener(this);
        saveData.setOnClickListener(this);
    }

    private void findViews(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        name = view.findViewById(R.id.fragment_my_profile_name);
        surname = view.findViewById(R.id.fragment_my_profile_surname);
        email = view.findViewById(R.id.fragment_my_profile_email);
        phone = view.findViewById(R.id.fragment_my_profile_tel);
        street = view.findViewById(R.id.fragment_my_profile_street);
        homeNr = view.findViewById(R.id.fragment_my_profile_street_nr);
        city = view.findViewById(R.id.fragment_my_profile_city);
        zipCode = view.findViewById(R.id.fragment_my_profile_zip_code);

        checkBox = view.findViewById(R.id.fragment_my_profile_checkbox);

        editDataBtn = view.findViewById(R.id.fragment_my_profile_edit_data);
        editPermissionBtn = view.findViewById(R.id.fragment_my_profile_edit_permission);

        saveData = view.findViewById(R.id.fragment_my_profile_save_data);
        savePermission = view.findViewById(R.id.fragment_my_profile_save_permission);

    }

    public void prepareViews() {
        name.setInputEnabled(false);
        surname.setInputEnabled(false);
        email.setInputEnabled(false);
        phone.setInputEnabled(false);
        street.setInputEnabled(false);
        homeNr.setInputEnabled(false);
        city.setInputEnabled(false);
        zipCode.setInputEnabled(false);

        checkBox.setClickable(false);


    }

    private void findUser() {
        User current = new User();
        ref = ref.child(users_table);
        Query userQuerry = ref.child(mAuth.getUid());

        userQuerry.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User u = dataSnapshot.getValue(User.class);
                current.setName(dataSnapshot.getValue(User.class).getName());
                current.setLastname(u.getLastname());
                current.setEmail(u.getEmail());
                current.setPassword(u.getPassword());
                current.setPhone(u.getPhone());
                current.setStreet(u.getStreet());
                current.setFlatNumber(u.getFlatNumber());
                current.setCity(u.getCity());
                current.setZipCode(u.getZipCode());
                current.setPermission(u.isPermission());

                currentUser.updateUserWithUser(current);
                fillViews(current);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
            }
        });
    }

    private void fillViews(User currentUser) {
        name.setText(currentUser.getName());
        surname.setText(currentUser.getLastname());
        email.setText(currentUser.getEmail());
        phone.setText(currentUser.getPhone());
        street.setText(currentUser.getStreet());
        homeNr.setText(currentUser.getFlatNumber());
        city.setText(currentUser.getCity());
        zipCode.setText(currentUser.getZipCode());

        checkBox.setChecked(currentUser.isPermission());

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.fragment_my_profile_edit_data:
                editData();
                break;
            case R.id.fragment_my_profile_edit_permission:
                editPermission();
                break;
            case R.id.fragment_my_profile_save_data:
                validate();
                if (isValid) {
                    saveDataFinish();
                }
                break;
            case R.id.fragment_my_profile_save_permission:
                savePermissionFinish();
                break;
        }
    }

    private void editData() {
        name.setInputEnabled(true);
        surname.setInputEnabled(true);
        email.setInputEnabled(true);
        phone.setInputEnabled(true);
        street.setInputEnabled(true);
        homeNr.setInputEnabled(true);
        city.setInputEnabled(true);
        zipCode.setInputEnabled(true);

        saveData.setVisibility(View.VISIBLE);

    }

    private void saveDataFinish() {
        User user = new User();
        user.updateUser(name.getText(), surname.getText(), email.getText(), currentUser.getPassword(), phone.getText(), street.getText(), homeNr.getText(), city.getText(), zipCode.getText(), currentUser.isPermission());
        Map<String, Object> userDataUpdate = new HashMap<>();
        userDataUpdate.put(mAuth.getUid(), user);


        ref.child(users_table);
        ref.updateChildren(userDataUpdate);
        TopToast.show(R.string.edit_success, TopToast.TYPE_SUCCESS, TopToast.DURATION_SHORT);

        name.setInputEnabled(false);
        surname.setInputEnabled(false);
        email.setInputEnabled(false);
        phone.setInputEnabled(false);
        street.setInputEnabled(false);
        homeNr.setInputEnabled(false);
        city.setInputEnabled(false);
        zipCode.setInputEnabled(false);

        saveData.setVisibility(View.GONE);
    }

    private void savePermissionFinish() {
        currentUser.setPermission(checkBox.isChecked());
        Map<String, Object> permissionUpdate = new HashMap<>();
        permissionUpdate.put(mAuth.getUid() + "/permission", checkBox.isChecked());

        ref.child(users_table);
        ref.updateChildren(permissionUpdate);
        TopToast.show(R.string.edit_success, TopToast.TYPE_SUCCESS, TopToast.DURATION_SHORT);

        savePermission.setVisibility(View.GONE);
        checkBox.setClickable(false);
    }

    private void editPermission() {
        checkBox.setClickable(true);
        savePermission.setVisibility(View.VISIBLE);
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
}

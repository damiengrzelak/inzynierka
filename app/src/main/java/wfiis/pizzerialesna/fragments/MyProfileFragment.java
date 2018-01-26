package wfiis.pizzerialesna.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.customViews.InputEditTextView;
import wfiis.pizzerialesna.customViews.ZipCodeListener;
import wfiis.pizzerialesna.model.User;
import wfiis.pizzerialesna.tools.UI;

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

    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private User currentUser = new User("","","","", "", "","","","",false);
    private User currentUser2;

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
        fillViews(currentUser);
        setListeners();
        Toast.makeText(getContext(), findUser().getName(), Toast.LENGTH_LONG).show();
        getActions().topBar().showBackIcon(true);
        getActions().topBar().showMenuIcon(false);
        return view;
    }

    private void setListeners() {
        zipCode.getEdit().addTextChangedListener(new ZipCodeListener(zipCode.getEdit()));
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

    private User findUser() {
        ref = ref.child(users_table);
        Query userQuerry = ref.child(mAuth.getUid());
        userQuerry.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);

//                currentUser =  new User(u.getName(), u.getSurnmale(), u.getEmail(),
//                        u.getPassword(), u.getPhone(), u.getStreet(), u.getHouseNr(),
//                        u.getCity(), u.getZipCode(), u.isPermission());
                currentUser.setName(u.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
            }
        });
        return currentUser;
    }

    private void fillViews(User currentUser) {
        if (currentUser != null) {
            name.getEdit().setText(currentUser.getName());
            surname.getEdit().setText(currentUser.getName());
            email.getEdit().setText(currentUser.getEmail());
            phone.getEdit().setText(currentUser.getPassword());
            street.getEdit().setText(currentUser.getStreet());
            homeNr.getEdit().setText(currentUser.getHouseNr());
            city.getEdit().setText(currentUser.getCity());
            zipCode.getEdit().setText(currentUser.getZipCode());

            checkBox.setChecked(currentUser.isPermission());

        }
    }

    @Override
    public void onClick(View view) {
    }
}

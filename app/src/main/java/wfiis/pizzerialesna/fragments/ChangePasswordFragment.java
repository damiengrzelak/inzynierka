package wfiis.pizzerialesna.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
import wfiis.pizzerialesna.model.User;
import wfiis.pizzerialesna.validation.Validator;

import static wfiis.pizzerialesna.Cfg.users_table;

public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {

    private InputEditTextView oldPassword;
    private InputEditTextView newPassword;
    private InputEditTextView confirmPassword;
    private Button changePassword;

    private boolean isValid;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        assert getActions() != null;

        findViews(view);
        setListeners();

        getActions().topBar().showBackIcon(true);
        getActions().topBar().showMenuIcon(false);
        return view;
    }

    private void setListeners() {
        changePassword.setOnClickListener(this);
    }

    private void changePassword() {
        validate();

        if (isValid) {

            final String email = user.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword.getText());

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword.getText()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    oldPassword.setError(true);
                                    TopToast.show(R.string.incorect_oldw_password, TopToast.TYPE_ERROR, TopToast.DURATION_SHORT);
                                } else {
                                    TopToast.show(R.string.change_pass_succ, TopToast.TYPE_SUCCESS, TopToast.DURATION_SHORT);
                                    saveUser();
                                    getActions().navigateBack();
                                }
                            }
                        });
                    } else {
                        oldPassword.setError(true);
                        TopToast.show(R.string.incorect_oldw_password, TopToast.TYPE_ERROR, TopToast.DURATION_SHORT);
                    }
                }
            });
        }
    }

    private void validate() {
        if (Validator.isPasswordValid(oldPassword.getText()) &&
                Validator.isPasswordValid(newPassword.getText()) && Validator.isPasswordValid(confirmPassword.getText())
                && newPassword.getText().equals(confirmPassword.getText())) {
            isValid = true;
        } else if (!newPassword.getText().equals(confirmPassword.getText())) {
            isValid = false;
            TopToast.show(R.string.incorect_new_password, TopToast.TYPE_ERROR, TopToast.DURATION_SHORT);
            confirmPassword.setError(true);
            return;
        } else if (!Validator.isPasswordValid(newPassword.getText())) {
            TopToast.show(R.string.password_incorect, TopToast.TYPE_ERROR, TopToast.DURATION_SHORT);
            isValid = false;
            newPassword.setError(true);
            return;
        }
    }

    private void findViews(View view) {
        oldPassword = view.findViewById(R.id.fragment_change_passwor_old);
        newPassword = view.findViewById(R.id.fragment_change_passwor_new);
        confirmPassword = view.findViewById(R.id.fragment_change_passwor_new_confirm);

        changePassword = view.findViewById(R.id.fragment_change_passwor_change_password);
    }

    @Override
    public void onClick(View view) {
        changePassword();
    }

    private void saveUser() {
        User current = new User();

        ref = ref.child(users_table);
        Query userQuerry = ref.child(user.getUid());
        Map<String, Object> userDataUpdate = new HashMap<>();

        userQuerry.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User u = dataSnapshot.getValue(User.class);
                current.setName(dataSnapshot.getValue(User.class).getName());
                current.setLastname(u.getLastname());
                current.setEmail(u.getEmail());
                current.setPassword(newPassword.getText());
                current.setPhone(u.getPhone());
                current.setStreet(u.getStreet());
                current.setFlatNumber(u.getFlatNumber());
                current.setCity(u.getCity());
                current.setZipCode(u.getZipCode());
                current.setPermission(u.isPermission());

                userDataUpdate.put(user.getUid(), current);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
            }
        });
        ref.child(users_table);
        ref.updateChildren(userDataUpdate);
    }
}

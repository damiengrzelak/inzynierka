package wfiis.pizzerialesna.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;

public class SetttingFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout permissionFragment;
    private LinearLayout passwordFragment;
    public static SetttingFragment newInstance(){
        return new SetttingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        assert getActions() != null;

        findViews(view);
        setListeners();

        getActions().topBar().showBackIcon(true);
        getActions().topBar().showMenuIcon(false);
        return view;
    }

    private void setListeners() {
        permissionFragment.setOnClickListener(this);
        passwordFragment.setOnClickListener(this);
    }

    private void findViews(View view) {
        permissionFragment = view.findViewById(R.id.fragment_settings_change_permission);
        passwordFragment = view.findViewById(R.id.fragment_settings_change_password);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.fragment_settings_change_password:
                getActions().navigateTo(ChangePasswordFragment.newInstance(), true);
                break;
            case R.id.fragment_settings_change_permission:
                getActions().navigateTo(PermissionFragment.newInstance(), true);
                break;
        }
    }
}

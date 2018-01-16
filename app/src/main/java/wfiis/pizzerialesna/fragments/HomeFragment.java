package wfiis.pizzerialesna.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;

public class HomeFragment extends BaseFragment implements View.OnClickListener{

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        assert getActions() != null;

        findViews(view);
        setListeners();

        getActions().topBar().showBackIcon(false );
        return view;
    }

    private void setListeners() {
    }

    private void findViews(View view) {
    }

    @Override
    public void onClick(View view) {

    }
}
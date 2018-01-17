package wfiis.pizzerialesna.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;

public class HomeFragment extends BaseFragment implements View.OnClickListener{

    private RelativeLayout menu;
    private RelativeLayout obiad;
    private RelativeLayout discount;
    private RelativeLayout fb;
    private RelativeLayout events;
    private RelativeLayout address;

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
        menu.setOnClickListener(this);
        obiad.setOnClickListener(this);
        discount.setOnClickListener(this);
        fb.setOnClickListener(this);
        events.setOnClickListener(this);
        address.setOnClickListener(this);
    }

    private void findViews(View view) {
        menu = view.findViewById(R.id.fragment_menu_pizza);
        obiad = view.findViewById(R.id.fragment_menu_obiad);
        discount = view.findViewById(R.id.fragment_menu_promo);
        fb = view.findViewById(R.id.fragment_menu_fb);
        events = view.findViewById(R.id.fragment_menu_party);
        address = view.findViewById(R.id.fragment_menu_adress);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.fragment_menu_pizza:
                break;
            case R.id.fragment_menu_obiad:
                break;
            case R.id.fragment_menu_promo:
                break;
            case R.id.fragment_menu_fb:
                break;
            case R.id.fragment_menu_party:
                break;
            case R.id.fragment_menu_adress:
                getActions().navigateTo(ContactFragment.newInstance(), true);
                break;
        }
    }
}
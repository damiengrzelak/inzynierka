package wfiis.pizzerialesna.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.fragments.facebookAtApp.FavebookAtApp;
import wfiis.pizzerialesna.fragments.imprezy.ImprezyFragment;
import wfiis.pizzerialesna.fragments.menu.OtherMenuFragment;
import wfiis.pizzerialesna.fragments.menu.PizzaMenuFragment;

public class HomeFragment extends BaseFragment implements View.OnClickListener{

    private RelativeLayout menu;
    private RelativeLayout obiad;
    private RelativeLayout discount;
    private RelativeLayout fb;
    private RelativeLayout events;
    private RelativeLayout address;

    public static String FACEBOOK_URL = "https://www.facebook.com/Pizzeria-Le%C5%9Bna-w-Kro%C5%9Bniewicach-457823184290541/";
    public static String FACEBOOK_PAGE_ID = "Pizzeria-Leśna-w-Krośniewicach-457823184290541";

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
        getActions().topBar().showMenuIcon(true);
        getActions().topBar().showBasketIcon(true);


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
                getActions().navigateTo(PizzaMenuFragment.newInstance(), true);
                break;
            case R.id.fragment_menu_obiad:
                getActions().navigateTo(OtherMenuFragment.newInstance(), true);
                break;
            case R.id.fragment_menu_promo:
                getActions().navigateTo(FavebookAtApp.newInstance(), true);
                break;
            case R.id.fragment_menu_fb:
                goToFacebook(getContext());
                break;
            case R.id.fragment_menu_party:
                getActions().navigateTo(ImprezyFragment.newInstance(), true);
                break;
            case R.id.fragment_menu_adress:
                getActions().navigateTo(ContactFragment.newInstance(), true);
                break;
        }
    }

    public static String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else {
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL;
        }
    }

    public void goToFacebook(Context context) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(context);
        facebookIntent.setData(Uri.parse(facebookUrl));
        startActivity(facebookIntent);
    }

}
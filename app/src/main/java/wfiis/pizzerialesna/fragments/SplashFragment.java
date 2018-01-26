package wfiis.pizzerialesna.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.tools.sharedPref.UserUtils;


public class SplashFragment extends BaseFragment {

    private UserUtils userUtils = new UserUtils();

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        userUtils.getPreferences();
        try {
            String version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            TextView textView = view.findViewById(R.id.fragment_splash_info);
            if (textView != null) {
                textView.setText(getString(R.string.open_app) + "\n"
                        + getString(R.string.app_name) + " v " + version);
            }
        } catch (Exception ignored) {
        }
        waitAtSplash();

        getActions().topBar().showMenuIcon(false);
        getActions().topBar().showBackIcon(false);
        return view;
    }


    private void waitAtSplash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (userUtils != null && !userUtils.isLogged()) {
                    getActions().navigateTo(LoginFragment.newInstance(), false);
                } else {
                    getActions().navigateTo(HomeFragment.newInstance(), false);
                }
            }
        }, 4000);
    }

}

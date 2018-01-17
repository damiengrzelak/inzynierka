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


public class SplashFragment extends BaseFragment {

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

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

        return view;
    }


    private void waitAtSplash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                getActions().navigateTo(LoginFragment.newInstance(), false);
            }
        }, 4000);
    }

}

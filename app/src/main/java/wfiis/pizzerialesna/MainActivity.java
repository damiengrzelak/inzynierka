package wfiis.pizzerialesna;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.fragments.SplashFragment;
import wfiis.pizzerialesna.interactions.ActivityInteractions;
import wfiis.pizzerialesna.interactions.TopBarInteractions;

public class MainActivity extends AppCompatActivity implements ActivityInteractions, FragmentManager.OnBackStackChangedListener {


    TopBarInteractions topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Maritime_Tropical_Neue.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        topBar = (TopBarInteractions) fragmentManager.findFragmentById(R.id.top_fragment);
        fragmentManager.addOnBackStackChangedListener(this);

        if (savedInstanceState == null) {
            navigateTo(SplashFragment.newInstance(), false);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onBackStackChanged() {
        topBar().showBackIcon(getSupportFragmentManager().getBackStackEntryCount() > 0);
    }

    @Override
    public boolean navigateTo(BaseFragment fragment, boolean addToBackstack) {
        FragmentManager manager = getSupportFragmentManager();

        // Activity must be initialized and fragment non null to proceed
        if (fragment == null || manager == null) {
            return false;
        }

        // Prevent adding same page twice
        if (manager.getFragments() != null) {
            Fragment current = manager.findFragmentById(R.id.activity_content);
            if (current != null && fragment.getClass().equals(current.getClass())) {
                return false;
            }
        }

        // finally make fragment transaction
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.activity_content, fragment);
        if (addToBackstack) {
            transaction.addToBackStack(fragment.toString());
        }
        transaction.commit();
        return true;
    }

    @Override
    public boolean navigateBack() {
        onBackPressed();
        return true;
    }

    @Override
    public TopBarInteractions topBar() {
        return topBar;
    }
}


package wfiis.pizzerialesna;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.inverce.mod.core.IM;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.customViews.Preloader;
import wfiis.pizzerialesna.fragments.HomeFragment;
import wfiis.pizzerialesna.fragments.SplashFragment;
import wfiis.pizzerialesna.interactions.ActivityInteractions;
import wfiis.pizzerialesna.interactions.TopBarInteractions;

public class MainActivity extends AppCompatActivity implements ActivityInteractions, FragmentManager.OnBackStackChangedListener, DrawerLayout.DrawerListener {
    TopBarInteractions topBar;
    private Preloader preloader;
    private boolean isDrawerMenuOpen;
    private DrawerLayout drawerMenu;

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

        preloader = this.findViewById(R.id.preloader);
        isDrawerMenuOpen = false;
        drawerMenu = findViewById(R.id.activity_main_drawer_layout);
        drawerMenu.addDrawerListener(this);

        if (savedInstanceState == null) {
            navigateTo(SplashFragment.newInstance(), false);
        }
    }

    @Override
    public void onBackPressed() {
        if (isDrawerMenuOpen) {
            changeDrawerMenuState();
        } else {
            super.onBackPressed();
            drawerMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
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
        if (fragment instanceof HomeFragment) {
            drawerMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
        } else {
            drawerMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        transaction.commit();
        return true;
    }

    @Override
    public boolean navigateBack() {
        disMissPreloader();
        onBackPressed();
        return true;
    }

    @Override
    public TopBarInteractions topBar() {
        return topBar;
    }

    @Override
    public void showPreloader() {
        preloader.show();
    }

    @Override
    public void disMissPreloader() {
        if (preloader != null) {
            preloader.dissMiss();
        }
    }

    @Override
    public int getPreloader() {
        return preloader.getVisibility();
    }

    @Override
    public void changeDrawerMenuState() {
        if (isDrawerMenuOpen) {
            drawerMenu.closeDrawer(GravityCompat.START);
        } else {
            drawerMenu.openDrawer(GravityCompat.START);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        drawerMenu.addDrawerListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        drawerMenu.removeDrawerListener(this);
    }


    //Drawer layout listener--------------------------------------------------------------------------------------
    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        isDrawerMenuOpen = true;
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        isDrawerMenuOpen = false;
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

}


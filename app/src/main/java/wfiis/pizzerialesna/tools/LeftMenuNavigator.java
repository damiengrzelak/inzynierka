package wfiis.pizzerialesna.tools;

import java.lang.ref.WeakReference;

import wfiis.pizzerialesna.fragments.LoginFragment;
import wfiis.pizzerialesna.interactions.ActivityInteractions;

public class LeftMenuNavigator {

    public static void logOut() {
        WeakReference<ActivityInteractions> mainActivityInterface = Tools.getMainActivityInterface();
        if (mainActivityInterface != null) {
            mainActivityInterface.get().navigateTo(LoginFragment.newInstance(), false);
            mainActivityInterface.get().changeDrawerMenuState();
        }
    }


}

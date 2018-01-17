package wfiis.pizzerialesna.interactions;

import wfiis.pizzerialesna.base.BaseFragment;

public interface ActivityInteractions {
    boolean navigateTo(BaseFragment fragment, boolean addToBackstack);
    boolean navigateBack();
    TopBarInteractions topBar();
}

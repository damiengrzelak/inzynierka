package wfiis.pizzerialesna.fragments.basket;

import com.inverce.mod.events.annotation.Listener;

public interface BasketInterActions extends Listener {
    void deleteFromBaseket(int position);
}

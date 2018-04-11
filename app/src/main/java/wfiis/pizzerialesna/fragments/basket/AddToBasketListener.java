package wfiis.pizzerialesna.fragments.basket;

import com.inverce.mod.events.annotation.Listener;

public interface AddToBasketListener extends Listener {
    void addToBasket(Object zamowienie, int pozycja, int ilosc);
}

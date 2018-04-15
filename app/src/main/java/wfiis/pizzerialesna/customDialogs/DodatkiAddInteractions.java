package wfiis.pizzerialesna.customDialogs;

import com.inverce.mod.events.annotation.Listener;

import java.util.ArrayList;

public interface DodatkiAddInteractions extends Listener {
    void onExtrasAdded(String dodatki, double dodatkiCena, ArrayList<String> listaDodatkow, int position);
}

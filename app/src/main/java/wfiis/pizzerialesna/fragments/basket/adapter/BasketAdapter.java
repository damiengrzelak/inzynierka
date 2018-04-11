package wfiis.pizzerialesna.fragments.basket.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.enums.PizzaStatusType;
import wfiis.pizzerialesna.model.Inne;
import wfiis.pizzerialesna.model.Obiad;
import wfiis.pizzerialesna.model.Pizza;
import wfiis.pizzerialesna.model.Salatka;
import wfiis.pizzerialesna.model.Zapiekanka;
import wfiis.pizzerialesna.tools.SpanUtils;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {
    private List<Object> zamowienie;
    private int wybranaPozycja = -1;
    private Drawable d;
    private int pos;

    public BasketAdapter(List<Object> zamowienieList, int wybranaPozycja) {
        this.zamowienie = zamowienieList;
        this.wybranaPozycja = wybranaPozycja;
    }

    @Override
    public BasketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (zamowienie.get(position) instanceof Pizza) {
            Pizza pizza = (Pizza) zamowienie.get(position);

            holder.name.setText(pizza.getNumber() + ". " + pizza.getName());
            holder.dodatki.setText("");

            if (wybranaPozycja == 0) {
                SpanUtils.on(holder.cena).convertToMoney(pizza.getPrice28());

            } else if (wybranaPozycja == 1) {
                SpanUtils.on(holder.cena).convertToMoney(pizza.getPrice34());
            } else if (wybranaPozycja == 2) {
                SpanUtils.on(holder.cena).convertToMoney(pizza.getPrice44());
            }
            if (pizza.getType() != 0) {
                holder.statusImg.setVisibility(View.VISIBLE);
                if (pizza.getType() == 1) {
                    d = PizzaStatusType.HOT.d;
                } else if (pizza.getType() == 2) {
                    d = PizzaStatusType.NEW.d;
                } else if (pizza.getType() == 3) {
                    d = PizzaStatusType.OUR.d;
                }

                holder.statusImg.setImageDrawable(d);

                holder.dodajSkladniki.setVisibility(View.VISIBLE);
            }


        } else if (zamowienie.get(position) instanceof Obiad) {
            Obiad obiad = (Obiad) zamowienie.get(position);

        } else if (zamowienie.get(position) instanceof Inne) {
            Inne inne = (Inne) zamowienie.get(position);

        } else if (zamowienie.get(position) instanceof Salatka) {
            Salatka salatka = (Salatka) zamowienie.get(position);

        } else if (zamowienie.get(position) instanceof Zapiekanka) {
            Zapiekanka zapiekanka = (Zapiekanka) zamowienie.get(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (zamowienie == null) {
            zamowienie = new ArrayList<>();
        }
        return zamowienie.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView statusImg;
        private TextView name;
        private TextView dodatki;
        private TextView cena;
        private Button dodajSkladniki, usun;

        public ViewHolder(View rootView) {
            super(rootView);
            statusImg = rootView.findViewById(R.id.item_basket_status_type);
            name = rootView.findViewById(R.id.item_basket_name);
            dodatki = rootView.findViewById(R.id.item_basket_content);
            cena = rootView.findViewById(R.id.item_basket_price);
            dodajSkladniki = rootView.findViewById(R.id.item_basket_dodaj_skladniki);
            usun = rootView.findViewById(R.id.item_basket_usun);
        }
    }
}
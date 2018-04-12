package wfiis.pizzerialesna.fragments.basket.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.inverce.mod.core.IM;
import com.inverce.mod.events.Event;

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.customDialogs.DodatkiDoPizzyDialog;
import wfiis.pizzerialesna.enums.PizzaStatusType;
import wfiis.pizzerialesna.fragments.basket.BasketInterActions;
import wfiis.pizzerialesna.model.Basket;
import wfiis.pizzerialesna.model.Extras;
import wfiis.pizzerialesna.model.Inne;
import wfiis.pizzerialesna.model.Obiad;
import wfiis.pizzerialesna.model.Pizza;
import wfiis.pizzerialesna.model.Salatka;
import wfiis.pizzerialesna.model.Zapiekanka;
import wfiis.pizzerialesna.tools.SpanUtils;

import static wfiis.pizzerialesna.Cfg.basket_table;
import static wfiis.pizzerialesna.Cfg.pizza_extras_table;
import static wfiis.pizzerialesna.Cfg.users_table;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {
    private List<Object> zamowienie;
    private List<Extras> dodatkiList;
    private int wybranaPozycja = -1;
    private Drawable d;
    private int pos;

    private FirebaseUser mAuth;
    private DatabaseReference ref;

    public BasketAdapter(List<Object> zamowienieList, int wybranaPozycja, List<Extras> dodatkiList) {
        if (zamowienieList.size() <= 0) {
            zamowienieList = new ArrayList<>();
        }

        this.zamowienie = zamowienieList;
        this.wybranaPozycja = wybranaPozycja;

        this.dodatkiList = dodatkiList;

        downloadDodatki();
    }

    private void downloadDodatki() {
        dodatkiList = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child(pizza_extras_table);
        Query extrasQuerry = ref.orderByChild("number");
        extrasQuerry.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Extras e = singleSnapshot.getValue(Extras.class);
                    if (e != null) {
                        dodatkiList.add(new Extras(e.getHighPrice(), e.getLowPrice(), e.getMediumPrice(), e.getName(), e.getNumber(), e.getVariants()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public BasketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Basket order = (Basket) zamowienie.get(position);
        holder.dodajSkladniki.setVisibility(order.getIsPizza() ? View.VISIBLE : View.INVISIBLE);

        holder.name.setText(setOrderName(order));
        holder.dodatki.setText(setOrderDodatki(order));
        SpanUtils.on(holder.cena).convertToMoney(order.getPrize());

        if (order.getType() != 0) {
            holder.statusImg.setVisibility(View.VISIBLE);
            if (order.getType() == 1) {
                d = PizzaStatusType.HOT.d;
            } else if (order.getType() == 2) {
                d = PizzaStatusType.NEW.d;
            } else if (order.getType() == 3) {
                d = PizzaStatusType.OUR.d;
            }

            holder.statusImg.setImageDrawable(d);
        } else {
            holder.statusImg.setVisibility(View.GONE);
        }

        holder.usun.setOnClickListener(view -> usunProdukt(order, position));

        holder.dodajSkladniki.setOnClickListener(view -> dodajSkladnikiDoProduktu(position));
    }

    private String setOrderName(Basket order) {
        String name = "";
        if (!order.getIsPizza()) {
            name = order.getNumber() + ". " + order.getName();
        } else {
            if (order.getSize().toLowerCase().contains("min")) {
                name = order.getNumber() + ". " + order.getName() + " - mini";
            } else if (order.getSize().toLowerCase().contains("mał")) {
                name = order.getNumber() + ". " + order.getName() + " - mała";
            } else if (order.getSize().toLowerCase().contains("śre")) {
                name = order.getNumber() + ". " + order.getName() + " - średnia";
            }
        }
        return name;
    }

    private String setOrderDodatki(Basket order) {
        String dodatki = "";

        if (!order.getIsPizza()) {
            if (order.getMeat() != null) {
                dodatki = "Mięso: " + order.getMeat();
            }
        } else {
            //TODO WHEN DODATKI ADDED
        }
        return dodatki;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (zamowienie.size() <= 0) {
            zamowienie = new ArrayList<>();
        }
        return zamowienie.size();
    }

    public void updateAdapter(List<Object> objectList, List<Extras> dodatkiList) {
        if (objectList.size() > 0) {
            zamowienie = new ArrayList<>();
            this.zamowienie = objectList;
            this.dodatkiList = dodatkiList;
            notifyDataSetChanged();
        }
    }

    private void usunProdukt(Basket order, int position) {
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(users_table);
        Query userBasketProductQuerry = ref.child(mAuth.getUid()).child(basket_table);
        userBasketProductQuerry.getRef().child(order.getKey()).removeValue();

        notifyItemRemoved(position);
        Event.Bus.post(BasketInterActions.class).deleteFromBaseket(position);
    }

    private void dodajSkladnikiDoProduktu(int position) {
        Basket produkt = (Basket) zamowienie.get(position);
        String rozmiar = produkt.getSize();
        int size = 0;

        if (rozmiar.toLowerCase().contains("min")) {
            size = 0;
        } else if (rozmiar.toLowerCase().contains("mał")) {
            size = 1;
        } else if (rozmiar.toLowerCase().contains("śre")) {
            size = 2;
        }

        DodatkiDoPizzyDialog dodatkiDialog = new DodatkiDoPizzyDialog(dodatkiList, size);
        dodatkiDialog.show(IM.activity().getFragmentManager(), IM.activity().getFragmentManager().getClass().toString());
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
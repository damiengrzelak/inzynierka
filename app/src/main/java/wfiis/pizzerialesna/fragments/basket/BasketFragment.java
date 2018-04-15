package wfiis.pizzerialesna.fragments.basket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.inverce.mod.events.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.customDialogs.DodatkiAddInteractions;
import wfiis.pizzerialesna.fragments.basket.adapter.BasketAdapter;
import wfiis.pizzerialesna.fragments.delivery.DeliveryFragment;
import wfiis.pizzerialesna.model.Basket;
import wfiis.pizzerialesna.model.Extras;

import static wfiis.pizzerialesna.Cfg.basket_table;
import static wfiis.pizzerialesna.Cfg.pizza_extras_table;
import static wfiis.pizzerialesna.Cfg.users_table;

public class BasketFragment extends BaseFragment implements BasketInterActions, DodatkiAddInteractions {
    private TextView emptyBasketText;
    private RecyclerView recyclerView;
    private BasketAdapter adapter;

    private List<Object> zamowienieList = new ArrayList<>();
    private Button goNext;
    private int wybranaPozycja = -1;


    private String dodatkiText = "";
    private double dodatkiValue = 0.0;
    private ArrayList<String> arrayList = new ArrayList<>();

    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


    private List<Extras> dodatkiList = new ArrayList<>();


    public static BasketFragment newInstance() {
        return new BasketFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        assert getActions() != null;

        findViews(view);
        prepareRecycler();
        getUserBasket();
        downloadDodatki();

        getActions().topBar().showBackIcon(true);
        getActions().topBar().showMenuIcon(false);
        getActions().topBar().showBasketIcon(false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Event.Bus.register(BasketInterActions.class, this);
        Event.Bus.register(DodatkiAddInteractions.class, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Event.Bus.unregister(BasketInterActions.class, this);
        Event.Bus.unregister(DodatkiAddInteractions.class, this);
    }

    private void getUserBasket() {
        ref = ref.child(users_table);
        Query userBasketQuerry = ref.child(mAuth.getUid()).child(basket_table);
        showPreloader();
        userBasketQuerry.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Basket item = new Basket();
                    Basket basket = singleSnapshot.getValue(Basket.class);
                    if (dataSnapshot != null && basket != null) {
                        item.setSize(basket.getSize());
                        item.setType(basket.getType());
                        item.setIsPizza(basket.getIsPizza());
                        item.setPrize(basket.getPrize());
                        item.setNumber(basket.getNumber());
                        item.setName(basket.getName());
                        item.setIngredients(basket.getIngredients());
                        item.setKey(singleSnapshot.getKey());
                        item.setMeat(basket.getMeat());
                        item.setPriceIngredients(basket.getPriceIngredients());
                        zamowienieList.add(item);

                    }
                }
                adapter.updateAdapter(zamowienieList, dodatkiList);
                adapter.notifyDataSetChanged();
                recyclerView.invalidate();
                prepareView();
                dissMissPreloader();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                android.util.Log.e("ERROR", "onCancelled", databaseError.toException());
                dissMissPreloader();
            }
        });


    }

    private void findViews(View view) {
        emptyBasketText = view.findViewById(R.id.basket_empty);
        recyclerView = view.findViewById(R.id.basket_recycler);
        goNext = view.findViewById(R.id.basket_go_next);


        goNext.setOnClickListener(v -> {
            double sum = calculateAmount();
            getActions().navigateTo(DeliveryFragment.newInstance(sum), true);
        });
    }

    private double calculateAmount() {
        double sumOfPrizes = 0.0;
        for (Object b : zamowienieList) {
            Basket basket = (Basket) b;
            sumOfPrizes += basket.getPrize();
            if (basket.getPriceIngredients() > 0.0) {
                sumOfPrizes += basket.getPriceIngredients();
            }
        }
        return sumOfPrizes;
    }

    private void prepareRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new BasketAdapter(zamowienieList, wybranaPozycja, dodatkiList);
        recyclerView.setAdapter(adapter);

        recyclerView.refreshDrawableState();

        getUserBasket();
        adapter.notifyDataSetChanged();
    }

    private void prepareView() {
        if (adapter.getItemCount() <= 0) {
            emptyBasketText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            goNext.setVisibility(View.GONE);
        } else {
            emptyBasketText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            goNext.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void deleteFromBaseket(int position) {
        getUserBasket();
        zamowienieList.remove(position);
        adapter.notifyDataSetChanged();
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
    public void onExtrasAdded(String dodatki, double dodatkiCena, ArrayList<String> listaDodatkow, int position) {
        dodatkiText = dodatki;
        dodatkiValue = dodatkiCena;
        arrayList = listaDodatkow;

        updateZamowienie(arrayList, arrayList, dodatkiValue, position);
        adapter.updateChosenExtras(dodatkiText, dodatkiValue, arrayList, position);
        adapter.notifyDataSetChanged();
    }

    private void updateZamowienie(ArrayList<String> list, ArrayList<String> arrayList, double dodatkiValue, int position) {
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        Basket orderKey = (Basket) zamowienieList.get(position);
        ref = ref.child(users_table).child(mAuth.getUid()).child(basket_table).child(orderKey.getKey());


        ref.child("ingredients/").removeValue();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> dodatki = new HashMap<>();
            dodatki.put(String.valueOf(i), list.get(i));
            ref.child("ingredients/").updateChildren(dodatki);
        }
        Map<String, Object> dodatkiCena = new HashMap<>();
        if (dodatkiValue > 0.0) {
            dodatkiCena.put("priceIngredients", dodatkiValue);
        } else {
            dodatkiCena.put("priceIngredients", 0);
        }
        ref.updateChildren(dodatkiCena);
        adapter.notifyDataSetChanged();
    }
}

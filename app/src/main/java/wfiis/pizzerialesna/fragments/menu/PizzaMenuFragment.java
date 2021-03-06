package wfiis.pizzerialesna.fragments.menu;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.fragments.menu.adapters.PizzaMenuAdapter;
import wfiis.pizzerialesna.model.Extras;
import wfiis.pizzerialesna.model.Pizza;

import static wfiis.pizzerialesna.Cfg.pizza_extras_table;
import static wfiis.pizzerialesna.Cfg.pizza_table;
import static wfiis.pizzerialesna.Cfg.special_table;

public class PizzaMenuFragment extends BaseFragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private PizzaMenuAdapter pizzaMenuAdapter;
    private TextView pizzes;
    private TextView special;
    private TextView extras;

    private List<Object> pizzaList;

    public static PizzaMenuFragment newInstance() {
        return new PizzaMenuFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza_menu, container, false);
        assert getActions() != null;

        findViews(view);
        getPizzas();
        prepareList();
        setListeners();


        getActions().topBar().showBackIcon(false);
        getActions().topBar().showMenuIcon(false);
        getActions().topBar().showBasketIcon(true);
        return view;
    }

    private void getPizzas() {
        pizzaList = new ArrayList<>();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child(pizza_table);
        Query pizzaQuery = ref;
        showPreloader();
        pizzaQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Pizza p = singleSnapshot.getValue(Pizza.class);
                    pizzaList.add(new Pizza(p.getName(), p.getIngredients(), p.getNumber(), p.getPrice28(), p.getPrice34(), p.getPrice44(), p.getType()));
                    pizzaMenuAdapter.notifyDataSetChanged();
                }
                dissMissPreloader();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
                dissMissPreloader();
            }
        });
    }

    private void getSpecial() {
        pizzaList = new ArrayList<>();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child(special_table);
        Query pizzaQuery = ref;
        showPreloader();
        pizzaQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Pizza p = singleSnapshot.getValue(Pizza.class);
                    pizzaList.add(new Pizza(p.getName(), p.getIngredients(), p.getNumber(), p.getPrice28(), p.getPrice34(), p.getPrice44(), p.getType()));
                    pizzaMenuAdapter.notifyDataSetChanged();
                }
                dissMissPreloader();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
                dissMissPreloader();
            }
        });
    }

    private void getExtras() {
        pizzaList.clear();
        pizzaList = new ArrayList<>();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child(pizza_extras_table);
        Query extrasQuerry = ref.orderByChild("number");
        showPreloader();
        extrasQuerry.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Extras e = singleSnapshot.getValue(Extras.class);
                    pizzaList.add(new Extras(e.getHighPrice(), e.getLowPrice(), e.getMediumPrice(), e.getName(), e.getNumber(), e.getVariants()));
                    pizzaMenuAdapter.notifyDataSetChanged();
                }
                dissMissPreloader();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
                dissMissPreloader();
            }
        });
    }

    private void prepareList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        pizzaMenuAdapter = new PizzaMenuAdapter(pizzaList);
        recyclerView.setAdapter(pizzaMenuAdapter);
    }

    private void setListeners() {
        pizzes.setOnClickListener(this);
        special.setOnClickListener(this);
        extras.setOnClickListener(this);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.fragment_pizza_menu_rv);
        pizzes = view.findViewById(R.id.fragment_pizza_menu_pizzes);
        special = view.findViewById(R.id.fragment_pizza_menu_special);
        extras = view.findViewById(R.id.fragment_pizza_menu_extras);

        //start selection color
        pizzes.setBackgroundColor(getResources().getColor(R.color.gray_border));
        special.setBackgroundColor(getResources().getColor(R.color.white));
        extras.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.fragment_pizza_menu_pizzes:
                getPizzas();
                pizzaMenuAdapter.updateAdapter(pizzaList);
                pizzaMenuAdapter.notifyDataSetChanged();
                special.setBackgroundColor(getResources().getColor(R.color.white));
                pizzes.setBackgroundColor(getResources().getColor(R.color.gray_border));
                extras.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.fragment_pizza_menu_special:
                getSpecial();
                pizzaMenuAdapter.updateAdapter(pizzaList);
                pizzaMenuAdapter.notifyDataSetChanged();
                pizzes.setBackgroundColor(getResources().getColor(R.color.white));
                special.setBackgroundColor(getResources().getColor(R.color.gray_border));
                extras.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.fragment_pizza_menu_extras:
                getExtras();
                pizzaMenuAdapter.updateAdapter(pizzaList);
                pizzaMenuAdapter.notifyDataSetChanged();
                special.setBackgroundColor(getResources().getColor(R.color.white));
                pizzes.setBackgroundColor(getResources().getColor(R.color.white));
                extras.setBackgroundColor(getResources().getColor(R.color.gray_border));
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause(){
        super.onPause();
        dissMissPreloader();
    }
}

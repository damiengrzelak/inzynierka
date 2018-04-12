package wfiis.pizzerialesna.fragments.basket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.fragments.basket.adapter.BasketAdapter;
import wfiis.pizzerialesna.model.Basket;

import static wfiis.pizzerialesna.Cfg.basket_table;
import static wfiis.pizzerialesna.Cfg.users_table;

public class BasketFragment extends BaseFragment {
    private TextView emptyBasketText;
    private RecyclerView recyclerView;
    private BasketAdapter adapter;

    private List<Object> zamowienieList = new ArrayList<>();
    private Button goNext;
    private int wybranaPozycja = -1;


    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

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
        getActions().topBar().showBackIcon(true);
        getActions().topBar().showMenuIcon(false);
        getActions().topBar().showBasketIcon(false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
                adapter.updateAdapter(zamowienieList);
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

    }

    private void prepareRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new BasketAdapter(zamowienieList, wybranaPozycja);
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
}

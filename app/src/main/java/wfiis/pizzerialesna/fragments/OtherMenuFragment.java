package wfiis.pizzerialesna.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import wfiis.pizzerialesna.fragments.adapters.OtherMenuAdapter;
import wfiis.pizzerialesna.fragments.adapters.SalatMenuAdapter;
import wfiis.pizzerialesna.fragments.adapters.ZapiekankaMenuAdapter;
import wfiis.pizzerialesna.model.Obiad;
import wfiis.pizzerialesna.model.Salatka;
import wfiis.pizzerialesna.model.Zapiekanka;

import static wfiis.pizzerialesna.Cfg.obiad_table;
import static wfiis.pizzerialesna.Cfg.salatki_table;
import static wfiis.pizzerialesna.Cfg.zapiekanki_table;

public class OtherMenuFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private OtherMenuAdapter otherMenuAdapter;

    private RecyclerView salatRV;
    private SalatMenuAdapter salatMenuAdapter;

    private RecyclerView zapRV;
    private ZapiekankaMenuAdapter zapiekankaMenuAdapter;

    private List<Obiad> otherList;
    private List<Zapiekanka> zapiekankaList;
    private List<Salatka> salatkaList;

    public static OtherMenuFragment newInstance() {
        return new OtherMenuFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_menu, container, false);
        assert getActions() != null;

        findViews(view);
        getOtherMenu();
        prepareList();
        setListeners();


        getActions().topBar().showBackIcon(false);
        return view;
    }

    private void getOtherMenu() {
       otherList = new ArrayList<>();
       zapiekankaList = new ArrayList<>();
       salatkaList = new ArrayList<>();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child(obiad_table);
        DatabaseReference ref2 = database.child(zapiekanki_table);
        DatabaseReference ref3 = database.child(salatki_table);
        Query obiadQuerry = ref;
        Query zapiekankaQuerry = ref2;
        Query salatkaQuerry = ref3;

        obiadQuerry.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Obiad o = singleSnapshot.getValue(Obiad.class);
                    otherList.add(new Obiad(o.getName(), o.getIngredients(), o.getPrice(), o.getType()));
                    otherMenuAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
            }
        });

        salatkaQuerry.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Salatka s = singleSnapshot.getValue(Salatka.class);
                    salatkaList.add(new Salatka(s.getName(), s.getIngredients(), s.getPrice(), s.getType()));
                    salatMenuAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
            }
        });

        zapiekankaQuerry.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Zapiekanka z = singleSnapshot.getValue(Zapiekanka.class);
                    zapiekankaList.add(new Zapiekanka(z.getName(), z.getPrice(), z.getType()));
                    zapiekankaMenuAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
            }
        });
    }


    private void prepareList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        salatRV.setLayoutManager(linearLayoutManager2);
        //
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext());
        linearLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        zapRV.setLayoutManager(linearLayoutManager3);

        otherMenuAdapter = new OtherMenuAdapter(otherList);
        recyclerView.setAdapter(otherMenuAdapter);
        //
        salatMenuAdapter = new SalatMenuAdapter(salatkaList);
        salatRV.setAdapter(salatMenuAdapter);
        //
        zapiekankaMenuAdapter = new ZapiekankaMenuAdapter(zapiekankaList);
        zapRV.setAdapter(zapiekankaMenuAdapter);
    }

    private void setListeners() {
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.fragment_other_menu_rv);
        salatRV = view.findViewById(R.id.fragment_other_menu_rv_salat);
        zapRV = view.findViewById(R.id.fragment_other_menu_rv_zap);
    }
}

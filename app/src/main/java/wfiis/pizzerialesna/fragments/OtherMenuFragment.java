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
import wfiis.pizzerialesna.model.Obiad;
import wfiis.pizzerialesna.model.OtherMenuItems;
import wfiis.pizzerialesna.model.Salatka;
import wfiis.pizzerialesna.model.Zapiekanka;

import static wfiis.pizzerialesna.Cfg.obiad_table;
import static wfiis.pizzerialesna.Cfg.salatki_table;
import static wfiis.pizzerialesna.Cfg.zapiekanki_table;

public class OtherMenuFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private OtherMenuAdapter otherMenuAdapter;

    private List<OtherMenuItems> otherMenuItemsList = new ArrayList<>();
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
                    //otherList.add(new Obiad(o.getName(), o.getIngredients(), o.getPrice(), o.getType()));
                    otherMenuItemsList.add(new OtherMenuItems(new Obiad(o.getName(), o.getIngredients(), o.getPrice(), o.getType()), OtherMenuItems.OTHER_TYPE));
                    otherMenuAdapter.notifyDataSetChanged();
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
                    //zapiekankaList.add(new Zapiekanka(z.getName(), z.getPrice(), z.getType()));
                    otherMenuItemsList.add(new OtherMenuItems(new Zapiekanka(z.getName(), z.getPrice(), z.getType()), OtherMenuItems.ZAP_TYPE));
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
                    otherMenuItemsList.add(new OtherMenuItems(new Salatka(s.getName(), s.getIngredients(), s.getPrice(), s.getType()), OtherMenuItems.SAL_TYPE));
                    otherMenuAdapter.notifyDataSetChanged();
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

        //otherMenuAdapter = new OtherMenuAdapter(otherList, zapiekankaList, salatkaList);
        otherMenuAdapter = new OtherMenuAdapter(otherMenuItemsList);
        recyclerView.setAdapter(otherMenuAdapter);
    }

    private void setListeners() {
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.fragment_other_menu_rv);
    }
}

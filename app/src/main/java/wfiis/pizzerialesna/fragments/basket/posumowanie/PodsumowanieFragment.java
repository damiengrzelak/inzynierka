package wfiis.pizzerialesna.fragments.basket.posumowanie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.model.BasketInformation;

import static wfiis.pizzerialesna.Cfg.basket_info_table;
import static wfiis.pizzerialesna.Cfg.basket_table;
import static wfiis.pizzerialesna.Cfg.users_table;

public class PodsumowanieFragment extends BaseFragment implements OnGetDataListener {
    private static final String CENA = "CENA";

    public TextView kosztZamowienia, tel, city, miasto;

    private BasketInformation basketInformation = new BasketInformation();

    public static PodsumowanieFragment newInstance() {
        return new PodsumowanieFragment();
    }

    public static PodsumowanieFragment newInstance(String s) {
        PodsumowanieFragment podsumowanieFragment = new PodsumowanieFragment();
        Bundle args = new Bundle();
        args.putString(CENA, s);
        podsumowanieFragment.setArguments(args);
        return podsumowanieFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podsumowanie, container, false);
        assert getActions() != null;

        showPreloader();
        mReadDataOnce(this);
        findViews(view);
        prepareRecycler();
        fillViews();

        getActions().topBar().showBackIcon(true);
        getActions().topBar().showMenuIcon(false);
        getActions().topBar().showBasketIcon(false);
        return view;
    }

    public BasketInformation getBasketInfo() {
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        BasketInformation basketInformation = new BasketInformation();

        ref = ref.child(users_table);
        Query userBasketQuerry = ref.child(mAuth.getUid()).child(basket_table).child(basket_info_table);
        showPreloader();
        userBasketQuerry.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    BasketInformation basket = singleSnapshot.getValue(BasketInformation.class);
                    if (dataSnapshot != null && basket != null) {
                        basketInformation.setPhone(basket.getPhone());
                        basketInformation.setFlatNumber(basket.getFlatNumber());
                        basketInformation.setZipCode(basket.getZipCode());
                        basketInformation.setStreet(basket.getStreet());
                        basketInformation.setCity(basket.getCity());
                    }
                }
                dissMissPreloader();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                android.util.Log.e("ERROR", "onCancelled", databaseError.toException());
                dissMissPreloader();
            }
        });
        return basketInformation;
    }

    private void prepareRecycler() {
    }

    private void findViews(View view) {
        kosztZamowienia = view.findViewById(R.id.zamowienie_koszt);
        city = view.findViewById(R.id.zamowienie_dostawa_city);
        miasto = view.findViewById(R.id.zamowienie_dostawa_miasto);
        tel = view.findViewById(R.id.zamowienie_dostawa_tel);
    }

    private void fillViews() {
        kosztZamowienia.setText(getArguments().getString(CENA));

        city.setText(basketInformation.getCity());

    }

    public void mReadDataOnce(final OnGetDataListener listener) {
        listener.onStart();
        FirebaseUser mAuth;
        DatabaseReference ref;
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(users_table);
        Query userBasketProductQuerry = ref.child(mAuth.getUid()).child(basket_table);

        userBasketProductQuerry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }

    @Override
    public void onSuccess(DataSnapshot data) {
        basketInformation = data.getValue(BasketInformation.class);
        dissMissPreloader();
    }

    @Override
    public void onFailed(DatabaseError databaseError) {

    }
}

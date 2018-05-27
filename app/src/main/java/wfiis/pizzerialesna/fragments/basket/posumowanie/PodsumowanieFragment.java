package wfiis.pizzerialesna.fragments.basket.posumowanie;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.inverce.mod.core.IM;

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.customViews.TopToast;
import wfiis.pizzerialesna.fragments.HomeFragment;
import wfiis.pizzerialesna.model.Basket;
import wfiis.pizzerialesna.model.BasketInformation;

import static wfiis.pizzerialesna.Cfg.basket_info_table;
import static wfiis.pizzerialesna.Cfg.basket_table;
import static wfiis.pizzerialesna.Cfg.users_table;

public class PodsumowanieFragment extends BaseFragment {
    private static final String CENA = "CENA";

    public TextView kosztZamowienia, tel, city, miasto;

    private BasketInformation basketInformation = new BasketInformation();
    private RecyclerView recyclerView;
    private OrderListAdapter adapter;
    private List<Basket> basketList = new ArrayList<>();
    private Button zlozZamowienie;

    private CardView adressContainer;
    private TextView obiorosobitytext;

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
        getBasketInfo();
        findViews(view);
        prepareRecycler();
        fillViews();
        setlisteners();

        getActions().topBar().showBackIcon(true);
        getActions().topBar().showMenuIcon(false);
        getActions().topBar().showBasketIcon(false);
        return view;
    }

    private void setlisteners() {
        zlozZamowienie.setOnClickListener(view -> {
            String phoneNumber = "781233692";
            String message = "";
            String zamowienie = "";
            String dodatki = "";
            double cena = 0.0;

            if (basketInformation.isPersonal()) {
                message = "Odbiór osobisty:\n" + "nr: " + basketInformation.getPhone() + "\nzamówienie:\n";
            } else {
                message = "Dostawa:\n" + "nr: " + basketInformation.getPhone() + "\nadres: " + basketInformation.getStreet() + " " + basketInformation.getFlatNumber() +
                        "\n" + basketInformation.getCity() + " " + basketInformation.getZipCode() + "\nzamówienie:\n";
            }

            for (int i = 0; i < basketList.size(); i++) {
                if (basketList.get(i).getIsPizza()) {
                    cena = cena + basketList.get(i).getPrize();
                    zamowienie = "pizza nr: " + basketList.get(i).getNumber();
                    if (basketList.get(i).getIngredients() != null && basketList.get(i).getIngredients().size() > 0) {
                        cena += basketList.get(i).getPriceIngredients();
                        dodatki = " + ";
                        for (int j = 0; j < basketList.get(i).getIngredients().size(); j++) {
                            if (j == basketList.get(i).getIngredients().size() - 1) {
                                dodatki = dodatki + basketList.get(i).getIngredients().get(j) + "\n";
                            } else {
                                dodatki = dodatki + basketList.get(i).getIngredients().get(j) + " + ";
                            }
                        }
                    }
                    message = message + zamowienie + dodatki;
                } else {
                    cena += basketList.get(i).getPrize();
                    if (basketList.get(i).getMeat() != null) {
                        message += basketList.get(i).getName() + " " + basketList.get(i).getMeat() + "\n";
                    } else {
                        message += basketList.get(i).getName() + "\n";
                    }
                }
            }

            message += "\nsuma: " + String.valueOf(cena) + " zł";

            if (!basketInformation.isPersonal()) {
                if (basketInformation.isInCache()) {
                    message += "\npłatność: gotówka";
                } else {
                    message += "\npłatność: karta płatnicza";
                }
            }


            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(message);
            if (ActivityCompat.checkSelfPermission(IM.context(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                try {
                    smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
                    TopToast.show("Zamówienie zostało złożone", TopToast.TYPE_SUCCESS, TopToast.DURATION_SHORT);
                    getActions().navigateTo(HomeFragment.newInstance(), false);
                    clearBackStack();
                    removeBasketFromDb();
                } catch (Exception ErrVar) {
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
                }
            }


        });
    }

    private void clearBackStack() {
        FragmentManager fm = IM.activitySupport().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    private void removeBasketFromDb() {
        FirebaseUser mAuth;
        DatabaseReference ref;

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();

        ref = ref.child(users_table);
        Query userBasketProductQuerry = ref.child(mAuth.getUid()).child(basket_table);
        userBasketProductQuerry.getRef().removeValue();

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(users_table);
        Query userDeliveryProductQuerry = ref.child(mAuth.getUid()).child(basket_info_table);
        userDeliveryProductQuerry.getRef().removeValue();
    }

    public void getBasketInfo() {
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref = ref.child(users_table);
        Query userDeliveryQuerry = ref.child(mAuth.getUid()).child(basket_info_table);
        showPreloader();
        userDeliveryQuerry.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BasketInformation basket = dataSnapshot.getValue(BasketInformation.class);
                if (dataSnapshot != null && basket != null) {
                    basketInformation.setPhone(basket.getPhone());
                    basketInformation.setFlatNumber(basket.getFlatNumber());
                    basketInformation.setZipCode(basket.getZipCode());
                    basketInformation.setStreet(basket.getStreet());
                    basketInformation.setCity(basket.getCity());
                    basketInformation.setInCache(basket.isInCache());
                    basketInformation.setPersonal(basket.isPersonal());
                }
                fillViews();
                dissMissPreloader();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                android.util.Log.e("ERROR", "onCancelled", databaseError.toException());
                dissMissPreloader();
            }
        });

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child(users_table);
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
                        basketList.add(item);
                    }
                }
                adapter.updateAdapter(basketList);
                adapter.notifyDataSetChanged();
                recyclerView.invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                android.util.Log.e("ERROR", "onCancelled", databaseError.toException());
                dissMissPreloader();
            }
        });

    }

    private void prepareRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new OrderListAdapter(basketList);
        recyclerView.setAdapter(adapter);

        recyclerView.refreshDrawableState();

        adapter.notifyDataSetChanged();
    }

    private void findViews(View view) {
        kosztZamowienia = view.findViewById(R.id.zamowienie_koszt);
        city = view.findViewById(R.id.zamowienie_dostawa_city);
        miasto = view.findViewById(R.id.zamowienie_dostawa_miasto);
        tel = view.findViewById(R.id.zamowienie_dostawa_tel);
        recyclerView = view.findViewById(R.id.podsumowanie_recycler);
        zlozZamowienie = view.findViewById(R.id.zloz_zamowienie);
        adressContainer = view.findViewById(R.id.zamowieni_adrres_container);
        obiorosobitytext = view.findViewById(R.id.obiorsobisttekst);
    }

    private void fillViews() {
        kosztZamowienia.setText(getArguments().getString(CENA));

        if (!basketInformation.isPersonal()) {
            city.setText(basketInformation.getStreet() + " " + basketInformation.getFlatNumber());
            miasto.setText(basketInformation.getCity() + " " + basketInformation.getZipCode());
            tel.setText("tel:" + basketInformation.getPhone());
            obiorosobitytext.setVisibility(View.GONE);
            adressContainer.setVisibility(View.VISIBLE);
        } else {
            obiorosobitytext.setVisibility(View.VISIBLE);
            adressContainer.setVisibility(View.GONE);
        }
    }
}

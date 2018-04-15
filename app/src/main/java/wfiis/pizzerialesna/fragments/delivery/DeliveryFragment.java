package wfiis.pizzerialesna.fragments.delivery;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.customViews.InputEditTextView;
import wfiis.pizzerialesna.customViews.ZipCodeListener;
import wfiis.pizzerialesna.model.Basket;
import wfiis.pizzerialesna.model.BasketInformation;
import wfiis.pizzerialesna.model.User;
import wfiis.pizzerialesna.tools.AppendMessage;
import wfiis.pizzerialesna.tools.SpanUtils;
import wfiis.pizzerialesna.tools.Util;
import wfiis.pizzerialesna.validation.Validator;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static wfiis.pizzerialesna.Cfg.basket_info_table;
import static wfiis.pizzerialesna.Cfg.basket_table;
import static wfiis.pizzerialesna.Cfg.users_table;

public class DeliveryFragment extends BaseFragment implements View.OnClickListener {
    private static final String SUM_OF_PRIZE = "SUM_OF_PRIZE";
    private TextView cena, info, cenaMore;
    private Button goNext;

    private TextView odbior, dostawa;
    private LinearLayout odbiorClick, dostawaClick;

    private boolean isOdbiorOsobisty = true;
    private boolean isCardPayment = false;


    private CardView platnoscContainer;
    private TextView gotwka, karta;
    private LinearLayout gotowkaClick, kartaClick;


    private InputEditTextView ulica, nrDom, city, tel, zip;
    private TextView adresText;


    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private boolean isValid;

    private BasketInformation basketInformation = new BasketInformation();

    public static DeliveryFragment newInstance(double sum) {
        DeliveryFragment deliveryFragment = new DeliveryFragment();
        Bundle args = new Bundle();
        args.putDouble(SUM_OF_PRIZE, sum);
        deliveryFragment.setArguments(args);
        return deliveryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);
        assert getActions() != null;

        findUser();
        findViews(view);
        setListener();
        fillViews();

        getActions().topBar().showBackIcon(true);
        getActions().topBar().showMenuIcon(false);
        getActions().topBar().showBasketIcon(false);
        return view;
    }

    private void findViews(View view) {
        cena = view.findViewById(R.id.delivery_cena);
        info = view.findViewById(R.id.delivery_delivery_info);
        odbior = view.findViewById(R.id.delivery_osobisty);
        dostawa = view.findViewById(R.id.delivery_dostawa);
        odbiorClick = view.findViewById(R.id.delivery_osobisty_container);
        dostawaClick = view.findViewById(R.id.delivery_dostawa_container);
        platnoscContainer = view.findViewById(R.id.delivery_platnosc_container);
        gotwka = view.findViewById(R.id.delivery_gotowka);
        karta = view.findViewById(R.id.delivery_karta);
        gotowkaClick = view.findViewById(R.id.delivery_gotowka_container);
        kartaClick = view.findViewById(R.id.delivery_karta_container);
        cenaMore = view.findViewById(R.id.delivery_cena_dostawa);


        Typeface tf = Typeface.createFromAsset(IM.context().getAssets(), "fonts/fontawesome-webfont.ttf");
        info.setTypeface(tf);
        odbior.setTypeface(tf);
        dostawa.setTypeface(tf);
        gotwka.setTypeface(tf);
        karta.setTypeface(tf);

        goNext = view.findViewById(R.id.delivery_go_next);

        adresText = view.findViewById(R.id.delivery_title_adress);
        ulica = view.findViewById(R.id.delivery_street);
        nrDom = view.findViewById(R.id.delivery_nr_domu);
        city = view.findViewById(R.id.delivery_city);
        tel = view.findViewById(R.id.delivery_tel);
        zip = view.findViewById(R.id.delivery_zip_code);
    }

    private void fillViews() {
        platnoscContainer.setVisibility(GONE);
        cena.setText("Koszt: " + String.valueOf(Util.decimPlace(getArguments().getDouble(SUM_OF_PRIZE), 2)) + IM.context().getResources().getString(R.string.zl));
        setDeliveryVisibility(false);
        findUser();
    }

    public void setDeliveryVisibility(boolean isVisible) {
        int visibility = (isVisible) ? View.VISIBLE : GONE;
        adresText.setVisibility(visibility);
        ulica.setVisibility(visibility);
        nrDom.setVisibility(visibility);
        city.setVisibility(visibility);
        tel.setVisibility(visibility);
        zip.setVisibility(visibility);
    }

    public void findUser() {
        ref = ref.child(users_table);
        Query userQuerry = ref.child(mAuth.getUid());

        userQuerry.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User u = dataSnapshot.getValue(User.class);

                if (u != null) {
                    ulica.setText(u.getStreet());
                    nrDom.setText(u.getFlatNumber());
                    city.setText(u.getCity());
                    tel.setText(u.getPhone());
                    zip.setText(u.getZipCode());

                    basketInformation = new BasketInformation();
                    basketInformation.setCity(u.getCity());
                    basketInformation.setStreet(u.getStreet());
                    basketInformation.setFlatNumber(u.getFlatNumber());
                    basketInformation.setInCache(true);
                    basketInformation.setPersonal(true);
                    basketInformation.setPhone(u.getPhone());
                    basketInformation.setZipCode(u.getZipCode());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
            }
        });
    }

    public void setListener() {
        zip.getEdit().addTextChangedListener(new ZipCodeListener(zip.getEdit()));
        odbiorClick.setOnClickListener(this);
        dostawaClick.setOnClickListener(this);

        gotowkaClick.setOnClickListener(this);
        kartaClick.setOnClickListener(this);

        goNext.setOnClickListener(this::podsumowanie);
    }

    private void podsumowanie(View view) {
        validate();
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        if (isValid) {
            BasketInformation basketInformation = new BasketInformation();

            if (isOdbiorOsobisty) {
                getUserData();
            } else {
                basketInformation.setCity(city.getText());
                basketInformation.setStreet(ulica.getText());
                basketInformation.setZipCode(zip.getText());
                basketInformation.setPersonal(false);
                basketInformation.setInCache(!isCardPayment);
                basketInformation.setFlatNumber(nrDom.getText());
                basketInformation.setPhone(tel.getText());
            }
            Map<String, Object> delivery = new HashMap<>();

            delivery.put("basketInformation", basketInformation);
            ref.child(users_table).child(mAuth.getUid()).updateChildren(delivery);
        }
    }

    private void getUserData() {
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(users_table);
        Query userQuerry = ref.child(mAuth.getUid());
        basketInformation = new BasketInformation();
        userQuerry.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                basketInformation.setCity(u.getCity());
                basketInformation.setStreet(u.getStreet());
                basketInformation.setFlatNumber(u.getFlatNumber());
                basketInformation.setInCache(true);
                basketInformation.setPersonal(true);
                basketInformation.setPhone(u.getPhone());
                basketInformation.setZipCode(u.getZipCode());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
            }
        });
    }

    private void setDostawa(boolean isDostawa) {
        if (!isDostawa) {
            isOdbiorOsobisty = true;
            odbior.setText(IM.context().getText(R.string.fa_checked));
            dostawa.setText(IM.context().getText(R.string.fa_unchecked));
            cena.setText("Koszt: " + String.valueOf(Util.decimPlace(getArguments().getDouble(SUM_OF_PRIZE), 2)) + IM.context().getResources().getString(R.string.zl));
            setDeliveryVisibility(false);
        } else {
            isOdbiorOsobisty = false;
            odbior.setText(IM.context().getText(R.string.fa_unchecked));
            dostawa.setText(IM.context().getText(R.string.fa_checked));
            cena.setText("Koszt: " + String.valueOf(Util.decimPlace(getArguments().getDouble(SUM_OF_PRIZE), 2)) + IM.context().getResources().getString(R.string.zl) + " " + cenaMore.getText());
            setDeliveryVisibility(true);
        }
        info.setVisibility((isDostawa) ? View.VISIBLE : GONE);
        platnoscContainer.setVisibility((isDostawa) ? View.VISIBLE : GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delivery_osobisty_container:
                findUser();
                setDostawa(false);
                break;
            case R.id.delivery_dostawa_container:
                setDostawa(true);
                break;
            case R.id.delivery_gotowka_container:
                setPlatnosc(false);
                break;
            case R.id.delivery_karta_container:
                setPlatnosc(true);
        }
    }

    private void setPlatnosc(boolean isCardPayment) {
        this.isCardPayment = isCardPayment;
        if (isCardPayment) {
            karta.setText(IM.context().getText(R.string.fa_checked));
            gotwka.setText(IM.context().getText(R.string.fa_unchecked));
        } else {
            karta.setText(IM.context().getText(R.string.fa_unchecked));
            gotwka.setText(IM.context().getText(R.string.fa_checked));
        }
    }

    private void validate() {
        if (isOdbiorOsobisty) {
            isValid = true;
            return;
        }

        isValid = true;

        if (Validator.phoneNumberFormatIsValid(tel.getText()) && !Util.nullOrEmpty(tel.getText())) {
            tel.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.phone_incorect);
            tel.setError(true);
            isValid = false;
        }
        if (Validator.streetIsValid(ulica.getText()) && !Util.nullOrEmpty(ulica.getText())) {
            ulica.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.street_incorrect);
            ulica.setError(true);
            isValid = false;
        }
        if (Validator.isHouseNumberValid(nrDom.getText()) && !Util.nullOrEmpty(nrDom.getText())) {
            nrDom.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.street_nr_incorect);
            nrDom.setError(true);
            isValid = false;
        }
        if (Validator.cityIsValid(city.getText()) && !Util.nullOrEmpty(city.getText())) {
            city.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.city_incorrect);
            city.setError(true);
            isValid = false;
        }
        if (Validator.zipCodeIsValid(zip.getText()) && !Util.nullOrEmpty(zip.getText())) {
            zip.setError(false);
        } else {
            AppendMessage.appendMessage(R.string.zip_code_incorrect);
            zip.setError(true);
            isValid = false;
        }

        if (!isValid) {
            AppendMessage.showSuccessOrError();
        }
    }
}

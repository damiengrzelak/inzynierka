package wfiis.pizzerialesna.customDialogs;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.SimpleAdapter;
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
import com.inverce.mod.core.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.customViews.InputEditTextView;
import wfiis.pizzerialesna.customViews.TopToast;
import wfiis.pizzerialesna.model.Basket;
import wfiis.pizzerialesna.model.Inne;
import wfiis.pizzerialesna.model.Obiad;
import wfiis.pizzerialesna.model.Pizza;
import wfiis.pizzerialesna.model.Salatka;
import wfiis.pizzerialesna.model.User;
import wfiis.pizzerialesna.model.Zapiekanka;
import wfiis.pizzerialesna.tools.Util;

import static wfiis.pizzerialesna.Cfg.users_table;

public class OrderCustomDialog extends DialogFragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private Object data;
    ListPopupWindow popupWindow = new ListPopupWindow(IM.context());
    private CardView cardView;

    private Button addBtn, minusBtn, zamow;
    private EditText count;

    private TextView title, contentTitle;
    private InputEditTextView spinner;

    private int selectedSpinnerId;

    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public OrderCustomDialog() {

    }

    @SuppressLint("ValidFragment")
    public OrderCustomDialog(Object o) {
        this.data = o;
        if (data instanceof Pizza || data instanceof Obiad) {
            this.selectedSpinnerId = 0;
        } else {
            this.selectedSpinnerId = -1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.custom_dialog, container,
                false);

        findViews(rootView);
        fillViews();
        setListeners();

        return rootView;
    }

    private void findViews(View rootView) {
        cardView = rootView.findViewById(R.id.custom_dialog_card_view);
        addBtn = rootView.findViewById(R.id.custom_dialog_plus_btn);
        minusBtn = rootView.findViewById(R.id.custom_dialog_minus_btn);
        count = rootView.findViewById(R.id.custom_dialog_count);
        contentTitle = rootView.findViewById(R.id.custom_dialog_order_type_title);
        title = rootView.findViewById(R.id.custom_dialog_title);
        spinner = rootView.findViewById(R.id.custom_spinner);
        zamow = rootView.findViewById(R.id.custom_dialog_add_to_basket_btn);
        //spp = rootView.findViewById(R.id.sppp);
    }

    private void fillViews() {
        if (data instanceof Pizza) {
            contentTitle.setText(R.string.pizza_rozmiar);
            title.setText(R.string.wybierz_pizze);
            spinner.setHintText(IM.resources().getString(R.string.rozmiar_hint));
            spinner.setInputEnabled(false);
            spinner.setText(getTextFromFirstPosition(data));
        } else if (data instanceof Obiad) {
            contentTitle.setText(R.string.kebab_rodzaj);
            title.setText(R.string.kebab_rodzaj);
            spinner.setHintText(IM.resources().getString(R.string.mieso_hint));
            spinner.setInputEnabled(false);
            spinner.setText(getTextFromFirstPosition(data));
        } else {
            contentTitle.setVisibility(View.GONE);
            title.setText(R.string.dodaj_do_koszyka);
            spinner.setVisibility(View.GONE);
        }
    }

    private void setListeners() {
        addBtn.setOnClickListener(this::changeCount);
        minusBtn.setOnClickListener(this::changeCount);
        spinner.setOnClickListener(this::showPopUp);
        zamow.setOnClickListener(v -> {
            TopToast.show("Dodano do koszyka", TopToast.TYPE_SUCCESS, TopToast.DURATION_SHORT);
            createBasketItem();
            this.dismiss();
        });


    }

    private void createBasketItem() {
        Basket item = new Basket();

        if (data instanceof Pizza) {
            item.setName(((Pizza) data).getName());
            item.setNumber(((Pizza) data).getNumber());
            if (selectedSpinnerId == 0) {
                item.setPrize(((Pizza) data).getPrice28());
                item.setSize("Mini - " + String.valueOf(Util.decimPlace(((Pizza) data).getPrice28(), 2)) + IM.context().getResources().getString(R.string.zl));
            } else if (selectedSpinnerId == 1) {
                item.setPrize(((Pizza) data).getPrice34());
                item.setSize("Mała - " + String.valueOf(Util.decimPlace(((Pizza) data).getPrice34(), 2)) + IM.context().getResources().getString(R.string.zl));
            } else if (selectedSpinnerId == 2) {
                item.setPrize(((Pizza) data).getPrice44());
                item.setSize("Średnia - " + String.valueOf(Util.decimPlace(((Pizza) data).getPrice44(), 2)) + IM.context().getResources().getString(R.string.zl));
            }
            item.setIsPizza(true);
            item.setType(((Pizza) data).getType());
        } else if (data instanceof Obiad) {
            item.setName(((Obiad) data).getName());
            item.setPrize(((Obiad) data).getPrice());
            item.setType(((Obiad) data).getType());
            if (selectedSpinnerId == 0) {
                item.setMeat("Mięso mieszane");
            } else if (selectedSpinnerId == 1) {
                item.setMeat("Wołowe");
            } else if (selectedSpinnerId == 2) {
                item.setMeat("Drobiowe");
            }
            item.setIsPizza(false);
        } else if (data instanceof Inne) {
            item.setName(((Inne) data).getName());
            item.setIsPizza(false);
            item.setType(((Inne) data).getType());
            item.setPrize(((Inne) data).getPrice());
        } else if (data instanceof Salatka) {
            item.setName(((Salatka) data).getName());
            item.setPrize(((Salatka) data).getPrice());
            item.setIsPizza(false);
            item.setType(((Salatka) data).getType());
        } else if (data instanceof Zapiekanka) {
            item.setName(((Zapiekanka) data).getName());
            item.setType(((Zapiekanka) data).getType());
            item.setIsPizza(false);
            item.setPrize(((Zapiekanka) data).getPrice());
        }

        Map<String, Object> basket = new HashMap<>();

        for (int i = 0; i < Integer.valueOf(count.getText().toString()); i++) {
            ref.child(users_table + "/" + mAuth.getUid() + "/basket").push();
            basket.put(ref.child(users_table + "/" + mAuth.getUid() + "/basket").push().getKey(), item);
            ref.child(users_table + "/" + mAuth.getUid() + "/basket").updateChildren(basket);
        }

    }

    private void changeCount(View view) {
        switch (view.getId()) {
            case R.id.custom_dialog_plus_btn:
                changeCount(true);
                break;
            case R.id.custom_dialog_minus_btn:
                changeCount(false);
                break;
        }
    }

    private void changeCount(boolean isAdding) {
        int ilosc = Integer.valueOf(String.valueOf(count.getText()));
        if (isAdding) {
            ilosc++;
        } else {
            if (ilosc > 1) {
                ilosc--;
            }
        }
        count.setText(String.valueOf(ilosc));
    }

    private String getTextFromFirstPosition(Object o) {
        if (o instanceof Pizza) {
            return "Mini - " + String.valueOf(Util.decimPlace(((Pizza) o).getPrice28(), 2)) + IM.context().getResources().getString(R.string.zl);
        } else {
            return "Mięso mieszane";
        }
    }

    private void showPopUp(View view) {
        List<HashMap<String, String>> haszList = new ArrayList<>();
        HashMap<String, String> spinnerItems = new HashMap<>();
        HashMap<String, String> spinnerItems2 = new HashMap<>();
        HashMap<String, String> spinnerItems3 = new HashMap<>();

        if (data instanceof Pizza) {
            spinnerItems.put("name",
                    "Mini - " + String.valueOf(Util.decimPlace(((Pizza) data).getPrice28(), 2)) + IM.context().getResources().getString(R.string.zl));
            haszList.add(spinnerItems);
            spinnerItems2.put("name",
                    "Mała - " + String.valueOf(Util.decimPlace(((Pizza) data).getPrice34(), 2)) + IM.context().getResources().getString(R.string.zl));
            haszList.add(spinnerItems2);
            spinnerItems3.put("name",
                    "Średnia - " + String.valueOf(Util.decimPlace(((Pizza) data).getPrice44(), 2)) + IM.context().getResources().getString(R.string.zl));
            haszList.add(spinnerItems3);
        } else {
            spinnerItems.put("name", "Mięso mieszane");
            haszList.add(spinnerItems);
            spinnerItems2.put("name", "Wołowe");
            haszList.add(spinnerItems2);
            spinnerItems3.put("name", "Drobiowe");
            haszList.add(spinnerItems3);
        }
        String[] from = {"name"};
        int[] to = {R.id.item_view};


        ListAdapter adapter = new SimpleAdapter(
                IM.context(),
                haszList,
                R.layout.drop_down_item,
                from,
                to
        );


        popupWindow.setAnchorView(spinner);
        popupWindow.setAdapter(adapter);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnItemClickListener(this);
        popupWindow.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedSpinnerId = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (data instanceof Pizza) {
            switch (i) {
                case 0:
                    selectedSpinnerId = i;
                    spinner.setText("Mini - " + String.valueOf(Util.decimPlace(((Pizza) data).getPrice28(), 2)) + IM.context().getResources().getString(R.string.zl));
                    break;
                case 1:
                    selectedSpinnerId = i;
                    spinner.setText("Mała - " + String.valueOf(Util.decimPlace(((Pizza) data).getPrice34(), 2)) + IM.context().getResources().getString(R.string.zl));
                    break;
                case 2:
                    selectedSpinnerId = i;
                    spinner.setText("Średnia - " + String.valueOf(Util.decimPlace(((Pizza) data).getPrice44(), 2)) + IM.context().getResources().getString(R.string.zl));
                    break;
            }
        } else if (data instanceof Obiad){
            switch (i) {
                case 0:
                    selectedSpinnerId = i;
                    spinner.setText("Mięso mieszane");
                    break;
                case 1:
                    selectedSpinnerId = i;
                    spinner.setText("Wołowe");
                    break;
                case 2:
                    selectedSpinnerId = i;
                    spinner.setText("Drobiowe");
                    break;
            }
        }
        popupWindow.dismiss();
    }
}


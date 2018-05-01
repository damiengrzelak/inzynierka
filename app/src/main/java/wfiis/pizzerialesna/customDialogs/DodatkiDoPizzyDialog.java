package wfiis.pizzerialesna.customDialogs;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inverce.mod.core.IM;
import com.inverce.mod.events.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.model.Extras;
import wfiis.pizzerialesna.tools.SpanUtils;
import wfiis.pizzerialesna.tools.Util;


public class DodatkiDoPizzyDialog extends DialogFragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private ListPopupWindow popupWindow = new ListPopupWindow(IM.context());
    private int size;
    private List<Extras> dodatkiList = new ArrayList<>();

    private int switchClicked = -1;

    private CheckBox ser, ciasto;
    private TextView serCena, ciastoCena;
    private TextView miesoText, owoceText, warzywaText, gratisyTextl;
    private LinearLayout mieso, owoceMorza, warzywa, gratisy;
    private Button done;

    private double dodatkiCena;


    private int position = -1;

    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public DodatkiDoPizzyDialog() {


    }

    @SuppressLint("ValidFragment")
    public DodatkiDoPizzyDialog(List<Extras> dodatkiList, int size, int position) {
        this.size = size;
        this.dodatkiList = dodatkiList;
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dodatki_do_pizzy_dialog, container,
                false);

        findViews(rootView);
        fillViews();
        setListeners();

        return rootView;
    }

    private void findViews(View rootView) {
        ser = rootView.findViewById(R.id.dodatki_dialog_ser_check);
        ciasto = rootView.findViewById(R.id.dodatki_dialog_ciasto_check);
        mieso = rootView.findViewById(R.id.dodatki_dialog_mieso);
        owoceMorza = rootView.findViewById(R.id.dodatki_dialog_owoce_morza);
        warzywa = rootView.findViewById(R.id.dodatki_dialog_warzywa);
        gratisy = rootView.findViewById(R.id.dodatki_dialog_gratisy);
        serCena = rootView.findViewById(R.id.dodatki_ser_cena);
        ciastoCena = rootView.findViewById(R.id.dodatki_ciasto_cena);

        miesoText = rootView.findViewById(R.id.dodatek_mieso_text);
        owoceText = rootView.findViewById(R.id.dodatek_owoce_text);
        warzywaText = rootView.findViewById(R.id.dodatek_warzywa_text);
        gratisyTextl = rootView.findViewById(R.id.dodatek_gratisy_text);

        done = rootView.findViewById(R.id.dodatki_dialog_done);
    }

    private void fillViews() {
        switch (size) {
            case 0:
                SpanUtils.on(serCena).convertToMoney(dodatkiList.get(0).getLowPrice());
                SpanUtils.on(ciastoCena).convertToMoney(dodatkiList.get(4).getLowPrice());

                miesoText.setText("DODATEK MIĘSNY " + String.valueOf(Util.decimPlace(dodatkiList.get(1).getLowPrice(), 2)) + IM.context().getResources().getString(R.string.zl));
                owoceText.setText("OWOCE MORZA " + String.valueOf(Util.decimPlace(dodatkiList.get(2).getLowPrice(), 2)) + IM.context().getResources().getString(R.string.zl));
                warzywaText.setText("WARZYWA " + String.valueOf(Util.decimPlace(dodatkiList.get(3).getLowPrice(), 2)) + IM.context().getResources().getString(R.string.zl));
                break;
            case 1:
                SpanUtils.on(serCena).convertToMoney(dodatkiList.get(0).getMediumPrice());
                SpanUtils.on(ciastoCena).convertToMoney(dodatkiList.get(4).getMediumPrice());

                miesoText.setText("DODATEK MIĘSNY " + String.valueOf(Util.decimPlace(dodatkiList.get(1).getMediumPrice(), 2)) + IM.context().getResources().getString(R.string.zl));
                owoceText.setText("OWOCE MORZA " + String.valueOf(Util.decimPlace(dodatkiList.get(2).getMediumPrice(), 2)) + IM.context().getResources().getString(R.string.zl));
                warzywaText.setText("WARZYWA " + String.valueOf(Util.decimPlace(dodatkiList.get(3).getMediumPrice(), 2)) + IM.context().getResources().getString(R.string.zl));
                break;
            case 2:
                SpanUtils.on(serCena).convertToMoney(dodatkiList.get(0).getHighPrice());
                SpanUtils.on(ciastoCena).convertToMoney(dodatkiList.get(4).getHighPrice());

                miesoText.setText("DODATEK MIĘSNY " + String.valueOf(Util.decimPlace(dodatkiList.get(1).getHighPrice(), 2)) + IM.context().getResources().getString(R.string.zl));
                owoceText.setText("OWOCE MORZA " + String.valueOf(Util.decimPlace(dodatkiList.get(2).getHighPrice(), 2)) + IM.context().getResources().getString(R.string.zl));
                warzywaText.setText("WARZYWA " + String.valueOf(Util.decimPlace(dodatkiList.get(3).getHighPrice(), 2)) + IM.context().getResources().getString(R.string.zl));
                break;
        }
    }

    private void setListeners() {
        mieso.setOnClickListener(view -> showPopUp(this.mieso));
        owoceMorza.setOnClickListener(view -> showPopUp(this.owoceMorza));
        warzywa.setOnClickListener(view -> showPopUp(this.warzywa));
        gratisy.setOnClickListener(view -> showPopUp(this.gratisy));

        done.setOnClickListener(view -> {
            String dodatkiText = "";
            ArrayList<String> listaDodatkow = new ArrayList<>();
            if (ser.isChecked()) {
                dodatkiText = dodatkiText + "ser, ";
                listaDodatkow.add("ser");
                if (size == 0) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(0).getLowPrice();
                } else if (size == 1) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(0).getMediumPrice();
                } else if (size == 2) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(0).getHighPrice();
                }
            }
            if (ciasto.isChecked()) {
                dodatkiText = dodatkiText + "ciasto, ";
                listaDodatkow.add("ciasto");
                if (size == 0) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(4).getLowPrice();
                } else if (size == 1) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(4).getMediumPrice();
                } else if (size == 2) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(4).getHighPrice();
                }
            }
            if (!miesoText.getText().toString().contains("DODATEK")) {
                dodatkiText = dodatkiText + miesoText.getText() + ", ";
                listaDodatkow.add(miesoText.getText().toString());
                if (size == 0) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(1).getLowPrice();
                } else if (size == 1) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(1).getMediumPrice();
                } else if (size == 2) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(1).getHighPrice();
                }
            }
            if (!owoceText.getText().toString().contains("OWOCE")) {
                dodatkiText = dodatkiText + owoceText.getText() + ", ";
                listaDodatkow.add(owoceText.getText().toString());
                if (size == 0) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(2).getLowPrice();
                } else if (size == 1) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(2).getMediumPrice();
                } else if (size == 2) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(2).getHighPrice();
                }
            }
            if (!warzywaText.getText().toString().contains("WARZY")) {
                dodatkiText = dodatkiText + warzywaText.getText() + ", ";
                ;
                listaDodatkow.add(warzywaText.getText().toString());
                if (size == 0) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(3).getLowPrice();
                } else if (size == 1) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(3).getMediumPrice();
                } else if (size == 2) {
                    dodatkiCena = dodatkiCena + dodatkiList.get(3).getHighPrice();
                }
            }
            if (!gratisyTextl.getText().toString().contains("GRAT")) {
                dodatkiText = dodatkiText + gratisyTextl.getText();
                listaDodatkow.add(gratisyTextl.getText().toString());
                dodatkiCena = dodatkiCena;
            }

            Event.Bus.post(DodatkiAddInteractions.class).onExtrasAdded(dodatkiText, dodatkiCena, listaDodatkow, position);
            dismiss();
        });
    }

    private void showPopUp(View view) {
        List<HashMap<String, String>> haszList = new ArrayList<>();
        int[] to = {R.id.item_view};
        switch (view.getId()) {
            case R.id.dodatki_dialog_mieso:
                switchClicked = 1;
                for (int i = 0; i < dodatkiList.get(1).getVariants().size(); i++) {
                    HashMap<String, String> spinnerItems = new HashMap<>();
                    spinnerItems.put("Mięso", dodatkiList.get(1).getVariants().get(i).getName());
                    haszList.add(spinnerItems);
                }
                ListAdapter adapter = new SimpleAdapter(
                        IM.context(),
                        haszList,
                        R.layout.drop_down_item,
                        new String[]{"Mięso"},
                        to
                );
                popupWindow.setAnchorView(view);
                popupWindow.setAdapter(adapter);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setOnItemClickListener((adapterView, view1, i, l) -> {
                    miesoText.setText(adapterView.getItemAtPosition(i).toString().replace("{Mięso=", "").replace("}", ""));
                    miesoText.setTextColor(IM.resources().getColor(R.color.black));
                    popupWindow.dismiss();
                });
                popupWindow.show();
                break;
            case R.id.dodatki_dialog_owoce_morza:
                switchClicked = 2;
                for (int i = 0; i < dodatkiList.get(2).getVariants().size(); i++) {
                    HashMap<String, String> spinnerItems = new HashMap<>();
                    spinnerItems.put("OwoceMorza", dodatkiList.get(2).getVariants().get(i).getName());
                    haszList.add(spinnerItems);
                }
                ListAdapter adapter2 = new SimpleAdapter(
                        IM.context(),
                        haszList,
                        R.layout.drop_down_item,
                        new String[]{"OwoceMorza"},
                        to
                );
                popupWindow.setAnchorView(view);
                popupWindow.setAdapter(adapter2);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setOnItemClickListener((adapterView, view1, i, l) -> {
                    owoceText.setText(adapterView.getItemAtPosition(i).toString().replace("{OwoceMorza=", "").replace("}", ""));
                    owoceText.setTextColor(IM.resources().getColor(R.color.black));
                    popupWindow.dismiss();
                });
                popupWindow.show();
                break;
            case R.id.dodatki_dialog_warzywa:
                switchClicked = 3;
                for (int i = 0; i < dodatkiList.get(3).getVariants().size(); i++) {
                    HashMap<String, String> spinnerItems = new HashMap<>();
                    spinnerItems.put("OwoceMorza", dodatkiList.get(3).getVariants().get(i).getName());
                    haszList.add(spinnerItems);
                }
                ListAdapter adapter3 = new SimpleAdapter(
                        IM.context(),
                        haszList,
                        R.layout.drop_down_item,
                        new String[]{"OwoceMorza"},
                        to
                );
                popupWindow.setAnchorView(view);
                popupWindow.setAdapter(adapter3);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setOnItemClickListener((adapterView, view1, i, l) -> {
                    warzywaText.setText(adapterView.getItemAtPosition(i).toString().replace("{OwoceMorza=", "").replace("}", ""));
                    warzywaText.setTextColor(IM.resources().getColor(R.color.black));
                    popupWindow.dismiss();
                });
                popupWindow.show();
                break;
            case R.id.dodatki_dialog_gratisy:
                switchClicked = 4;
                for (int i = 0; i < dodatkiList.get(5).getVariants().size(); i++) {
                    HashMap<String, String> spinnerItems = new HashMap<>();
                    spinnerItems.put("Gratisy", dodatkiList.get(5).getVariants().get(i).getName());
                    haszList.add(spinnerItems);
                }
                ListAdapter adapter4 = new SimpleAdapter(
                        IM.context(),
                        haszList,
                        R.layout.drop_down_item,
                        new String[]{"Gratisy"},
                        to
                );
                popupWindow.setAnchorView(view);
                popupWindow.setAdapter(adapter4);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setOnItemClickListener((adapterView, view1, i, l) -> {
                    gratisyTextl.setText(adapterView.getItemAtPosition(i).toString().replace("{Gratisy=", "").replace("}", ""));
                    gratisyTextl.setTextColor(IM.resources().getColor(R.color.black));
                    popupWindow.dismiss();
                });
                popupWindow.show();
                break;
        }
        switchClicked = -1;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }
}


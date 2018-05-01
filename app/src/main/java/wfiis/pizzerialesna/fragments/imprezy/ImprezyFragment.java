package wfiis.pizzerialesna.fragments.imprezy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inverce.mod.core.IM;

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.fragments.imprezy.adapter.ImageAdapter;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;

public class ImprezyFragment extends BaseFragment {
    private List<String> imagesList;
    private RecyclerView images;
    private ImageAdapter imageAdapter;
    private TextView info;

    public static ImprezyFragment newInstance() {
        return new ImprezyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_imprezy, container, false);
        assert getActions() != null;

        getImagesList();
        findViews(view);
        setListeners();
        prepareRecycler();

        getActions().topBar().showBackIcon(true);
        getActions().topBar().showMenuIcon(false);
        getActions().topBar().showBasketIcon(false);


        return view;
    }

    private void getImagesList() {
        imagesList = new ArrayList<>();
        imagesList.add("https://zapodaj.net/images/936dbd5117d07.jpg");
        imagesList.add("https://zapodaj.net/images/1dbc8a72a392f.jpg");
        imagesList.add("https://zapodaj.net/images/df1ec0136fc06.jpg");
        imagesList.add("https://zapodaj.net/images/197729db0081c.jpg");
        imagesList.add("https://zapodaj.net/images/7d689130a1227.jpg");
        imagesList.add("https://zapodaj.net/images/acfbe0b4a116d.jpg");
        imagesList.add("https://zapodaj.net/images/0d6d42f7b4d10.jpg");
        imagesList.add("https://zapodaj.net/images/66b02f6bdbe42.jpg");
        imagesList.add("https://zapodaj.net/images/f40cdd6cc741a.jpg");
        imagesList.add("https://zapodaj.net/images/3da77b24eedd2.jpg");
        imagesList.add("https://zapodaj.net/images/212bd1fe43e57.jpg");
        imagesList.add("https://zapodaj.net/images/d609d220b8ecf.jpg");
    }

    private void prepareRecycler() {
        images.setLayoutManager(new LinearLayoutManager(IM.context(), HORIZONTAL, false));
        imageAdapter = new ImageAdapter(imagesList);

        images.setAdapter(imageAdapter);
    }

    private void setListeners() {
    }

    private void findViews(View view) {
        info = view.findViewById(R.id.imprezy_info_text);
        images = view.findViewById(R.id.imprezy_info_rv);
    }


}

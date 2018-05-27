package wfiis.pizzerialesna.fragments.facebookAtApp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inverce.mod.core.IM;

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.fragments.facebookAtApp.adapter.FbPostAdapter;
import wfiis.pizzerialesna.model.FbPost;

public class FavebookAtApp extends BaseFragment {
    private RecyclerView recyclerView;
    private FbPostAdapter adapter;
    private List<FbPost> dataList;

    public static FavebookAtApp newInstance() {
        return new FavebookAtApp();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook_at_app, container, false);
        assert getActions() != null;

        findViews(view);
        createPostList();
        prepareRecycler();

        getActions().topBar().showBackIcon(true);
        getActions().topBar().showMenuIcon(false);
        getActions().topBar().showBasketIcon(false);
        return view;
    }

    private void createPostList() {
        if (dataList == null || dataList.isEmpty()) {
            dataList = new ArrayList<>();
        }

        FbPost post = new FbPost(), post1 = new FbPost(), post2 = new FbPost(), post3 = new FbPost();

        post.setImg(ContextCompat.getDrawable(IM.context(), R.drawable.promotion1));
        post.setText("Serdecznie zapraszamy do oglądania Gali!");
        post1.setImg(ContextCompat.getDrawable(IM.context(), R.drawable.promotion2));
        post1.setText("WALENTYNKI !!! Tradycyjnie jak co roku na życzenie klienta robimy pizze w kształcie serca . Zapraszamy serdecznie.");
        post2.setImg(ContextCompat.getDrawable(IM.context(), R.drawable.promotion3));
        post2.setText("Tylko dziś każda pizza -25% \nZapraszamy!!");
        post3.setImg(ContextCompat.getDrawable(IM.context(), R.drawable.promotion4));
        post3.setText("UWAGA SZUKAMY STAŻYSTKI !!!\nCV Prosimy przynosić do Pizzerii.\nDokładnych informacji udzielimy na miejscu.");

        dataList.add(post);
        dataList.add(post1);
        dataList.add(post2);
        dataList.add(post3);
    }

    private void prepareRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new FbPostAdapter(dataList);
        recyclerView.setAdapter(adapter);

        recyclerView.refreshDrawableState();
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.fb_recycler);
    }

}

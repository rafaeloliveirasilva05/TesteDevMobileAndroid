package com.example.rafael.testedevmobileandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.rafael.testedevmobileandroid.adapters.PostAdapter;
import com.example.rafael.testedevmobileandroid.domain.domainPost.Items;
import com.example.rafael.testedevmobileandroid.interfaces.ItemClickListener;
import com.example.rafael.testedevmobileandroid.interfaces.ItemClickListenerPostDetalhes;
import com.example.rafael.testedevmobileandroid.presenter.PostPresenter;
import com.example.rafael.testedevmobileandroid.presenter.PostPresenterImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoPubView.BannerAdListener, PostView.PostViewMainActivity{

    private MoPubView moPubView;
    private PostAdapter adapter;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    public static final String ID_MOPUB = "3418daeccb294098993d6be32e9ba1ab";

    private PostPresenter.PostItemPresenter mPostItemPresenter;
    private ArrayList<Items> itemsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createToolbar();

        //Configura o moPub
        moPubView = findViewById(R.id.adview);
        moPubView.setAdUnitId(ID_MOPUB);
        moPubView.setBannerAdListener(this);

        configFirebase();

        checkAds();

        //Configuração da anel de progresso
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        itemsArrayList = new ArrayList<>();

        //Configurando RecycleView
        final RecyclerView recyclerView =  findViewById(R.id.recycler);
        adapter = new  PostAdapter(itemsArrayList,this,
                showPostDetailsScreen(),
                showProfileScreen());

        recyclerView.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        mPostItemPresenter = new PostPresenterImpl(this);
        mPostItemPresenter.getPost();

        //Configura o sroll infinito
        EndlessRecyclerViewScrollListener scrollListener =
                new EndlessRecyclerViewScrollListener(layout) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                        mPostItemPresenter.getReloadPost();
                    }
                };
        recyclerView.addOnScrollListener(scrollListener);

        //Configura o onReload
        swipeRefreshLayout = findViewById(R.id.simpleSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemsArrayList.clear();
                mPostItemPresenter.getPost();
            }
        });
    }

    //Configura o Firebase
    public void configFirebase (){

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        FirebaseRemoteConfigSettings configSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(BuildConfig.DEBUG).build();

        mFirebaseRemoteConfig.setConfigSettings(configSettings);
    }

    //Função responsavel por criar a Toolbar
    public void createToolbar(){

        Toolbar mToolbar = findViewById(R.id.tb_main);
        mToolbar.setTitle(R.string.feed);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorSecondytext));
        setSupportActionBar(mToolbar);
    }

    //Caso não tenha conexão com a internet o usuario recebera uma mensage de erro de conexão
    @Override
    public void msgFaltaConexao(){
        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setTitle(R.string.semInternet);
        msgBox.setPositiveButton(R.string.fechar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        msgBox.show();
    }

    //Caso o endpoint nao seja encontrado, o usuario receberá uma mensagem
    @Override
    public void msgErroEndpoit(){
        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setTitle(R.string.erroEndpoint);

        msgBox.setPositiveButton(R.string.fechar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        msgBox.show();
    }

    //Metodo que acessa a atividade da tela2, tela que apresenta os detalhes da refeição do usuario
    public ItemClickListener showPostDetailsScreen(){
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, PostDetalhesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("items",itemsArrayList.get(position));

                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        return itemClickListener;
    }

    public ItemClickListenerPostDetalhes showProfileScreen(){

        ItemClickListenerPostDetalhes itemClickListenerPostDetalhes = new ItemClickListenerPostDetalhes() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("items2",itemsArrayList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        return itemClickListenerPostDetalhes;
    }

    boolean prop;
    public void checkAds(){

        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                            prop = mFirebaseRemoteConfig.getBoolean("props");

                            if(prop){
                                moPubView.loadAd();

                            }
                        }
                    }
                });
    }

    @Override
    public void showPostDetail(List<Items> items) {
        itemsArrayList.addAll(items);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBannerLoaded(MoPubView banner) {}

    @Override
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {}

    @Override
    public void onBannerClicked(MoPubView banner) {}

    @Override
    public void onBannerExpanded(MoPubView banner) {}

    @Override
    public void onBannerCollapsed(MoPubView banner) {}
}
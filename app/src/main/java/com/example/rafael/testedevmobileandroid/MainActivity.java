package com.example.rafael.testedevmobileandroid;

import android.content.Context;
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
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rafael.testedevmobileandroid.adapters.PostAdapter;
import com.example.rafael.testedevmobileandroid.api.PostService;
import com.example.rafael.testedevmobileandroid.domain.Post;
import com.example.rafael.testedevmobileandroid.interfaces.ItemClickListener;
import com.example.rafael.testedevmobileandroid.interfaces.ItemClickListenerPostDetalhes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoPubView.BannerAdListener{

    private PostService postService;
    private Post postBody;
    private Post novoPostBody;
    private Context context;
    private String numeroPagina;
    private String timestamp;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private PostAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MoPubView moPubView;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String idMopub = "3418daeccb294098993d6be32e9ba1ab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        criaToolbar();

        //Configura o moPub
        moPubView = findViewById(R.id.adview);
        moPubView.setAdUnitId(idMopub);
        moPubView.setBannerAdListener(this);

        configFirebase();

        checaPropaganda();

        //Configuração da anel de progresso
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        context = this;
        postService = PostService.retrofit.create(PostService.class);
        refresh(postService);

        swipeRefreshLayout = findViewById(R.id.simpleSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(postService);
            }
        });
    }

    private void refresh(final PostService postService) {

        Call<Post> requestPost = postService.listPosts();
        requestPost.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if(response.isSuccessful()){
                    postBody = response.body();

                    numeroPagina = postBody.getP();
                    timestamp    = postBody.getT();

                    //Configurando RecycleView
                    final RecyclerView recyclerView =  findViewById(R.id.recycler);
                    adapter = new  PostAdapter(postBody.getItems(),context, chamaTelaPostDetalhes(),chamaTelaProfile());
                    recyclerView.setAdapter(adapter);
                    LinearLayoutManager layout = new LinearLayoutManager(context,
                            LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layout);

                    //Configurando o Scrow Infinito
                    ListaSemFimRecyclerviewSrollListener scrollListener =
                            new ListaSemFimRecyclerviewSrollListener(layout) {
                        @Override
                        public void onLoadMore(int page, int totalItemsCount, final RecyclerView view) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            Call<Post> requestPost = postService.listPostsReload(numeroPagina,timestamp);
                            requestPost.enqueue(new Callback<Post>() {
                                @Override
                                public void onResponse(Call<Post> call, Response<Post> response) {

                                    if(response.isSuccessful()){
                                        novoPostBody = response.body();
                                        postBody.getItems().addAll(novoPostBody.getItems());

                                        view.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                        mProgressBar.setVisibility(View.GONE);
                                    }else{
                                        if(response.code() == 404){
                                            mProgressBar.setVisibility(View.GONE);
                                            msgErroEndpoit();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<Post> call, Throwable t) {
                                    mProgressBar.setVisibility(View.GONE);
                                    msgFaltaConexao();
                                }
                            });
                        }
                    };
                    swipeRefreshLayout.setRefreshing(false);
                    recyclerView.addOnScrollListener(scrollListener);
                }
                else{
                    if(response.code() == 404){
                        mProgressBar.setVisibility(View.GONE);
                        msgErroEndpoit();
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {

                mProgressBar.setVisibility(View.GONE);
                msgFaltaConexao();
            }
        });
    }

    @Override
    public void onBannerLoaded(MoPubView banner) {
    }

    @Override
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
    }

    @Override
    public void onBannerClicked(MoPubView banner) {

    }

    @Override
    public void onBannerExpanded(MoPubView banner) {

    }

    @Override
    public void onBannerCollapsed(MoPubView banner) {

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
    public void criaToolbar(){

        mToolbar = findViewById(R.id.tb_main);
        mToolbar.setTitle(R.string.feed);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorSecondytext));
        setSupportActionBar(mToolbar);
    }

    //Caso não tenha conexão com a internet o usuario recebera uma mensage de erro de conexão
    public void msgFaltaConexao(){
        AlertDialog.Builder msgBox = new AlertDialog.Builder(context);
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
    public void msgErroEndpoit(){
        AlertDialog.Builder msgBox = new AlertDialog.Builder(context);
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
    public ItemClickListener chamaTelaPostDetalhes(){
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, PostDetalhesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("items",postBody.getItems().get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        return itemClickListener;
    }

    public ItemClickListenerPostDetalhes chamaTelaProfile(){
        ItemClickListenerPostDetalhes itemClickListenerPostDetalhes = new ItemClickListenerPostDetalhes() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("items2",postBody.getItems().get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        return itemClickListenerPostDetalhes;
    }

    boolean prop;
    public void checaPropaganda(){

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

}
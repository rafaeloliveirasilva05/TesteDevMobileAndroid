package com.example.rafael.testedevmobileandroid;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rafael.testedevmobileandroid.adapters.AdapterItemAlimento;
import com.example.rafael.testedevmobileandroid.api.PostService;
import com.example.rafael.testedevmobileandroid.domain.Items;
import com.example.rafael.testedevmobileandroid.domain.dadosRefeicao.Foods;
import com.example.rafael.testedevmobileandroid.domain.dadosRefeicao.ItemRefeicao;
import com.example.rafael.testedevmobileandroid.domain.dadosRefeicao.PostRefeicao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetalhesActivity extends AppCompatActivity  implements MoPubView.BannerAdListener{

    private TextView nome;
    private TextView objetivo;
    private TextView quantidadeCalTotal;
    private TextView quantidadeCarbTotal;
    private TextView quantidadeProtTotal;
    private TextView quantidadeGordTotal;
    private MoPubView moPubView;
    private Items items;
    private Context context;
    private ImageView imgAlimento;
    private ProgressBar mProgressBar;
    private ImageButton imageButton;
    private PostRefeicao postRefeicaoBody;
    private CircleImageView circleImageUsuario;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String idMopub = "3418daeccb294098993d6be32e9ba1ab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detalhe);

        context = this;
        final Activity activity = this;

        Bundle bundle = getIntent().getExtras();
        items = (Items) bundle.getSerializable("items");

        criaToolbar();

        //Configura o moPub
        moPubView = findViewById(R.id.adview);
        moPubView.setAdUnitId(idMopub);
        moPubView.setBannerAdListener(this);

        configFirebase();

        checaPropaganda();

        mProgressBar = findViewById(R.id.progressBarDetalhes);
        mProgressBar.setVisibility(View.VISIBLE);

        nome        = findViewById(R.id.nomeTela2);
        objetivo    = findViewById(R.id.objetivo2);
        imgAlimento = findViewById(R.id.person_photo2);
        circleImageUsuario = findViewById(R.id.item_foto_detalhes);

        quantidadeCalTotal  = findViewById(R.id.texCalQuantidadeTotal);
        quantidadeCarbTotal = findViewById(R.id.texCarbQuantidadeTotal);
        quantidadeProtTotal = findViewById(R.id.texProtQuantidadeTotal);
        quantidadeGordTotal = findViewById(R.id.texGordQuantidadeTotal);

        PostService postService = PostService.retrofit.create(PostService.class);
        Call<PostRefeicao> requestPost = postService.dadosRefeicao(items.getFeedHash());
        requestPost.enqueue(new Callback<PostRefeicao>() {
            @Override
            public void onResponse(Call<PostRefeicao> call, Response<PostRefeicao> response) {

                if(response.isSuccessful()){
                    postRefeicaoBody = response.body();


                    List<Foods> foods = postRefeicaoBody.item.foods;
                    ItemRefeicao itemRefeicao = postRefeicaoBody.item;

                    ListView listView = findViewById(R.id.listNomeRefeicao2);
                    AdapterItemAlimento adapter = new AdapterItemAlimento(foods, activity);
                    listView.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(listView);


                    nome.setText(items.getProfile().getName());
                    objetivo.setText(items.getProfile().getGeneral_goal());
                    Picasso.with(context).load(items.getProfile().getImage()).into(circleImageUsuario);
                    Picasso.with(context).load(items.getImage()).into(imgAlimento);
                    imageButton = findViewById(R.id.btnCurtirDetalhes);


                    DecimalFormat decimalFormat = new DecimalFormat("0");

                    quantidadeCalTotal.setText(decimalFormat.
                            format(Double.parseDouble(itemRefeicao.getEnergy())));
                    quantidadeCarbTotal.setText(decimalFormat.
                            format(Double.parseDouble(itemRefeicao.getCarbohydrate())));
                    quantidadeProtTotal.setText(decimalFormat.
                            format(Double.parseDouble(itemRefeicao.getProtein())));
                    quantidadeGordTotal.setText(decimalFormat.
                            format(Double.parseDouble(itemRefeicao.getFat())));

                    checaFotoProfile(items);

                    checaCurtidasPost();

                }
                else{
                    if(response.code() == 404){
                        mProgressBar.setVisibility(View.GONE);
                        msgErroEndpoit();
                    }
                }
            }
            @Override
            public void onFailure(Call<PostRefeicao> call, Throwable t) {

                mProgressBar.setVisibility(View.GONE);
                msgFaltaConexao();
            }
        });
    }

    //Verifica se existe foto de profile
    public void checaFotoProfile(Items items){

        if(items.getProfile().getImage() != null){
            Picasso.with(context).load(items.getProfile()
                    .getImage()).into(circleImageUsuario);
        }
        else{
            circleImageUsuario.setImageResource(R.drawable.sem_imagem_avatar);
        }
    }

    //Verifica se o post recebeu curtidas
    public void checaCurtidasPost(){

        if(!items.isCurtiu()){
            imageButton.setImageResource(R.drawable.ic_fav_borda_branca);
        }
        else{
            imageButton.setImageResource(R.drawable.ic_fav_branco);
        }
        mProgressBar.setVisibility(View.GONE);
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

    //Função responsavel por criar a Toolbar
    public void criaToolbar(){

        String dataFormatada = formadaData(items.getDate());

        Toolbar mToolbar = findViewById(R.id.tb_main_detalhes);
        mToolbar.setTitle("");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorSecondytext));
        TextView diaRefeicaoDetalhes = findViewById(R.id.texdiaRefeicaoDetalhes);
        diaRefeicaoDetalhes.setText(dataFormatada);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //Função responsavel por marcar as publicações curtidas
    public void curtirDetalhes(View view){

        if(items.isCurtiu()){
            imageButton.setImageResource(R.drawable.ic_fav_borda_branca);
            items.setCurtiu(false);
        }
        else {
            imageButton.setImageResource(R.drawable.ic_fav_branco);
            items.setCurtiu(true);
        }

        EventBus.getDefault().post(new BroadcastCurtidas(items.getId()));
    }

    public void btnAcessaProfile(View view){
        Intent intent = new Intent(PostDetalhesActivity.this, ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("items2",items);
        intent.putExtras(bundle);
        startActivity(intent);


    }

    public String formadaData(String datafor){

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date data = null;

        try {
            data = formato.parse(datafor);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formato.applyPattern("dd/MM/yyyy");

        return formato.format(data);
    }

    //Função fecha a atividade atual e volta para anterior
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return true;
    }

    //Função mede o tamanho de cada item da lista, para lista ser apresentada no tamanho exato
    public void setListViewHeightBasedOnChildren(ListView listView) {
        AdapterItemAlimento listAdapter  = (AdapterItemAlimento) listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if(listItem != null){
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout
                        .LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
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

    //Configura o Firebase
    public void configFirebase (){

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        FirebaseRemoteConfigSettings configSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(BuildConfig.DEBUG).build();

        mFirebaseRemoteConfig.setConfigSettings(configSettings);
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
}



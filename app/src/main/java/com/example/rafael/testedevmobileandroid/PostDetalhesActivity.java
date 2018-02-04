package com.example.rafael.testedevmobileandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetalhesActivity extends AppCompatActivity {

    private PostService postService;
    private PostRefeicao postRefeicaoBody;
    private Items items;
    private TextView nome;
    private TextView  objetivo;
    private ImageView imgAlimento;
    private CircleImageView circleImageUsuario;
    private Context context;
    private ImageButton imageButton;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private ListView listView;

    private TextView quantidadeCalTotal;
    private TextView quantidadeCarbTotal;
    private TextView quantidadeProtTotal;
    private TextView quantidadeGordTotal;
    private TextView diaRefeicaoDetalhes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detalhe2);

        Bundle bundle = getIntent().getExtras();
        items = (Items) bundle.getSerializable("items");

        String idioma = Locale.getDefault().getLanguage();
        String dataFormatada = formadaData(items.getDate(),idioma);


        mToolbar = findViewById(R.id.tb_main_detalhes);
        mToolbar.setTitle("");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));

        diaRefeicaoDetalhes = findViewById(R.id.texdiaRefeicaoDetalhes);
        diaRefeicaoDetalhes.setText(dataFormatada);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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


        context = this;

        postService = PostService.retrofit.create(PostService.class);
        Call<PostRefeicao> requestPost = postService.dadosRefeicao(items.getFeedHash());
        requestPost.enqueue(new Callback<PostRefeicao>() {
            @Override
            public void onResponse(Call<PostRefeicao> call, Response<PostRefeicao> response) {

                if(response.isSuccessful()){
                    postRefeicaoBody = response.body();

                    List<Foods> foods = postRefeicaoBody.item.foods;
                    ItemRefeicao itemRefeicao = postRefeicaoBody.item;

                    te(foods);

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


                    //Verifica se existe foto de profile
                    if(items.getProfile().getImage() != null){
                        Picasso.with(context).load(items.getProfile().getImage()).into(circleImageUsuario);
                    }
                    else{
                        circleImageUsuario.setImageResource(R.drawable.sem_imagem_avatar);
                    }

                    //Verifica se o post recebeu curtidas
                    if(!items.isCurtiu()){
                        imageButton.setImageResource(R.drawable.ic_fav_borda_branca);
                    }
                    else{
                        imageButton.setImageResource(R.drawable.ic_fav_branco);
                    }
                    mProgressBar.setVisibility(View.GONE);
                }
                else{

                    //Caso o endpoint nao seja encontrado, o usuario receberá uma mensagem
                    if(response.code() == 404){
                        mProgressBar.setVisibility(View.GONE);
                        AlertDialog.Builder msgBox = new AlertDialog.Builder(context);
                        msgBox.setTitle("Não foi possivel carregar a lista de Games no momento.");

                        msgBox.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        msgBox.show();
                    }
                }
            }
            @Override
            public void onFailure(Call<PostRefeicao> call, Throwable t) {
                //Caso não tenha conexão com a internet o usuario recebera uma mensage de erro de conexão
                mProgressBar.setVisibility(View.GONE);
                AlertDialog.Builder msgBox = new AlertDialog.Builder(context);
                msgBox.setTitle("Falha na conexão com a Iternet");

                msgBox.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                msgBox.show();
            }
        });
    }

    public void te(List<Foods> foods){

        listView = findViewById(R.id.listNomeRefeicao2);
        AdapterItemAlimento adapter = new AdapterItemAlimento(foods,this);

        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
    }

    public void curtirDetalhes(View view){

        if(items.isCurtiu()){
            imageButton.setImageResource(R.drawable.ic_fav_borda_branca);
            items.setCurtiu(false);
        }
        else {
            imageButton.setImageResource(R.drawable.ic_fav_branco);
            items.setCurtiu(true);
        }

        EventBus.getDefault().post(new HelloWorldEvent(items.getId()));
    }

    public void btnAcessaProfile(View view){
        Intent intent = new Intent(PostDetalhesActivity.this, ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("items2",items);
        intent.putExtras(bundle);
        startActivity(intent);


    }

    public String formadaData(String datafor, String idioma){

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date data = null;

        try {
            data = formato.parse(datafor);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(idioma.compareTo("en") >=0){
            formato.applyPattern("yyyy/MM/dd");
        }
        else{
            formato.applyPattern("dd/MM/yyyy");
        }
        return formato.format(data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return true;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        AdapterItemAlimento listAdapter  = (AdapterItemAlimento) listView.getAdapter();

        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if(listItem != null){
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
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
}



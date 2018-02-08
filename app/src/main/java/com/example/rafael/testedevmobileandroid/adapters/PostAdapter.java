package com.example.rafael.testedevmobileandroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.rafael.testedevmobileandroid.BroadcastCurtidas;
import com.example.rafael.testedevmobileandroid.MyViewHolder;
import com.example.rafael.testedevmobileandroid.R;
import com.example.rafael.testedevmobileandroid.domain.domainPost.Items;
import com.example.rafael.testedevmobileandroid.interfaces.ItemClickListener;
import com.example.rafael.testedevmobileandroid.interfaces.ItemClickListenerPostDetalhes;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by rafael on 26/01/18.
 */

public class PostAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Items> itemsList;
    public int pos;


    ItemClickListener  itemClickListener;
    ItemClickListenerPostDetalhes itemClickListenerPostDetalhes;

    public PostAdapter(List<Items> itemsList, Context context, ItemClickListener itemClickListener,
                       ItemClickListenerPostDetalhes itemClickListenerPostDetalhes){

        this.itemsList = itemsList;
        this.context = context;
        this.itemClickListener  = itemClickListener;
        this.itemClickListenerPostDetalhes = itemClickListenerPostDetalhes;

        EventBus.getDefault().register(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_card_view,parent,false);

        //Acessa os detalhes da refeição do usuário
        final MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final MyViewHolder holder = (MyViewHolder) viewHolder;

        String dataFormatada = formadaData(itemsList.get(position).getDate());
        DecimalFormat decimalFormat = new DecimalFormat("0");

        holder.dataRefeitacao.setText(dataFormatada);
        holder.botaoNome.setText(itemsList.get(position).getProfile().getName());
        holder.objetivo.setText(itemsList.get(position).getProfile().getGeneral_goal());
        holder.kcal.setText(decimalFormat.
                format(Double.parseDouble(itemsList.get(position).getEnergy())));

        pos = holder.getAdapterPosition();

        //Verifica se o post recebeu curtidas
        if(!itemsList.get(position).isCurtiu()){
            holder.botaoCurtir.setImageResource(R.drawable.ic_fav_borda_branca);
        }
        else{
            holder.botaoCurtir.setImageResource(R.drawable.ic_fav_branco);
        }

        //Verifica se existe foto de profile
        if(itemsList.get(position).getProfile().getImage() != null){
            Picasso.with(context).load(itemsList.get(position).getProfile().getImage())
                    .into(((MyViewHolder) viewHolder).imgUsuarioCircular);
        }
        else{
            holder.imgUsuarioCircular.setImageResource(R.drawable.sem_imagem_avatar);
        }

        Picasso.with(context).load(itemsList.get(position).getImage())
                .into(((MyViewHolder) viewHolder).imgUsuario);

        //Acessa o profile do usuario
        RelativeLayout relativeLayout = holder.relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListenerPostDetalhes.onItemClick(view, holder.getAdapterPosition());
            }
        });

        //Adiciona uma curtida
        ImageButton imageButton = holder.botaoCurtir;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(itemsList.get(position).isCurtiu()){
                    itemsList.get(position).setCurtiu(false);
                }
                else {
                    itemsList.get(position).setCurtiu(true);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    //Broadcast das curtidas
    @Subscribe
    public void magicSent(BroadcastCurtidas event) {

        for(int i=0;i<itemsList.size();i++){
            if(Long.parseLong(itemsList.get(i).getId()) == Long.parseLong(event.getMessage())){

                if(itemsList.get(i).isCurtiu()){
                    itemsList.get(i).setCurtiu(false);
                }
                else{
                    itemsList.get(i).setCurtiu(true);
                }
                notifyDataSetChanged();
            }
        }
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
}

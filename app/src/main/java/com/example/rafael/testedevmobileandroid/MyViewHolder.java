package com.example.rafael.testedevmobileandroid;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rafael on 26/01/18.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView objetivo;
    public TextView dataRefeitacao;
    public TextView kcal;
    public ImageView imgUsuario;
    public TextView botaoNome;
    public ImageButton botaoCurtir;
    public CircleImageView imgUsuarioCircular;
    public RelativeLayout relativeLayout;

    public MyViewHolder(View itemView) {
        super(itemView);

        imgUsuarioCircular = itemView.findViewById(R.id.item_foto);
        botaoNome          = itemView.findViewById(R.id.btnNome_Card);
        objetivo           = itemView.findViewById(R.id.objetivo_Card);
        imgUsuario         = itemView.findViewById(R.id.person_photo_Card);
        dataRefeitacao     = itemView.findViewById(R.id.data_refeicao_Card);
        kcal               = itemView.findViewById(R.id.kcal_Card);
        botaoCurtir        = itemView.findViewById(R.id.btnCurtir_Card);
        relativeLayout     = itemView.findViewById(R.id.layoutNomeObj);
    }
}

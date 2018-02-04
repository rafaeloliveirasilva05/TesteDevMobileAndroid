package com.example.rafael.testedevmobileandroid.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rafael.testedevmobileandroid.R;
import com.example.rafael.testedevmobileandroid.domain.dadosRefeicao.Foods;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by rafael on 29/01/18.
 */

public class AdapterItemAlimento extends BaseAdapter {

    private final List<Foods> foodsList;
    private final Activity act;

    public AdapterItemAlimento(List<Foods> foodsList, Activity act) {
        this.foodsList = foodsList;
        this.act = act;
    }

    @Override
    public int getCount() {
        return foodsList.size();
    }

    @Override
    public Object getItem(int i) {
        return foodsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = act.getLayoutInflater().inflate(R.layout.item_alimento2, viewGroup,false);

        Foods foods = foodsList.get(i);

        TextView nomeAlimento       = view.findViewById(R.id.texNomeRefeicao); // "description"
        TextView quantidadeDaMedida = view.findViewById(R.id.texQuantidadeDaMedida); // amount
        TextView unidadeDaMedida    = view.findViewById(R.id.texUnidadeDeMedida); // "measure"
        TextView quantidadeCal      = view.findViewById(R.id.texCalQuantidade);   //"energy"
        TextView quantidadeCarb     = view.findViewById(R.id.texCarbQuantidade); //"carbohydrate"
        TextView quantidadeProt     = view.findViewById(R.id.texProtQuantidade); //"protein"
        TextView quantidadeGord     = view.findViewById(R.id.texGordQuantidade); //fat

        DecimalFormat decimalFormat = new DecimalFormat("0");

        nomeAlimento.setText(foods.getDescription());
        quantidadeDaMedida.setText(foods.getAmount());
        unidadeDaMedida.setText(foods.getMeasure());

        quantidadeCal.setText(decimalFormat.format(Double.parseDouble(foods.getEnergy())));
        quantidadeCarb.setText( decimalFormat.format(Double.parseDouble(foods.getCarbohydrate())));
        quantidadeProt.setText(decimalFormat.format(Double.parseDouble(foods.getProtein())));
        quantidadeGord.setText( decimalFormat.format(Double.parseDouble(foods.getFat())));

        return view;
    }
}

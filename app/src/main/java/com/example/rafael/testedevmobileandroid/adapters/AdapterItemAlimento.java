package com.example.rafael.testedevmobileandroid.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rafael.testedevmobileandroid.R;
import com.example.rafael.testedevmobileandroid.domain.domainDadosRefeicao.Foods;

import java.text.DecimalFormat;
import java.util.List;

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
        View view = act.getLayoutInflater().inflate(R.layout.item_alimento, viewGroup,false);

        Foods foods = foodsList.get(i);

        TextView nomeAlimento       = view.findViewById(R.id.texNomeRefeicao);
        TextView quantidadeDaMedida = view.findViewById(R.id.texQuantidadeDaMedida);
        TextView unidadeDaMedida    = view.findViewById(R.id.texUnidadeDeMedida);
        TextView quantidadeCal      = view.findViewById(R.id.texCalQuantidade);
        TextView quantidadeCarb     = view.findViewById(R.id.texCarbQuantidade);
        TextView quantidadeProt     = view.findViewById(R.id.texProtQuantidade);
        TextView quantidadeGord     = view.findViewById(R.id.texGordQuantidade);

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

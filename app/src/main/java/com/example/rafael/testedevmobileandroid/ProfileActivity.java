package com.example.rafael.testedevmobileandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.testedevmobileandroid.domain.Items;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private Items items;
    private TextView nomeProfile;
    private ImageView imgProfile;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mToolbar = findViewById(R.id.tb_main_profile);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle = getIntent().getExtras();
        items =(Items) bundle.getSerializable("items2");

        nomeProfile = findViewById(R.id.texNomeProfile);
        imgProfile = findViewById(R.id.imgProfile);

        nomeProfile.setText(items.getProfile().getName());

        //Verifica se existe foto de profile
        if(items.getProfile().getImage() != null){
            Picasso.with(this).load(items.getProfile().getImage()).into(imgProfile);
        }
        else{
            imgProfile.setImageResource(R.drawable.sem_imagem_avatar);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return true;
    }
}

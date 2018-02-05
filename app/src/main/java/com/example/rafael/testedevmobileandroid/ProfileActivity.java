package com.example.rafael.testedevmobileandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.testedevmobileandroid.domain.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements MoPubView.BannerAdListener{

    private Items items;
    private TextView nomeProfile;
    private ImageView imgProfile;
    private Toolbar mToolbar;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String idMopub = "3418daeccb294098993d6be32e9ba1ab";
    private MoPubView moPubView;

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

        //Configura o moPub
        moPubView = findViewById(R.id.adviewProfile);
        moPubView.setAdUnitId(idMopub);
        moPubView.setBannerAdListener(this);

        configFirebase();

        checaPropaganda();

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

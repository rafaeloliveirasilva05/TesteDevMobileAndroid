package com.example.rafael.testedevmobileandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.testedevmobileandroid.domain.domainPost.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements MoPubView.BannerAdListener{


    private MoPubView moPubView;
    private ImageView imgProfile;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    public static final String ID_MOPUB = "3418daeccb294098993d6be32e9ba1ab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        createToolbar();

        Bundle bundle = getIntent().getExtras();
        Items items = (Items) bundle.getSerializable("items2");

        //Configura o moPub
        moPubView = findViewById(R.id.adviewProfile);
        moPubView.setAdUnitId(ID_MOPUB);
        moPubView.setBannerAdListener(this);

        configFirebase();

        checkAds();

        TextView nomeProfile = findViewById(R.id.texNomeProfile);
        nomeProfile.setText(items.getProfile().getName());

        imgProfile = findViewById(R.id.imgProfile);

        photoProfileIsEmpty(items);

    }

    private void createToolbar() {
        Toolbar mToolbar = findViewById(R.id.tb_main_profile);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //Verifica se existe foto de profile
    public void photoProfileIsEmpty(Items items){

        if(items.getProfile().getImage() != null){
            Picasso.with(this).load(items.getProfile().getImage()).into(imgProfile);
        }
        else{
            imgProfile.setImageResource(R.drawable.sem_imagem_avatar);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return true;
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

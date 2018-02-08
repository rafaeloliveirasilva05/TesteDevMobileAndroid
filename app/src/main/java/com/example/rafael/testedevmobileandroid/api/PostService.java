package com.example.rafael.testedevmobileandroid.api;

import com.example.rafael.testedevmobileandroid.domain.domainPost.Post;
import com.example.rafael.testedevmobileandroid.domain.domainDadosRefeicao.PostRefeicao;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by rafael on 25/01/18.
 */

public interface PostService {

    @GET("feed")
    Call<Post> listPosts();

    @GET("feed")
    Call<Post> listPostsReload(@Query("p") String p, @Query("t") String t);

    @GET("feed/{feedHash}")
    Call<PostRefeicao> dadosRefeicao(@Path("feedHash") String feedHash);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.tecnonutri.com.br/api/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}

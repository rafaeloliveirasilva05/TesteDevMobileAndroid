package com.example.rafael.testedevmobileandroid.presenter;


import com.example.rafael.testedevmobileandroid.PostView;
import com.example.rafael.testedevmobileandroid.api.PostService;
import com.example.rafael.testedevmobileandroid.domain.domainPost.Post;

import retrofit2.Callback;
import retrofit2.Response;

public class PostPresenterImpl implements PostPresenter.PostItemPresenter {

    private PostService postService;
    private Post postBody;
    private PostView.PostViewMainActivity mPostView;
    private String numeroPagina;
    private String timestamp;

    public PostPresenterImpl(PostView.PostViewMainActivity mPostView){
        this.mPostView = mPostView;
    }

    @Override
    public void getPost() {
        postService = PostService.retrofit.create(PostService.class);
        retrofit2.Call<Post> requestPost = postService.listPosts();
        requestPost.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(retrofit2.Call<Post> call, Response<Post> response) {

                if(response.isSuccessful()){
                    postBody = response.body();
                    numeroPagina = postBody.getP();
                    timestamp    = postBody.getT();
                    mPostView.showPostDetail(postBody.getItems());
                }
                else{
                    if(response.code() == 404){
                        mPostView.msgErroEndpoit();
                    }
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Post> call, Throwable t) {
                mPostView.msgFaltaConexao();
            }
        });
    }

    @Override
    public void getReloadPost() {

        postService = PostService.retrofit.create(PostService.class);
        retrofit2.Call<Post> requestPost = postService.listPostsReload(numeroPagina,timestamp);
        requestPost.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(retrofit2.Call<Post> call, Response<Post> response) {

                if(response.isSuccessful()){
                    postBody = response.body();
                    mPostView.showPostDetail(postBody.getItems());

                }else{
                    if(response.code() == 404){
                        mPostView.msgErroEndpoit();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Post> call, Throwable t) {
                mPostView.msgFaltaConexao();
            }
        });
    }
}

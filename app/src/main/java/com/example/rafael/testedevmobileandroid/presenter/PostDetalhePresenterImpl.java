package com.example.rafael.testedevmobileandroid.presenter;

import com.example.rafael.testedevmobileandroid.PostView;
import com.example.rafael.testedevmobileandroid.api.PostService;
import com.example.rafael.testedevmobileandroid.domain.domainDadosRefeicao.PostRefeicao;
import com.example.rafael.testedevmobileandroid.domain.domainPost.Items;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetalhePresenterImpl implements PostPresenter.PostDetalhePresenter {

    private PostRefeicao postRefeicaoBody;
    private PostView.PostViewDetalheActivity mPostViewDetalhes;

    private Items items;

    public PostDetalhePresenterImpl(PostView.PostViewDetalheActivity mPostViewDetalhes ,Items items){
        this.mPostViewDetalhes = mPostViewDetalhes;
        this.items = items;
    }

    @Override
    public void getPostDetail() {

        PostService postService = PostService.retrofit.create(PostService.class);
        Call<PostRefeicao> requestPost = postService.dadosRefeicao(items.getFeedHash());
        requestPost.enqueue(new Callback<PostRefeicao>() {
            @Override
            public void onResponse(Call<PostRefeicao> call, Response<PostRefeicao> response) {

                if(response.isSuccessful()){
                    postRefeicaoBody = response.body();
                    mPostViewDetalhes.showPostDetail(postRefeicaoBody);

                }
                else{
                    if(response.code() == 404){
                        mPostViewDetalhes.msgErroEndpoit();

                    }
                }
            }
            @Override
            public void onFailure(Call<PostRefeicao> call, Throwable t) {
                mPostViewDetalhes.msgFaltaConexao();

            }
        });
    }
}

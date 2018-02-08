package com.example.rafael.testedevmobileandroid;

import com.example.rafael.testedevmobileandroid.domain.domainDadosRefeicao.PostRefeicao;
import com.example.rafael.testedevmobileandroid.domain.domainPost.Items;

import java.util.List;

public interface PostView {


    interface PostViewMainActivity{
        void showPostDetail(List<Items> posts);
        void msgErroEndpoit();
        void msgFaltaConexao();
    }

    interface PostViewDetalheActivity{
        void showPostDetail(PostRefeicao postRefeicaoBody);
        void msgErroEndpoit();
        void msgFaltaConexao();
    }
}

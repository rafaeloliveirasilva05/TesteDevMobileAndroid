package com.example.rafael.testedevmobileandroid.presenter;

public interface PostPresenter {


    interface PostItemPresenter{
        void getPost();
        void getReloadPost();
    }

    interface PostDetalhePresenter{
        void getPostDetail();
    }

}

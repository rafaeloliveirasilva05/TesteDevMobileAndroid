package com.example.rafael.testedevmobileandroid.presenter;

/**
 * Created by rafael on 07/02/18.
 */

public interface PostPresenter {


    interface PostItemPresenter{
        void getPost();
        void getReloadPost();
    }

    interface PostDetalhePresenter{
        void getPostDetail();
    }

}

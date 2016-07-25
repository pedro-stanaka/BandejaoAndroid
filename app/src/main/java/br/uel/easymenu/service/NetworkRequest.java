package br.uel.easymenu.service;

public interface NetworkRequest {

    void get(String url, NetworkServiceListener serviceListener);

    interface NetworkServiceListener {

        void onSuccess(String response);

        void onError(NetworkEvent.NetworkErrorType error);
    }

}

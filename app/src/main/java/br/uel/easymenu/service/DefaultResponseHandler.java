package br.uel.easymenu.service;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import br.uel.easymenu.App;
import br.uel.easymenu.service.NetworkEvent.NetworkErrorType;
import br.uel.easymenu.service.NetworkRequest.NetworkServiceListener;

public class DefaultResponseHandler<T> {

    private Serializer serializer;

    private EventBus eventBus;

    private NetworkRequest networkRequest;

    public DefaultResponseHandler(Serializer serializer, EventBus eventBus, NetworkRequest networkRequest) {
        this.serializer = serializer;
        this.eventBus = eventBus;
        this.networkRequest = networkRequest;
    }

    public interface Action<T> {
        boolean makeBusiness(List<T> objects);
    }

    public void makeRequest(String url, final Class<T> clazz, final Action<T> action) {
        NetworkEvent event = new NetworkEvent(NetworkEvent.Type.START);
        this.eventBus.post(event);

        this.networkRequest.get(url, new NetworkServiceListener() {
            @Override
            public void onSuccess(String response) {

                List<T> objects = serializer.deserialize(response, clazz);

                if(objects != null) {
                    boolean updateUi = action.makeBusiness(objects);
                    if(updateUi) {
                        NetworkEvent event = new NetworkEvent(NetworkEvent.Type.SUCCESS);
                        eventBus.post(event);
                    }
                    else {
                        NetworkEvent event = new NetworkEvent(NetworkEvent.Type.NO_CHANGE);
                        eventBus.post(event);
                    }
                }
                else {
                    NetworkEvent event = new NetworkEvent(NetworkErrorType.INVALID_JSON);
                    eventBus.post(event);
                }
            }

            @Override
            public void onError(NetworkErrorType error) {
                NetworkEvent event = new NetworkEvent(error);
                eventBus.post(event);
                Log.e(App.TAG, "Error: " + error + "");
            }
        });
    }
}

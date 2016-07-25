package br.uel.easymenu.service;

import br.uel.easymenu.R;

public class NetworkEvent {

    private String message;
    private Type eventType;
    private NetworkErrorType errorType;
    public NetworkEvent(NetworkErrorType errorType) {
        this.eventType = Type.ERROR;
        this.errorType = errorType;
    }
    public NetworkEvent(NetworkErrorType errorType, String message) {
        this(errorType);
        this.message = message;
    }

    public NetworkEvent(Type type) {
        this.eventType = type;
    }

    public NetworkEvent(Type type, String message) {
        this.eventType = type;
        this.message = message;
    }

    public boolean hasMessage() {
        return message != null;
    }

    public String getMessage() {
        return message;
    }

    public Type getEventType() {
        return eventType;
    }

    public NetworkErrorType getError() {
        return errorType;
    }

    public enum Type {
        ERROR, SUCCESS, START, NO_CHANGE
    }

    public enum NetworkErrorType {
        NO_CONNECTION(R.string.connection_error),
        AUTH_ERROR(R.string.permission_error),
        SERVER_ERROR(R.string.server_error),
        GENERIC_ERROR(R.string.network_error),
        PARSE_ERROR(R.string.parse_error),
        UNKNOWN_ERROR(R.string.unknown_network_error),
        INVALID_JSON(R.string.invalid_json);

        public final int resourceId;

        NetworkErrorType(int resourceId) {
            this.resourceId = resourceId;
        }
    }
}

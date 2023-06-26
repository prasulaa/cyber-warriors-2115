package pl.prasulakorpo.cyberwarriors.connection.handler;

public class HandlerNotRegisteredException extends RuntimeException {

    public HandlerNotRegisteredException(String message) {
        super(message);
    }
}

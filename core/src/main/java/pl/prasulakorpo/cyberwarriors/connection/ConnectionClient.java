package pl.prasulakorpo.cyberwarriors.connection;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import pl.prasulakorpo.cyberwarriors.connection.handler.MessageHandlerRepository;
import pl.prasulakorpo.cyberwarriors.connection.message.GeneralMsg;

import java.net.URI;
import java.util.logging.Level;

@Log
public class ConnectionClient extends WebSocketClient {

    private final ObjectMapper mapper;
    private final MessageHandlerRepository handlerRepository;

    public ConnectionClient(URI serverUri, ObjectMapper mapper, MessageHandlerRepository handlerRepository) {
        super(serverUri);
        this.mapper = mapper;
        this.handlerRepository = handlerRepository;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.log(Level.INFO, "Websocket opened - " + handshakedata);
    }

    @Override
    public void onMessage(String message) {
        log.log(Level.INFO, "Message received - " + message);

        try {
            GeneralMsg msg = mapper.readValue(message, GeneralMsg.class);
            Gdx.app.postRunnable(() -> handlerRepository.messageHandler(msg).handle(msg, this));
        } catch (JsonProcessingException e) {
            log.log(Level.INFO, "Exception occurred during message mapping", e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.log(Level.INFO, "Connection closed - " + code + " - " + remote + " - " + reason);
    }

    @Override
    public void onError(Exception ex) {
        log.log(Level.INFO, "Websocket error occurred - ", ex);
    }
}

package pl.prasulakorpo.cyberwarriors.connection.handler;

import pl.prasulakorpo.cyberwarriors.connection.message.GeneralMsg;
import pl.prasulakorpo.cyberwarriors.connection.message.PlayerDTO;
import pl.prasulakorpo.cyberwarriors.connection.message.WorldInfoDTO;
import pl.prasulakorpo.cyberwarriors.model.GameState;

import java.util.HashMap;
import java.util.Map;

public class MessageHandlerRepository {

    private final Map<String, MessageHandler> handlers;

    public MessageHandlerRepository(GameState gameState) {
        this.handlers = new HashMap<>();
        handlers.put(PlayerDTO.NAME, new PlayerDTOHandler(gameState));
        handlers.put(WorldInfoDTO.NAME, new WorldInfoDTOHandler(gameState));
    }

    public MessageHandler messageHandler(GeneralMsg msg) {
        MessageHandler handler = handlers.get(msg.getMsgType());

        if (handler == null) {
            throw new HandlerNotRegisteredException("Handler for message type " + msg.getMsgType() + " not registered");
        }

        return handler;
    }

}

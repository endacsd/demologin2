package com.example.demo.config;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        if(request !=null){
            HttpSession httpSession =(HttpSession) request.getHttpSession();
            if(httpSession==null) return;
            System.out.println(httpSession);
            sec.getUserProperties()
                    .put(HttpSession.class.getName(),httpSession);
        }

    }
}

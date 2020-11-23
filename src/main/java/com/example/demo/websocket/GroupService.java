package com.example.demo.websocket;



import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.GetHttpSessionConfigurator;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.security.acl.Group;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/group",configurator = GetHttpSessionConfigurator.class)
@Service
public class GroupService {



    //@Autowired
    //private static UserService userService;
    private static int all=0;
    private static int onlineCount = 0;
    private static int cnt=0;
    private static Map<String,GroupService> GSMap = new ConcurrentHashMap<>();

    private Session session;

    public Session getSession() {
        return session;
    }
    private String username;
    private HttpSession httpSession;

    List<String> messagesCache;
    public GroupService(){
        System.out.println(new Date()+ " "+ toString() +" " + (++all));
    }
    // 标注客户端打开WebSocket服务端点调用方法
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        System.out.println(config);
        System.out.println(config.getUserProperties());
        System.out.println(HttpSession.class.getName());
        System.out.println(config.getUserProperties().get(HttpSession.class.getName()));
        this.httpSession=(HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        username="null";
        if(httpSession==null){
            username="none";
        }else{
            try{
                username=(String) httpSession.getAttribute("username");
            }catch (Exception e){
                System.out.println("NO");

            }
            if(username==null){
                System.out.println("ERROR");
                username="null";
                username+= Integer.toString(++cnt);
            }

        }
        System.out.println("username:" + username);
        //
        GSMap.put(username,this);
        addOnlineCount();
        System.out.println("有新连接加入，当前在线人数：" + getOnlineCount());
        try {
            // 连接后,需要重发一下上一次的谈话 ?
            // 留还是不留
            //
        } catch (Exception e) {
            System.out.println("IO异常");
        }
    }

    // 标注客户端关闭WebSocket服务端点调用方法
    @OnClose
    public void onClose() {

        if(this.username==null){
            System.out.println("???");
        }else{
            GSMap.remove(this.username);
        }

        subOnlineCount();
        System.out.println("有连接关闭，当前在线人数：" + getOnlineCount());
    }

    // 标注客户端发送消息，WebSocket服务端点调用方法
    @OnMessage
    public void onMessage(String message, Session session)  {
        System.out.println("from "+ username + ":" + message);
        JSONObject json=JSONObject.parseObject(message);
        //
        String from = json.getString("from");

        // 对message 进行一些修改
        try {
            broadcastAllUsers(message,new HashSet<String>());
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    //标注客户端请求WebSocket服务端点发生异常调用方法
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        GroupService.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        GroupService.onlineCount--;
    }
    private void broadcastAllUsers(String message, Set<String> remove) throws IOException {
        if(remove==null){
            remove=new HashSet<>();
        }
        for(String uname : GSMap.keySet()){
            if(remove.contains(uname)) continue;
            GroupService service = GSMap.get(uname);
            service.sendMessage(message);
        }

    }
}


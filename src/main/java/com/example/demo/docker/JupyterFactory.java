package com.example.demo.docker;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


@Service
public class JupyterFactory {

    // 创建一个 docker 容器 运行 jupyter 服务
    @Autowired
    RestTemplate restTemplate;
    @Value("${docker.url}")
    private String ip;


    @Value("${docker.port}")
    private String port;

    static List<String> cons=new ArrayList<>();
    public static class JupyterContainerJSON{
        /*
        {
            "Image": "jupyter/base-notebook",
            "Env": ["LabApp.token=''"],
            "PortBindings": { "8888/tcp": [{ "HostPort":"8888"}]},
            "StdinOnce" : false,
            "OpenStdin" : true
        }
         */

        static String round(String str){
            return "\"" + str +"\"";
        }


        public String getImage() {
            //"Image": "jupyter/base-notebook"
            return round("Image")+":" + round(Image);
        }
        public String getEnv() {
            //"Env": ["LabApp.token=''"],

            return round("Env") +": []";
        }
        public String getOutPort() {
            //"PortBindings": { "8888/tcp": [{ "HostPort":"8888"}]},
            return round("PortBindings") + ":" +
                    "{"
                    + round("8888/tcp") +":" + "[{" + round("HostPort") +":" + round(outPort)+"}]}";
        }
        private final String Image;
        private String []Env;
        private final String outPort;

        //
        public JupyterContainerJSON(String Image,String []Env,String outPort){
            //check

            this.Image=Image;
            this.Env=Env;
            this.outPort=outPort;
        }
        //

        public String toJSON(){
            return "{\n" +
                    getImage() +",\n"+
                    getEnv() +",\n"+
                    getOutPort() +",\n"+
                    "\"StdinOnce\" : false,\n" +
                    "\"OpenStdin\" : true\n"
                    +"}";
        }

    }

    public String makeJupyter(String image, String []envs ,String outPort){
        // url = containers/create
        /* 通过 curl 创建的方法
        curl -X POST -H "Content-Type: application/json" -d '{
        "Image": "jupyter/base-notebook",
        "Env": ["LabApp.token=''"],
        "PortBindings": { "8888/tcp": [{ "HostPort": "8888" }] },
        "StdinOnce" : false,
        "OpenStdin" : true
        }' http://localhost:2375/containers/create
         */
        String path="http://"+ip+":"+port+"/containers/create";
        // 进行检查
        // 现在无

        System.out.println(path);
        // 设置 json
        String json=new JupyterContainerJSON(image,envs,outPort).toJSON();
        // 设置请求
        String id=post(path,json);

        try{
            id= new JSONObject(id).getString("Id");
        }catch (Exception e){
            e.printStackTrace();
            return "ERROR : 1";
        }
        //

        //2 利用 id 启动
        //containers/(id)/start
        path="http://"+ip+":"+port+"/containers/" +id +"/start";
        if(post(path,"{}")!=null){
            return "ERROR:2";
        }
        cons.add(id);

        // 利用id获得 token
        // 3.1 exec
        /*
        curl -XPOST -H "Content-Type: application/json"
         http://localhost:2375/containers/54c/exec -d
         '{"AttachStdin": false,
         "AttachStdout": true,
         "AttachStderr":true,
         "Tty":false,
         "Cmd": ["jupyter-notebook","list"]
         }'
         */

        json ="{\"AttachStdin\": false,\n" +
                "         \"AttachStdout\": true,\n" +
                "         \"AttachStderr\":true,\n" +
                "         \"Tty\":false,\n" +
                "         \"Cmd\": [\"jupyter-notebook\",\"list\"]\n" +
                "         }";
        path="http://"+ip+":"+port+"/containers/" +id +"/exec";

        String idexec=post(path,json);
        try {
            idexec= new JSONObject(idexec).getString("Id");
        }catch (Exception e){
            e.printStackTrace();
            return "ERROR:3 \n" +idexec;
        }
        //3.2
        /*
        curl -XPOST -H "Content-Type: application/json"
         http://localhost:2375/exec/0ab718a347450bd0a1911125bdb2c6cece0bb974be017c3089d4666180057770/start
         -d '{"Detach":false,"Tty":false}'
         */
        try {
            Thread.sleep(1000);
        }catch (Exception e){

        }
        path="http://"+ip+":"+port+"/exec/" +idexec +"/start";
        json="{\"Detach\":false,\"Tty\":false}";
        String tokenJSON=post(path,json);
        System.out.println(path);
        System.out.println(tokenJSON);

        String tmpurl=tokenJSON.replace("0.0.0.0:8888",ip+":"+outPort);
        int l =tmpurl.indexOf("http");
        int r =tmpurl.indexOf("::");

        return tmpurl.substring(l,r);

    }

    public String post(String url,String json){
        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json");
        httpHeaders.setContentType(type);

        HttpEntity<String> requestEntity = new HttpEntity<String>(json, httpHeaders);
        ResponseEntity<String> responseEntity=null;
        try{
            responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        }catch (Exception e ){
            e.printStackTrace();
            return "ERROR";
        }
        return responseEntity.getBody();
    }

}

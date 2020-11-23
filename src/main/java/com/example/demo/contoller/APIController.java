package com.example.demo.contoller;


import com.example.demo.entity.Code;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class APIController {



    @Autowired
    private Producer kapchaProducer;
    @RequestMapping("/api/toRun")
    public String API_toRun(@ModelAttribute("codeDomain") Code codeDomain){
        return "run";
    }


    @RequestMapping("/api/run")
    @ResponseBody
    public String API_run(@ModelAttribute("codeDomain") Code codeDomain){

        return Code.run(codeDomain.getLang(),codeDomain.getSource());
    }


    @RequestMapping("/api/getIdentifyingCode")
    public void getIdentifyingCode(HttpServletResponse response,
                                   HttpSession session){
        // 生成验证码
        String text=kapchaProducer.createText();
        BufferedImage image = kapchaProducer.createImage(text);

        //将验证码存入session
        //
        //
        session.setAttribute("identifyingCode",text);
        response.setContentType("image/png");
        try {
            OutputStream os =response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

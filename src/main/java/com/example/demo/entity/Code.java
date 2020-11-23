package com.example.demo.entity;


public class Code {

    private String author;



    private String lang;
    private String source;

    public String getAuthor() {
        return author;
    }

    public String getLang() {
        return lang;
    }

    public String getSource() {
        return source;
    }

    public void setAuthor(String author) {this.author=author;}
    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public static String run(String lang,String source){
        if(lang==null || lang.equals("")) return "ERROR: NO LANG";
        if(source==null || "".equals(source)) return "ERROR:NO source";
        return "ok:0";
    }

}

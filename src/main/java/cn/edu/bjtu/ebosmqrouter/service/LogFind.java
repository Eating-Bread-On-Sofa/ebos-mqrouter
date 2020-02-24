package cn.edu.bjtu.ebosmqrouter.service;

public interface LogFind {
    String read(String key, String value);
    String readAll();
}

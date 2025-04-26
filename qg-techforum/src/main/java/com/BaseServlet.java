package com;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet{
    //封装一个方法，用于发送json响应
    public void sendJsonResponse(HttpServletResponse res, Object data) throws IOException {
        //设置编码格式和响应类型
        res.setContentType("application/json;charset=utf-8");
        //获取输出流，用于向客户端发送响应
        PrintWriter out = res.getWriter();
        //将对象转换为json字符串
        Gson gson = new Gson();
        String json = gson.toJson(data);
        //将json字符串发送给客户端
        out.print(json);
        //关闭输出流
        out.flush();
        out.close();
    }
}

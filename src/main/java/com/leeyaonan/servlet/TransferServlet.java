package com.leeyaonan.servlet;

import com.leeyaonan.factory.BeanFactory;
import com.leeyaonan.factory.ProxyFactory;
import com.leeyaonan.pojo.Result;
import com.leeyaonan.service.TransferService;
import com.leeyaonan.service.impl.TransferServiceImpl;
import com.leeyaonan.utils.JsonUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Leeyaonan
 * @Date 2020/4/12 12:09
 */
@WebServlet(name = "transferServlet", urlPatterns = "/transferServlet")
public class TransferServlet extends HttpServlet {

    // 1. 实例化service层对象
//    private TransferService transferService = new TransferServiceImpl();

    // 2020年4月13日 针对问题一的改造，将dao层的实现由直接new改为从BeanFactory中获取
//    private TransferService transferService = (TransferService) BeanFactory.getBean("transferService");

    // 2020年4月13日 从工厂中获取委托对象（委托对象是增强了事务控制的功能）

    // 首先从BeanFactory获取到proxyFactory代理工厂的实例化对象
//    private ProxyFactory proxyFactory = (ProxyFactory) BeanFactory.getBean("proxyFactory");
//    private TransferService transferService = (TransferService) proxyFactory.getJdkProxy(BeanFactory.getBean("transferService")) ;

    private TransferService transferService = null;


    @Override
    public void init() throws ServletException {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        ProxyFactory proxyFactory = (ProxyFactory) webApplicationContext.getBean("proxyFactory");
        transferService = (TransferService) proxyFactory.getJdkProxy(webApplicationContext.getBean("transferService"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 设置请求体的字符编码
        req.setCharacterEncoding("UTF-8");

        String fromCardNo = req.getParameter("fromCardNo");
        String toCardNo = req.getParameter("toCardNo");
        String moneyStr = req.getParameter("money");
        int money = Integer.parseInt(moneyStr);

        Result result = new Result();

        // 调用service层方法
        try {
            transferService.transfer(fromCardNo, toCardNo, money);
            result.setStatus("200");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 响应
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(JsonUtils.object2Json(result));

    }
}

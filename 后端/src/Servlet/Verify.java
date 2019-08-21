package Servlet;

import Pojo.Admin;
import Pojo.Teacher;
import Pojo.User;
import Utils.DataBase;
import Utils.SocketPush;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "Servlet.Verify")
public class Verify extends HttpServlet {
    private List<Teacher> teachers = null;
    private List<User> users = null;
    private List<Admin> admins = null;
    private int flag;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");
        SqlSession sqlSession = DataBase.getSqlSession();
        if (userType.equals("学生")) {
            admins = null;
            teachers = null;
            users = sqlSession.selectList("UserMapper.verify", username);
        } else if (userType.contains("教师")) {
            admins = null;
            users = null;
            teachers = sqlSession.selectList("TeacherMapper.verify", username);
        } else if (userType.contains("管理员")) {
            users = null;
            teachers = null;
            admins = sqlSession.selectList("AdminMapper.verify", username);
        }
        /**
         *
         *  FLAG = 1 未找到
         *  FLAG = 2 成功
         *  FLAG = 3 密码错误
         *
         * */
        if (users != null) {

            if (users.size() == 0) {
                flag = 1;
            } else {
                if (password.equals((users.get(0)).getPassword())) {
                    flag = 2;
                } else {
                    flag = 3;
                }
            }
        } else if (teachers != null) {
            if (teachers.size() == 0) {
                flag = 1;
            } else {
                if (password.equals((teachers.get(0)).getPassword())) {
                    flag = 2;
                } else {
                    flag = 3;
                }
            }
        } else if (admins != null) {
            if (admins.size() == 0) {
                flag = 1;
            } else {
                if (password.equals((admins.get(0)).getPassword())) {
                    flag = 2;
                } else {
                    flag = 3;
                }
            }
        }
        //构造返回信息
        Map<String, Object> re = new HashMap<>();
        if (flag == 1) {
            System.out.println("账户不存在：（" + userType + "账号" + ":" + username +"PW:"+password+")");
            re.put("status", "unError");
        } else if (flag == 2) {
            //控制台日志
            System.out.println("登陆成功：（" + userType + "账号" + ":" + username +"PW:"+password+")");
            //写入UUID;
            String uuid = UUID.randomUUID().toString().replace("-","");
            if(SocketPush.uuids.containsKey(username+userType))
            {
                //什么也不用做
                //是不可能的
                //session.invalidate();
            }
            else
            {
                //第一次登录
                SocketPush.uuids.put(username+userType,uuid);
            }
            re.put("status", "success");
            re.put("uuid", uuid);
            re.put("userType", userType);
            if (users != null) {
                re.put("data", users.get(0));
            } else if (teachers != null) {
                re.put("data", teachers.get(0));
            } else if (admins != null) {
                re.put("data", admins.get(0));
            }
            session.setAttribute("loginData", re);
        } else if (flag == 3) {
            //控制台日志
            System.out.println("错误验证账户：（" + userType + "ID:" + username +"PW:"+password+")");
            re.put("status", "pwError");
        } else {
            re.put("status", "unKnowError");
        }
        response.getWriter().write(JSON.toJSONString(re));

        //根据网上教程 sql可不关闭
        // sqlSession.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    }
}

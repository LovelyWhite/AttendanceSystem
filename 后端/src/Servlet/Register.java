package Servlet;

import Pojo.User;
import Utils.DataBase;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Servlet.Register")
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        SqlSession sqlSession =  DataBase.getSqlSession();
        List<User> result =sqlSession.selectList("UserMapper.verify",username);
        if(result.size()==0)
        {
            User temp = new User();
            temp.setUsername(username);
            temp.setPassword(password);
            if( 1==sqlSession.insert("UserMapper.insertUser",temp))
            {
                sqlSession.commit();
                response.getWriter().write("success");
            }
            else
            {
                response.getWriter().write("failed");
            }
        }
        else
        {
            response.getWriter().write("userExist");
        }
        sqlSession.close();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

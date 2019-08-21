package Servlet;

import Model.SchoolCourse;
import Model.SchoolInformation;
import Pojo.*;
import Pojo.Class;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import static Utils.Utils.MD5Enc;
import static Utils.Utils.getUserPic;

@WebServlet(name = "LinkSchool")
public class LinkSchool extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User loggedUser = (User) (((Map<String, Object>) request.getSession().getAttribute("loginData")).get("data"));
        String schoolId = request.getParameter("schoolid");
        String phone = request.getParameter("phone");
        //这里不用穿这些信息，可以直接从后台获取到
        // String schoolPw = request.getParameter("schoolpw");
        //  String sex = request.getParameter("sex");
        String option = request.getParameter("option");
        String strkey = schoolId + "DAFF8EA19E6BAC86E040007F01004EA";
        strkey = MD5Enc(strkey);
        if (option.equals("getcourse")) {
            String url = "http://123.15.36.138:8008/zfmobile_port/webservice/jw/EducationalPortXMLService";
            String data = "<v:Envelope xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:d=\"http://www.w3.org/2001/XMLSchema\" xmlns:c=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:v=\"http://schemas.xmlsoap.org/soap/envelope/\"><v:Header /><v:Body><n0:StudentInfoSearch id=\"o0\" c:root=\"1\" xmlns:n0=\"http://service.jw.com/\"><sid i:type=\"d:string\">" + schoolId + "</sid><count i:type=\"d:string\">0</count><strKey i:type=\"d:string\">" + strkey + "</strKey></n0:StudentInfoSearch></v:Body></v:Envelope>";
            OutputStream outObject;
            URL u = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) u.openConnection();
            // 设置HTTP请求相关信息
            httpConn.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            outObject = httpConn.getOutputStream();
            outObject.write(data.getBytes());
            DataOutputStream dos = new DataOutputStream(outObject);
            dos.write(data.getBytes(StandardCharsets.UTF_8));
            dos.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            StringBuilder strBuf = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                strBuf.append(line);
            }
            dos.close();
            reader.close();
            String result = strBuf.toString();
            int start = result.indexOf("<return>");
            int end = result.indexOf("</return>");
            if (start != -1 && end != -1) {
                if (start + 100 == end)//判断是否为空
                {
                    response.getWriter().write("failed");
                } else {
                    loggedUser.setSchoolid(schoolId);
                    //loggedUser.setSchoolpw(schoolPw);
                    //可以不用密码
                    SchoolInformation schoolUser;
                    result = result.substring(start + 8, end);
                    schoolUser = Utils.Utils.resolveLogin(result);
                    response.getWriter().write(JSON.toJSONString(schoolUser));
                    //设置数据库用户信息
                    loggedUser.setName(schoolUser.getName());
                    loggedUser.setClassid(schoolUser.getClas());
                    loggedUser.setSex(schoolUser.getSex());
                    loggedUser.setPhone(phone);
                    loggedUser.setPic("data:img/jpg;base64," + getUserPic(schoolId));
                    //获取课表
                    ArrayList<SchoolCourse> courses = Utils.Utils.getSyllabus(loggedUser.getSchoolid());
                    SqlSession sqlSession = Utils.DataBase.getSqlSession();
                    //更新用户存储的个人信息
                    sqlSession.update("UserMapper.updateSchool", loggedUser);


                    //更新数据库中的班级
                    //构造班级
                    Class classTemp = new Class();
                    classTemp.setClassid(schoolUser.getClas());
                    classTemp.setCollege(schoolUser.getXy());
                    classTemp.setMajor(schoolUser.getMajor());
                    classTemp.setGrade(schoolUser.getGrade());
                    sqlSession.insert("ClassMapper.addClass", classTemp);
                    //更新数据库中课程数据&加课到当前用户

                    //先删除当前用户课程表中所有数据
                    sqlSession.delete("SyllabusMapper.deleteAll", loggedUser.getUsername());
                    //添加课程表数据
                    //构造默认老师账号数据
                    //构造地点数据
                    for (SchoolCourse i : courses) {
                        Syllabus syllabus = new Syllabus();
                        syllabus.setCid(i.getCourseid());
                        syllabus.setUid(loggedUser.getUsername());
                        sqlSession.insert("SyllabusMapper.addCourses", syllabus);
                        String[] ts =  i.getTusername().split("/");
                        String tx = "";
                        for( int j=0;j<ts.length;j++)
                        {
                            String hc = String.valueOf(ts[j].hashCode());
                            Teacher teacher = new Teacher();
                            teacher.setName(ts[j]);
                            teacher.setUsername(hc);
                            sqlSession.insert("TeacherMapper.insertTeacher", teacher);
                            tx+=hc;
                            if(j<ts.length-1)
                                tx+="/";
                        }
                        i.setTusername(tx);
                        Dest dest = new Dest();
                        dest.setDid(i.getDest().hashCode());
                        dest.setDest(i.getDest());
                        Course course = new Course(i);
                        sqlSession.insert("DestMapper.insertDest",dest);
                        sqlSession.insert("CourseMapper.addCourses", course);
                    }
                    sqlSession.commit();
                    // sqlSession.close();
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    }
}

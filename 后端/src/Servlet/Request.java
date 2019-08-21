package Servlet;

import Model.AbClassResult;
import Model.TDest;
import Model.TeacherCourseView;
import Pojo.*;
import Utils.DataBase;
import Utils.FullCalendar;
import Utils.SocketPush;
import Utils.Utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("unchecked")
@WebServlet(name = "Request")
public class Request extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        SqlSession sqlSession = DataBase.getSqlSession();
        String dataType = request.getParameter("dataType");
        String requestType = request.getParameter("requestType");
        if (requestType.equals("query")) {
            if (dataType.equals("teacher")) {
                List<Teacher> teachers = sqlSession.selectList("TeacherMapper.selectAllTeacher");
                response.getWriter().write(JSON.toJSONString(teachers));
                sqlSession.close();
            } else if (dataType.equals("teacherCourses")) {
                ArrayList<TeacherCourseView> teacherCourseView = new ArrayList<>();
                //找到某位老师所有的课
                List<Course> courses = sqlSession.selectList("CourseMapper.selectCourseByTid", ((Teacher) (((Map<String, Object>) session.getAttribute("loginData")).get("data"))).getUsername());
                //根据某一项具体的课来找学生
                for (int i = 0; i < courses.size(); i++) {
                    TeacherCourseView t = new TeacherCourseView();
                    List<String> stuIds = sqlSession.selectList("SyllabusMapper.selectCourseStu", courses.get(i).getCourseid());
                    List<User> stus= new ArrayList<>();
                    for(int j = 0;j<stuIds.size();j++)
                    {
                        stus = sqlSession.selectList("UserMapper.selectUserByUsername",stuIds);
                    }
                    t.setStudents(stus);
                    t.setTeacherName(((Teacher) (((Map<String, Object>) session.getAttribute("loginData")).get("data"))).getName());
                    t.setCourseInformation(courses.get(i));
                    t.setStuNum(stus.size());
                    //判断是否存在该课
//                    boolean exist = false;
//                    for (TeacherCourseView aTeacherCourseView : teacherCourseView) {
//                        if (aTeacherCourseView.getCourseInformation().getCourseName().equals(t.getCourseInformation().getCourseName())) {
//                            exist = true;
//                        }
//                    }
//                    if(!exist)
//                    {
//                        teacherCourseView.add(t);
//                    }
                    teacherCourseView.add(t);
                }
                response.getWriter().write(JSON.toJSONString(teacherCourseView, SerializerFeature.DisableCircularReferenceDetect));
               // sqlSession.close();
            } else if (dataType.equals("course")) {
                String username = ((User) (((Map<String, Object>) session.getAttribute("loginData")).get("data"))).getUsername();
                List<Course> courses = sqlSession.selectList("CourseMapper.selectCourses", username);
                List<FullCalendar> f = new ArrayList<>();
                Calendar c = Calendar.getInstance();
                int rqWeek = -1;
                c.set(2019, Calendar.FEBRUARY, 24, 0, 0);
                int t1 = c.get(Calendar.DAY_OF_YEAR);
                c.setTime(new Date(Long.parseLong(request.getParameter("start"))));
                int t2 = c.get(Calendar.DAY_OF_YEAR);
                rqWeek = ((t2 - t1) / 7) + 1;
                //开始构造
                //2019年2月24日 为今年第一周
                Calendar start = Calendar.getInstance();
                int in = 0;
                for (Course i : courses) {
                    boolean now = false;
                    String[] w = i.getWorkweek().split(",");
                    for (String aW : w) {
//                        String a = w[j];
//                        String b = String.valueOf(i.getCurrentweek());
                        if (aW.equals(String.valueOf(rqWeek))) {
                            now = true;
                            break;
                        }
                    }
                    if (now)//说明该课程在已经开始上了
                    {
                        FullCalendar temp = new FullCalendar();
                        start.set(2019, Calendar.FEBRUARY, 25, 8, 30, 0);
                        start.add(Calendar.DATE, 7 * (rqWeek - 1) + Integer.parseInt(i.getClassweek() )- 1);
                        String time = i.getTime();
                        if (time.equals("3,4")) {
                            start.set(Calendar.HOUR_OF_DAY, 10);
                            start.set(Calendar.MINUTE, 30);
                        } else if (time.equals("5,6")) {
                            start.set(Calendar.HOUR_OF_DAY, 14);
                            start.set(Calendar.MINUTE, 30);
                        } else if (time.equals("7,8")) {
                            start.set(Calendar.HOUR_OF_DAY, 16);
                            start.set(Calendar.MINUTE, 0);
                        } else if (time.equals("9,10")) {
                            start.set(Calendar.HOUR_OF_DAY, 19);
                            start.set(Calendar.MINUTE, 30);
                        }
                        temp.setTitle(i.getName() + "  " + i.getDescription().split(",")[3] + "\n" + i.getDescription().split(",")[4]);
                        temp.setStart(start.getTime().getTime());
                        start.add(Calendar.MINUTE, 90);
                        temp.setEnd(start.getTime().getTime());
                        if (in == 0) {
                            temp.setBackgroundColor("#999900");
                            temp.setBorderColor("#999900");
                        } else if (in == 1) {
                            temp.setBackgroundColor("#990033");
                            temp.setBorderColor("#990033");
                        }
                        //temp.setBackgroundColor("#FFCC00");

                        if (in == 2) {
                            in = 0;
                        }
                        in++;
                        f.add(temp);
                    }
                }
                if (f.size() == 0) {
                    response.getWriter().write("nodata");
                } else
                    response.getWriter().write(JSON.toJSONString(f));
            } else if (dataType.equals("getClassStu")) {
                String userClassId = ((User) (((Map<String, Object>) session.getAttribute("loginData")).get("data"))).getClassid();
                List<User> users = sqlSession.selectList("UserMapper.getClassStu", userClassId);
                if (users.size() != 0) {
                    response.getWriter().write(JSON.toJSONString(users));
                } else {
                    response.getWriter().write("noData");
                }
            } else if (dataType.equals("getLogin")) {
                response.getWriter().write(JSON.toJSONString((((Map<String, Object>) session.getAttribute("loginData")).get("data"))));
            } else if (dataType.equals("sign")) {
                //经纬度信息 用于 定位
                double lng = Double.parseDouble(request.getParameter("lng"));
                double lat = Double.parseDouble(request.getParameter("lat"));
                //地点信息 用于 查询地点坐标
                String signDid = request.getParameter("did");
                //cid 课程id 用于 增加 sign 记录
                String signCid =request.getParameter("cid");
                //用于传送信息给老师 找到老师的socket&存入数据库
                String signTid =request.getParameter("tid");
                //用户名 用于 sign 的信息组成 传给老师，作为记录
                String signUid = request.getParameter("username");
                //notice id 用于查看消息是否过期
                String signNid = request.getParameter("nid");

                int signSize =  sqlSession.selectList("NoticeMapper.selectOutDateByNid",Long.parseLong(signNid)).size();
                if(signSize==0)
                {
                    //查找当前signDid信息
                    List<Dest> dests =  sqlSession.selectList("DestMapper.selectDestByDid",Long.parseLong(signDid));
                    double dLng =  dests.get(0).getLongitude();
                    double dLat =  dests.get(0).getLatitude();
                    //计算该地点是否在范围内
                    //判断两点之间距离是否小于 xx米。
                    //  System.out.println(Utils.getDistance(lat, lng, dLat, dLng));
                    double distance =Utils.getDistance(lat, lng, dLat, dLng);
                    //大于100m
                    if(distance>=100)
                    {
                        response.getWriter().write("notInArea");
                    }
                    else
                    {
                        JSONObject match = Utils.match(((User) (((Map<String, Object>) session.getAttribute("loginData")).get("data"))).getPic()
                                , request.getParameter("dataImg"));

                        try {
                            @SuppressWarnings("ConstantConditions")
                            JSONObject result = new JSONObject(match.get("result").toString());
                            //当匹配程度超过80判断是本人
                            if (result.getInt("score") > 80) {
                                //向老师的socket传送成功信息,其中包含sign
                                SocketPush socketPush =SocketPush.pushMap.get(SocketPush.uuids.get(signTid+"教师"));
                                //在数据库stuSigns表中增加一条记录
                                StuSign sign = new StuSign();
                                sign.setCid(Long.parseLong(signCid));
                                sign.setUid(Long.parseLong(signUid));
                                sign.setNid(Long.parseLong(signNid));
                                sqlSession.insert("SignMapper.addSign",sign);
                                //更新未读状态为已读状态
                                sqlSession.update("NoticeMapper.updateOutDateByNid",Long.parseLong(signNid));
                                sqlSession.commit();
                                if(socketPush!=null)
                                {
                                    socketPush.sendData("signSuccess",JSON.toJSONString(sign));
                                }
                                response.getWriter().write("success");
                            } else {
                                response.getWriter().write("failed");
                            }
                        } catch (org.json.JSONException e) {
                            if (match != null) {
                                response.getWriter().write(match.getString("error_msg"));
                            }
                            else
                            {
                                response.getWriter().write("link error");
                            }
                        }
                    }
                }
                else
                {
                    response.getWriter().write("outOfDate");
                }


            }
            else if(dataType.equals("unRead"))
            {
                Map<String, String> params = new HashMap<>();
                params.put("uid",((UserBase)((Map<String, Object>)session.getAttribute("loginData")).get("data")).getUsername());
                params.put("type",((String)((Map<String, Object>)session.getAttribute("loginData")).get("userType")));
                List<Unread> unreads =  sqlSession.selectList("UnReadMapper.selectAllUnReadByUser",params);
//                System.out.println(unreads.size());
                List<Notice> notices = null;
                if(unreads.size()!=0)
                {
                    notices =  sqlSession.selectList("NoticeMapper.selectNoticesByNid",unreads);
               //     System.out.println(notices.get(0).getData());
                    response.getWriter().write(JSON.toJSONString(notices));
                }
                else
                {
                    response.getWriter().write("noData");
                }
//                System.out.println(notices.size());
            } else if(dataType.equals("allDests")) {
                List<TDest> tDests = new ArrayList<>();
                List<Dest> dests = sqlSession.selectList("DestMapper.selectAllDest");
                //封装数据
//                dests:[
//                {
//                    'dest':'4号楼',
//                        'longitude':'1234.1',
//                        'latitude':'1222.1',
//                        'macs':
//                                    [
//                    {
//                        'mac':'11111111',
//                            'db':'23',
//                    },
//                    {
//                        'mac':'22',
//                            'db':'33',
//                    }
//                                    ]
//                },
                //查找到的地点
                //优化：先拿到数据再查
                dests.forEach((e)->
                {
                    List<Mac> macs = sqlSession.selectList("MacMapper.selectMacByDid",e.getDid());
                    TDest tDest = new TDest();
                    tDest.setMacs(macs);
                    tDest.setDid(e.getDid());
                    tDest.setDest(e.getDest());
                    tDest.setLongitude(e.getLongitude());
                    tDest.setLatitude(e.getLatitude());
                    tDests.add(tDest);
                });
                if(tDests.size()!=0)
                {
                    response.getWriter().write(JSON.toJSONString(tDests,SerializerFeature.DisableCircularReferenceDetect));
                }
                else
                {
                    response.getWriter().write("noData");
                }
            }
            else if(dataType.equals("stuCurrentSigns"))
            {
               long cid = Long.parseLong(request.getParameter("cid"));
               long nid = Long.parseLong(request.getParameter("nid"));
               Map<String,Long> para = new HashMap<>();
               para.put("cid",cid);
               para.put("nid",nid);
               List<StuSign> stuSigns = sqlSession.selectList("SignMapper.selectSignsByCidAndNid",para);
               response.getWriter().write(JSON.toJSONString(stuSigns,SerializerFeature.DisableCircularReferenceDetect));
               System.out.println(stuSigns.size());
            }
            else if(dataType.equals("classAbTime"))
            {
                long uid = Long.parseLong(request.getParameter("uid"));
                List<AbClassResult> signData = sqlSession.selectList("SyllabusMapper.selectAbByUid",uid);
                System.out.println(signData.size());
                response.getWriter().write(JSON.toJSONString(signData));
            }
            else if(dataType.equals("TClassAbTime")) {
                long tid = Long.parseLong(request.getParameter("tid"));
                List<AbClassResult> signData = sqlSession.selectList("SyllabusMapper.selectAbByUid", tid);
                response.getWriter().write(JSON.toJSONString(signData));
            }
        } else if (requestType.equals("delete")) {
            if (dataType.equals("teacher")) {
                String data = request.getParameter("data");
                String[] temp = data.split(",");
                ArrayList e = new ArrayList<>(Arrays.asList(temp));
                int a = sqlSession.delete("TeacherMapper.deleteTeacherByUserName", e);
                if (a == e.size()) {
                    response.getWriter().write("success");
                } else {
                    response.getWriter().write("failed");
                }
                sqlSession.commit();
            }
        } else if (requestType.equals("insert")) {
            if (dataType.equals("teacher")) {
                String data = request.getParameter("data");
                StringBuilder sb = new StringBuilder(data);
                sb.insert(0, '[');
                sb.append(']');
                List<Teacher> teachers = JSON.parseArray(sb.toString(), Teacher.class);
                List<Teacher> result = sqlSession.selectList("TeacherMapper.verify", teachers.get(0).getUsername());
                if (result.size() == 0) {
                    if (1 == sqlSession.insert("TeacherMapper.insertTeacher", teachers.get(0))) {
                        sqlSession.commit();
                        response.getWriter().write("success");
                    } else {
                        response.getWriter().write("failed");
                    }

                } else {
                    response.getWriter().write("userExist");
                }
            }
        } else if (requestType.equals("update")) {
            if (dataType.equals("teacher")) {
                String data = request.getParameter("data");
                StringBuilder sb = new StringBuilder(data);
                sb.insert(0, '[');
                sb.append(']');
                List<Teacher> teachers = JSON.parseArray(sb.toString(), Teacher.class);
                List<Teacher> result = sqlSession.selectList("TeacherMapper.updateTeacher", teachers.get(0));
                if (result.size() == 0) {
                    if (1 == sqlSession.update("TeacherMapper.updateTeacher", teachers.get(0))) {
                        sqlSession.commit();
                        response.getWriter().write("success");
                    } else {
                        response.getWriter().write("failed");
                    }

                } else {
                    response.getWriter().write("userExist");
                }
            }
            else if(dataType.equals("dest"))
            {
                String did = request.getParameter("did");
                String longitude = request.getParameter("longitude");
                String latitude = request.getParameter("latitude");
                Dest dest = new Dest();
                dest.setDid(Integer.parseInt(did));
                dest.setLongitude(Double.parseDouble(longitude));
                dest.setLatitude(Double.parseDouble(latitude));
                if(1 == sqlSession.update("DestMapper.updateDestByDid", dest))
                {
                    response.getWriter().write("success");
                }
                else
                {
                    response.getWriter().write("failed");
                }
                sqlSession.commit();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    }
}

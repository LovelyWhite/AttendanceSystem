package Utils;

import Pojo.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

//使用username+userType来做标志
//将每个Socket进程加入HashMap集合
@ServerEndpoint("/server/webSocket/{uuid}/{username}/{type}")
public class SocketPush{
    public static HashMap<String,SocketPush> pushMap = new HashMap<>();
    public static HashMap<String,String> uuids = new HashMap<>();
    SqlSession sqlSession = DataBase.getSqlSession();
    //学生在线数量
    public static int onlineCount=0;
    //是否验证过
    //如果验证过就不需要在验证了
    private boolean isVerify=false;
    private Session session;
    private String type;

    @OnOpen
    public void onOpen(@PathParam("uuid") String uuid ,@PathParam("username") String username,@PathParam("type") String type , Session session){
        //解决二次登录问题
        //自动下线
        this.session =session;
        if(type.equals("null"))
        {
            if(pushMap.get(uuid)!=null&&pushMap.get(uuid).isVerify)
            {
                this.type = pushMap.get(uuid).type;
                pushMap.replace(uuid,this);
                isVerify = true;
                sendData("inf","socketEstablish");

            }
            else
            {
                sendData("inf","socketFailed");
            }
        }
        else
        {
            //当存在登录
            //推送下线请求
            //每次登录都会新发起一个uuid
            //当不存在的时候加入uuids
            //当存在的时候什么也不做
            if(pushMap.get(uuids.get(username+type))!=null)//该username已登录
            {
                String tempUUID = uuids.get(username+type);
                //替换当前uuid
                uuids.replace(username+type,uuid);
                //发送下线请求
                pushMap.get(tempUUID).sendData("request","logout");
                isVerify = true;
                this.type = type;
                sendData("inf","socketEstablish");
                pushMap.remove(tempUUID);
                pushMap.put(uuid,this);
            }
            else
            {
                //未登录
//                System.out.println(uuid);
//                System.out.println(uuids.get(username + type));
                if(uuid.equals(uuids.get(username+type)))
                {
                    //当登录时设置登录状态
                    isVerify=true;
                    this.type = type;
                    pushMap.put(uuid,this);
                    onlineCount++;
                    sendData("inf","socketEstablish");
                }
                else
                {
                    sendData("inf","socketFailed");
                }
            }

        }
    }
    @OnClose
    public void onClose() {
  //      System.out.println("连接断开");
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        onlineCount--;
    }
    @OnMessage
    public void onMessage(String message) {
     //   System.out.println(message);
        JSONObject o = (JSONObject) JSON.parse(message);
        String type = o.getString("type");
        String data = o.getString("data");

        Notice notice = new Notice();
        JSONObject x = (JSONObject) JSON.parse(data);
        String sType = x.getString("sType");
        String inData = x.getString("data");
        String sendUsername = x.getString("sendUsername");
        String sendType = x.getString("sendType");
        String recvUsername = x.getString("recvUsername");
        String recvType = x.getString("recvType");

        if (type.equals("startSign")) {
            //构造notice数据
            notice.setSType(sType);
            notice.setSendUsername(sendUsername);
            notice.setSendType(sendType);
            notice.setRecvUsername(recvUsername);
            notice.setRecvType(recvType);
            notice.setType("startSign");
            //course下的signtime+1
            sqlSession.update("CourseMapper.updateSignTimeByCid",Long.parseLong(inData));
            //course下的ing = 1
            sqlSession.update("CourseMapper.updateIngByCid",Long.parseLong(inData));
            //course下的startTime
            sqlSession.update("CourseMapper.updateStartTimeByCid",Long.parseLong(inData));
            List<Course>  course = sqlSession.selectList("CourseMapper.selectCourseByCid",Long.parseLong(inData));
            course.get(0).setSigntime(course.get(0).getSigntime()+1);
            //System.out.println(JSON.toJSONString(course.get(0)));
            notice.setData(JSON.toJSONString(course.get(0)));
            Vector<String> extra = new Vector<>();
            List<Dest> dests =  sqlSession.selectList("DestMapper.selectDestByDid",course.get(0).getDid());

            //添加额外信息
            //经度纬度
            double dLng =  dests.get(0).getLongitude();
            double dLat =  dests.get(0).getLatitude();
            extra.add(String.valueOf(dLng));
            extra.add(String.valueOf(dLat));
            extra.add(String.valueOf(100));
            notice.setExtra(JSON.toJSONString(extra));
            List<String> stuIds = sqlSession.selectList("SyllabusMapper.selectCourseStu", inData);
            sqlSession.insert("NoticeMapper.addNotice", notice);
            Map<String,Long> map = new HashMap<>();
            map.put("nid",notice.getNid());
            map.put("cid",course.get(0).getCourseid());
            sqlSession.update("CourseMapper.updateNidByCid",map);
            sqlSession.update("SyllabusMapper.updateTotalTimeByCid",map.get("cid"));
            stuIds.forEach((i) -> {
                //离线推送
                //构造 unread数据
                Unread unread = new Unread();
                unread.setUid(i);
                unread.setType(notice.getRecvType());//接受者类型
                unread.setNid(notice.getNid());
                sqlSession.insert("UnReadMapper.addUnRead", unread);
                //在线推送
                String uuid = uuids.get(i + notice.getRecvType());
                if (uuid != null) {
                    //这里需要推送哪些消息？
                    pushMap.get(uuid).sendData("startSign", JSON.toJSONString(notice));
                }
            });
            sendData("startSignResult",String.valueOf(notice.getNid()));
        }
        else if(type.equals("stopSign"))
        {
            //inData：cid 课程号
            notice.setSType(sType);
            notice.setSendUsername(sendUsername);
            notice.setSendType(sendType);
            notice.setRecvUsername(recvUsername);
            notice.setRecvType(recvType);
            notice.setType("stopSign");
            //course下的ing = 0
            sqlSession.update("CourseMapper.updateIngByCid",Long.parseLong(inData));
            List<Long> nid = sqlSession.selectList("CourseMapper.selectNidByCid",Long.parseLong(inData));
            sqlSession.update("NoticeMapper.updateOutDateByNid",nid.get(0));
            sendData("inf", "stopSignSuccess");

            //停止签到后设置签到的同学签到+1
            Map<String,Long> map = new HashMap<>();
            map.put("nid",nid.get(0));
            map.put("cid",Long.parseLong(inData));
            List<StuSign> stuSigns = sqlSession.selectList("SignMapper.selectSignsByCidAndNid",map);
            stuSigns.forEach((e)->
            {
                long uid = e.getUid();
                Map<String,Long> para = new HashMap<>();
                para.put("uid",uid);
                para.put("cid",map.get("cid"));
                sqlSession.update("SyllabusMapper.updateSTimeByCidAndUid",para);
            });
            }
        sqlSession.commit();
    }
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    /*
     *socket 传送协议
     * 返回数据格式
     *   type(请求类型):inf(通知) punch(签到)
     *   data(数据):object
     */
    public void sendData(String type,String data)
    {
        HashMap<String,String> m = new HashMap<>();
        m.put("type",type);
        m.put("data",data);
        if(session.isOpen())
         session.getAsyncRemote().sendText(JSON.toJSONString(m));
    }
    //广播信息
    public static void boardCast(String type,String data)
    {
        HashMap<String,String> m = new HashMap<>();
        m.put("type",type);
        m.put("data",data);
        pushMap.forEach((k,v)->{
            if(v.session.isOpen()&&v.type.equals("学生"))
            v.session.getAsyncRemote().sendText(JSON.toJSONString(m));
        });
    }
}
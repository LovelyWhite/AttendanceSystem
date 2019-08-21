package Pojo;


import Model.SchoolCourse;

import java.sql.Timestamp;

public class Course {

    private long courseid;
    private String tusername;
    private String name;
    private String currentweek;
    private String property;
    private double credit;
    private String classweek;
    private String workweek;
    private String time;
    private String description;
    private long did;
    private long nid;//用于标记当前签到消息编号
    private String isodd;
    private int signtime;//用于标记签到次数
    private int ing;//用于当前标记签到是否结束
    private Timestamp starttime;//用于标记当前签到开始的时间

    public Course() {

    }

    public Course(SchoolCourse s) {
        courseid = s.getCourseid();
        tusername = s.getTusername();
        name = s.getName();
        currentweek = s.getCurrentweek();
        property = s.getProperty();
        credit = s.getCredit();
        classweek = s.getClassweek();
        workweek = s.getWorkweek();
        time = s.getTime();
        description = s.getDescription();
        did = s.getDest().hashCode();
        isodd = s.getIsodd();
    }

    public long getCourseid() {
        return courseid;
    }

    public void setCourseid(long courseid) {
        this.courseid = courseid;
    }


    public String getTusername() {
        return tusername;
    }

    public void setTusername(String tusername) {
        this.tusername = tusername;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCurrentweek() {
        return currentweek;
    }

    public void setCurrentweek(String currentweek) {
        this.currentweek = currentweek;
    }


    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }


    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }


    public String getClassweek() {
        return classweek;
    }

    public void setClassweek(String classweek) {
        this.classweek = classweek;
    }


    public String getWorkweek() {
        return workweek;
    }

    public void setWorkweek(String workweek) {
        this.workweek = workweek;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }


    public String getIsodd() {
        return isodd;
    }

    public void setIsodd(String isodd) {
        this.isodd = isodd;
    }

    public int getSigntime() {
        return signtime;
    }

    public void setSigntime(int signtime) {
        this.signtime = signtime;
    }

    public int getIng() {
        return ing;
    }

    public void setIng(int ing) {
        this.ing = ing;
    }

    public Timestamp getStarttime() {
        return starttime;
    }

    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    public long getNid() {
        return nid;
    }

    public void setNid(long nid) {
        this.nid = nid;
    }
}

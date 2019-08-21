package Model;

import Pojo.Course;
import Pojo.StuSign;
import Pojo.User;

import java.util.List;

public class TeacherCourseView
{

    Course courseInformation;
    List<User> students;
    List<StuSign> signs;
    String teacherName;
    int stuNum;

    public Course getCourseInformation() {
        return courseInformation;
    }

    public void setCourseInformation(Course courseInformation) {
        this.courseInformation = courseInformation;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public List<StuSign> getSigns() {
        return signs;
    }

    public void setSigns(List<StuSign> signs) {
        this.signs = signs;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getStuNum() {
        return stuNum;
    }

    public void setStuNum(int stuNum) {
        this.stuNum = stuNum;
    }
}
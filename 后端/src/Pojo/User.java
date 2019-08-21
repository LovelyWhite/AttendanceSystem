package Pojo;


public class User extends UserBase{

  private String classid;
  private String schoolid;
  private String schoolpw;
  private String phone;
  private String pic;
  private java.sql.Timestamp lastlogintime;
  private java.sql.Timestamp signtime;




  public String getClassid() {
    return classid;
  }

  public void setClassid(String classid) {
    this.classid = classid;
  }


  public String getSchoolid() {
    return schoolid;
  }

  public void setSchoolid(String schoolid) {
    this.schoolid = schoolid;
  }


  public String getSchoolpw() {
    return schoolpw;
  }

  public void setSchoolpw(String schoolpw) {
    this.schoolpw = schoolpw;
  }



  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }


  public String getPic() {
    return pic;
  }

  public void setPic(String pic) {
    this.pic = pic;
  }


  public java.sql.Timestamp getLastlogintime() {
    return lastlogintime;
  }

  public void setLastlogintime(java.sql.Timestamp lastlogintime) {
    this.lastlogintime = lastlogintime;
  }


  public java.sql.Timestamp getSigntime() {
    return signtime;
  }

  public void setSigntime(java.sql.Timestamp signtime) {
    this.signtime = signtime;
  }


}

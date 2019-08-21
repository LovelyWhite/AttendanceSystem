package Pojo;


public class Teacher extends UserBase {

  private String phone;
  private String level;
  private String description;
  private String pic;
  private java.sql.Timestamp signtime;
  private java.sql.Timestamp lastlogintime;
  private String noReadNotices;

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }


  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public String getPic() {
    return pic;
  }

  public void setPic(String pic) {
    this.pic = pic;
  }


  public java.sql.Timestamp getSigntime() {
    return signtime;
  }

  public void setSigntime(java.sql.Timestamp signtime) {
    this.signtime = signtime;
  }


  public java.sql.Timestamp getLastlogintime() {
    return lastlogintime;
  }

  public void setLastlogintime(java.sql.Timestamp lastlogintime) {
    this.lastlogintime = lastlogintime;
  }


  public String getNoReadNotices() {
    return noReadNotices;
  }

  public void setNoReadNotices(String noReadNotices) {
    this.noReadNotices = noReadNotices;
  }

}

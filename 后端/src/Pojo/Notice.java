package Pojo;


import com.mysql.cj.jdbc.Blob;

import java.util.Vector;

public class Notice {

  private long nid;//随机值
  private String type;//数据类型 sign 或者其他
  private String sType;//发送的范围 全体all 或者 个体 single
  private String sendUsername; // 发送账号
  private String sendType;//发送者类型 teacher
  private String recvUsername;//接受者 账号
  private String recvType;//接受者类型
  private String data;//发送的数据
  private String extra;//附加数据
  private java.sql.Timestamp time;//发送的时间
  private int outdate;


  public long getNid() {
    return nid;
  }

  public void setNid(long nid) {
    this.nid = nid;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getsType() {
    return sType;
  }

  public void setsType(String sType) {
    this.sType = sType;
  }

  public String getSType() {
    return sType;
  }

  public void setSType(String sType) {
    this.sType = sType;
  }


  public String getSendUsername() {
    return sendUsername;
  }

  public void setSendUsername(String sendUsername) {
    this.sendUsername = sendUsername;
  }


  public String getSendType() {
    return sendType;
  }

  public void setSendType(String sendType) {
    this.sendType = sendType;
  }


  public String getRecvUsername() {
    return recvUsername;
  }

  public void setRecvUsername(String recvUsername) {
    this.recvUsername = recvUsername;
  }


  public String getRecvType() {
    return recvType;
  }

  public void setRecvType(String recvType) {
    this.recvType = recvType;
  }


  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }


  public java.sql.Timestamp getTime() {
    return time;
  }

  public void setTime(java.sql.Timestamp time) {
    this.time = time;
  }

  public String getExtra() {
    return extra;
  }

  public void setExtra(String extra) {
    this.extra = extra;
  }
  public int getOutdate() {
    return outdate;
  }

  public void setOutdate(int outdate) {
    this.outdate = outdate;
  }
}

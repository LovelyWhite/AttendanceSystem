# AttendanceSystem
考勤系统（JavaWeb课设）

## 项目介绍

```txt
 基于正方移动教务获取数据，河南工业大学测试通过。
 分为三个页面：
 学生页面-student
 教师页面-teacher
 管理页面-admin
```

## 主要功能

1.课表获取、更新

2.基于websocket的在线推送消息

## 简要数据格式

1. socket信息格式
  
  ```java
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
  private int outdate;//过期时间
  ```
## 未完成部分

1. 教师学生互动
2. 请假
3. wifi定位（测试阶段）
4. GPS定位（测试阶段）

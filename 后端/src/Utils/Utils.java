package Utils;

import Model.SchoolCourse;
import Model.SchoolInformation;
import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Utils {
    private static ArrayList<Element> resolve(String loginResult) {
        String string = loginResult.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&quot;", "\"").replace("<br>", ",");
        SchoolInformation s = new SchoolInformation();
        SAXReader saxReader = new SAXReader();
        ArrayList<Element> res = new ArrayList<>();
        try {
            Document dom = saxReader.read(new ByteArrayInputStream(string.getBytes()));
            Element root = dom.getRootElement();
            for (Iterator<Element> i = root.elementIterator("row"); i.hasNext(); ) {
                res.add(i.next());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static SchoolInformation resolveLogin(String loginResult) {
        String string = loginResult.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&quot;", "\"").replace("<br>", ",");
        SchoolInformation s = new SchoolInformation();
        SAXReader saxReader = new SAXReader();
        ArrayList<String> x = new ArrayList<>();
        try {
            Document dom = saxReader.read(new ByteArrayInputStream(string.getBytes()));
            Element root = dom.getRootElement();
            for (Iterator<Element> it = root.elementIterator("data"); it.hasNext(); ) {
                Element element = it.next();
                x.add(element.element("value").getStringValue());
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        s.setSchoolid(x.get(0));
        s.setName(x.get(1));
        s.setSex(x.get(3));
        s.setBirthday(x.get(4));
        s.setPeople(x.get(5));
        s.setZzmm(x.get(6));
        s.setIdCard(x.get(7));
        s.setXjzt(x.get(8));
        s.setXy(x.get(9));
        s.setGrade(x.get(10));
        s.setMajor(x.get(11));
        s.setClas(x.get(12));
        s.setMajorid(x.get(13));
        s.setExamid(x.get(14));
        return s;
    }

    public static String MD5Enc(String string) {
        String re_md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder();
            for (byte value : b) {
                i = value;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5.toUpperCase();
    }

    public static ArrayList<SchoolCourse> getSyllabus(String schoolUserName) {
        String enc = schoolUserName + "%26" + "2018-2019%26" + "2DAFF8EA19E6BAC86E040007F01004EA";
        String strkey = MD5Enc(enc);
        String url = "http://123.15.36.138:8008/zfmobile_port/webservice/jw/EducationalPortXMLService";
        String data = "<v:Envelope xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:d=\"http://www.w3.org/2001/XMLSchema\" xmlns:c=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:v=\"http://schemas.xmlsoap.org/soap/envelope/\"><v:Header /><v:Body><n0:CourseScheduleSearch id=\"o0\" c:root=\"1\" xmlns:n0=\"http://service.jw.com/\"><userName i:type=\"d:string\">" + schoolUserName + "</userName><year i:type=\"d:string\">2018-2019</year><term i:type=\"d:string\">2</term><role i:type=\"d:string\">XS</role><count i:type=\"d:string\">0</count><strKey i:type=\"d:string\">" + strkey + "</strKey></n0:CourseScheduleSearch></v:Body></v:Envelope>";
        OutputStream outObject;
        URL u;
        ArrayList<Element> temp;
        ArrayList<SchoolCourse> res = new ArrayList<>();
        try {
            u = new URL(url);
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
                if (end != start + 8) {
                    result = result.substring(start + 8, end);
                    temp = resolve(result);
                    System.out.println();
                    for (Element element : temp) {
                        SchoolCourse c = new SchoolCourse();
                        c.setCurrentweek(element.content().get(0).getStringValue());
                        c.setName(element.content().get(1).getStringValue());
                        c.setTusername(element.content().get(2).getStringValue());
                        c.setProperty(element.content().get(4).getStringValue());
                        c.setCredit(Double.parseDouble(element.content().get(5).getStringValue()));
                        c.setWorkweek(element.content().get(6).getStringValue());
                        c.setClassweek(element.content().get(7).getStringValue());
                        c.setTime(element.content().get(8).getStringValue());
                        c.setCourseid((element.content().get(3).getStringValue() + c.getClassweek() + c.getTime() + c.getTusername()).hashCode());
                        c.setDest(element.content().get(9).getStringValue());
                        c.setDescription(element.content().get(10).getStringValue());
                        String t = element.content().get(11).getStringValue();
                        c.setIsodd(t.equals(" ") ? "单" : t);
                        res.add(c);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String getUserPic(String schoolUserName) {
        String enc = schoolUserName + "DAFF8EA19E6BAC86E040007F01004EA";
        String strkey = MD5Enc(enc);
        String url = "http://123.15.36.138:8008/zfmobile_port/webservice/jw/EducationalPortXMLService";
        String data = "<v:Envelope xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:d=\"http://www.w3.org/2001/XMLSchema\" xmlns:c=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:v=\"http://schemas.xmlsoap.org/soap/envelope/\"><v:Header /><v:Body><n0:StudentPhotosSearch id=\"o0\" c:root=\"1\" xmlns:n0=\"http://service.jw.com/\"><sid i:type=\"d:string\">" + schoolUserName + "</sid><strKey i:type=\"d:string\">" + strkey + "</strKey></n0:StudentPhotosSearch></v:Body></v:Envelope>";
        URL u = null;
        String result = "";
        try {
            u = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) u.openConnection();
            // 设置HTTP请求相关信息
            httpConn.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            OutputStream outObject = httpConn.getOutputStream();
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
            result = strBuf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int start = result.indexOf("<return>");
        int end = result.indexOf("</return>");
//        System.out.println(result);
        if (result.contains("失败"))
            return "";
        if(result.length()==211)
        {
            return "";
        }
        else {
            return result.substring(start + 8, end);
        }
    }

    public static JSONObject match(String a, String b) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + "osBgj9oIxUe9ZsTvVpKhAA1l"
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + "Lgh7lQvtwtw3X2YhPFwcXqrpxrpNGKZ2";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response;
        HttpGet httpGet = new HttpGet(getAccessTokenUrl);
        try {
            response = httpClient.execute(httpGet);
            String result = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            List<Map<String, Object>> images = new ArrayList<>();
            Map<String, Object> map1 = new HashMap<>();
            map1.put("image", a.split("base64,")[1]);
            map1.put("image_type", "BASE64");
            map1.put("face_type", "CERT");
            map1.put("quality_control", "LOW");
            map1.put("liveness_control", "NONE");
            Map<String, Object> map2 = new HashMap<>();
            map2.put("image", b.split("base64,")[1]);
            map2.put("image_type", "BASE64");
            map2.put("face_type", "LIVE");
            map2.put("quality_control", "LOW");
            map2.put("liveness_control", "NORMAL");
            images.add(map1);
            images.add(map2);
            String url = "https://aip.baidubce.com/rest/2.0/face/v3/match?access_token=" + access_token;
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(JSON.toJSONString(images)));
            response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
            /*
             *
             * {"error_code":0,"error_msg":"SUCCESS","log_id":304569239273181391,"timestamp":1553927318,"cached":0,
             * "result":{"score":100,
             * "face_list":[{"face_token":"dce00527410066d1ae1ed35a85fe8180"},
             * {"face_token":"dce00527410066d1ae1ed35a85fe8180"}]
             * }
             * }
             * */
            response.close();
            return new JSONObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double  b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s *6378.137 ;// EARTH_RADIUS;
        s = Math.round(s * 10000) / 10.0;
        return s;
    }
}
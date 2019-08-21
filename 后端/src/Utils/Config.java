package Utils;

import Pojo.Plugins;
import Utils.DataBase;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
//        SqlSession sqlSession = DataBase.getSqlSession();
//        List<Plugins> plugins = sqlSession.selectList("getPluginsInf");
//        System.out.println("正在搜索插件：");
//        for (Plugins plugin : plugins) {
//            System.out.print("插件名："+plugin.getName()+"\t版本："+plugin.getVersion());
//            if(plugin.getIsOpen().equals("0"))
//            {
//                System.out.println("\t未加载");
//            }
//            else
//            {
//                try {
//                    File file = new File("D://plugins/"+plugin.getFileName());
//                    URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{file.toURL()},getClass().getClassLoader());
//                    Class<?> clazz = urlClassLoader.loadClass(plugin.getMainClass());
//                    Object ps = clazz.newInstance();
//                    clazz.getDeclaredMethod("info").invoke(ps);
//                    clazz.getDeclaredMethod("service").invoke(ps);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}

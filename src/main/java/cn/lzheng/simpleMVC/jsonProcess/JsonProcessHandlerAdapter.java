package cn.lzheng.simpleMVC.jsonProcess;

import cn.lzheng.simpleMVC.Utils.ConfigurationLoader;
import cn.lzheng.simpleMVC.Utils.PropertiesLoader;
import cn.lzheng.simpleMVC.annotation.JsonProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @ClassName GetJsonProcessHandler
 * @Author 刘正
 * @Date 2020/9/25 10:19
 * @Version 1.0
 * @Description:
 */


public class JsonProcessHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(JsonProcessHandler.class);
    private static JsonProcessHandler ProcessHandler;

    private JsonProcessHandlerAdapter() {
    }

    public static void Init(){
        logger.debug("jsonProcess init--------");
        if (ProcessHandler==null){
            synchronized (JsonProcessHandlerAdapter.class){
                if(ProcessHandler==null){
                    Class configuration = ConfigurationLoader.getConfiguration();
                    Method[] methods = configuration.getMethods();
                    for (Method method : methods) {
                        if(method.getAnnotation(JsonProcess.class)!=null){
                            try {
                                ProcessHandler=(JsonProcessHandler)method.invoke(configuration.getConstructor().newInstance());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(ProcessHandler==null){
                        ProcessHandler=new FastJsonProcessHandler();
                    }
                }
            }
        }
    }


    public static JsonProcessHandler getJsonProcessHandler(){
        return ProcessHandler;
    }



}

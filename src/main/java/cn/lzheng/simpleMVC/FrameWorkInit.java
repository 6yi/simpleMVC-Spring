package cn.lzheng.simpleMVC;


import cn.lzheng.simpleMVC.Utils.ClassScanner;
import cn.lzheng.simpleMVC.Utils.ConfigurationLoader;
import cn.lzheng.simpleMVC.annotation.Configuration;
import cn.lzheng.simpleMVC.jsonProcess.JsonProcessHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.HandlesTypes;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * @ClassName FrameWorkInit
 * @Author 刘正
 * @Date 2020/9/23 23:33
 * @Version 1.0
 * @Description:
 */

@HandlesTypes(WebInitializer.class)
public class FrameWorkInit implements ServletContainerInitializer {

    private static Logger logger = LoggerFactory.getLogger(FrameWorkInit.class);


    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        logger.debug("start--------------------------");
        List<Class<?>> beans=new LinkedList<>();
        try {
            //diy init
            if (set!=null&&!set.isEmpty()){
                for (Class clazz:set){
                    try {
                        WebInitializer var1 = (WebInitializer) clazz.getConstructor().newInstance();
                        var1.onStartup(servletContext);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            Class configuration = ConfigurationLoader.getConfiguration();

            //初始化spring容器
            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(configuration);


            Configuration annotationConfig = (Configuration) configuration.getAnnotation(Configuration.class);
            String controllerPKG = annotationConfig.controllerSrc();

            beans=ClassScanner.getClasses(controllerPKG);

            if(controllerPKG==null){
                throw new Exception(" Controller ScanSrc NotFound");
            }


            servletContext.setAttribute("beansIOC",ctx);
            servletContext.setAttribute("controllerClazz",beans);
            servletContext.setAttribute("config",annotationConfig);

            //staticServlet init
            ServletRegistration.Dynamic staticServlet = servletContext.addServlet("staticServlet", new simpleMvcStaticServlet());
            staticServlet.addMapping("/simpleMvcStatic/*");

            //dispatcher init
            ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new RouterServlet( servletContext));
            dispatcher.addMapping(annotationConfig.dispatcherUrl());


            //Json init
            JsonProcessHandlerAdapter.Init();


        }  catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }



}

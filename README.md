# simpleMVC-spring

#### 介绍
simpleMVC的spring整合版


#### 对比

@Configuration

```
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@org.springframework.context.annotation.Configuration
public @interface Configuration {

    String controllerSrc();

    String dispatcherUrl() default "/";

    String staticPath() default "";

    String suffix() default ".jsp";

}
```
加入了Spring的Configuration注解功能,你可以直接把他当成spring的配置



@Controller

```
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Controller  {
}
```
加入了spring的@Component,控制器会被加载到spring容器中




### Quick start

#### 添加配置文件application.properties,指定配置类


```
configuration=cn.lzheng.simpleMVC.config.SimpleMvcConfiguration
```

#### 编写java配置类


```
@Configuration(controllerSrc = "cn.lzheng.simpleMVC.controller")
@ComponentScan("cn.lzheng.simpleMVC")
public class SimpleMvcConfiguration {

}
```
提供控制器的包以及需要开启spring自动扫描的路径



package com.example.soap.webservices.coursemanagement.webConfig;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.security.xwss.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import javax.security.auth.callback.CallbackHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//enable spring web services
@EnableWs

//spring configuration
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
    // messages dispatcher servlet
    //  we need applicationcontext
    //and url to exponse all our our web services==>   /ws/*
    // servletRegistration bean maps servlet to a uri
    @Bean
   public ServletRegistrationBean messageDispatcherServlet(ApplicationContext context){
        MessageDispatcherServlet messageDispatcherServlet= new MessageDispatcherServlet();
        messageDispatcherServlet.setApplicationContext(context);
        messageDispatcherServlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(messageDispatcherServlet,"/ws/*");

    }

    // /ws/courses.wsdl
        //porttype- courseport
         //name space ="http://in28minutes.com/courses"
    // course-details.xsd
    @Bean(name ="courses")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema coursesSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("CoursePort");
        definition.setTargetNamespace("http://in28minutes.com/courses");
        definition.setLocationUri("/ws");
        definition.setSchema(coursesSchema);
        return definition;
    }
    @Bean
    public XsdSchema courseSchema(){
        return new SimpleXsdSchema(new ClassPathResource("xsd/course-details.xsd"));
    }

    //when ever we get a request we would like to intercept it appy the security intercepter
    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        interceptors.add(securityInterceptor());
    }

    @Bean
    public XwsSecurityInterceptor securityInterceptor(){

        XwsSecurityInterceptor securityInterceptor= new XwsSecurityInterceptor();
        // callback handler - SimplePasswordValidationCallbackHandler
        securityInterceptor.setCallbackHandler(callbackHandler());
        // security policy -securitypolicy.xml
        securityInterceptor.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));

        return  securityInterceptor;
    }
   @Bean
    public SimplePasswordValidationCallbackHandler callbackHandler() {
       SimplePasswordValidationCallbackHandler handler = new SimplePasswordValidationCallbackHandler();
       handler.setUsersMap(Collections.singletonMap("user","password"));
       return handler;
    }


    //XwsSecurityInterceptor
       // callback handler - SimlePasswordValidationCallbackHandler  what should the interceptor do when it gets the request,
       // it should check username and passsword wheather its  vaild
        // security policy we will define a securitypolicy.xml
}

package com.example.soap.webservices.coursemanagement.exception;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

//@SoapFault(faultCode = FaultCode.CLIENT)
@SoapFault(faultCode = FaultCode.CUSTOM, customFaultCode = "{http://in28minutes.com/courses} _404 COURSE-ID-NOT-FOUND")
public class CourseNotFoundException  extends RuntimeException{
    private static final long serialVersionUID = 7526472295622776147L;
    public CourseNotFoundException(String message) {
        super(message);
    }
}

package com.example.soap.webservices.coursemanagement.endpoints;

import com.example.soap.webservices.coursemanagement.*;
import com.example.soap.webservices.coursemanagement.exception.CourseNotFoundException;
import com.example.soap.webservices.coursemanagement.model.Course;
import com.example.soap.webservices.coursemanagement.service.CourseDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class CourseDetailsEndpoint {
    @Autowired
    CourseDetailsService courseDetailsService;


    @PayloadRoot(namespace ="http://in28minutes.com/courses" ,
            localPart="GetCourseDetailsRequest")
    @ResponsePayload
  public GetCourseDetailsResponse processCourseDetails(@RequestPayload GetCourseDetailsRequest request){

      Course course= courseDetailsService.findById(request.getId());
      if (course==null){
          throw  new CourseNotFoundException("Invalid Course Id "+ request.getId());
      }
      GetCourseDetailsResponse response=mapCourseDetails(course);
      return response;
            }


    private  GetCourseDetailsResponse mapCourseDetails(Course course){
        GetCourseDetailsResponse response = new GetCourseDetailsResponse();
        response.setCourseDetails(mapCourse(course));
        return  response;

    }


    private CourseDetails mapCourse(Course course) {
        CourseDetails courseDetails=new CourseDetails();
       courseDetails.setId(course.getId());
       courseDetails.setName(course.getName());
       courseDetails.setDescription(course.getDescription());
       return courseDetails;

   }


    @PayloadRoot(namespace ="http://in28minutes.com/courses" ,
            localPart="GetAllCourseDetailsRequest")
    @ResponsePayload
    public GetAllCourseDetailsResponse processAllCourseDetails(@RequestPayload GetAllCourseDetailsRequest request){

       List<Course> courses= courseDetailsService.findAll();
        GetAllCourseDetailsResponse response= mapAllCourses(courses);
        return response;
    }

    private GetAllCourseDetailsResponse mapAllCourses(List<Course> courses) {

        GetAllCourseDetailsResponse response = new GetAllCourseDetailsResponse();
        for (Course course : courses) {
            CourseDetails mapCourse = mapCourse(course);
            response.getCourseDetails().add(mapCourse);
        }
        return response;
    }


    @PayloadRoot(namespace ="http://in28minutes.com/courses" ,
            localPart="DeleteCourseDetailsRequest")
    @ResponsePayload
    public DeleteCourseDetailsResponse DeleteCourseDetails(@RequestPayload DeleteCourseDetailsRequest request){

        CourseDetailsService.Status status = courseDetailsService.deleteById(request.getId());
        DeleteCourseDetailsResponse response= new DeleteCourseDetailsResponse();
        response.setStatus(mapStatus(status));
        return response;
    }

    private Status mapStatus(CourseDetailsService.Status status) {
        if (status==CourseDetailsService.Status.FAILURE){
            return Status.FAILURE;
        }
        return  Status.SUCCESS;
    }
}

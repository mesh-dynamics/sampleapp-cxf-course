package com.meshd.cxf.jaxrs.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("meshd")
@Produces("application/json")
public class CourseRepository {
    private Map<Integer, Course> courses = new HashMap<>();

    {
        List<Integer> studentIds = new ArrayList<>();
        studentIds.add(1);
        studentIds.add(2);
        studentIds.add(2);

        Course course1 = new Course();
        Course course2 = new Course();
        course1.setId(1);
        course1.setName("REST with Spring");
        course1.setStudents(studentIds);
        course2.setId(2);
        course2.setName("Learn Spring Security");

        courses.put(1, course1);
        courses.put(2, course2);
    }

    @GET
    @Path("courses/{courseId}")
    public Course getCourse(@PathParam("courseId") int courseId) {
        return findById(courseId);
    }

    @PUT
    @Path("courses/{courseId}")
    public Response updateCourse(@PathParam("courseId") int courseId, Course course) {
        Course existingCourse = findById(courseId);
        if (existingCourse == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (existingCourse.equals(course)) {
            return Response.notModified().build();
        }
        courses.put(courseId, course);
        return Response.ok().build();
    }

    @POST
    @Path("courses/{courseId}/student")
    public Response addStudent(@PathParam("courseId") int courseId, Student student) {
        Course existingCourse = findById(courseId);
        if (existingCourse == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            return existingCourse.createStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.serverError().build();
    }

    @Path("courses/{courseId}/students")
    public Course pathToStudent(@PathParam("courseId") int courseId) {
        return findById(courseId);
    }

    private Course findById(int id) {
        for (Map.Entry<Integer, Course> course : courses.entrySet()) {
            if (course.getKey() == id) {
                return course.getValue();
            }
        }
        return null;
    }
}

package ch.bzz.learningapp.service;
import ch.bzz.learningapp.data.DataHandler;
import ch.bzz.learningapp.model.Student;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("student")
public class StudentService {

    private static int cntr = DataHandler.getStudentCount();
    /**
     * lists all students from students.json
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listStudent(){
        int httpStatus = 200;
        List<Student> studentList = null;
        studentList = DataHandler.readAllStudents();

        return Response
                .status(httpStatus)
                .entity(studentList)
                .build();
    }

    /**
     * lists the corresponding student to given id
     */
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readStudent(
            @QueryParam("studentID") String studentID
    ){
        int httpStatus = 200;
        Student student = null;
        student = DataHandler.readStudentByID(studentID);
        if (student == null){
            httpStatus = 404;
        }

        return Response
                .status(httpStatus)
                .entity(student)
                .build();
    }
}
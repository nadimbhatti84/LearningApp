package ch.bzz.learningapp.service;

import ch.bzz.learningapp.data.DataHandler;
import ch.bzz.learningapp.model.SchoolClass;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.crypto.Data;
import java.util.List;

/**
 * schoolclass service
 */
@Path("schoolClass")
public class SchoolClassService {

    private static int cntr = DataHandler.getSchoolClassCount();
    /**
     * lists all schoolclasses from schoolClass.json
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listSchoolClass(){
        List<SchoolClass> schoolClassList = null;
        int httpStatus;
        schoolClassList = DataHandler.readAllSchoolClasses();
        return Response
                .status(200)
                .entity(schoolClassList)
                .build();
    }

    /**
     * adds learned status to schoolClass
     */
    @GET
    @Path("addLearned")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLearned(
            @QueryParam("schoolClassID") String schoolClassID,
            @QueryParam("learnedValue") int learnedValue
    ){
        int httpStatus = 404;
        SchoolClass schoolClass = new SchoolClass();
        schoolClass = DataHandler.readSchoolClassByID(schoolClassID);
        if (learnedValue > 0 && learnedValue <= 100 && schoolClass!=null){
            schoolClass.addToLearned(learnedValue);
            DataHandler.updateSchoolClass();
            httpStatus = 200;
        }
        return Response
                .status(httpStatus)
                .entity("")
                .build();
    }


}

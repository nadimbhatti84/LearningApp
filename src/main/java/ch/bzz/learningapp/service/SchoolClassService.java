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
     * lists the corresponding schoolClass to given id
     */
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readCountry(
            @QueryParam("schoolClassID") String schoolClassID
    ){
        int httpStatus;
        SchoolClass schoolClass = new SchoolClass();
        httpStatus = 200;
        schoolClass = DataHandler.readSchoolClassByID(schoolClassID);
        if (schoolClass == null){
            httpStatus = 404;
        }

        return Response
                .status(httpStatus)
                .entity(schoolClass)
                .build();
    }

    /**
     * updates a schoolClass (adds learning process)
     * @param schoolClass the id
     * @return Response
     */
    @POST
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateSchoolClass(
            @Valid @BeanParam SchoolClass schoolClass
    ){
        int httpStatus = 200;
        SchoolClass oldSchoolClass = DataHandler.readSchoolClassByID(Integer.toString(schoolClass.getSchoolClassID()));
        oldSchoolClass.getLearned().add(schoolClass.getLearned().get(0));
        DataHandler.updateSchoolClass();
        return Response
                .status(httpStatus)
                .entity("")
                .build();
    }

}

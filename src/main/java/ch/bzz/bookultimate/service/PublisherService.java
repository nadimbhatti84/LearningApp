package ch.bzz.bookultimate.service;

import ch.bzz.bookultimate.data.DataHandler;
import ch.bzz.bookultimate.model.Publisher;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

/**
 * services for reading, adding, changing and deleting publishers
 */
@Path("publisher")
public class PublisherService {

    /**
     * reads a list of all publishers
     * @param fieldname  the name of the field to be filtered or sorted
     * @param filter  the filter to be applied (null=no filter)
     * @param sortOrder  the sorting order (null=unsorted)
     * @return  publishers as JSON
     */
    @RolesAllowed({"admin", "user"})
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPublishers(
            @Pattern(regexp = "^publisherName|publisherUUID")
            @QueryParam("field") String fieldname,
            @QueryParam("filter") String filter,
            @QueryParam("sort") String sortOrder
    ) {
        List<Publisher> publisherList = null;
        if (fieldname != null && sortOrder != null) {
            publisherList = DataHandler.readSortedPublishers(fieldname, filter, sortOrder);
        } else if (fieldname != null && filter != null) {
            publisherList = DataHandler.readFilteredPublishers(fieldname, filter);
        } else {
            publisherList = DataHandler.readAllPublishers();
        }
        return Response
                .status(200)
                .entity(publisherList)
                .build();
    }

    /**
     * reads a publisher identified by the uuid
     * @param publisherUUID  the key
     * @return publisher
     */
    @RolesAllowed({"admin", "user"})
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readPublisher(
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @QueryParam("uuid") String publisherUUID
    ) {
        int httpStatus = 200;
        Publisher publisher = DataHandler.readPublisherByUUID(publisherUUID);
        if (publisher == null) {
            httpStatus = 410;
        }
        return Response
                .status(httpStatus)
                .entity(publisher)
                .build();
    }


    /**
     * inserts a new publisher
     * @param name the name of the publisher
     * @return Response
     */
    @RolesAllowed({"admin", "user"})
    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response insertPublisher(
            @FormParam("name") String name
    ) {
        Publisher publisher = new Publisher();
        publisher.setPublisherUUID(UUID.randomUUID().toString());
        publisher.setPublisherName(name);

        DataHandler.insertPublisher(publisher);
        return Response
                .status(200)
                .entity("")
                .build();
    }

    /**
     * updates a publisher
     * @param publisher  a valid publisher-object
     * @return Response
     */
    @RolesAllowed({"admin", "user"})
    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updatePublisher(
            @Valid @BeanParam Publisher publisher
    ) {
        int httpStatus = 200;
        Publisher oldPublisher = DataHandler.readPublisherByUUID(publisher.getPublisherUUID());
        if (oldPublisher != null) {
            oldPublisher.setPublisherName(publisher.getPublisherName());

            DataHandler.updatePublisher();
        } else {
            httpStatus = 410;
        }
        return Response
                .status(httpStatus)
                .entity("")
                .build();
    }

    /**
     * deletes a publisher identified by its uuid
     * @param publisherUUID  the key
     * @return  Response
     */
    @RolesAllowed({"admin"})
    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deletePublisher(
            @QueryParam("uuid") String publisherUUID
    ) {
        int httpStatus = 200;
        if (!DataHandler.deletePublisher(publisherUUID)) {
            httpStatus = 410;
        }
        return Response
                .status(httpStatus)
                .entity("")
                .build();
    }
}
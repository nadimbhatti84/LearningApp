package ch.bzz.bookultimate.service;

import ch.bzz.bookultimate.data.DataHandler;
import ch.bzz.bookultimate.model.Author;

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
 * services for reading, adding, changing and deleting authors
 */
@Path("author")
public class AuthorService {

    /**
     * reads a list of all authors
     * @param fieldname  the name of the field to be filtered or sorted
     * @param filter  the filter to be applied (null=no filter)
     * @param sortOrder  the sorting order (null=unsorted)
     * @return  authors as JSON
     */
    @RolesAllowed({"admin", "user"})
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAuthors(
            @Pattern(regexp = "^authorName|authorUUID")
            @QueryParam("field") String fieldname,
            @QueryParam("filter") String filter,
            @QueryParam("sort") String sortOrder
    ) {
        List<Author> authorList;
        if (fieldname != null && sortOrder != null) {
            authorList = DataHandler.readSortedAuthors(fieldname, filter, sortOrder);
        } else if (fieldname != null && filter != null) {
            authorList = DataHandler.readFilteredAuthors(fieldname, filter);
        } else {
            authorList = DataHandler.readAllAuthors();
        }
        return Response
                .status(200)
                .entity(authorList)
                .build();
    }

    /**
     * reads a author identified by the uuid
     * @param authorUUID  the key
     * @return author
     */
    @RolesAllowed({"admin", "user"})
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readAuthor(
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @QueryParam("uuid") String authorUUID
    ) {
        int httpStatus = 200;
        Author author = DataHandler.readAuthorByUUID(authorUUID);
        if (author == null) {
            httpStatus = 410;
        }
        return Response
                .status(httpStatus)
                .entity(author)
                .build();
    }


    /**
     * inserts a new author
     * @param name the name of the author
     * @return Response
     */
    @RolesAllowed({"admin", "user"})
    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response insertAuthor(
            @FormParam("name") String name
    ) {
        Author author = new Author();
        author.setAuthorUUID(UUID.randomUUID().toString());
        author.setAuthorName(name);

        DataHandler.insertAuthor(author);
        return Response
                .status(200)
                .entity("")
                .build();
    }

    /**
     * updates a author
     * @param author  a valid author-object
     * @return Response
     */
    @RolesAllowed({"admin", "user"})
    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateAuthor(
            @Valid @BeanParam Author author
    ) {
        int httpStatus = 200;
        Author oldAuthor = DataHandler.readAuthorByUUID(author.getAuthorUUID());
        if (oldAuthor != null) {
            oldAuthor.setAuthorName(author.getAuthorName());

            DataHandler.updateAuthor();
        } else {
            httpStatus = 410;
        }
        return Response
                .status(httpStatus)
                .entity("")
                .build();
    }

    /**
     * deletes a author identified by its uuid
     * @param authorUUID  the key
     * @return  Response
     */
    @RolesAllowed({"admin"})
    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteAuthor(
            @QueryParam("uuid") String authorUUID
    ) {
        int httpStatus = 200;
        if (!DataHandler.deleteAuthor(authorUUID)) {
            httpStatus = 410;
        }
        return Response
                .status(httpStatus)
                .entity("")
                .build();
    }
}
package ch.bzz.bookultimate.service;

import ch.bzz.bookultimate.data.DataHandler;
import ch.bzz.bookultimate.model.Book;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.UUID;

/**
 * services for reading, adding, changing and deleting books
 */
@Path("book")
public class BookService {

    /**
     * reads a list of all books
     * @return  books as JSON
     */
    @RolesAllowed({"admin", "user"})
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listBooks(
            @CookieParam("userRole") String userRole,
            @Pattern(regexp = "^title|author|publisher|price|isbn")
            @QueryParam("sortField") String sortField,
            @QueryParam("filterField") String filterField,
            @QueryParam("filter") String filter,
            @QueryParam("sort") String sortOrder
    ) {

        List<Book> bookList;
        if (sortField != null && sortOrder != null) {
            bookList = DataHandler.readSortedBooks(sortField,sortOrder,filterField, filter );
        } else if (sortField != null && filter != null) {
            bookList = DataHandler.readFilteredBooks(filterField, filter);
        } else {
            bookList = DataHandler.readAllBooks();
        }

        NewCookie cookie = new NewCookie(
                "userRole",
                userRole,
                "/",
                "",
                "Login-Cookie",
                600,
                false
        );
        return Response
                .status(200)
                .entity(bookList)
                .cookie(cookie)
                .build();
    }

    /**
     * reads a book identified by the uuid
     * @param bookUUID the key
     * @return book
     */
    @RolesAllowed({"admin", "user"})
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readBook(
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @QueryParam("uuid") String bookUUID,
            @CookieParam("userRole") String userRole
    ) {
        int httpStatus = 200;
        Book book = DataHandler.readBookByUUID(bookUUID);
        if (book == null) {
            httpStatus = 410;
        }

        NewCookie cookie = new NewCookie(
                "userRole",
                userRole,
                "/",
                "",
                "Login-Cookie",
                600,
                false
        );
        return Response
                .status(httpStatus)
                .entity(book)
                .cookie(cookie)
                .build();
    }

    /**
     * inserts a new book
     * @param publisherUUID the uuid of the publisher
     * @return Response
     */
    @RolesAllowed({"admin", "user"})
    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response insertBook(
            @Valid @BeanParam Book book,
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @FormParam("publisherUUID") String publisherUUID,
            @FormParam("author") List<String> authorList,
            @CookieParam("userRole") String userRole
    ) {
        int httpStatus = 200;

        book.setBookUUID(UUID.randomUUID().toString());
            book.setPublisherUUID(publisherUUID);
            book.setAuthorsUUID(authorList);
            DataHandler.insertBook(book);

        NewCookie cookie = new NewCookie(
                "userRole",
                userRole,
                "/",
                "",
                "Login-Cookie",
                600,
                false
        );

        return Response
                .status(httpStatus)
                .entity("")
                .cookie(cookie)
                .build();
    }

    /**
     * updates an existing book
     * @param publisherUUID the uuid of the publisher
     * @return Response
     */
    @RolesAllowed({"admin", "user"})
    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateBook(
            @Valid @BeanParam Book book,
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @FormParam("publisherUUID") String publisherUUID,
            @FormParam("author") List<String> authorList,
            @CookieParam("userRole") String userRole
    ) {
        int httpStatus = 200;

            Book oldBook = DataHandler.readBookByUUID(book.getBookUUID());
            if (oldBook != null) {
                oldBook.setTitle(book.getTitle());
                oldBook.setAuthorsUUID(authorList);
                oldBook.setPublisherUUID(publisherUUID);
                oldBook.setPrice(book.getPrice());
                oldBook.setIsbn(book.getIsbn());
                oldBook.setRelease(book.getRelease());

                DataHandler.updateBook();
            } else {
                httpStatus = 410;
            }

        NewCookie cookie = new NewCookie(
                "userRole",
                userRole,
                "/",
                "",
                "Login-Cookie",
                600,
                false
        );
        return Response
                .status(httpStatus)
                .entity("")
                .cookie(cookie)
                .build();
    }

    /**
     * deletes a book identified by its uuid
     * @param bookUUID  the key
     * @return  Response
     */
    @RolesAllowed({"admin"})
    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBook(
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @QueryParam("uuid") String bookUUID,
            @CookieParam("userRole") String userRole
    ) {
        int httpStatus = 200;
        if (userRole.equals("admin")) {
            if (!DataHandler.deleteBook(bookUUID)) {
                httpStatus = 410;
            }
        } else {
            httpStatus = 403;
        }

        NewCookie cookie = new NewCookie(
                "userRole",
                userRole,
                "/",
                "",
                "Login-Cookie",
                600,
                false
        );
        return Response
                .status(httpStatus)
                .entity("")
                .cookie(cookie)
                .build();
    }
}
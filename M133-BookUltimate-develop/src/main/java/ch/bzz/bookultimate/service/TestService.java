package ch.bzz.bookultimate.service;

import ch.bzz.bookultimate.data.DataHandler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * test service
 */
@Path("test")
public class TestService {

    /**
     * confirms the application runs
     * @return  message
     */
    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public Response test() {

        return Response
                .status(200)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "foobar")
                .entity("Test erfolgreich")
                .build();
    }

    /**
     * restores the json-files
     * @return Response
     */
    @GET
    @Path("restore")
    @Produces(MediaType.TEXT_PLAIN)
    public Response restore() {
        String[] keys = {"bookJSON", "publisherJSON", "authorJSON", "userJSON"};
        for (String key : keys) {
            try {
                java.nio.file.Path path = Paths.get(Config.getProperty(key));
                String filename = path.getFileName().toString();
                String folder = path.getParent().toString();

                byte[] dataJSON = Files.readAllBytes(Paths.get(folder, "backup", filename));
                FileOutputStream fileOutputStream = new FileOutputStream(filename);
                fileOutputStream.write(dataJSON);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DataHandler.initLists();
        return Response
                .status(200)
                .entity("Erfolgreich")
                .build();
    }
}


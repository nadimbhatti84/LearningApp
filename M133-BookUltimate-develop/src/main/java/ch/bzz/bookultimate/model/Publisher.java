package ch.bzz.bookultimate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

/**
 * the publisher of a book
 */
@Getter
@Setter
public class Publisher {
    @FormParam("publisherUUID")
    @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
    private String publisherUUID;
    @FormParam("publisherName")
    @NotEmpty
    @Size(min=4, max=50)
    private String publisherName;

 }

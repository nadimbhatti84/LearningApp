package ch.bzz.bookultimate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

@Getter
@Setter
/**
 * the author of a book
 */
public class Author {
    @FormParam("authorUUID")
    @Pattern(regexp = "|[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
    private String authorUUID;

    @FormParam("authorName")
    @NotEmpty
    @Size(min=4, max=40)
    private String authorName;


}

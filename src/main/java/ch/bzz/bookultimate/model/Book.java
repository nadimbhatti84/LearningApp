package ch.bzz.bookultimate.model;

import ch.bzz.bookultimate.data.DataHandler;
import ch.bzz.bookultimate.util.ISBN13;
import ch.bzz.bookultimate.util.LocalDateDeserializer;
import ch.bzz.bookultimate.util.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import javax.ws.rs.FormParam;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * a book in the bookshelf
 */
@Getter
@Setter
public class Book {
    @JsonIgnore
    private Publisher publisher;

    @JsonIgnore
    private List<Author> authorList;

    @FormParam("bookUUID")
    @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
    private String bookUUID;

    @FormParam("title")
    @NotEmpty
    @Size(min = 5, max = 40)
    private String title;

    @FormParam("price")
    @NotNull
    @DecimalMax(value = "199.95")
    @DecimalMin(value = "0.05")
    private BigDecimal price;

    @FormParam("isbn")
    @ISBN13
    private String isbn;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @FormParam("release")
    private LocalDate release;

    /**
     * gets the publisherUUID from the Publisher-object
     *
     * @return the publisherUUID or null
     */
    public String getPublisherUUID() {
        if (getPublisher() == null) return null;
        return getPublisher().getPublisherUUID();
    }

    /**
     * creates a Publisher-object without the bookultimate
     *
     * @param publisherUUID the key
     */
    public void setPublisherUUID(String publisherUUID) {
        setPublisher(new Publisher());
        Publisher publisher = DataHandler.readPublisherByUUID(publisherUUID);
        getPublisher().setPublisherUUID(publisherUUID);
        getPublisher().setPublisherName(publisher.getPublisherName());
    }

    /**
     * gets the UUIDs for all authors of this book
     *
     * @return list of uuids
     */
    public List<String> getAuthorsUUID() {
        if (this.getAuthorList() != null) {
            List<String> uuidList = new ArrayList<>();
            for (Author author : this.getAuthorList()) {
                uuidList.add(author.getAuthorUUID());
            }
            return uuidList;
        }
        return null;
    }

    /**
     * sets the authors from their UUIDs
     *
     * @param uuidList  list of author-uuids
     */
    public void setAuthorsUUID(List<String> uuidList) {

        this.setAuthorList(new ArrayList<>());
        for (String uuid : uuidList) {
            Author author = DataHandler.readAuthorByUUID(uuid);
            this.getAuthorList().add(author);
        }
    }

    /**
     * gets allthe authors of a book
     * @return  all author names as comma separated string
     */
    @JsonIgnore
    public String getAuthors() {
        StringBuilder authors = new StringBuilder();
        if (this.getAuthorList() != null) {
            List<String> uuidList = new ArrayList<>();
            for (Author author : this.getAuthorList()) {
                authors.append(author.getAuthorName()).append(", ");
            }

        }
        return (authors.length() == 0)
                ? null
                : (authors.substring(0, authors.length() - 2));
    }
}

package ch.bzz.bookultimate.data;

import ch.bzz.bookultimate.model.Author;
import ch.bzz.bookultimate.model.Book;
import ch.bzz.bookultimate.model.Publisher;
import ch.bzz.bookultimate.model.User;
import ch.bzz.bookultimate.service.Config;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * reads and writes the data in the JSON-files
 */
public final class DataHandler {
    private static List<Book> bookList;
    private static List<Author> authorList;
    private static List<Publisher> publisherList;
    private static List<User> userList;

    /**
     * private constructor defeats instantiation
     */
    private DataHandler() {
    }

    /**
     * initialize the lists with the data
     */
    public static void initLists() {
        DataHandler.setAuthorList(null);
        DataHandler.setPublisherList(null);
        DataHandler.setBookList(null);
    }

    /**
     * reads and sorts all the books matching the filter
     *
     * @param sortField the field to order by
     * @param sortOrder the sorting order (ASC or DESC)
     * @param filterField the field to apply the filter
     * @param filter    the value of the filter
     * @return list of books
     */

    public static List<Book> readSortedBooks(
            String sortField,
            String sortOrder,
            String filterField,
            String filter

    ) {
        final Comparator<Book> compareBooks = new Comparator<Book>() {
            @Override
            public int compare(Book book1, Book book2) {
                int result = 0;
                if (sortField.equals("title")) {
                    result = book1.getTitle().compareTo(book2.getTitle());
                } else if (sortField.equals("price")) {
                    result = book1.getPrice().compareTo(book2.getPrice());
                } else if (sortField.equals("isbn")) {
                    result = book1.getIsbn().compareTo(book2.getIsbn());
                } else if (sortField.equals("publisher")) {
                    result = book1.getPublisher().getPublisherName().compareTo(book2.getPublisher().getPublisherName());
                }
                // TODO add author

                if (sortOrder.equals("DESC")) {
                    result *= -1;
                }
                return result;
            }
        };

        List<Book> books;
        if (filter == null || filter.equals("")) {
            books = getBookList();
        } else {
            books = readFilteredBooks(filterField, filter);
        }

        books.sort(compareBooks);

        return books;
    }

    /**
     * reads all the books matching the filter
     *
     * @param fieldname the field to apply the filter
     * @param filter    the value of the filter
     * @return list of publishers
     */
    public static List<Book> readFilteredBooks(
            String fieldname,
            String filter
    ) {


        Predicate<Book> predicate = null;
        if (fieldname.equals("titleFilter")) {
            predicate = book ->
                    book.getTitle().toLowerCase().contains(filter.toLowerCase());
        } else if (fieldname.equals("authorFilter")) {
            predicate = book ->
                    book.getAuthors().toLowerCase().contains(filter.toLowerCase());
        } else if (fieldname.equals("priceFilter")) {
            predicate = book ->
                    book.getPrice().toString().equals(filter);
        } else if (fieldname.equals("isbnFilter")) {
            predicate = book ->
                    book.getIsbn().toLowerCase().contains(filter.toLowerCase());
        } else if (fieldname.equals("publisherFilter")) {
            predicate = book ->
                    book.getPublisher().getPublisherName().toLowerCase().contains(filter.toLowerCase());
        }
        return DataHandler.getBookList().
                        stream().
                        filter(predicate).
                        collect(Collectors.toList());
    }

    /**
     * reads all books
     *
     * @return list of books
     */
    public static List<Book> readAllBooks() {
        return getBookList();
    }

    /**
     * reads a book by its uuid
     *
     * @param bookUUID  the uuid of the book to be read
     * @return the Book (null=not found)
     */
    public static Book readBookByUUID(String bookUUID) {
        Book book = null;
        for (Book entry : getBookList()) {
            if (entry.getBookUUID().equals(bookUUID)) {
                book = entry;
            }
        }
        return book;
    }

    /**
     * inserts a new book into the bookList
     *
     * @param book the book to be saved
     */
    public static void insertBook(Book book) {
        getBookList().add(book);
        writeBookJSON();
    }

    /**
     * updates the bookList
     */
    public static void updateBook() {
        writeBookJSON();
    }

    /**
     * deletes a book identified by the bookUUID
     *
     * @param bookUUID the key
     * @return success=true/false
     */
    public static boolean deleteBook(String bookUUID) {
        Book book = readBookByUUID(bookUUID);
        if (book != null) {
            getBookList().remove(book);
            writeBookJSON();
            return true;
        } else {
            return false;
        }
    }

    /**
     * reads all the publishers matching the filter
     *
     * @param fieldname the field to apply the filter
     * @param filter    the value of the filter
     * @param sortOrder the sorting order (ASC or DESC)
     * @return list of publishers
     */

    public static List<Publisher> readSortedPublishers(
            String fieldname,
            String filter,
            String sortOrder
    ) {
        final Comparator<Publisher> comparePublisher = new Comparator<Publisher>() {
            @Override
            public int compare(Publisher publisher1, Publisher publisher2) {
                int result = 0;
                if (fieldname.equals("publisherName")) {
                    result = publisher1.getPublisherName().compareTo(publisher2.getPublisherName());
                }

                if (sortOrder.equals("DESC")) {
                    result *= -1;
                }
                return result;
            }
        };

        List<Publisher> publishers;
        if (filter == null) {
            publishers = getPublisherList();
        } else {
            publishers = readFilteredPublishers(fieldname, filter);
        }

        publishers.sort(comparePublisher);

        return publishers;
    }

    /**
     * reads all the publishers matching the filter
     *
     * @param fieldname the field to apply the filter
     * @param filter    the value of the filter
     * @return list of publishers
     */
    public static List<Publisher> readFilteredPublishers(
            String fieldname,
            String filter
    ) {
        if (fieldname.equals("publisherName")) {
            Predicate<Publisher> predicate = publisher ->
                    publisher.getPublisherName().toLowerCase().contains(filter.toLowerCase());

            return DataHandler.getPublisherList().
                            stream().
                            filter(predicate).
                            collect(Collectors.toList());
        }
        return getPublisherList();
    }

    /**
     * reads all publishers
     *
     * @return list of books
     */
    public static List<Publisher> readAllPublishers() {
        return getPublisherList();
    }

    /**
     * reads a publisher by its uuid
     *
     * @param publisherUUID the uuid of the publisher to be read
     * @return the Publisher (null=not found)
     */
    public static Publisher readPublisherByUUID(String publisherUUID) {
        Publisher publisher = null;
        for (Publisher entry : getPublisherList()) {
            if (entry.getPublisherUUID().equals(publisherUUID)) {
                publisher = entry;
            }
        }
        return publisher;
    }

    /**
     * inserts a new publisher into the bookList
     *
     * @param publisher the publisher to be saved
     */
    public static void insertPublisher(Publisher publisher) {
        getPublisherList().add(publisher);
        writePublisherJSON();
    }

    /**
     * updates the publisherList
     */
    public static void updatePublisher() {
        writePublisherJSON();
    }

    /**
     * deletes a publisher identified by the publisherUUID
     *
     * @param publisherUUID the key
     * @return success=true/false
     */
    public static boolean deletePublisher(String publisherUUID) {
        Publisher publisher = readPublisherByUUID(publisherUUID);
        if (publisher != null) {
            getPublisherList().remove(publisher);
            writePublisherJSON();
            return true;
        } else {
            return false;
        }
    }


    /**
     * reads all the authors matching the filter
     *
     * @param fieldname the field to apply the filter
     * @param filter    the value of the filter
     * @param sortOrder the sorting order (ASC or DESC)
     * @return list of authors
     */

    public static List<Author> readSortedAuthors(
            String fieldname,
            String filter,
            String sortOrder
    ) {
        final Comparator<Author> compareAuthor = new Comparator<Author>() {
            @Override
            public int compare(Author author1, Author author2) {
                int result = 0;
                if (fieldname.equals("authorName")) {
                    result = author1.getAuthorName().compareTo(author2.getAuthorName());
                }

                if (sortOrder.equals("DESC")) {
                    result *= -1;
                }
                return result;
            }
        };

        List<Author> authors;
        if (filter == null) {
            authors = getAuthorList();
        } else {
            authors = readFilteredAuthors(fieldname, filter);
        }

        authors.sort(compareAuthor);

        return authors;
    }

    /**
     * reads all the authors matching the filter
     *
     * @param fieldname the field to apply the filter
     * @param filter    the value of the filter
     * @return list of authors
     */
    public static List<Author> readFilteredAuthors(
            String fieldname,
            String filter
    ) {
        Predicate<Author> predicate = null;
        if (fieldname.equals("authorName")) {
            predicate = author ->
                    author.getAuthorName().toLowerCase().contains(filter.toLowerCase());
        } else if (fieldname.equals("authorUUID")) {
            predicate = author ->
                    author.getAuthorUUID().equals(filter);
        }
        return  DataHandler.getAuthorList().
                        stream().
                        filter(predicate).
                        collect(Collectors.toList());
    }

    /**
     * reads all authors
     *
     * @return list of books
     */
    public static List<Author> readAllAuthors() {
        return getAuthorList();
    }

    /**
     * reads a author by its uuid
     *
     * @param authorUUID the uuid of the author to be read
     * @return the Author (null=not found)
     */
    public static Author readAuthorByUUID(String authorUUID) {
        Author author = null;
        for (Author entry : getAuthorList()) {
            if (entry.getAuthorUUID().equals(authorUUID)) {
                author = entry;
            }
        }
        return author;
    }

    /**
     * inserts a new author into the bookList
     *
     * @param author the author to be saved
     */
    public static void insertAuthor(Author author) {
        getAuthorList().add(author);
        writeAuthorJSON();
    }

    /**
     * updates the authorList
     */
    public static void updateAuthor() {
        writeAuthorJSON();
    }

    /**
     * deletes a author identified by the authorUUID
     *
     * @param authorUUID the key
     * @return success=true/false
     */
    public static boolean deleteAuthor(String authorUUID) {
        Author author = readAuthorByUUID(authorUUID);
        if (author != null) {
            getAuthorList().remove(author);
            writeAuthorJSON();
            return true;
        } else {
            return false;
        }
    }

    /**
     * reads a user by the username/password provided
     *
     * @param username  the username
     * @param password  the password
     * @return user-object
     */
    public static User readUser(String username, String password) {
        User user = new User();
        for (User entry : getUserList()) {
            if (entry.getUsername().equals(username) &&
                    entry.getPassword().equals(password)) {
                user = entry;
            }
        }
        return user;
    }

    /**
     * reads the books from the JSON-file
     */
    private static void readBookJSON() {
        try {
            String path = Config.getProperty("bookJSON");
            byte[] jsonData = Files.readAllBytes(
                    Paths.get(path)
            );
            ObjectMapper objectMapper = new ObjectMapper();
            Book[] books = objectMapper.readValue(jsonData, Book[].class);
            for (Book book : books) {
                getBookList().add(book);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * writes the bookList to the JSON-file
     */
    private static void writeBookJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
        FileOutputStream fileOutputStream;
        Writer fileWriter;

        String bookPath = Config.getProperty("bookJSON");
        try {
            fileOutputStream = new FileOutputStream(bookPath);
            fileWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            objectWriter.writeValue(fileWriter, getBookList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * reads the books from the JSON-file
     */
    private static void readAuthorJSON() {
        try {
            String path = Config.getProperty("authorJSON");
            byte[] jsonData = Files.readAllBytes(
                    Paths.get(path)
            );
            ObjectMapper objectMapper = new ObjectMapper();
            Author[] authors = objectMapper.readValue(jsonData, Author[].class);
            for (Author author : authors) {
                getAuthorList().add(author);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * writes the bookList to the JSON-file
     */
    private static void writeAuthorJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
        FileOutputStream fileOutputStream;
        Writer fileWriter;

        String authorPath = Config.getProperty("authorJSON");
        try {
            fileOutputStream = new FileOutputStream(authorPath);
            fileWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            objectWriter.writeValue(fileWriter, getBookList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * reads the publishers from the JSON-file
     */
    private static void readPublisherJSON() {
        try {
            byte[] jsonData = Files.readAllBytes(
                    Paths.get(
                            Config.getProperty("publisherJSON")
                    )
            );
            ObjectMapper objectMapper = new ObjectMapper();
            Publisher[] publishers = objectMapper.readValue(jsonData, Publisher[].class);
            for (Publisher publisher : publishers) {
                getPublisherList().add(publisher);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * writes the publisherList to the JSON-file
     */
    private static void writePublisherJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
        FileOutputStream fileOutputStream;
        Writer fileWriter;

        String bookPath = Config.getProperty("publisherJSON");
        try {
            fileOutputStream = new FileOutputStream(bookPath);
            fileWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            objectWriter.writeValue(fileWriter, getPublisherList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * reads the users from the JSON-file
     */
    private static void readUserJSON() {
        try {
            byte[] jsonData = Files.readAllBytes(
                    Paths.get(
                            Config.getProperty("userJSON")
                    )
            );
            ObjectMapper objectMapper = new ObjectMapper();
            User[] users = objectMapper.readValue(jsonData, User[].class);
            for (User user : users) {
                getUserList().add(user);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * gets bookList
     *
     * @return value of bookList
     */

    private static List<Book> getBookList() {

        if (bookList == null) {
            setBookList(new ArrayList<>());
            readBookJSON();
        }
        return bookList;
    }

    /**
     * sets bookList
     *
     * @param bookList the value to set
     */

    private static void setBookList(List<Book> bookList) {
        DataHandler.bookList = bookList;
    }

    /**
     * gets publisherList
     *
     * @return value of publisherList
     */

    private static List<Publisher> getPublisherList() {
        if (publisherList == null) {
            setPublisherList(new ArrayList<>());
            readPublisherJSON();
        }

        return publisherList;
    }

    /**
     * sets publisherList
     *
     * @param publisherList the value to set
     */

    private static void setPublisherList(List<Publisher> publisherList) {
        DataHandler.publisherList = publisherList;
    }

    /**
     * gets authorList
     *
     * @return value of authorList
     */

    public static List<Author> getAuthorList() {
        if (DataHandler.authorList == null) {
            DataHandler.setAuthorList(new ArrayList<>());
            readAuthorJSON();
        }
        return authorList;
    }

    /**
     * sets authorList
     *
     * @param authorList the value to set
     */

    public static void setAuthorList(List<Author> authorList) {

        DataHandler.authorList = authorList;
    }

    /**
     * gets userList
     *
     * @return value of userList
     */

    public static List<User> getUserList() {
        if (DataHandler.userList == null) {
            DataHandler.setUserList(new ArrayList<>());
            readUserJSON();
        }
        return userList;
    }

    /**
     * sets userList
     *
     * @param userList the value to set
     */

    public static void setUserList(List<User> userList) {
        DataHandler.userList = userList;
    }

}
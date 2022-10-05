package ch.bzz.learningapp.model;

public class Student {

    private int studentID;
    private String studentFirstName;
    private String studentLastName;
    private String student_Notes;
    private String portraitPath;


    public int getStudentID() {
        return studentID;
    }
    public String getStudentFirstName() {
        return studentFirstName;
    }
    public String getStudentLastName() {
        return studentLastName;
    }
    public String getStudent_Notes() {
        return student_Notes;
    }
    public String getPortraitPath() {
        return portraitPath;
    }
    public boolean setStudent_Notes(String student_Notes) {
        this.student_Notes = student_Notes;
        return true;
    }
}

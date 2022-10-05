package ch.bzz.learningapp.model;

import java.util.Vector;

public class SchoolClass {

    private int schoolClassID;
    private String schoolClassName;
    private int[] studentIDs;
    private int[] learned;

    public int getSchoolClassID() {
        return schoolClassID;
    }
    public String getSchoolClassName() {
        return schoolClassName;
    }
    public int[] getStudentIDs() { return studentIDs; }
    public int[] getLearned() {
        return learned;
    }
}

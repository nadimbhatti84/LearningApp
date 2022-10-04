package ch.bzz.learningapp.model;

import java.util.Vector;

public class SchoolClass {

    private int schoolClassID;
    private String schoolClassName;
    private int[] studentIDs;
    private Vector<Float> learned;

    public int getSchoolClassID() {
        return schoolClassID;
    }
    public String getSchoolClassName() {
        return schoolClassName;
    }
    public int[] getStudentIDs() { return studentIDs; }
    public Vector<Float> getLearned() {
        return learned;
    }
}

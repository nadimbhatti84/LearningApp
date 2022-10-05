package ch.bzz.learningapp.model;

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

    public void addToLearned(int value){
        int[] tempArray = new int[learned.length+1];
        for (int i = 0; i < learned.length; i++){
            tempArray[i] = learned[i];
        }
        tempArray[tempArray.length-1] = value;
        learned = tempArray.clone();
    }
}

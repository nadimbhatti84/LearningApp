package ch.bzz.learningapp.data;

import ch.bzz.learningapp.model.SchoolClass;
import ch.bzz.learningapp.model.Student;
import ch.bzz.learningapp.service.Config;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * reads and writes the data in the JSON-files
 */
public class DataHandler {
    private static DataHandler instance = null;
    private static List<SchoolClass> schoolClassList;
    private static List<Student> studentList;

    /**
     * private constructor defeats instantiation
     */
    private DataHandler() {}

    /**
     * reads all schoolclasses
     * @return list of schoolclasses
     */
    public static List<SchoolClass> readAllSchoolClasses() {
        return getSchoolClassList();
    }
    /**
     * reads all students
     * @return list of students
     */
    public static List<Student> readAllStudents() {
        return getStudentList();
    }

    /**
     * reads a student by its id
     * @param studentID
     * @return student (null=not found)
     */
    public static Student readStudentByID(String studentID) {
        Student student = null;
        for (Student entry : getStudentList()) {
            if (entry.getStudentID() == Integer.parseInt(studentID)) {
                student = entry;
            }
        }
        return student;
    }

    /**
     * reads a schoolclass by its id
     * @param schoolClassID
     * @return schoolClass (null=not found)
     */
    public static SchoolClass readSchoolClassByID(String schoolClassID) {
        SchoolClass schoolClass = null;
        for (SchoolClass entry : getSchoolClassList()) {
            if (entry.getSchoolClassID() == Integer.parseInt(schoolClassID)) {
                schoolClass = entry;
            }
        }
        return schoolClass;
    }

    /**
     * reads the schoolclass from the JSON-file
     */
    private static void readSchoolClassJSON() {
        try {
            byte[] jsonData = Files.readAllBytes(
                    Paths.get(
                            Config.getProperty("schoolClassJSON")
                    )
            );
            ObjectMapper objectMapper = new ObjectMapper();
            SchoolClass[] schoolClasses = objectMapper.readValue(jsonData, SchoolClass[].class);
            for (SchoolClass schoolClass : schoolClasses) {
                getSchoolClassList().add(schoolClass);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * reads the student from the JSON-file
     */
    private static void readStudentJSON() {
        try {
            byte[] jsonData = Files.readAllBytes(
                    Paths.get(
                            Config.getProperty("studentJSON")
                    )
            );
            ObjectMapper objectMapper = new ObjectMapper();
            Student[] students = objectMapper.readValue(jsonData, Student[].class);
            for (Student student : students) {
                getStudentList().add(student);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * gets schoolclass list
     *
     * @return value of schoolclass
     */
    private static List<SchoolClass> getSchoolClassList(){
        if (schoolClassList == null){
            setSchoolClassList(new ArrayList<>());
            readSchoolClassJSON();
        }
        return schoolClassList;
    }

    /**
     * sets schoolclass list
     *
     * @param schoolClassList the value to set
     */
    private static void setSchoolClassList(List<SchoolClass> schoolClassList) {
        DataHandler.schoolClassList = schoolClassList;
    }

    /**
     * gets student list
     *
     * @return value of student
     */
    private static List<Student> getStudentList(){
        if (studentList == null){
            setStudentList(new ArrayList<>());
            readStudentJSON();
        }
        return studentList;
    }

    /**
     * sets student list
     *
     * @param studentList the value to set
     */
    private static void setStudentList(List<Student> studentList) {
        DataHandler.studentList = studentList;
    }


    /**
     * gets students count
     */
    public static int getStudentCount(){
        int maxID = 0;
        for (int i = 0; i < DataHandler.getStudentList().size(); i++){
            if (maxID < DataHandler.getStudentList().get(i).getStudentID()){
                maxID = DataHandler.getStudentList().get(i).getStudentID();
            }
        }
        return maxID;
    }

    /**
     * gets schoolclass count
     */
    public static int getSchoolClassCount(){
        int maxID = 0;
        for (int i = 0; i < DataHandler.getSchoolClassList().size(); i++){
            if (maxID < DataHandler.getSchoolClassList().get(i).getSchoolClassID()){
                maxID = DataHandler.getSchoolClassList().get(i).getSchoolClassID();
            }
        }
        return maxID;
    }

    /**
     * updates the schoolList
     */
    public static void updateSchoolClass() {
        writeSchoolClassJSON();
    }

    /**
     * writes the schoolList to the JSON-file
     */
    private static void writeSchoolClassJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
        FileOutputStream fileOutputStream = null;
        Writer fileWriter;

        String schoolClassPath = Config.getProperty("schoolClassJSON");
        try {
            fileOutputStream = new FileOutputStream(schoolClassPath);
            fileWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            objectWriter.writeValue(fileWriter, getSchoolClassList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
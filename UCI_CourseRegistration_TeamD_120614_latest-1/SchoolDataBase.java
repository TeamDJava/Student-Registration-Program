/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseregistration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author kieky
 */
public class SchoolDataBase {
    private String studentAccount;
    private String databasePath;
    private String studentDatabase;
    private String courseDatabase;
    
    private CSVOperations studentCSV;
    private CSVOperations courseCSV;
    
    private HashMap studentHashMap;
    private ArrayList<CourseStructure> schoolCourses;
    /**
     * set default values to all variables
     * @throws java.io.IOException
     */
    public SchoolDataBase() throws IOException
    {
        studentAccount = "";
        File file=new File(".");   
        String path=file.getAbsolutePath();
        path=file.getAbsolutePath();
        System.out.println(path);
        databasePath = path;
        studentDatabase = "UCI_Students.csv";
        courseDatabase = "UCI_Courses.csv";
        studentCSV = new CSVOperations(databasePath + "\\" + studentDatabase);
        courseCSV = new CSVOperations(databasePath + "\\" + courseDatabase);
        //put account and password into hashmap
        studentHashMap = new HashMap();
        for(int i=0;i<studentCSV.csvContents.size();i++)
        {
            System.out.println(studentCSV.csvGetCell(i, 0) + ":" + studentCSV.csvGetCell(i, 1));
            studentHashMap.putIfAbsent(studentCSV.csvGetCell(i, 0),studentCSV.csvGetCell(i, 1));
        }
        //pub the courses into courseStructure
        schoolCourses = new ArrayList<>();
        for(int i=0;i<courseCSV.csvContents.size();i++)
        {
            CourseStructure oneCourse = new CourseStructure();
            oneCourse.courseName = courseCSV.csvGetCell(i, 0);
            oneCourse.courseSchedule = courseCSV.csvGetCell(i, 2);
            oneCourse.availableSeats = courseCSV.csvGetCell(i, 1);
            int j = 3;
            while(courseCSV.csvGetCell(i, j).length()>0)
            {
                oneCourse.regStudents.add(courseCSV.csvGetCell(i, j));
                j=j+1;
            }
            if(oneCourse.regStudents.contains(studentAccount)==true)
            {
                oneCourse.regged=true;
            }
            else
            {
                oneCourse.regged=false;
            }
            schoolCourses.add(oneCourse);
        }
    }
    public SchoolDataBase(String inDatabasePath, String inStudentDatabase, String inCourseDatabase) throws IOException
    {
        studentAccount = "";
        databasePath = inDatabasePath;
        studentDatabase = inStudentDatabase;
        courseDatabase = inCourseDatabase;
        studentCSV = new CSVOperations(databasePath + "\\" + studentDatabase);
        courseCSV = new CSVOperations(databasePath + "\\" + courseDatabase);
        //put account and password into hashmap
        studentHashMap = new HashMap();
        for(int i=0;i<studentCSV.csvContents.size();i++)
        {
            studentHashMap.putIfAbsent(studentCSV.csvGetCell(i, 0),studentCSV.csvGetCell(i, 1));
        }
        //pub the courses into courseStructure
        schoolCourses = new ArrayList<>();
        for(int i=0;i<courseCSV.csvContents.size();i++)
        {
            CourseStructure oneCourse = new CourseStructure();
            oneCourse.courseName = courseCSV.csvGetCell(i, 0);
            oneCourse.courseSchedule = courseCSV.csvGetCell(i, 2);
            oneCourse.availableSeats = courseCSV.csvGetCell(i, 1);
            int j = 3;
            while(courseCSV.csvGetCell(i, j).length()>0)
            {
                oneCourse.regStudents.add(courseCSV.csvGetCell(i, j));
                j=j+1;
            }
            if(oneCourse.regStudents.contains(studentAccount)==true)
            {
                oneCourse.regged=true;
            }
            else
            {
                oneCourse.regged=false;
            }
            schoolCourses.add(oneCourse);
        }
    }
    
    /**
     * 
     * @param account
     * @param password
     * @return
     * This will add one more row in the student accounts db file
     */
    public int createStudentAccount(String account, String password)
    {
        int i = 0;
        if((databasePath.length()>0) && (studentDatabase.length()>0))
        {
            //check if file exists or not, if no, return -2
            //if exists, add one more row to the end
            if((account.length()>0)&&(password.length()>0))
            {
                //already exists
                if(studentHashMap.containsKey(account))
                {
                    return -4;
                }
                else
                {
                    ArrayList<String> tmpArrayList = new ArrayList();
                    tmpArrayList.add(account);
                    tmpArrayList.add(password);
                    studentCSV.csvAddRow(tmpArrayList);
                    studentHashMap.putIfAbsent(account, password);
                    studentAccount = account;
                    for(CourseStructure oneCourse:schoolCourses)
                    {
                        oneCourse.regged = oneCourse.regStudents.contains(studentAccount);
                    }
                }
            }
            else
            {
                return -3;
            }
            return 1;
        }
        else
        {
            return -1;
        }
    }
    /**
     * 
     * @param account
     * @param password
     * @return 
     * This is for login. Check if provided account/pw match the record in student account file.
     */
    public int checkPassword(String account, String password)
    {
        if(studentHashMap.containsKey(account))
        {
            if(studentHashMap.get(account).equals(password))
            {
                for(CourseStructure oneCourse:schoolCourses)
                {
                    oneCourse.regged = oneCourse.regStudents.contains(account);
                }
                studentAccount = account;
                return 1;
            }
            else
            {
                return -2;
            }
        }
        else
        {
            return -1;
        }
    }
    /**
     * 
     * @param courseName
     * @param dropCourse
     * @return 
     * This is only for logged in student. 
     * This will add one more student name or delete one name in the course row in the course file.
     */
    public int registerCourse(String courseName, boolean dropCourse)
    {
        for(int i=0;i<courseCSV.csvContents.size();i++)
        {
            String tmpRow = courseCSV.csvContents.get(i);
            if(tmpRow.contains(courseName))
            {
                if(dropCourse == false)  //reg
                {
                    tmpRow = tmpRow + "\"" + studentAccount + "\",";
                    courseCSV.csvContents.remove(i);
                    courseCSV.csvContents.add(i, tmpRow);
                    courseCSV.createCSV();
                    schoolCourses.get(i).regStudents.add(studentAccount);
                    schoolCourses.get(i).regged=true;
                }
                else
                {
                    tmpRow = tmpRow.replace("\"" + studentAccount + "\",", "");
                    courseCSV.csvContents.remove(i);
                    courseCSV.csvContents.add(i, tmpRow);
                    courseCSV.createCSV();
                    schoolCourses.get(i).regStudents.remove(studentAccount);
                    schoolCourses.get(i).regged=false;
                }
            }
        }
        return 1;
    }
    /**
     * 
     * @param courses
     * @return 
     * list all courses and related information such as signed or not, available or not
     */
    public int listCourses(ArrayList<CourseStructure> courses)
    {
        for(CourseStructure oneCourse:schoolCourses)
        {
            courses.add(oneCourse);
        }
        return 1;
    }
    /**
     * 
     * @param myCourses
     * @return 
     * list signed courses for the student
     */
    public int listStudentCourses(ArrayList<CourseStructure> myCourses)
    {
        for(CourseStructure oneCourse:schoolCourses)
        {
            if(oneCourse.regged==true)
            {
                myCourses.add(oneCourse);
            }
        }
        return 1;
    }
    /**
     * 
     * @param availableCourses
     * @return 
     * list available courses which not signed up by student
     */
    public int listAvailableCourses(ArrayList<String> availableCourses)
    {
        return 1;
    } 
    /**
     * 
     * @param courseName
     * @return 
     * Get the schedule from course file.
     */
    public int checkCourseSchedule(String courseName)
    {
        return 1;
    }
}

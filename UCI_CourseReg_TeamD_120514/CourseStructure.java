/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseregistration;

import java.util.ArrayList;

/**
 *
 * @author kieky
 */
public class CourseStructure {
    String courseName;
    String courseSchedule;
    String availableSeats;
    boolean regged;
    ArrayList<String> regStudents;
    
    public CourseStructure()
    {
        courseName = "";
        courseSchedule = "";
        availableSeats = "";
        regged = false;
        regStudents = new ArrayList<>();
    }
}

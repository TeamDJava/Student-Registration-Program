/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseregistration;

import java.io.BufferedWriter;  
import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.FileWriter;  
import java.io.IOException;
import java.io.BufferedReader;  
import java.io.FileReader;  
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Just for small csv file operations. Not good for big files.
 * @author kieky
 */
public class CSVOperations 
{
    //each row of the csv file will be put into each item of this arraylist
    // " and , will be included in this csvContents
    public ArrayList<String> csvContents = new ArrayList<String>();
    private String fileName;
    
    public CSVOperations(String csvFile) throws IOException
    {
        this.csvContents.clear();
        this.fileName = csvFile;
        
        File file = new File(csvFile);
        if(!file.exists())
        {
            try {
                file.createNewFile();
            }
            catch(IOException e) 
            {  
                //any other exception 
                System.out.println("csv file write operation error!");
                return;
            }
        }
        
        try {  
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));//create buffer reader
            String line = null;  
            while((line=reader.readLine())!=null){  
                this.csvContents.add(line);
                //for debug
                System.out.println(line); 
            }
            reader.close();
        } catch (Exception e) {  
            System.out.println("csv file read operation error!"); 
        }
    }
    
    public int csvAddRow(ArrayList<String> rowIn)
    {
        //input is arraylist, need to put all items into arraylist first
        try
        {
            File csv = new File(this.fileName); // open the file 
 
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true)); //append to end 
            //write the new row 
            for(String value : rowIn)
            {
                // add , even for the last item
                bw.write("\"" + value + "\"" + ",");  
            }
            bw.newLine();  
            bw.close();  
        }
        catch (FileNotFoundException e) 
        {  
            //no such file 
            System.out.println("csv file not found!"); 
            return -1;
        } 
        catch (IOException e) 
        {  
            //any other exception 
            System.out.println("csv file write operation error!");
            return -1;
        }  
        return 1;
    }
    public int csvGetRow(int rowIndex, ArrayList<String> rowContents)
    {
        //split the row to arraylist
        String line = "";
        
        rowContents.clear();
        if(this.csvContents.size()<rowIndex)
        {
            return -1;
        }
        else
        {
            line = this.csvContents.get(rowIndex);
            //split each column by ,
            String items[] = line.split(",");
            boolean addAll = rowContents.addAll(Arrays.asList(items));
            if(addAll == false)
            {
                return -2;
            }
            //remove the " for each string
            for(int j=0;j<rowContents.size();j++)
            {
                String cellString = rowContents.get(j);
                if(cellString.length()>2)
                {
                    cellString = cellString.substring(1, cellString.length()-1);
                }
                else
                {
                    cellString = "";
                }
                rowContents.set(j, cellString);
            }
        }
        return 1;
    }
    public String csvGetCell(int rowIndex, int colIndex)
    {
        String cellValue;
        ArrayList<String> rowValue = new ArrayList<String>();
        int rowReturn = csvGetRow(rowIndex,rowValue);
        
        if(rowReturn<0)
        {
            return "";
        }
        
        if(rowValue.size()<(colIndex+1))
        {
            return "";
        }
        
        cellValue = rowValue.get(colIndex);
        
        return cellValue;
    }
    public int csvDeleteRow(int rowIndex)
    {
        if(csvContents.size()<rowIndex)
        {
            return -1;
        }
        csvContents.remove(rowIndex);
        //write this arraylist to the file again
        return createCSV();
    }
    public int csvDeleteCell(int rowIndex,int colIndex)
    {
        if(csvContents.size()<rowIndex)
        {
            return -1;
        }
        String items[] = csvContents.get(rowIndex).split(",");
        ArrayList<String> rowValue = new ArrayList<String>();
        boolean addAll = rowValue.addAll(Arrays.asList(items));
        if(rowValue.size()<colIndex)
        {
            return -2;
        }
        rowValue.remove(colIndex);
        //convert rowValue to string and then put back into csvContents
        String workingRow = "";
        for(String item : rowValue)
        {
            // add , even for the last item
            workingRow = workingRow + item + ",";
        }
        csvContents.remove(rowIndex);
        csvContents.add(rowIndex, workingRow);
        
        //write the arraylist to file again
        return createCSV();
    }
    //create the csv with the arraylist
    public int createCSV()
    {
        //use the arraylist to create a file
        //only for this class use
        File csv = new File(this.fileName); // open the file 
        boolean delete = csv.delete();
        if(delete==false)
        {
            return -1;
        }
        
        //input is arraylist, need to put all items into arraylist first
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true)); //append to end 
            //write the new row 
            for(String value : csvContents)
            {
                // add , even for the last item
                bw.write(value);  
                bw.newLine();  
            }
            bw.close();  
        }
        catch (FileNotFoundException e) 
        {  
            //no such file 
            System.out.println("csv file not found!"); 
            return -1;
        } 
        catch (IOException e) 
        {  
            //any other exception 
            System.out.println("csv file write operation error!");
            return -1;
        }
        
        
        return 1;
    }
            
}

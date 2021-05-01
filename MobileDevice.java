/*
 * Final Project: Covid-19 Contact Tracer.
 * Banner ID: B00870489
 * @author Yashvi
 */
package final_project;
import java.io.*;
import java.security.*;
import java.sql.SQLException;
import java.util.*;

public class MobileDevice{ 

    //contact class 
    static class Contact{
        public String individual;
        public int date;
        public int duration;
        public Contact(){}
        
        //class constructor
        public Contact(String individual, int date, int duration){
            this.individual = individual;
            this.date = date;
            this.duration = duration;
        }
    }
    
    public ArrayList<Contact> contact_info = new ArrayList<>(); //ArrayList storing all contact information.
    public ArrayList<String> testHash_info = new ArrayList<>(); //ArrayList storing all testHashes.
    static Government contactTracer = new Government(); //Government class object.
    StringBuffer hexString = new StringBuffer(); // hexString stores hash value of device information. 
    public String hash_of_device; //stores hexString.
    byte[] digest_data; //variable stores message digest data.
    public String contact_infoToString; //stores contact information in string.
    public String store_testHash; //variable will store testHash.
    
    //constructor of class
    MobileDevice(String configFile, Government contactTracer) throws NoSuchAlgorithmException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        
        //checks if configFile is null or empty.
        if(configFile == null || configFile.isEmpty()){
           return;
        }
        
        try{
            FileReader reader = new FileReader(configFile); //reads data from configFile and store it in reader.
            Properties props = new Properties(); //creates properties object(properties is a subclass of hashtable.)
            props.load(reader); //loads reader data into props object.
            String Address = props.getProperty("Address"); // gets the value of "Address" key and stores in Address var.
            String DeviceName = props.getProperty("DeviceName"); //gets the value of "DeviceName" key and stores in DeviceName var.   
            MessageDigest md = MessageDigest.getInstance("SHA-256"); //Creates MessageDigest object. 
            
           //It passes data to the created MessageDigest Object
            md.update(Address.getBytes());
            md.update(DeviceName.getBytes());

            //It will compute the message digest
            digest_data = md.digest(); 
            
            //loop for appending values of address and devicename to hexString variable by converting it into hash value.
            for (int i = 0;i<digest_data.length;i++) {
               hexString.append(Integer.toHexString(0xFF & digest_data[i]));
            }
            
            hash_of_device = hexString.toString(); //hash_of_device stores hash value from hexString.
            
            reader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        catch(IOException e){
            System.out.println("IO Exception");
        }
     }
    
    //Empty constructor of class.
    public MobileDevice() {}
    
    /* 
     * recordContact method will record all the information of nearby devices locally.
     * Like some individual was near you on such date for some duration.
     * It stores all this information into one list name contact_info and 
     * converts list into string again by storing it into variable name contact_infoToString.
     */
    public void recordContact(String individual, int date, int duration){
        
        //checks if parameter values are null or empty.
        if(individual == null || individual.isEmpty() || date == 0 || duration == 0){
             return;
        }
        
        Contact c = new Contact(individual,date,duration); //constructor call.
        contact_info.add(c); //adds value to list.
    }

    /*
     * positiveTest method will provide testHash of an individual which identifies the test of user
     * and it is unique to an individual.
     */
    public void positiveTest(String testHash){
        
        //checks if testHash value is null or empty.
        if(testHash == null || testHash.isEmpty()){
            return;
        }
        testHash_info.add(testHash);
    }
    
    /*
     * synchronizeData method will synchronize all the information of an individual like hash value and information 
     * stored in contact_infoToString(testHash, date, duration, individual) to MobileContact method of Government class.
    */
    public boolean synchronizeData() throws NoSuchAlgorithmException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        
        String c_detail = ""; //string to concat contact detail values.
        //loop that iterates and concats values into string.
        for(int i=0; i<contact_info.size(); i++){
            c_detail = c_detail.concat(contact_info.get(i).individual);
            c_detail = c_detail.concat("/");
            c_detail = c_detail.concat(String.valueOf(contact_info.get(i).date));
            c_detail = c_detail.concat("/");
            c_detail = c_detail.concat(String.valueOf(contact_info.get(i).duration));
            c_detail = c_detail.concat("/");
            
        } 
        
        for(int i=0; i<testHash_info.size(); i++){
            c_detail = c_detail.concat(testHash_info.get(i));
            c_detail = c_detail.concat("/");
            c_detail = c_detail.concat("\n");
        }
//        contactTracer.MobileContact(hash_of_device, c_detail); //calls MobileContact Method of Government class.
        
    //returns true when government notifies that user tested positive.
    return contactTracer.MobileContact(hash_of_device, c_detail);
    }
 
}

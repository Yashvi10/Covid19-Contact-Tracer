/*
 * Final Project: Covid-19 Contact Tracer.
 * Banner ID: B00870489
 * @author Yashvi
 */

package final_project;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class Government {

    public static String Database; ////stores database name
    public static String User; //stores username of database 
    public static String Password; //stores password of database
    public static Statement statement; //interface of Statement to excute query.
    public static ResultSet resultSet; //interface representing resultSet.
    public static Connection connection; //to connect database.
    public static PreparedStatement ps; //interface of PreparedStatement to excute parameterized query.
    public static Properties props = new Properties(); //object of properties. 
    private static String store_initiator; //var to store value of initiator.
    public List<String> myList;
    public String[] s = null;

    //constructor of class
    Government(String configFile) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        
        //checks if configFile is null or empty.
        if(configFile == null || configFile.isEmpty()){
           return;
        }
        
        try{
            FileReader reader = new FileReader(configFile); //reads file and stores in reader.
            props.load(reader); //loads reader into properties object.

            Database = props.getProperty("Database"); //gets database value and stores in Database.
            User = props.getProperty("user"); //gets user value and stores in User. 
            Password = props.getProperty("password"); //gets password value and stores in Password.
            
            reader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        catch(IOException e){
            System.out.println("IO Exception");
        }
        
        try{
            
            //Load connection library between database and java
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            //Connect to dal database
            connection = DriverManager.getConnection(Database, User, Password);
            //Create a statement
            statement = connection.createStatement();
            System.out.println("Connection Successful");
        }
        catch(SQLException e){
            System.out.println("Error in connection with JDBC");
        }
    }
    
    //Empty constructor of class.
    Government(){};

    /*
     * MobileContact method will get all details of individual's device(like individual, date, duration, testHash) 
     * from recordContact method and hash value from MobileDevice class.
     * It also transfers information of initiator and contactInfo to government's database by inserting values in tables.
     */
    public boolean MobileContact(String initiator, String contactInfo) throws NoSuchAlgorithmException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
 
         //checks if initiator is null or empty.
        if(initiator == null || initiator.isEmpty()){
            return false;
        }
        
        //checks if contactInfo is null or empty.
        if(contactInfo == null || contactInfo.isEmpty()){
            return false;
        }
        
        String[] array_for_contact = contactInfo.split("\n"); //array stores splited contactInfo values.
        ArrayList<String> arraylist_for_contact = new ArrayList<>(); //ArrayList 
        arraylist_for_contact.addAll(Arrays.asList(contactInfo)); //stores contactInfo values in list
        store_initiator = initiator; //stores value of initiator into store_initiator var.
        ArrayList<Integer> contactList = new ArrayList<Integer>(); //list for storing positive tests.
        ResultSet govt_resultSet; //interface representing resultSet.
        
        //loop for iterating testHash values and storing into table.
        for(String store_testHash : arraylist_for_contact){
         s = store_testHash.split("//");
        }
        
        statement = connection.createStatement(); //create statement.
        statement.execute("INSERT IGNORE INTO MobileDevice VALUES (\"" + initiator + "\"); \n"); //insert query to insert hash value into table.
        
        //loop for iterating contactInfo values and storing into table.
        for(String contact : arraylist_for_contact){
            String[] s1 = contact.split("/");
            String inserts_contactInfo = "INSERT IGNORE INTO ContactInfo(mdHash, cdHash, Days, Duration, Notify) VALUES(?,?,?,?,?)"; //insert query.
            ps = connection.prepareStatement(inserts_contactInfo); //prepare statement.
            //sets parameters.
            ps.setString(1, initiator);
            ps.setString(2, s1[0]);
            ps.setString(3, s1[1]);
            ps.setInt(4, Integer.valueOf(s1[2]));
            ps.setBoolean(5, false);
            //executes query.
            ps.executeUpdate();
        }
        
      //select query for selecting such contacts who tested positive.
      String govt_result = "select distinct c.contactID from ContactInfo AS c, Device_Result AS dr, TestResult AS tr where c.cdHash = dr.mdHash AND"
                        + " dr.tHash = tr.TestHash AND c.mdHash = \"" + store_initiator + "\" AND ((tr.Days - c.Days) between 0 and 14) OR "
                        + "((c.Days - tr.Days) between 0 and 14) AND tr.Result = true";
        
        govt_resultSet = statement.executeQuery(govt_result); //executes query.
        
//        iterates over result set and adds into list.
        while(govt_resultSet.next()){
            contactList.add(govt_resultSet.getInt("contactID"));
        }
        
        //checks if contactList is empty or else it updates notify to true when user tests positive.
        if(contactList.isEmpty()){
            return false;
        }
        else{
            for(Integer in : contactList){
                statement.execute("UPDATE ContactInfo SET Notify = true WHERE contactID = \"" + in + "\";");
            }
        }
    //returns true when it sends data to database.
    return true;
    }
 
    /*
     * recordTestResult method will record the test of testHash and specifies that the collection taken before how many days which tested positive or negative.
     * It inserts values of testHash, date and result into TestResult table in database.
     */
    public void recordTestResult(String testHash, int date, boolean result) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
        
        //checks if testHash is null or empty and if date is .
        if(testHash == null || testHash.isEmpty()){
            return;
        }
        
        //insert query to insert into TestResult table database.
        String insert_in_testResult = "INSERT IGNORE INTO TestResult(TestHash, Days, Result) VALUES(?,?,?)";
        ps = connection.prepareStatement(insert_in_testResult); //prepare statement.
        //sets parameters.
        ps.setString(1,testHash); 
        ps.setInt(2,date);
        ps.setBoolean(3, result);
        //executes query.
        ps.executeUpdate();
        
       
        //checks if store_initiator is null or empty.
        if(store_initiator == null || store_initiator.isEmpty()){
            return;
        }
        
        //insert query to insert into table Device_Result.
        String insert_in_DeviceResult = "INSERT IGNORE INTO Device_Result(mdHash, tHash) VALUES(?,?)";
        ps = connection.prepareStatement(insert_in_DeviceResult); //prepare statement.
        //sets parameters.
        ps.setString(1, store_initiator);
        ps.setString(2, testHash);
        //executes query.
        ps.executeUpdate();
    }
   
    /*
     * this method performs intersection between pairs of individuals and returns number of large gatherings occuring to report to government.
    */
    int findGatherings(int date, int minSize, int minTime, float density) throws SQLException{
        
        Set<ArrayList<String>> individuals_pair = new HashSet<>(); //ArrayList stores pairs of individuals.
        Map<String,Set<String>> all_individuals = new HashMap<>(); //map for storing intersected key will values.
        
        //query to select mdHash and cdHash based on date.
        String reocrd_hashes = "SELECT c.mdHash, c.cdHash FROM ContactInfo as c WHERE Days = \""+date+  "\" GROUP BY c.mdHash, c.cdHash HAVING SUM(Duration) >= \""+ minTime+  "\";";
        //resultset executes query
        ResultSet rs = statement.executeQuery(reocrd_hashes);

        //iterating over result set and adding values into set.
        while (rs.next()){
            ArrayList<String>  Individuals_pair= new ArrayList<>();
            Individuals_pair.add(rs.getString("mdhash"));
            Individuals_pair.add(rs.getString("cdhash"));
            individuals_pair.add(Individuals_pair);
        }
        
        //intersects different pairs and retains all values.
        for (ArrayList<String> set : individuals_pair) {

            Set<String> t = new HashSet<>(set);
            for (ArrayList<String> pairs : individuals_pair) {
                Set<String> intersection_of_pairs = new HashSet<>(t);
                Set<String> t1 = new HashSet<>(pairs);
                //ignore duplicates and move ahead.
                if (t1.equals(t)) {
                    continue;
                }
                //retains all the values of the intersected pairs from individual sets.
                if (pairs.contains(set.get(0)) || pairs.contains(set.get(1))) {  
                    intersection_of_pairs.retainAll(t1);
                    t1.addAll(t);
                } else {
                    intersection_of_pairs.clear(); //clears set when finds dulpicate values and continues.
                    continue;
                }
                //makes union of sets having common key and adds it into other set.
                if (all_individuals.containsKey(intersection_of_pairs.toString())) {
                    all_individuals.get(intersection_of_pairs.toString()).addAll(t1); //
                } else {
                    all_individuals.put(intersection_of_pairs.toString(), t1); //if both values in set are mismatching then puts it as it is in map.
                }
            }
        }
        for (ArrayList<String> set : individuals_pair) {
            Set<String> pqr = new HashSet<>(set);
            for (String s : set) {
                if (all_individuals.containsKey("[" + s.toString() + "]")) {
                    continue;
                } else {
                    all_individuals.put("[" + s.toString() + "]", pqr);
                }
            }
        }
        
        Set<Set<String>> gatherings = new HashSet<>(all_individuals.values());//all gatherings values.
        int num_of_individuals = gatherings.size(); //gets gathering size.
        ArrayList<Set<String>> resultPairs = new ArrayList<>(gatherings);
        
        //if gatherings contains atleast minimum size individuals.
        for (int i = 0; i < resultPairs.size(); i++) {
            if (!(resultPairs.get(i).size() >= minSize)){
                resultPairs.remove(resultPairs.get(i));
            }
        }

        int n = num_of_individuals; //total number of individuals found.
        float c = individuals_pair.size(); //total size of individual pairs found.
        int m = (n*(n-1))/2; //calculating m.
        if (c/m > density){
            return resultPairs.size(); //returns resulting pairs.
        }else {
            return 0;
        }
        
    }
}

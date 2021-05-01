/*
 * Final Project: Covid-19 Contact Tracer.
 * Banner ID: B00870489
 * @author Yashvi
 */

package final_project;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

//Driver class which tests all methods
public class Contact_Tracer {
    
    public static void main(String args[]) throws InterruptedException, NoSuchAlgorithmException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
        
        //Government class object. Calling its constructor by passing file path of configFile.
        Government contactTracer = new Government("E:\\configFile.properties");
        //MobileDevice class objects.
        MobileDevice md = new MobileDevice("E:\\Yashvi\\MACS(Dalhousie University)\\Term 1(Winter 2021)\\Sdc lec\\Final Project\\configFile.properties", contactTracer);
        MobileDevice md1 = new MobileDevice("E:\\Yashvi\\MACS(Dalhousie University)\\Term 1(Winter 2021)\\Sdc lec\\Final Project\\configFile1.properties", contactTracer);
        MobileDevice md2 = new MobileDevice("E:\\Yashvi\\MACS(Dalhousie University)\\Term 1(Winter 2021)\\Sdc lec\\Final Project\\configFile2.properties", contactTracer);
        MobileDevice md3 = new MobileDevice("E:\\Yashvi\\MACS(Dalhousie University)\\Term 1(Winter 2021)\\Sdc lec\\Final Project\\configFile3.properties", contactTracer);
        MobileDevice md4 = new MobileDevice("E:\\Yashvi\\MACS(Dalhousie University)\\Term 1(Winter 2021)\\Sdc lec\\Final Project\\configFile4.properties", contactTracer);
        MobileDevice md5 = new MobileDevice("E:\\Yashvi\\MACS(Dalhousie University)\\Term 1(Winter 2021)\\Sdc lec\\Final Project\\configFile5.properties", contactTracer);
        MobileDevice md6 = new MobileDevice("E:\\Yashvi\\MACS(Dalhousie University)\\Term 1(Winter 2021)\\Sdc lec\\Final Project\\configFile6.properties", contactTracer);
        
        //1st insertion
        // method of MobileDevice class.
        md.recordContact(md1.hash_of_device, 6, 45); 
        //calls positiveTest method of MobileDevice class.
        md.positiveTest("Covid1*#1");
        //calls synchronizeData method of MobileDevice class.
        System.out.println(md.synchronizeData());
        //calls recordTestResult method of Government class.
        contactTracer.recordTestResult("Covid1*#1", 5, true);
        System.out.println(contactTracer.findGatherings(5, 3, 10, 1));

        //2nd insertion
        md3.recordContact(md.hash_of_device, 3, 10);
        md3.positiveTest("CovidTest2#");
        System.out.println(md3.synchronizeData());
        contactTracer.recordTestResult("CovidTest2#", 4, true);
        System.out.println(contactTracer.findGatherings(3, 3, 10, 1));

        //3rd insertion
        md4.recordContact(md1.hash_of_device, 4, 15);
        md4.positiveTest("CovidTest3#");
        System.out.println(md4.synchronizeData());
        contactTracer.recordTestResult("CovidTest3#", 7, true);
        System.out.println(contactTracer.findGatherings(3, 3, 10, 1));
    
        //4th insertion
        md5.recordContact(md4.hash_of_device, 2, 17);
        md5.positiveTest("CovidTest4#");
        System.out.println(md5.synchronizeData());
        contactTracer.recordTestResult("CovidTest4#", 3, true);
        System.out.println(contactTracer.findGatherings(1, 3, 10, 1));
      
        //5th insertion
        md6.recordContact(md5.hash_of_device, 7, 20);
        md6.positiveTest("CovidTest4#");
        System.out.println(md6.synchronizeData());
        contactTracer.recordTestResult("CovidTest908", 12, false);
        System.out.println(contactTracer.findGatherings(5, 3, 10, 1));
  
        //6th insertion
        md4.recordContact(md3.hash_of_device, 9, 8);
        md4.positiveTest("CovidTest5#");
        System.out.println(md4.synchronizeData());
        contactTracer.recordTestResult("CovidTest5#", 14, true);
        System.out.println(contactTracer.findGatherings(8, 3, 10, 1));
   
        //7th insertion
        md1.recordContact(md5.hash_of_device, 11, 16);
        md1.positiveTest("CovidTest6#");
        System.out.println(md1.synchronizeData());
        contactTracer.recordTestResult("CovidTest89#", 6, false);
        System.out.println(contactTracer.findGatherings(11, 3, 10, 1));

        //8th insertion
        md2.recordContact(md6.hash_of_device, 11, 16);
        md2.positiveTest("CovidTest6#");
        System.out.println(md2.synchronizeData());
        contactTracer.recordTestResult("CovidTest6#", 6, true);
        System.out.println(contactTracer.findGatherings(10, 3, 10, 1));

        //9th insertion
        md2.recordContact(md5.hash_of_device, 11, 16);
        md2.positiveTest("CovidTest778#");
        System.out.println(md2.synchronizeData());
        contactTracer.recordTestResult("CovidTest778#", 6, true);
        System.out.println(contactTracer.findGatherings(8, 3, 10, 1));
    }
}

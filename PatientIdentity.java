import java.util.Date;
import java.text.SimpleDateFormat;
public class PatientIdentity {
    private Name patientName;
    private Date patientDOB; 

    public PatientIdentity(Name name, Date DOB){
        patientName = name;
        patientDOB = DOB;
    }

    public boolean equals(PatientIdentity PID){
        return patientName.equals(PID.getName()) && patientDOB.equals(PID.getDOB());
    }

    //checks names first then Dates
    public boolean isLessThan (PatientIdentity PID){
        if(patientName.equals(PID.getName())){
            return patientDOB.compareTo(PID.getDOB()) < 0;
        }
        else if (patientName.equals(PID.getName()) && patientDOB.equals(PID.getDOB())){
            return false;
        }
        else {
            return patientName.isLessThan(PID.getName());
        }
    }

    public Name getName(){
        return patientName;
    }

    public Date getDOB(){
        return patientDOB;
    }

    public String toString(){
        return String.format("%s , %s" , patientDOB.toString() , patientName.toString());
    }
    //returns as null if the date format is incorrect
    public static Date addDate(String dateString){
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd" );
        try {
            date = format.parse(dateString);
        }
        catch(java.text.ParseException ex){
            return null;

        }
        return date;

    }
    public String outputDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    
    public static void unitTests(){

        int successCount = 0;
        int failCount = 0;
        PatientIdentity[] patientIdentities = new PatientIdentity[20];
        Patient[] patients = new Patient[20];

        //builds an array of patients to check that constructors work as expected
        for(int i = 0; i < 20; i++){
            Date date = new Date(i);
            Name name = new Name("First" + i, "Last" + i);
            PatientIdentity PID = new PatientIdentity(name, date);
            patientIdentities[i] = PID;
        }
        for(int i = 0; i < patientIdentities.length; i++){
            Patient patient = new Patient(patientIdentities[i]);
            patients[i] = patient;
            if(patients[i].getName().toString().equals("First" + i + ", Last" + i) && patients[i].getFullName().equals("Last" + i + ", First" + i)){
                successCount++;
            }
            else{
                failCount++;
                System.out.println("failed at name check");
            }
            
        }

        Name name1 = new Name("John", "Hill");
        Name name2 = new Name("Jane","Hancock");
        Name name3 = new Name("Cory", "smIth");
        Name name4 = new Name("Andy","Smith");
        Name name5 = new Name("WiLliam", "BroWn");
        Name name6 = new Name("William", "brown");


        Date date1 = addDate("2001-01-13");
        Date date2 = addDate("2000-03-12");
        Date date3 = addDate("2001-01-12");
        Date date4 = addDate("2002-02-02");
        Date date5 = addDate("2005-03-12");
        Date date6 = addDate("2005-03-12");
        //incorrect date format
        Date date7 = addDate("4gfha");


        PatientIdentity PID1 = new PatientIdentity(name1, date1);
        PatientIdentity PID2 = new PatientIdentity(name2, date2);
        PatientIdentity PID3 = new PatientIdentity(name3, date3);
        PatientIdentity PID4 = new PatientIdentity(name4, date4);
        PatientIdentity PID5 = new PatientIdentity(name5, date5);
        PatientIdentity PID6 = new PatientIdentity(name6, date6);
        PatientIdentity PID7 = new PatientIdentity(name6, date1);


        //incorrect format addDate function check
        if(date7 == null){
            successCount++;

        }
        else{
            failCount++;
            System.out.println("Failed at addDate funtion check, incorrect entry format");
        }
        //addDate function check
        if(date1.toString().equals("Sat Jan 13 00:00:00 EST 2001")){
            successCount++;
        }
        else{
            failCount++;
            System.out.println("Failed at addDate function check");
        }
        //toString function check
        if(PID1.toString().equals("Sat Jan 13 00:00:00 EST 2001 , John, Hill")){
            successCount++;
        }
        else{
            failCount++;
            System.out.println("Failed at Patient Identity toString check (PID1)");
        }
        //PID's are equal check
        if(PID5.equals(PID6)){
            successCount++;
        }
        else{
            failCount++;
            System.out.println("Failed at Patient Identity equals check (PID5,PID6)");
        }
        //PID's are equal check
        if(PID4.equals(PID4)){
            successCount++;
        }
        else{
            failCount++;
            System.out.println("Failed at Patient Identity equals check (PID4,PID4)");
        }
        //PID's are not equal check
        if(PID2.equals(PID3)){
            failCount++;
            System.out.println("Failed at Patient Identity equals check (PID2,PID3)");
        }
        //names are equal dates are different
        if(PID7.isLessThan(PID6)){
            successCount++;
        }
        else{
            failCount++;
            System.out.println("Failed at is less than check, names are equal dates different (PID7,PID6)");
        }
        //dates are equal names are different
        if(PID7.isLessThan(PID1)){
            successCount++;
        }
        else{
            failCount++;
            System.out.println("Failed at Patient identity check dates are equal names are different (PID7,PID1)");
        }
        //checks the behavior of the is less than function when two equal values are plugged into it 
        if(PID5.isLessThan(PID5)){
            failCount++;
            System.out.println("Failed at Patient Identity check; plugging equal identities into is less than function");
        }
        else{
            successCount++;
        }

        System.out.println("PATIENT IDENTITY   Successes: " + successCount + " Failures : " + failCount);
    }
}

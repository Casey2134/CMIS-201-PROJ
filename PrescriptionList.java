import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PrescriptionList {
    private class PrescriptionRecord{
        private Prescription data;
        private PrescriptionRecord next;

    }
    private class Alert{
        Medicine med1;
        Medicine med2;
        public Alert (String name1, String name2){
            med1 = new Medicine(name1);
            med2 = new Medicine(name2);
        }
        public boolean isLessThan(Alert alert){
            if(med1.getName().compareTo(alert.med1.getName()) < 0){
                return true;
            }
            else if(med2.getName().compareTo(alert.med2.getName())<0){
                return true;
            }
            else{
                return false;
            }
        }
        public String toString(){
            return ("Contraindication between " + med1.getName() + " and " + med2.getName());
        }

    }
    private class AlertList{
        
        private class AlertRecord {
            Alert data;
            AlertRecord next;
        }
        
        AlertRecord head = null;
        public void add(String name1, String name2){
            Alert alert = new Alert(name1, name2);
            AlertRecord newRecord = new AlertRecord();
            newRecord.data = alert;
            
            if(head == null){
                head = newRecord;
            }
            else if(newRecord.data.isLessThan(head.data)){
                newRecord.next = head;
                head = newRecord;
    
            }
            else{
                AlertRecord currentRecord = head;
                while (currentRecord != null) {
                    if(currentRecord.next == null){
                        currentRecord.next = newRecord;
                        break;
                    }
                    else if(newRecord.data.isLessThan(currentRecord.next.data)){
                        newRecord.next = currentRecord.next;
                        currentRecord.next = newRecord;
                        break;
                    }
                    
                    else{
                        currentRecord = currentRecord.next;
                    }
                    
                }
            }
            
        }
        AlertRecord currentRecord;
        public void init(){
            currentRecord = head;
        }
        public Alert next(){
            Alert returnValue = null;
            if(currentRecord == null){
                return null;
            }
            else{
                returnValue = currentRecord.data;
                currentRecord = currentRecord.next;
            }
            return returnValue;
        }
    }
    public Prescription find(String name){
        boolean done = false;
        Prescription returnRecord = null;
        PrescriptionRecord currentRecord = head;
        while(!done){
            if(currentRecord == null){
                return null;
            }
            else{
                if(name.equals(currentRecord.data.getName())){
                    returnRecord = currentRecord.data;
                    done = true;
                }
                else{
                    currentRecord = currentRecord.next;
                }
            }
        }
        return returnRecord;
    }
    public void add(Prescription prescription, MedicineList medicineList){
        if(!contraindicated(prescription, medicineList)){
            addPrescription(prescription);
        }
        
    }
    AlertList alertList = new AlertList();
    public boolean contraindicated(Prescription newPrescription, MedicineList medicineList){
        init();
        Prescription currentPrescription;
        while ((currentPrescription = next()) != null) {
            if(medicineList.checkInteraction(currentPrescription , newPrescription) && diffDays(currentPrescription.getDate(), newPrescription.getDate()) < 365 ){
                alertList.add(currentPrescription.getName(), newPrescription.getName());
                return true;
            }
            
        }
        return false;
        
    }
    public void printAlertList(){
        Alert alert;
        alertList.init();
        while ((alert = alertList.next())!= null) {
            System.out.println(alert.toString());
        }
    }
    
    PrescriptionRecord head = null;
    public void addPrescription(Prescription prescription){
        PrescriptionRecord newRecord = new PrescriptionRecord();
        newRecord.data = prescription;
        if(head == null){
            head = newRecord;
        }
        else if(newRecord.data.isLessThan(head.data)){
            newRecord.next = head;
            head = newRecord;

        }
        else{
            PrescriptionRecord currentRecord = head;
            while (currentRecord != null) {
                if(currentRecord.next == null){
                    currentRecord.next = newRecord;
                    break;
                }
                else if(newRecord.data.isLessThan(currentRecord.next.data)){
                    newRecord.next = currentRecord.next;
                    currentRecord.next = newRecord;
                    break;
                }
                
                else{
                    currentRecord = currentRecord.next;
                }
                
            }
        }
    }
    public boolean importFromFile(String fileName, PatientList patientList, MedicineList medicineList){
        boolean returnValue = true;
        File file = new File(fileName);
        Scanner scanner = null;
        Patient pat;
        try{
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                Prescription pres = Prescription.makePrescription(scanner.nextLine());
                if(pres != null){
                    patientList.initIteration();
                    while((pat = patientList.next()) != null){
                        if(pres.getUuid().equals(pat.getUUID())){
                            pat.getPrescriptionList().add(pres, medicineList);
                        }
                    }
                }
            }
        }
            catch(IOException ex){
                returnValue = false;
                ex.printStackTrace();
            }
            return returnValue;
        }
    PrescriptionRecord indexOfIteration = null;
    public void init(){
        indexOfIteration = head;
    }
    public Prescription next(){
        Prescription returnValue = null;
        if(indexOfIteration == null){
            return null;
        }
        else{
            returnValue = indexOfIteration.data;
            indexOfIteration = indexOfIteration.next;
        }
        return returnValue;


    }
    private static int diffDays( Date date1, Date date2 ) {
        return (int) (TimeUnit.DAYS.convert(Math.abs(date2.getTime() - date1.getTime()), TimeUnit.MILLISECONDS));
    }
    public boolean saveToFile(String fileName){
        boolean result = true;
        File file = new File(fileName);
        Prescription prescription;
        try{
            FileWriter writer = new FileWriter(file);
            init();
            while((prescription = next()) != null){
                writer.write(prescription.toCSV());
            }
            writer.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
            result = false;
        }
        return result;
    }

    public static void unitTests(){
        int successCount = 0;
        int failCount = 0;
        PatientList importList = new PatientList();
        importList.importFromFile("patients1000.csv");
        MedicineList medicineList = new MedicineList();
        medicineList.importFromFile("interactions.csv");
        PrescriptionList prescriptionList = new PrescriptionList();
        prescriptionList.importFromFile("prescriptions1000.csv", importList, medicineList);
        prescriptionList.importFromFile("new_prescriptions.csv", importList, medicineList);
        Patient nPatient;
        importList.initIteration();
        prescriptionList.init();
        int i = 0;

        importList.initIteration();
        prescriptionList.init();
        Prescription[] prescriptions = new Prescription[20];
        prescriptions[0] = Prescription.makePrescription("950993b0-7e84-44b4-83d3-bad25a1b3672,benzozone,2023-04-13,250,Miller");
        prescriptions[1] = Prescription.makePrescription("950993b0-7e84-44b4-83d3-bad25a1b3672,fantaprine,2022-05-08,50,Martinez");
        prescriptions[2] = Prescription.makePrescription("950993b0-7e84-44b4-83d3-bad25a1b3672,hexapone,2023-10-19,250,Soult");
        
        nPatient = importList.find(UUID.fromString("950993b0-7e84-44b4-83d3-bad25a1b3672"));
        PrescriptionList tList = new PrescriptionList();
        Prescription tPrescription;
        tList = nPatient.getPrescriptionList();
        tList.printAlertList();
        tList.init();
        i = 0;
        while((tPrescription = tList.next())!= null){
            if(tPrescription.equals(prescriptions[i])){
                successCount++;
                i++;
            }
            else{
                failCount++;
                i++;
            }
        }

        Patient patient;
        while ((patient = importList.next()) != null) {
            patient.getPrescriptionList().init();
            Prescription prescription1;
            while((prescription1 = patient.getPrescriptionList().next())!=null){
                
                System.out.println(prescription1.getName().toString());
            }
        }

        prescriptions[0] = Prescription.makePrescription("51ee21d6-e1e1-44a0-9b3a-f5fb889887b7,phytosome,2022-03-22,1000,Dehejia");
        prescriptions[1] = Prescription.makePrescription("51ee21d6-e1e1-44a0-9b3a-f5fb889887b7,sucrotan,2022-05-03,250,Gomez");
        prescriptions[2] = Prescription.makePrescription("51ee21d6-e1e1-44a0-9b3a-f5fb889887b7,sucrotan,2023-08-25,100,Miller");
        
        
        
        nPatient = importList.find(UUID.fromString("51ee21d6-e1e1-44a0-9b3a-f5fb889887b7"));
        tList = nPatient.getPrescriptionList();
        tList.printAlertList();
        tList.init();
        i = 0;
        while((tPrescription = tList.next())!= null){
            if(tPrescription.equals(prescriptions[i])){
                successCount++;
                i++;
            }
            else{
                failCount++;
                i++;
            }
        }
        prescriptions[0] = Prescription.makePrescription("f33847bd-68e5-4dbd-9f99-135774995171,diadrochlor,2023-05-26,1000,Pelly");
        prescriptions[1] = Prescription.makePrescription("f33847bd-68e5-4dbd-9f99-135774995171,diazozine,2023-03-12,100,Park");
        prescriptions[2] = Prescription.makePrescription("f33847bd-68e5-4dbd-9f99-135774995171,diazozine,2023-04-02,1000,Elizalde");
        prescriptions[3] = Prescription.makePrescription("f33847bd-68e5-4dbd-9f99-135774995171,oxypril,2023-06-12,250,Victor");

        
        
        nPatient = importList.find(UUID.fromString("f33847bd-68e5-4dbd-9f99-135774995171"));
        tList = nPatient.getPrescriptionList();
        tList.printAlertList();
        tList.init();
        i = 0;
        while((tPrescription = tList.next())!= null){
            if(tPrescription.equals(prescriptions[i])){
                successCount++;
                i++;
            }
            else{
                failCount++;
                i++;
            }
        }

        //should be 17 successes
        System.out.println("PRESCRIPTION LIST   Successes: " + successCount + " Failures: " + failCount);

    }
    
}

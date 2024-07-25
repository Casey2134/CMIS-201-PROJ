import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
public class Prescription {
    private Medicine medicine;
    private Date prescribedDate;
    private String prescribingDoctor;
    private UUID Uuid;
    private int dosage;

    public Prescription (String medicineName, Date date, String doctorString, UUID patientUuid, int prescriptionDosage){
        medicine = new Medicine(medicineName);
        prescribedDate = date;
        prescribingDoctor = doctorString;
        Uuid = patientUuid;
        dosage = prescriptionDosage;
        }
    public String getName(){
        return medicine.getName();
    }
    public UUID getUuid(){
            return Uuid;
    }
    public Date getDate(){
        return prescribedDate;
    }
    public String getDoctor(){
        return prescribingDoctor;
    }
    public static Prescription makePrescription(String line){
        Prescription returnValue = null;
        String[] details = line.split(",");
        String name = null;
        String docName = null;
        Date date = new Date();
        UUID newUuid = null;
        int dosage = 0;
        for (int i = 0; i < details.length; i++){
            if(details[i] == null){
                break;
            }
            else if(i == 0){
                try{
                    newUuid = UUID.fromString(details[i]);
                }
                catch(IllegalArgumentException ex){
                    
                }
            }
            else if(i == 1){
                name = details[i];
            }
            else if(i == 2){
                date = PatientIdentity.addDate(details[i]);
            }
            else if(i == 3){
                dosage = Integer.valueOf(details[i]);
            }
            else{
                docName = details[i];
                
            }
            
        }
        if(name != null && docName != null && date != null && newUuid != null){
            returnValue = new Prescription(name, date, docName, newUuid, dosage);
        }
        return returnValue;
    }
    public String outputDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    public boolean isLessThan(Prescription prescription){
        if(getName().compareTo(prescription.getName()) < 0) return true;
        else if (prescription.getName().equals(getName()) && getDate().before(prescription.getDate())) return true;
        else return false;
    }
    public String toCSV(){
        return (Uuid.toString() + ", " + medicine + ", " + prescribedDate + ", " + dosage + ", " + prescribingDoctor + "\n");
    }
    public boolean equals(Prescription prescription){
        if(getDate().equals(prescription.getDate()) && getDoctor().equals(prescription.getDoctor()) && getUuid().equals(prescription.getUuid()) && getDate().equals(prescription.getDate())){
            return true;
        }
        else{
            return false;
        }
    }
    
    
}

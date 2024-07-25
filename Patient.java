import java.util.UUID;
import java.util.Date;
public class Patient{
    private PatientIdentity patientIdentity;
    private UUID patientUUID;
    private PrescriptionList prescriptionList = new PrescriptionList();
    
    public Patient(PatientIdentity PID){
        patientIdentity = PID;
        patientUUID = UUID.randomUUID();
    }
    private Patient(PatientIdentity PID , UUID uuid){
        patientIdentity = PID;
        patientUUID = uuid;
    }
    public PrescriptionList getPrescriptionList(){
        return prescriptionList;
    }

    public PatientIdentity getPatientIdentity(){
        return patientIdentity;
    }
    public UUID getUUID(){
        return patientUUID;
    }
    public String getName(){
        return patientIdentity.getName().toString();
    }
    public String getFullName(){
        return patientIdentity.getName().getFullName();
    }
    public Date getDOB(){
        return patientIdentity.getDOB();
    }
    public String toString(){
        return (patientIdentity.getName().getFullName() + " " + patientIdentity.getDOB().toString());
    }
    public String toCSV(){
        return (patientIdentity.getName().getLastName() + "," + patientIdentity.getName().getFirstName() + "," + patientIdentity.outputDate(getDOB()) + "," + patientUUID.toString() + "\n");
    }

    static Patient makePatient(String line){
        Patient returnPatient = null;
        String[] details = line.split(",");
        String firstName = null;
        String lastName = null;
        Date DOB = new Date();
        UUID newUuid = null;
        for (int i = 0; i < details.length; i++){
            if(details[i] == null){
                break;
            }
            else if(i == 0){
                lastName = details[i];
            }
            else if(i == 1){
                firstName = details[i];
            }
            else if(i == 2){
                DOB = PatientIdentity.addDate(details[i]);
            }
            else{
                try{
                    newUuid = UUID.fromString(details[i]);
                }
                catch(IllegalArgumentException ex){
                    
                }
            }
        }
        if(lastName != null && firstName != null && DOB != null && newUuid != null){
            Name name = new Name(firstName, lastName);
            PatientIdentity patientID = new PatientIdentity(name, DOB);
            returnPatient =  new Patient(patientID , newUuid);
        }
        return returnPatient;
    }
 }
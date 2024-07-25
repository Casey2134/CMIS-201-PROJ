import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MedicineList {
    private class MedicineRecord{
        Medicine data;
        MedicineRecord next;
        public boolean isLessThan(MedicineRecord record){
            if(data.name.compareTo(record.data.name) < 0){
                return true;
            }
            else return false;
        }
    }
    MedicineRecord head = null;
    public void addMedicineRecord(MedicineRecord medicineRecord){
        if(head == null){
            head = medicineRecord;
        }
        else if(medicineRecord.isLessThan(head)){
            medicineRecord.next = head;
            head = medicineRecord;

        }
        else{
            MedicineRecord currentRecord = head;
            while (currentRecord != null) {
                if(currentRecord.next == null){
                    currentRecord.next = medicineRecord;
                    break;
                }
                else if(medicineRecord.isLessThan(currentRecord.next)){
                    medicineRecord.next = currentRecord.next;
                    currentRecord.next = medicineRecord;
                    break;
                }
                
                else{
                    currentRecord = currentRecord.next;
                }
                
            }
        }
    }
    public void add(String medicineName){
        MedicineRecord medicineRecord = new MedicineRecord();
        Medicine medicine = new Medicine(medicineName);
        medicineRecord.data = medicine;
        addMedicineRecord(medicineRecord);
    }
    public Medicine find(String name){
        init();
        Medicine currentMedicine = null;
        Medicine returnValue = null;
        while((currentMedicine = next())!= null){
            if(currentMedicine.name.equals(name)){
                returnValue = currentMedicine;
                break;
            }

        }
        return returnValue;
    }
    public boolean checkInteraction(Prescription prescription, Prescription checkedPrescription){
        InteractionsList interactionsList = find(prescription.getName()).getInteractionsList();
        if(interactionsList.find(checkedPrescription.getName()) != null){
            return true;
        }
        else return false;
    }
    MedicineRecord indexOfIteration = null;
    public void init(){
        indexOfIteration = head;
    }
    public Medicine next(){
        Medicine returnValue = null;
        if(indexOfIteration == null){
            return null;
        }
        else{
            returnValue = indexOfIteration.data;
            indexOfIteration = indexOfIteration.next;
        }
        return returnValue;
    }
    public boolean importFromFile(String fileName){
        boolean returnValue = false;
        File file = new File(fileName);
        Scanner scanner = null;
        try{
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] names = line.split(",");
                for(int i = 0; i < names.length; i++){
                    if(find(names[i]) == null){
                        add(names[i]);
                    }
                }
                find(names[0]).interactions.add(find(names[1]));
                find(names[1]).interactions.add(find(names[0]));
                

            }
        }
            catch(IOException ex){
                returnValue = false;
                ex.printStackTrace();
            }
            return returnValue;
        
    }

    public static void unitTests(){
        
    }

    
}

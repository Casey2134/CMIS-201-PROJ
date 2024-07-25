public class InteractionsList {
    public class InteractionRecord{
        Medicine data;
        InteractionRecord next;
        public boolean isLessThan(InteractionRecord interactionRecord){
            if(data.name.compareTo(interactionRecord.data.name) < 0)return true;
            else return false;
        }
    }
    InteractionRecord head = null;
    public void addInteractionRecord(InteractionRecord interactionRecord){
        if(head == null){
            head = interactionRecord;
        }
        else if(interactionRecord.isLessThan(head)){
            interactionRecord.next = head;
            head = interactionRecord;

        }
        else{
            InteractionRecord currentRecord = head;
            while (currentRecord != null) {
                if(currentRecord.next == null){
                    currentRecord.next = interactionRecord;
                    break;
                }
                else if(interactionRecord.isLessThan(currentRecord.next)){
                    interactionRecord.next = currentRecord.next;
                    currentRecord.next = interactionRecord;
                    break;
                }
                
                else{
                    currentRecord = currentRecord.next;
                }
                
            }
        }
    }
    public void add(Medicine medicine){
        InteractionRecord interactionRecord = new InteractionRecord();
        interactionRecord.data = medicine;
        if(find(medicine.name) == null){
            addInteractionRecord(interactionRecord);
        }
    }
    public Medicine find(String medicineName){
        init();
        Medicine currentMedicine;
        Medicine returnValue = null;
        while((currentMedicine = next()) != null){
            if(medicineName.equals(currentMedicine.name)){
                returnValue = currentMedicine;
                break;
            }
        }
        return returnValue;
    }
    InteractionRecord currentInteractionRecord;
    public void init(){
        currentInteractionRecord = head;
    }
    public Medicine next(){
        Medicine returnValue = null;
        if(currentInteractionRecord == null){
            return null;
        }
        else{
            returnValue = currentInteractionRecord.data;
            currentInteractionRecord = currentInteractionRecord.next;
        }
        return returnValue;
    }
    
    
    
}

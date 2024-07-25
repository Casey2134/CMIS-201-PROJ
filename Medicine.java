public class Medicine {
    String name;
    InteractionsList interactions = new InteractionsList();
    public Medicine(String medName){
        name = medName;
    }
    public boolean isLessThan(Medicine medicine){
        if (name.compareTo(medicine.name) < 0) return true;
        else return false;
    }
    public boolean equals(Medicine medicine){
        if(name.equals(medicine.name)) return true;
        else return false;
    }
    public InteractionsList getInteractionsList(){
        return interactions;
    }
    public String getName(){
        return name;
    }
}

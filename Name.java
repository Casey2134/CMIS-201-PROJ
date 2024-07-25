public class Name {
    private String firstName;
    private String lastName;
    public Name (String first, String last){
        firstName = (first);
        lastName = (last);
    }
    //used for debugging
    public String getFullName(){
        return  String.format("%s, %s", getLastName(), getFirstName());
    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }

    public boolean equals(Name patientName){
        return this.getFullName().toLowerCase().equals(patientName.getFullName().toLowerCase());
    }

    public boolean isLessThan(Name name){
        if(!this.getFullName().toLowerCase().equals(name.getFullName().toLowerCase())){
            return this.getFullName().toLowerCase().compareTo(name.getFullName().toLowerCase()) < 0;
        }
        else{
            //returns as false if the two names are equal because having an equal name is not less than another equal name
            return false;
        }
    }

    public String toString(){
        return String.format("%s, %s", firstName,lastName);
    }

    public static void unitTests(){
        int successCount = 0;
        int failCount = 0;
        Name name1 = new Name("John", "Hill");
        Name name2 = new Name("Jane","Hancock");
        Name name3 = new Name("Cory", "smIth");
        Name name4 = new Name("Andy","Smith");
        Name name5 = new Name("WiLliam", "BroWn");
        Name name6 = new Name("William", "brown");
        Name name7 = new Name("AnDy" , "jacobson");
        Name name8 = new Name("Mandy" , "Davis");

        //checks the behavior of the is less than function when two equal Names are plugged into it 
        if(name1.isLessThan(name1)){
            failCount++;
            System.out.println("Failed at Name isLessThan check with two equal names");
        }
        else{
            successCount++;
        }
        //checks toString method
        if(name8.toString().equals("Mandy, Davis")){
            successCount++;
        }
        else{
            failCount++;
            System.out.println("Failed at Name toString check (name8)");
        }
        //checks if equals method is working correctly
        if(name5.equals(name6)){
            successCount++;
        }
        else{
            failCount++;
            System.out.println("Failed at equals Name check; Names are equal (name5,name6)");
        }

        //checks is less than method
        if(name1.isLessThan(name2)){
            failCount++;
            System.out.println("Failed at is less than check, (name1,name2)");
        }
        else{
            successCount++;
        }

        //checks that the islessthan method is checking the first names 
        if(name3.isLessThan(name4)){
            failCount++;
            System.out.println("Failed at is less than check, last names equal first names different (name3,name4)");
        }
        else{
            successCount++;
        }
        //checks if is less than program is checking the last name before the first name
        if(name8.isLessThan(name7)){
            successCount++;
        }
        else{
            failCount++;
            System.out.println("Failed at Name is less than check, likely checking first names before last names (name8,name7)");
        }

        System.out.println("NAME   Successes: " + successCount + " Failures : " + failCount);

    }
}

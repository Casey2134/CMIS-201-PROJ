import java.util.Date;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
public class PatientList {

    private class Stack{

        int stackSize;
        int nStackIndex = 0;
        private PatientTree.TreeNode[] stack;
        public Stack(int size){
            stackSize = size;
            stack = new PatientTree.TreeNode[stackSize];
        }
        

        public void push(PatientTree.TreeNode node){
            stack[nStackIndex++] = node;
        }
        public PatientTree.TreeNode pop(){
            PatientTree.TreeNode node = null;
        if (nStackIndex > 0) {
            node = stack[ nStackIndex-1 ];
            stack[ nStackIndex-1 ] = null;
            nStackIndex--;
        }
        return node;

        }
    }
    private class PatientTree {
        
        private void findLeftMost(TreeNode root, Stack stack){
            TreeNode node = root;
            while(node != null){
                stack.push(node);
                node = node.left;
            }
        }
        private class TreeNode {
            public Patient data;
            public TreeNode left, right;
            public TreeNode(Patient pat){
                data = pat;
             }
        }
        // Root node of tree
        private TreeNode root = null;
        private TreeNode nextNode(Stack stack){
            TreeNode returnNode = stack.pop();
            if(returnNode != null){
                findLeftMost(returnNode.right, stack);
            }
            return returnNode;
        }
        public boolean addPatient(Patient pat){
            root = add(root, pat);
            return true;
        }
        public Patient findPatient(PatientIdentity PID){
            return find(PID,root).data; 
        }
        private TreeNode add(TreeNode node, Patient pat){
            if(node == null){
                node = new TreeNode(pat);
            }
            else{
                if(pat.getPatientIdentity().isLessThan(node.data.getPatientIdentity())){
                    node.left = add(node.left , pat);
                }
                else{
                    node.right = add(node.right , pat);
                }
            }
            return node;

        }
        private TreeNode find(PatientIdentity PID, TreeNode node){
            if(node == null){
                return null;
            }
            if(node.data.getPatientIdentity() == PID){
                return node;
            }
            else{
                if(PID.isLessThan(root.data.getPatientIdentity())){
                    find(PID, node.left);
                }
                else{
                    find(PID, node.right);
                }
                return node;
            }
            
        }
    }
    //list of patients in the system
    private PatientTree patients = new PatientTree();
  
    public boolean add(Patient newPatient){
        return patients.addPatient(newPatient);
    }
    public Patient find(UUID uuid){
        initIteration();
        Patient returnPatient = null;
        Patient pat;
        while((pat = next())!= null){
            if(pat.getUUID().equals(uuid)){
                returnPatient = pat;           
            }
        }
        return returnPatient;
    }
    public Patient find(PatientIdentity id){
        return patients.findPatient(id);
    }
    private Stack patientStack = new Stack(1000);
    public void initIteration(){
        patients.findLeftMost(patients.root,patientStack);
        
    }
    public Patient next(){
        PatientTree.TreeNode node = patients.nextNode(patientStack);
        if(node != null){
            return node.data;
        }
        else{
            return null;
        }
    }

    public boolean saveToFile(String fileName){
        boolean result = true;
        File file = new File(fileName);
        Patient pat;
        try{
            FileWriter writer = new FileWriter(file);
            initIteration();
            while((pat = next()) != null)
            {
                writer.write(pat.toCSV());
            }
            writer.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
            result = false;
        }
        return result;
    }
    public boolean importArray(Patient[] patientList){
        
        return merge(patientList);
    }
    private boolean merge(Patient[] patientList){
        for(int i = 0; i < patientList.length; i++){
            patients.addPatient(patientList[i]);
        }
        return true;
    }
    public boolean importFromFile(String fileName){
        boolean returnValue = true;
        File file = new File(fileName);
        Scanner scanner = null;
        try{
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                Patient pat = Patient.makePatient(scanner.nextLine());
                if(pat != null){
                    add(pat);
                }
            }
        }
            catch(IOException ex){
                returnValue = false;
                ex.printStackTrace();
            }
            return returnValue;
        }      
    







    public static void unitTests(){
        int successCount = 0;
        int failCount = 0;
        Name[] names = new Name[6];
        names[0] = new Name("John", "Hill");
        names[1]= new Name("Jane","Hancock");
        names[2] = new Name("Cory", "smIth");
        names[3] = new Name("Andy","Smith");
        names[4] = new Name("WiLliam", "Apple");
        names[5] = new Name("William", "brown");

        Date[] dates = new Date[6];
        dates[0] = PatientIdentity.addDate("2001-01-13");
        dates[1] = PatientIdentity.addDate("2000-03-12");
        dates[2] = PatientIdentity.addDate("2001-01-12");
        dates[3] = PatientIdentity.addDate("2002-02-02");
        dates[4] = PatientIdentity.addDate("2005-03-12");
        dates[5] = PatientIdentity.addDate("2005-03-12");
       

        PatientIdentity[] patientIdentities = new PatientIdentity[7];

        for(int i = 0; i < 6; i++){
            patientIdentities[i] = new PatientIdentity(names[i], dates[i]);
        }
        //unable to use for loop because one date and one name is reused
        patientIdentities[6] = new PatientIdentity(names[5], dates[0]);
        //not added to the list for later tests
        PatientIdentity patID = new PatientIdentity(names[0], dates[1]);

        Patient[] patients = new Patient[7];
        for(int i = 0; i < patients.length; i++){
            patients[i] = new Patient(patientIdentities[i]);
        }
        PatientList list = new PatientList();
        for(int i = 0; i < patients.length; i++){
            list.add(patients[i]);
        } 
        Patient[] ordredList = new Patient[7];
        ordredList[0] = patients[4];
        ordredList[1] = patients[6];
        ordredList[2] = patients[5];
        ordredList[3] = patients[1];
        ordredList[4] = patients[0];
        ordredList[5] = patients[3];
        ordredList[6] = patients[2];
        list.initIteration();
        //testing different orders patients are added to the list
        for(int i = 0; i < ordredList.length; i++){
            if(ordredList[i].getPatientIdentity().equals(list.next().getPatientIdentity())){
                successCount++;
            }
            else{
                failCount++;
                System.out.println("Failed at list order check");
            }
        }
        PatientList list2 = new PatientList();
        for(int i = patients.length - 1; i > -1; i--){
            list2.add(patients[i]);
        }
        list2.initIteration();
        for(int i = 0; i < ordredList.length; i++){
            if(ordredList[i].getPatientIdentity().equals(list2.next().getPatientIdentity())){
                successCount++;
            }
            else{
                failCount++;
                System.out.println("Failed at list order check");
            }
        }
        PatientList list3 = new PatientList();
        for(int i = 0; i < ordredList.length; i++){
            list3.add(ordredList[i]);
        }
        list3.initIteration();
        for(int i = 0; i < ordredList.length; i++){
            if(ordredList[i].getPatientIdentity().equals(list3.next().getPatientIdentity())){
                successCount++;
            }
            else{
                failCount++;
                System.out.println("Failed at list order check");
            }
        }
        //searches for a patient id that is not in the list
        if(list.find(patID) != null){
            successCount++;
        }
        else{
            failCount++;
        }


        //checks iteration as well as list order
        String[] expectedOutput = new String[7];
        expectedOutput[0] = "Apple, WiLliam Sat Mar 12 00:00:00 EST 2005";
        expectedOutput[1] = "brown, William Sat Jan 13 00:00:00 EST 2001";
        expectedOutput[2] = "brown, William Sat Mar 12 00:00:00 EST 2005";
        expectedOutput[3] = "Hancock, Jane Sun Mar 12 00:00:00 EST 2000";
        expectedOutput[4] = "Hill, John Sat Jan 13 00:00:00 EST 2001";
        expectedOutput[5] = "Smith, Andy Sat Feb 02 00:00:00 EST 2002";
        expectedOutput[6] = "smIth, Cory Fri Jan 12 00:00:00 EST 2001";
        String[] actualOutput = new String[7];
        int count = 0;
        list.initIteration();
        Patient pat;
        while ((pat = list.next()) != null){
            actualOutput[count] = (pat.toString());
            count++;
        }
        for(int i = 0; i < expectedOutput.length; i++){
            if(expectedOutput[i].equals(actualOutput[i])){
                successCount++;
            }
            else{
                failCount++;
            }
        }
        PatientList importList = new PatientList();
        importList.importFromFile("patients500.csv");
        importList.importFromFile("patients250.csv");
        importList.importFromFile("patients100.csv");
        importList.importFromFile("patientsErrors.csv");
        importList.saveToFile("output.csv");

        PatientList keyList = new PatientList();
        keyList.importFromFile("key.csv");
        importList.initIteration();
        keyList.initIteration();
        Patient importListPatient;
        Patient keyListPatient;
        System.out.println("PATIENT LIST  Successes: " + successCount + " Failures: " + failCount);
        //checks the order as well as the number of patients in the list
        //should be 886 successes
        while((importListPatient = importList.next()) != null && (keyListPatient = keyList.next()) != null){
            if(importListPatient.getPatientIdentity().equals(keyListPatient.getPatientIdentity())){
                successCount++;
            }
            else{
                failCount++;
                System.out.println("Failed at ouput file check");
                break;
            }
        }
        System.out.println("PATIENT LIST  Successes: " + successCount + " Failures: " + failCount);
    }
}

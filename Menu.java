//for doing a console.readline:
import java.io.Console;

public class Menu {
  private ContactManager manager = new ContactManager();
  //for all the console.readLines():
  private Console console = System.console();

 public Menu() { }

 public void Start() {

  String menuChoice = "9";
  do {
   menuChoice = displayMenu();
   switch (menuChoice) {
    case "1":  //get contact info by name
       manager.getContactByName();
     break;
    case "2":  //see all the contacts     
     manager.getAllContacts();
     break;
    case "3": //add contact
     createContact();
     break;
    case "4":  //remove contact
     manager.deleteAContact();
     break;
    case "5": //get the next upcoming birthday
     manager.getNextBirthday();
     break;
    case "6": //exit
     System.out.println("Thank you, have a nice day.");
     break;
    default:
     System.out.println("Invalid choice. Please try again.");
   }

  } while (!menuChoice.equals("6"));

 }

 public String displayMenu() {

   System.out.println();
   System.out.println();
   System.out.println();
  // this will need to return an int (the menuChoice)
  String birthdayTitle = "Birthday Program Menu";
  manager.prettifyString(birthdayTitle, "*", 0);
  System.out.println("Please choose an option:");
  System.out.println("1. Get Contact by name");
  System.out.println("2. See all contacts");
  System.out.println("3. Add Contact");
  System.out.println("4. Remove Contact");
  System.out.println("5. Get the next birthday");
  System.out.println("6. Exit");
  manager.prettifyString("", "*", birthdayTitle.length());
  String menuChoice = console.readLine();
  System.out.println();
  System.out.println();
  return menuChoice;
 }

 

 public void createContact(){

//get the info for the new contact:
  System.out.println("Please enter the first name of the contact: ");
  String fname = console.readLine();
  System.out.println("Enter the last name: ");
  String lname = console.readLine();
  String phone = getProperPhone();  
  String bday = getProperBirthdate();
  //create that new contact so I can use addContact in the manager class:
  Contact c = new Contact(fname, lname, phone, bday);
  //add the new contact in the manager contact
  if (manager.addContact(c)){
   System.out.println("Contact has been added.");
  }
  else{
    System.out.println("There seems to be an error. Please try again.");
  };


 }

 public String getProperBirthdate(){
//the hyphen pattern is proper way for the user to format the birthday: yyyy-mm-dd
  String hyphenPattern = "\\d{4}-\\d{2}-\\d{2}";
  boolean isProperBday = false;
  String bday = "";
  //running a do-while to make sure the user enters the bday in the proper format
  do{
  System.out.println("Enter the birthdate: ");
  String birthdayString = console.readLine();
  if (birthdayString.matches(hyphenPattern)) {
    isProperBday = true;
    bday = birthdayString;
  } else {
    System.out.println("Birthday format needs to be yyyy-mm-dd. Try again. ");
  }
  }while(!isProperBday);
  return bday;
 }

 public String getProperPhone(){
//this is the proper way for the user to format the phone: 000-000-0000
  String hyphenPattern = "\\d{3}-\\d{3}-\\d{4}";
  boolean isProperPhone = false;
  String phone = "";
  //running a do-while to make sure the user enters the phone in the proper format
  do{
  System.out.println("Enter the phone number: ");
  String phoneNumber = console.readLine();
  if (phoneNumber.matches(hyphenPattern)) {
    isProperPhone = true;
    phone = phoneNumber;
  } else {
    System.out.println("Phone format needs to be 000-000-0000. Try again.");
  }
  }while(!isProperPhone);
  return phone;
 }


}

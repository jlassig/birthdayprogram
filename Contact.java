
public class Contact {
 private String firstName;
 private String lastName;
 private String phone;
 private String birthdayString;

   public Contact(String firstName, String lastName, String phone, String birthdayString){
  this.firstName = firstName;
  this.lastName = lastName;
  this.phone = phone;
  this.birthdayString = birthdayString;
 }

 ContactManager manager = new ContactManager();

 
 public String displayContact(){
  String contactString = ("Name: "+firstName + " " + lastName + "\tPhone: "+ phone + "\t Birthday: " + birthdayString+"\tDays till Bday: " + manager.getDaysLeftUntilBday(birthdayString));
  return contactString;
 }
//this is for when the user adds a contact. This is how it is written to the txt file from the ContactManager. 
 public String saveContact(){
  String contactInfo = (firstName + "|" + lastName + "|" + phone + "|" + birthdayString);
  return contactInfo;
 }

 public String getFirstName(){
  return firstName;
 }
 public String getBirthday(){
  return birthdayString;
 }
 public String getPhone(){
  return phone;
 }
 public String getFullName(){
  String fullName = (firstName + " " + lastName);
  return fullName;
 }
 
}

import java.io.Console; //ability to use console.readline()
import java.io.FileWriter; // Import the FileWriter class
import java.io.IOException; // Import the IOException class to handle errors
import java.nio.charset.StandardCharsets; // for reading files
import java.util.ArrayList; //for the contacts arrayList
import java.util.List; //for the string List
import java.nio.file.Files; 
import java.nio.file.Paths; //used for getting the path to the txt file that holds the contacts
import java.io.PrintWriter; //so that when I write to the file, it goes to the next line
import java.time.LocalDate; //these next two are for the dates
import java.time.temporal.ChronoUnit;

public class ContactManager {

  // create an arrayList to hold the Contacts:
  private ArrayList<Contact> contactsList;
  private Console console = System.console();

  public ContactManager() {
    contactsList = new ArrayList<>();

  }

  // pulling from a file to be able to do stuff with:
  public void loadContacts() {
    // empty contact list first
    contactsList.clear();

    try {
      List<String> contactStrings = Files.readAllLines(Paths.get("contacts.txt"), StandardCharsets.UTF_8);

      // contactStrings.forEach(line -> System.out.println(line));
      contactStrings.forEach(line -> {
        String[] parts = line.split("[|]");
        Contact contact = new Contact(parts[0], parts[1], parts[2], parts[3]);
        contactsList.add(contact);

      });

    } catch (IOException e) {
      System.out.println("Failed to read file: " + "contacts.txt" + e);
    }
  }

  public void getAllContacts() {
    // counter to number the contacts, starting with 1 because people like their
    // lists to start with 1
    int counter = 1;
    loadContacts();
    String contactTitle = " ".repeat(29) + "Your Contacts:" + " ".repeat(29);
    prettifyString(contactTitle, "-", 0);
    if (contactsList.size() > 0) {

      // column names:
      System.out.println("\tName:\t\t\tPhone:\t\tBirthdate:\tDays till Bday:");

      // now to print out the table of contact information:
      for (Contact contact : contactsList) {
        // this first bit makes sure the name isn't too long. Since the name is the
        // first column and a variable length, it can mess up the alignment of the other
        // columns if the name is too short or too long.
        // the size of the nameColumn:
        int nameColumn = 20;
        int nameLength = contact.getFullName().length();
        String modifiedName = "";
        // for the short names
        if (nameLength < nameColumn) {
          int addSpace = nameColumn - nameLength;
          modifiedName = contact.getFullName() + (" ").repeat(addSpace);
        } // for the long names
        else if (nameLength > nameColumn) {
          int removeLetters = nameColumn - nameLength;
          modifiedName = contact.getFullName().substring(0, nameLength + removeLetters);
        } // for the rest of the names
        else {
          modifiedName = contact.getFullName();
        }
        System.out.println(counter + ".\t" + modifiedName + "\t" + contact.getPhone() + "\t" + contact.getBirthday()
            + "\t" + getDaysLeftUntilBday(contact.getBirthday()));
        counter++;
      }
    } else {
      System.out.println("There are no contacts. You should add one.");
    }
    prettifyString("", "-", contactTitle.length());
  }

  public void deleteAContact() {

    boolean isNumber = false;
    int contactNum = 0;
    loadContacts();
    if (contactsList.size() > 0) {

      do {

        String contactToDelete = "";
        getAllContacts();

        System.out.println("Enter the contact # of the contact you wish to delete.");
        contactToDelete = console.readLine();
        try {
          contactNum = Integer.parseInt(contactToDelete);
          if ((contactNum <= contactsList.size()) && (contactNum >= 1)) {
            isNumber = true;
          } else {
            System.out.println("You entered an invalid number. Try again.");
          }

        } catch (NumberFormatException e) {
          System.out.println("You need to enter a number. Try again.");
        }
      } while (!isNumber);

      contactNum--;
      contactsList.remove(contactNum);

      try {
        FileWriter myWriter = new FileWriter("contacts.txt", false);

        myWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      for (Contact contact : contactsList) {
        addContact(contact);
      }
    } else {
      System.out.println("There are no contacts. You should add one.");
    }

  }

  public void getContactByName() {
    // first get the contacts:
    loadContacts();

     // get the user's input for the name they want to find:
    System.out.println("Enter the contact first name:");
    String nameQuery = console.readLine();
    boolean found = false;
    // iterate through the contacts to see if such a first name exists:
    for (Contact contact : contactsList) {
      if (contact.getFirstName().toLowerCase().equals(nameQuery.toLowerCase())) {
        System.out.println();
        System.out.println();
        String contactString = contact.displayContact();
        //make it oh so pretty:
        prettifyString("", "-", contactString.length()+4);
        System.out.println(contactString);
        prettifyString("", "-", contactString.length()+4);
        found = true;
      }
    }
    if (!found) {
      System.out.println(nameQuery + " is not in your contacts. Perhaps you should add them.");
    }
  }

  // writing to a file:
  public boolean addContact(Contact contactInfo) {
    try {
      // FileWriter writes to the file. "true" means that it doesn't overwrite, but
      // that it adds on to what is already in there
      FileWriter myWriter = new FileWriter("contacts.txt", true);
      // PrintWriter means that we can have the next line get printed on the next
      // line.
      PrintWriter myPrinter = new PrintWriter(myWriter);
      // myWriter.write(contactInfo.saveContact());
      myPrinter.println(contactInfo.saveContact());
      myPrinter.close();
      myWriter.close();
      return true;

    } catch (IOException e) {
      System.out.println("An error occured");
      e.printStackTrace();
      return false;
    }

  }

  public void getNextBirthday() {
    loadContacts();
    if (contactsList.size() > 0) {
      long[] daysTillBday = new long[(contactsList.size())];
      int counter = 0;
      // find out how many days until each birthday:
      for (Contact contact : contactsList) {
        long daysLeft = getDaysLeftUntilBday(contact.getBirthday());
        // filling up the long[] array
        daysTillBday[counter] = daysLeft;
        // incrementing the counter to go through the entire array.
        counter++;
      }
      // now go through and see which is the smallest number:
      long daysTill = 1000;
      int indexOfDaysTill = 150;
      for (int i = 0; i < daysTillBday.length; i++) {
        if (daysTillBday[i] < daysTill) {
          indexOfDaysTill = i;
          daysTill = daysTillBday[i];
        }
      }
      // get the contact out of the contactsList based on the array
      String nextBdayString = contactsList.get(indexOfDaysTill).displayContact();
      //now make it oh so pretty:
      System.out.println("The next birthday in your contacts is: ");
      prettifyString("", "-", nextBdayString.length() +4);      
      System.out.println(nextBdayString);
      prettifyString("", "-", nextBdayString.length() +4);
    } else {
      System.out.println("There are no contacts. You should add one.");
    }

  }

  public long getDaysLeftUntilBday(String birthdayString) {
    LocalDate today = LocalDate.now();
    //get the current year
    int year = today.getYear();
    int nextYear = year + 1;
    //turn the birthdayString into a LocalDate 
    LocalDate birthdate = LocalDate.parse(birthdayString);
    //add the current year
    LocalDate updatedBirthdate = birthdate.withYear(year);
    long daysLeft;
    //now we run a conditional check. If the birthday of this year - today returns a negative amount of days, then we update the birthday to next year and daysLeft now has a positive amount of days.
    if (today.until(updatedBirthdate, ChronoUnit.DAYS) < 0) {
      LocalDate nextBirthdate = birthdate.withYear(nextYear);
      daysLeft = today.until(nextBirthdate, ChronoUnit.DAYS);
      //else if birthday of this year - today = 0, that means the birthday is today! yay!
    } else if (today.until(updatedBirthdate, ChronoUnit.DAYS) == 0) {
      daysLeft = 0;
      //otherwise get the amount of days left from birthday of this year - today. 
    } else {
      daysLeft = today.until(updatedBirthdate, ChronoUnit.DAYS);
    }
    return daysLeft;

  }

  public void prettifyString(String plainString, String prettySymbol, int numOfRepeats) {
    // get the length of the string
    int plainStringLength = plainString.length();
    if (plainStringLength > 0) {
      // print a line of the character in pretty Symbol with an extra 4 on each side
      System.out.println(prettySymbol.repeat(plainStringLength + 8));
      // print the plainString in the middle with a padding of 4 spaces on each side
      System.out.println("    " + plainString + "    ");
      System.out.println(prettySymbol.repeat(plainStringLength + 8));
    } else {
      System.out.println(prettySymbol.repeat(numOfRepeats + 8));
    }
  }

}

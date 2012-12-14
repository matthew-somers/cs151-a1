package a1;
import java.util.*;
import java.io.*;

/**
 * A system for making plane reservations. It supports reading and saving from
 * 	file, and is 100% working to spec.
 * @author Matthew Somers
 * @version 1.0
 */
public class ReservationSystem 
{
   public static void main(String[] args) 
   {
      Plane plane = new Plane();

      if (args.length == 1)
         readinfile(plane, args[0]);
      
      Scanner in = new Scanner(System.in);
      String input;
      
      //main loop
      while(true)
      {
         System.out.println("Add [P]assenger, Add [G]roup, [C]ancel Reservations,\n" +
            "Print Seating [A]vailability Chart, Print [M]anifest, [Q]uit.");
         System.out.print("   Type the bracketed letter of your selection: ");
         
         input = in.nextLine();
         input = input.toUpperCase();
         
         if (input.length() == 0)
        	 System.out.println("Invalid input. Type the letter in brackets of your selection.\n");
         
         else if (input.charAt(0) == 'P')
            add_passengerUI(plane, in);
         
         else if (input.charAt(0) == 'G')
            add_groupUI(plane, in);
         
         else if (input.charAt(0) == 'C')
            cancelUI(plane, in);
         
         else if (input.charAt(0) == 'A')
            System.out.println(plane.get_availability_chart());
         
         else if (input.charAt(0) == 'M')
            manifestUI(plane, in);
         
         else if (input.charAt(0) == 'Q')
         {
           if (args.length == 1)
              savefile(plane, args[0]);
           
           else
              System.out.println("Flight data not saved because file to be written was not provided.\n");
            
            System.exit(0);
         }
         
         else
            System.out.println("Invalid input. Type the letter in brackets of your selection.\n");
      }
   }
   
   /**
    * The method for taking in the command line text file's passengers.
    * @param plane The plane we're working with.
    * @param args The name of the text file from command line.
    * @param in The scanner passed mainly for input redirection.
    */
   public static void readinfile(Plane plane, String args)
   {
         File file = new File(args);
         
         //don't bother doing anything here if file doesn't exist yet
         if (file.exists())
         {
            String line;
            int seat;
            String[] linedata = new String[Plane.TEXT_FILE_PARAMETERS]; //
            String group;
            try
            {
                Scanner fin = new Scanner(file);
                
                while (fin.hasNextLine())
                {
                   line = fin.nextLine();
                   linedata = line.split(",");
                   seat = Integer.parseInt(linedata[1]);
                   
                   //manage last comma of text file, people not necessarily having groups
                   if (linedata.length == (Plane.TEXT_FILE_PARAMETERS-1))
                      group = null;
                   else
                      group = linedata[Plane.TEXT_FILE_PARAMETERS-1];
                   
                   //seat preference isn't important anymore, already seated
                   plane.addfromfile(seat, new Passenger(linedata[2], linedata[0], group));
                }
                
            }
            catch (FileNotFoundException filenotfound) {}
         }
   }
   
   /**
    * Deals with the input and output when adding a new passenger.
    * @param plane The plane we're adding to.
    * @param in The scanner passed mainly for input redirection.
    */
   public static void add_passengerUI(Plane plane, Scanner in)
   {
      String name;
      String service_class;
      String seat_preference;
      //Scanner in = new Scanner(System.in);
      String output;
      
      do 
      {
          System.out.print("Name: ");
          name = in.nextLine();
          if (name.contains(","))
             System.out.println("ERROR: Commas aren't allowed in names.");
      }
      while (name.contains(","));
      
      
      do
      {
         System.out.print("Service Class? ([F]irst or [E]conomy): ");
         service_class = in.nextLine();
         service_class = service_class.toUpperCase();
         if ((service_class.charAt(0) != 'F') 
               && (service_class.charAt(0) != 'E'))
         {
            System.out.println("ERROR: Invalid service class selected.");
         }
      }
      while ((service_class.charAt(0) != 'F') 
            && (service_class.charAt(0) != 'E'));
      
      
      do
      {
         if (service_class.charAt(0) == 'F')
            System.out.print("Seat Preference? ([W]indow, or [A]isle): ");
         
         else if (service_class.charAt(0) == 'E')
            System.out.print("Seat Preference? ([W]indow, [A]isle, or [C]enter): ");
         
         seat_preference = in.nextLine();
         
         seat_preference = seat_preference.toUpperCase();
            
         Passenger passenger = new Passenger(name, service_class, null);
         
         //actually add the passenger
         output = plane.add_passenger(seat_preference, passenger);
         
         if (output.equals("")) //seat_preference not available
            System.out.println("Sorry, no seats available at your seat preference. Please choose another.");
         
         else if (output.equals("full")) // plane full
            System.out.println("Sorry, that service class is full.\n");
         
         else //successfully seated
            System.out.println("Seated at: " + output + "\n");
      }
      while (output.equals(""));
      
   }
   
   /**
    * Deals with input and output when adding a new group.
    * @param plane The plane we're adding to.
    * @param in The scanner passed mainly for input redirection.
    */
   public static void add_groupUI(Plane plane, Scanner in)
   {
      String groupname;
      String names;
      String service_class;
      int i;
      
      do
      {
         System.out.print("Group Name: ");
         groupname = in.nextLine();
         if (groupname.contains(","))
            System.out.println("Error: Commas aren't allowed in group names.");
      }
      while (groupname.contains(","));
      
      System.out.print("Names of people in group (separated by commas): ");
      names = in.nextLine();
      
      do
      {
         System.out.print("Service Class? ([F]irst or [E]conomy): ");
         service_class = in.nextLine();
         service_class = service_class.toUpperCase();
         if ((service_class.charAt(0) != 'F') && (service_class.charAt(0) != 'E'))
            System.out.println("ERROR: Invalid service class selected.");
      }
      while ((service_class.charAt(0) != 'F') && (service_class.charAt(0) != 'E'));
      
      int totalnames = 1;
      for (i = 0; i < names.length(); i++)
      {
         if (names.charAt(i) == ',')
            totalnames++;
      }
      String[] namearray = new String[totalnames];
      namearray = names.split(",");
      
      ArrayList<Passenger> group = new ArrayList<Passenger>();
      
      for (i = 0; i < totalnames; i++)
      {
        namearray[i] = namearray[i].trim(); //in case people put spaces
         group.add(new Passenger(namearray[i], service_class, groupname));
      }
      
      String output = plane.add_group(group);
      if (output != null)
         System.out.println(output);
      else
         System.out.println("This service class is full.\n");
   }
   
   /**
    * Deals with input and output when canceling a reservation.
    * @param plane The plane we're canceling from.
    * @param in The scanner passed mainly for input redirection.
    */
   public static void cancelUI(Plane plane, Scanner in)
   {
      String input;
      boolean deleted = false;
      
      System.out.print("What type of cancellation? ([I]ndividual, or a [G]roup): ");
      input = in.nextLine();
      input = input.toUpperCase();
      
      if (input.charAt(0) == 'I')
      {
         System.out.print("Name: ");
         input = in.nextLine();
         deleted = plane.cancel_reservation(input);
      }
      
      else if (input.charAt(0) == 'G')
      {
         System.out.print("Group Name: ");
         input = in.nextLine();
         deleted = plane.cancel_group(input);
      }
      
      if (deleted)
         System.out.println("Successful reservation cancelation for " + input + ".\n");
      else
         System.out.println("Request failed.\n");
   }
   
   /**
    * Deals with input and output when generating a plane's manifest.
    * @param plane The plane who's manifest we're getting.
    * @param in The scanner passed mainly for input redirection.
    */
   public static void manifestUI(Plane plane, Scanner in)
   {
      String service_class;
      
      System.out.print("Manifest of which service class? ([F]irst or [E]conomy): ");
      service_class = in.nextLine();
      service_class = service_class.toUpperCase();
      
      System.out.println(plane.get_manifest(service_class));
   }
   
   /**
    * Saves all plane passengers to the text file provided from command line
    * 	at the beginning of program.
    * @param plane The plane who's data we're saving.
    * @param filename The file provided from command line.
    */
   public static void savefile(Plane plane, String filename)
   {
      try
      {   
         PrintWriter output = new PrintWriter(new FileWriter(filename));
         
         String flightdata = plane.get_flightdata();
         
         output.print(flightdata);
         output.close();
      }
      
      catch (IOException e) {} //should never happen
      
      System.out.println("\nFlight data successfully saved to: " + filename);
   }
}
      
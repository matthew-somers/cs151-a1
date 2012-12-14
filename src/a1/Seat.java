package a1;

/**
 * A class representing an individual seat on a plane.
 * @author Matthew Somers
 * @version 1.0
 */
public class Seat 
{
   private Passenger occupant;
   private String seat_type;
   private String seat_number;
   
   /**
    * Constructor to build a seat. Contains specific info for this plane's
    * 	layout of First and Economy seat layout.
    * @param seat_number The number for this seat.
    * @param service_class Which service class this seat is in. This is only
    * 	used to help set seat type.
    */
   public Seat(String seat_number, String service_class)
   {
      occupant = null; //until a specific passenger is assigned this seat
      this.seat_number = seat_number;
      
      if (service_class.equals("F"))
      {
         if (seat_number.contains("A") || seat_number.contains("D"))
            seat_type = "W";
         
         else
            seat_type = "A";
      }
      
      else if (service_class.equals("E"))
      {
         if (seat_number.contains("A") || seat_number.contains("F"))
            seat_type = "W";
         
         else if (seat_number.contains("C") || seat_number.contains("D"))
            seat_type = "A";
         
         else
            seat_type = "C";
      }
   }
   
   /**
    * A simple getter for this seat's "plane" number (e.g. 1A)
    * @return This seat's seat number.
    */
   public String getseat_number() { return seat_number; }
   
   /**
    * A simple getter for this seat's type (W, C, or A)
    * @return This seat's type.
    */
   public String getseat_type() { return seat_type; }
   
   /**
    * Gets the person in this seat.
    * @return This seat's passenger.
    */
   public Passenger getoccupant() { return occupant; }
   
   /**
    * Sets a passenger into this seat (or removes passenger if null).
    * @param passenger The passenger to be seated (or removed if null).
    */
   public void setoccupant(Passenger passenger)
   {
      occupant = passenger;
   }
   
}

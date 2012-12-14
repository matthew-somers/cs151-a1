package a1;
import java.util.*;

/**
 * A class representing a plane full of seats.
 * This plane has 2 sections of seats.
 * @author Matthew Somers
 *
 */
public class Plane 
{
   private ArrayList<Seat> first_class_seats;
   private ArrayList<Seat> economy_class_seats;
   
   private static final int FIRST_CLASS_COLS = 4;
   private static final int FIRST_CLASS_ROWS = 2;
   private static final int ECONOMY_CLASS_COLS = 6;
   private static final int ECONOMY_CLASS_ROWS = 20;
   private static final int FIRST_CLASS_SIZE = (FIRST_CLASS_ROWS*FIRST_CLASS_COLS);
   private static final int ECONOMY_CLASS_SIZE = (ECONOMY_CLASS_ROWS*ECONOMY_CLASS_COLS);
   private static final int ECONOMY_ROW_START = 10;
   public static int TEXT_FILE_PARAMETERS = 4; //useful for ReservationSystem when reading in file values
   
   private static int first_class_seats_remaining = FIRST_CLASS_SIZE;
   private static int economy_class_seats_remaining = ECONOMY_CLASS_SIZE;

   /**
    * Plane constructor that makes all of the seats.
    * Each seat has a seat number that takes a bit of plane specific info to create.
    * The plane specific info is all constants defined at the top of this class.
    */
   public Plane()
   {
      first_class_seats = new ArrayList<Seat>();
      economy_class_seats = new ArrayList<Seat>();
      int row, col;
      String seat_number = "";

      //create first class seats
      for (row = 1; row <= FIRST_CLASS_ROWS; row++)
      {
         for (col = 1; col <= FIRST_CLASS_COLS; col++)
         {
            seat_number += row;

            if (col == 1)
               seat_number += "A";

            else if (col == 2)
               seat_number += "B";

            else if (col == 3)
               seat_number += "C";

            else if (col == 4)
               seat_number += "D";

            //potentially larger aircraft support
            else if (col == 5)
               seat_number += "E";

            else if (col == 6)
               seat_number += "F";

            //add this newly created seat
            first_class_seats.add(new Seat(seat_number, "F"));
            seat_number = ""; //RESET
         }
      }

      //create economy seats
      for (row = ECONOMY_ROW_START; row <= ECONOMY_CLASS_ROWS+ECONOMY_ROW_START; row++)
      {
         for (col = 1; col <= ECONOMY_CLASS_COLS; col++)
         {
            seat_number += row;

            if (col == 1)
               seat_number += "A";

            else if (col == 2)
               seat_number += "B";

            else if (col == 3)
               seat_number += "C";

            else if (col == 4)
               seat_number += "D";

            else if (col == 5)
               seat_number += "E";

            else if (col == 6)
               seat_number += "F";

            //potentially larger aircraft support
            else if (col == 7)
               seat_number += "G";

            else if (col == 8)
               seat_number += "H";

            //add this newly created seat
            economy_class_seats.add(new Seat(seat_number, "E"));
            seat_number = ""; //RESET
         }
      }
   }

   /**
    * Loads in previously added passengers from a saved file.
    * @param seat This passengers seat. It's stored in integer as opposed to
    * 	the plane's seat number system.
    * @param passenger The passenger to be seated.
    */
   public void addfromfile(int seat, Passenger passenger)
   {
      if (passenger.getservice_class().equals("F"))
      {
         first_class_seats.get(seat).setoccupant(passenger);
         first_class_seats_remaining--;
      }

      else if (passenger.getservice_class().equals("E"))
      {
         economy_class_seats.get(seat).setoccupant(passenger);
         economy_class_seats_remaining--;
      }
   }

   /**
    * Add a passenger from user input.
    * @param seat_preference This passengers preferred seat type. If it isn't
    * 	available, it returns and ReservationSytem asks for another.
    * @param passenger The passenger to be seated.
    * @return The passenger's seat number if successful. If a service class is
    * 	full, returns "". If plane is full, returns "full".
    */
   public String add_passenger(String seat_preference, Passenger passenger)
   {
      int i;

      if (passenger.getservice_class().equals("F"))
      {
         if (first_class_seats_remaining == 0)
            return "full";

         //check if preferred seat is available
         for (i = 0; i < FIRST_CLASS_SIZE; i++)
         {
            //empty seat?
            if ((first_class_seats.get(i).getoccupant() == null) 
                  && (first_class_seats.get(i).getseat_type().equals(seat_preference)))
            {
               first_class_seats.get(i).setoccupant(passenger);
               first_class_seats_remaining--;
               return first_class_seats.get(i).getseat_number();
            }
         }

         //seat preference is not available
         return "";
      }

      else if (passenger.getservice_class().equals("E"))
      {
         if (economy_class_seats_remaining == 0)
            return "full";

         //check if preferred seat is available
         for (i = 0; i < ECONOMY_CLASS_SIZE; i++)
         {
            //empty seat?
            if ((economy_class_seats.get(i).getoccupant() == null) 
                  && (economy_class_seats.get(i).getseat_type().equals(seat_preference)))
            {
               economy_class_seats.get(i).setoccupant(passenger);
               economy_class_seats_remaining--;
               return economy_class_seats.get(i).getseat_number();
            }
         }

         //seat preference not available
         return "";
      }

      //incorrect class
      else
         return "error";
   }

   /**
    * Adds a group from user input.
    * @param group An ArrayList of passengers to be seated as adjacently
    * 	as possible.
    * @return A String of the seated passengers name's and seat numbers if
    * 	successful. If not enough room to seat all, returns null.
    */
   public String add_group(ArrayList<Passenger> group)
   {
      int i, j, k;
      int max_empty_seats_in_row = 0;
      int adjacent_empty_seats = 0;
      ArrayList<Integer> col_list = new ArrayList<Integer>();
      String groupseats = "";

      //don't seat anyone if not enough seats
      if ((group.get(0).getservice_class().equals("F")) 
            && first_class_seats_remaining <= group.size())
      {
         return null;
      }

      else if ((group.get(0).getservice_class().equals("E")) 
            && economy_class_seats_remaining <= group.size())
      {
         return null;
      }

      //groups are added from the end of a row, inwards as space provides
      if (group.get(0).getservice_class().equals("F"))
      {
         //try to seat whole group at once
         for (i = group.size(); i > 0; i--)
         {   
            //move through every individual seat, stop at beginning of each row
            for (j = 0; j < FIRST_CLASS_SIZE; j++)
            {
               //check and reset each new row
               if ((j % FIRST_CLASS_COLS) == 0 || (j == FIRST_CLASS_SIZE-1))
               {
                  if (adjacent_empty_seats > max_empty_seats_in_row)
                  {
                     max_empty_seats_in_row = adjacent_empty_seats;

                     for (k = 1; k <= max_empty_seats_in_row; k++)
                        col_list.add(j-k);
                  }

                  if (max_empty_seats_in_row >= i)
                  {
                     while (!col_list.isEmpty() && !group.isEmpty())
                     {
                        first_class_seats.get(col_list.get(0)).setoccupant(group.get(0));
                        groupseats += (group.get(0).getname() + " seated at: " 
                              + first_class_seats.get(col_list.get(0)).getseat_number() + "\n");
                        col_list.remove(0);
                        group.remove(0);
                     }
                  }
                  max_empty_seats_in_row = 0;
                  adjacent_empty_seats = 0;
                  col_list.clear();
               }

               if (first_class_seats.get(j).getoccupant() == null)
                  adjacent_empty_seats++;

               else if (first_class_seats.get(j).getoccupant() != null)
               {
                  if (adjacent_empty_seats > max_empty_seats_in_row)
                  {
                     col_list.clear();
                     max_empty_seats_in_row = adjacent_empty_seats;

                     for (k = 1; k <= max_empty_seats_in_row; k++)
                        col_list.add(j-k);
                  }

                  adjacent_empty_seats = 0;
               }
            }
         }
      }

      else if (group.get(0).getservice_class().equals("E"))
      {
         //try to seat whole group at once
         for (i = group.size(); i > 0; i--)
         {   
            //move through every individual seat, stop at beginning of each row
            for (j = 0; j < ECONOMY_CLASS_SIZE; j++)
            {
               //check and reset each new row
               if ((j % ECONOMY_CLASS_COLS) == 0 || (j == ECONOMY_CLASS_SIZE-1))
               {
                  if (adjacent_empty_seats > max_empty_seats_in_row)
                  {
                     max_empty_seats_in_row = adjacent_empty_seats;

                     for (k = 1; k <= max_empty_seats_in_row; k++)
                        col_list.add(j-k);
                  }

                  if (max_empty_seats_in_row >= i)
                  {
                     while (!col_list.isEmpty() && !group.isEmpty())
                     {
                        economy_class_seats.get(col_list.get(0)).setoccupant(group.get(0));
                        groupseats += (group.get(0).getname() + " seated at: " 
                              + first_class_seats.get(col_list.get(0)).getseat_number() + "\n");
                        col_list.remove(0);
                        group.remove(0);
                     }
                  }
                  max_empty_seats_in_row = 0;
                  adjacent_empty_seats = 0;
                  col_list.clear();
               }

               if (economy_class_seats.get(j).getoccupant() == null)
                  adjacent_empty_seats++;

               else if (economy_class_seats.get(j).getoccupant() != null)
               {
                  if (adjacent_empty_seats > max_empty_seats_in_row)
                  {
                     col_list.clear();
                     max_empty_seats_in_row = adjacent_empty_seats;

                     for (k = 1; k <= max_empty_seats_in_row; k++)
                        col_list.add(j-k);
                  }

                  adjacent_empty_seats = 0;
               }
            }
         }
      }

      return groupseats;
   }

   /**
    * Removes an individual passenger from a seat.
    * @param name The passenger's name to be removed.
    * @return True if passenger exists and gets deleted. False otherwise.
    */
   public boolean cancel_reservation(String name)
   {
      int i;
      boolean deleted = false;

      //deletes everyone with the same name
      for (i = 0; i < FIRST_CLASS_SIZE; i++)
      {
         if (first_class_seats.get(i).getoccupant() != null)
         {
            if (name.equals(first_class_seats.get(i).getoccupant().getname()))
            {
               first_class_seats.get(i).setoccupant(null);
               first_class_seats_remaining++;
               deleted = true;
            }
         }
      }

      for (i = 0; i < ECONOMY_CLASS_SIZE; i++)
      {
         if (economy_class_seats.get(i).getoccupant() != null)
         {
            if (name.equals(economy_class_seats.get(i).getoccupant().getname()))
            {
               economy_class_seats.get(i).setoccupant(null);
               first_class_seats_remaining++;
               deleted = true;
            }
         }
      }

      return deleted;
   }

   /**
    * Cancel's every passenger associated with a particular group.
    * @param group_name The group name to be removed.
    * @return True if a passenger was found and deleted with this group name.
    * 	False otherwise.
    */
   public boolean cancel_group(String group_name)
   {
      int i;
      boolean deleted = false;
      //deletes everyone with the same group name
      for (i = 0; i < FIRST_CLASS_SIZE; i++)
      {
         if (first_class_seats.get(i).getoccupant() != null)
         {
            if (group_name.equals(first_class_seats.get(i).getoccupant().getgroup()))
            {
               first_class_seats.get(i).setoccupant(null);
               first_class_seats_remaining++;
               deleted = true;
            }
         }
      }

      for (i = 0; i < ECONOMY_CLASS_SIZE; i++)
      {
         if (economy_class_seats.get(i).getoccupant() != null)
         {
            if (group_name.equals(economy_class_seats.get(i).getoccupant().getgroup()))
            {
               economy_class_seats.get(i).setoccupant(null);
               economy_class_seats_remaining++;
               deleted = true;
            }
         }
      }

      return deleted;
   }

   /**
    * Makes a String of all avavilable seats in the plane.
    * @return A String representing all available seats.
    */
   public String get_availability_chart()
   {
      String availability = "\nFirst:";
      int row, col;
      int i = 0;
      boolean row_has_empty_seat = false;

      for (row = 1; row <= FIRST_CLASS_ROWS; row++)
      {
         row_has_empty_seat = false;

         for (col = 1; col <= FIRST_CLASS_COLS; col++)
         {
            if (first_class_seats.get(i).getoccupant() == null)
            {
               if (!row_has_empty_seat)
               {
                  availability += ("\n" + row + ": ");
                  row_has_empty_seat = true;
               }
               else
                  availability += ",";

               availability += first_class_seats.get(i).getseat_number().charAt(1);
            }
            i++;
         }
      }

      availability += "\n\nEconomy:";
      i = 0;
      for (row = ECONOMY_ROW_START; row < ECONOMY_CLASS_ROWS+ECONOMY_ROW_START; row++)
      {
         row_has_empty_seat = false;

         for (col = 1; col <= ECONOMY_CLASS_COLS; col++)
         {
            if (economy_class_seats.get(i).getoccupant() == null)
            {
               if (!row_has_empty_seat)
               {
                  availability += ("\n" + row + ": ");
                  row_has_empty_seat = true;
               }
               else
                  availability += ",";

               availability += economy_class_seats.get(i).getseat_number().charAt(2);
            }
            i++;
         }
      }

      availability += "\n";
      return availability;
   }

   /**
    * Makes a String of all current passengers in a service class.
    * @param service_class The service class to be represented.
    * @return A String of all passengers in a service class.
    */
   public String get_manifest(String service_class)
   {
      String manifest = "";
      int i;
      if (service_class.equals("F"))
      {
         manifest += "\nFirst:\n";
         for (i = 0; i < FIRST_CLASS_SIZE; i++)
         {
            if (first_class_seats.get(i).getoccupant() != null)
            {
               manifest += first_class_seats.get(i).getseat_number() + ": ";
               manifest += first_class_seats.get(i).getoccupant().getname() + "\n";
            }
         }
      }

      else if (service_class.equals("E"))
      {
         manifest += "\nEconomy:\n";
         for (i = 0; i < ECONOMY_CLASS_SIZE; i++)
         {
            if (economy_class_seats.get(i).getoccupant() != null)
            {
               manifest += economy_class_seats.get(i).getseat_number() + ": ";
               manifest += economy_class_seats.get(i).getoccupant().getname() + "\n";
            }
         }
      }

      return manifest;
   }

   /**
    * Makes the output for the text file for plane data to be saved.
    * It consists of comma separated values.
    * @return Data to be printed into a text file representing the whole plane.
    */
   public String get_flightdata()
   {
      String flightdata = "";
      int i;

      for (i = 0; i < FIRST_CLASS_SIZE; i++)
      {
         if (first_class_seats.get(i).getoccupant() != null)
         {
            flightdata += (first_class_seats.get(i).getoccupant().getservice_class() + ",");
            flightdata += (i + ",");
            flightdata += (first_class_seats.get(i).getoccupant().getname());

            if (first_class_seats.get(i).getoccupant().getgroup() == null)
               ; // do nothing
            else
               flightdata += ("," + first_class_seats.get(i).getoccupant().getgroup());

            flightdata += "\n";
         }
      }

      for (i = 0; i < ECONOMY_CLASS_SIZE; i++)
      {
         if (economy_class_seats.get(i).getoccupant() != null)
         {
            flightdata += (economy_class_seats.get(i).getoccupant().getservice_class() + ",");
            flightdata += (i + ",");
            flightdata += (economy_class_seats.get(i).getoccupant().getname());

            if (economy_class_seats.get(i).getoccupant().getgroup() == null)
               ; // do nothing
            else
               flightdata += ("," + economy_class_seats.get(i).getoccupant().getgroup());

            flightdata += "\n";
         }
      }

      return flightdata;
   }
}

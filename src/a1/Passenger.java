package a1;

/**
 * A class representing a passenger for a plane.
 * @author Matthew Somers
 * @version 1.0
 */
public class Passenger 
{
   private String name;
   private String service_class;
   private String group;
   
   /**
    * Constructor to build a passenger.
    * @param name The name of the passenger.
    * @param service_class Which service class this passenger is in.
    * @param group An optional field for when the passenger is added with a group.
    */
   public Passenger(String name, String service_class, String group)
   {
      this.name = name;
      this.service_class = service_class;
      this.group = group;
   }
   
   /**
    * A simple getter for the passenger's name.
    * @return The passenger's name.
    */
   public String getname() { return name; }
   
   /**
    * A simple getter for the passenger's service class.
    * @return The passenger's service class.
    */
   public String getservice_class() { return service_class; }
   
   /**
    * A simple getter for the passenger's group.
    * @return The passenger's group (or null if it was an individually added passenger).
    */
   public String getgroup() { return group; }
}

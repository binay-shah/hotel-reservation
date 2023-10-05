import api.AdminResource;
import api.HotelResource;
import model.*;
import service.CustomerService;
import service.ReservationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {

    static HotelResource hotelResource = HotelResource.getInstance();

    public static void main(String[] args) throws ParseException {
        boolean keepRunning = true;
        try(Scanner scanner = new Scanner(System.in)){
            while(keepRunning){
                try{
                    System.out.println("1. Find and reserve a room");
                    System.out.println("2. See my reservations");
                    System.out.println("3. Create an account");
                    System.out.println("4. Admin");
                    System.out.println("5. Exit");
                    System.out.println("Please enter your selection");
                    int selection = Integer.parseInt(scanner.nextLine());
                    switch (selection){
                        case 1:
                            findAndReserveARoom(scanner);
                            break;
                        case 2:
                            showCustomerReservation(scanner);
                            break;
                        case 3:
                            createCustomerAccount(scanner);
                            break;
                        case 4:
                            AdminMenu adminMenu = new AdminMenu();
                            adminMenu.displayAdminMenu();
                            break;
                        case 5:
                            keepRunning = false;
                            break;
                        default:
                            System.out.println("Please enter a number between 1 and 5.");
                    }
                }catch (Exception ex){
                    System.out.println("\nError - Invalid Input\n");
                }
            }
        }
    }

    private static void createCustomerAccount(Scanner scanner) {
        System.out.println("Please enter your email");
        String email = scanner.nextLine();
        System.out.println("Please enter your firstName: ");
        String firstName = scanner.nextLine();
        System.out.println("Please enter your lastName: ");
        String lastName = scanner.nextLine();
        hotelResource.createCustomer(email, firstName, lastName);
    }

    private static void showCustomerReservation(Scanner scanner) {
        System.out.println("Please enter your email: ");
        String email = scanner.nextLine();
        Collection<Reservation> customerReservation = hotelResource.getCustomersReservations(email);
        System.out.println(customerReservation);
    }

    private static void findAndReserveARoom(Scanner scanner) throws ParseException {
        System.out.println("Please Enter your checkIn date in dd/MM/yyyy: ");
        String checkInDateStr = scanner.nextLine();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);
        Date checkInDate = formatter.parse(checkInDateStr);
        System.out.println("Please enter your checkOut date in dd/MM/yyyy: ");
        String checkOutDateStr = scanner.nextLine();
        Date checkOutDate = formatter.parse(checkOutDateStr);
        if (checkOutDate.before(checkInDate)) {
            throw new  IllegalArgumentException();
        }
        Collection<IRoom> roomList  = hotelResource.findARoom(checkInDate, checkOutDate);
        if(roomList.isEmpty()){
            Calendar c = Calendar.getInstance();
            c.setTime(checkInDate);
            c.add(Calendar.DATE, 7);
            checkInDate = c.getTime();
            c.setTime(checkOutDate);
            c.add(Calendar.DATE, 7);
            checkOutDate = c.getTime();
            roomList  = hotelResource.findARoom(checkInDate, checkOutDate);
            if(roomList.isEmpty()){
                System.out.println("\nRooms are not available.\n");
            }else{
                listAvailableRooms(roomList, checkInDate, checkOutDate);
                bookARoom(scanner, checkInDate, checkOutDate);
            }
        }else{
            listAvailableRooms(roomList, checkInDate, checkOutDate);
            bookARoom(scanner, checkInDate, checkOutDate);
        }
    }

    private static void bookARoom(Scanner scanner, Date checkInDate, Date checkOutDate) {
        System.out.println("Please enter room number to book: ");
        String roomNumber = scanner.nextLine();
        System.out.println("Please enter your email: ");
        String email = scanner.nextLine();
        IRoom room = hotelResource.getRoom(roomNumber);
        hotelResource.bookARoom(email, room, checkInDate, checkOutDate);
    }

    private static void listAvailableRooms(Collection<IRoom> roomList, Date checkInDate, Date checkOutDate) {
        for(IRoom room : roomList){
            System.out.println(room + " " + "is avaiable on range: " + checkInDate + " and "+ checkOutDate);
        }
    }

    public static void populateData() throws ParseException {
        ReservationService reservationService = ReservationService.getInstance();
        CustomerService customerService = CustomerService.getInstance();
        //creating a customer
        Customer customer = new Customer("test", "udacity", "test@udacity.com");
        customerService.addCustomer(customer.getEmail(), customer.getFirstName(), customer.getLastName() );
        //creating two rooms
        IRoom room1 = new Room("1", 100.0, RoomType.SINGLE);
        IRoom room2 = new Room("2", 200.0, RoomType.DOUBLE);
        reservationService.addRoom(room1);
        reservationService.addRoom(room2);
        //checkin and checkout dates
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date checkIn = format.parse("10/10/2023");
        Date checkOut = format.parse("15/10/2023");
        //reserve room1
        reservationService.reserveARoom(customer, room1, checkIn , checkOut);
        //room2 should be available
        Collection<IRoom> availableRooms = reservationService.findRooms(checkIn, checkOut);
        System.out.println(availableRooms);
        reservationService.reserveARoom(customer, room2, checkIn , checkOut);

        availableRooms = reservationService.findRooms(checkIn, checkOut);
        System.out.println(availableRooms);
    }

}

import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    static AdminResource adminResource = AdminResource.getInstance();

    public void displayAdminMenu() {



        boolean keepRunning = true;
        Scanner scanner = new Scanner(System.in);
            while (keepRunning) {
                try {
                    System.out.println("1. See all Customers");
                    System.out.println("2. See all Rooms");
                    System.out.println("3. See all Reservations");
                    System.out.println("4. Add a room");
                    System.out.println("5. Back to main menu");
                    System.out.println("Please enter your selection");
                    int selection = Integer.parseInt(scanner.nextLine());
                    switch (selection) {
                        case 1:
                            Collection<Customer> customers = adminResource.getALlCustomers();
                            if(customers.isEmpty()){
                                System.out.println("\nThere are currently no customers registered.\n");
                            }else
                                System.out.println(customers);
                            break;
                        case 2:
                            Collection<IRoom> rooms = adminResource.getAllRooms();
                            if(rooms.isEmpty()){
                                System.out.println("\nThere are currently no rooms.\n");
                            }else
                                System.out.println(rooms);
                            break;
                        case 3:
                            adminResource.displayAllReservations();
                            break;
                        case 4:
                            addARoom(scanner);
                            break;
                        case 5:
                            keepRunning = false;
                            break;
                        default:
                            System.out.println("Please enter a number between 1 and 5.");
                    }
                } catch (Exception ex) {
                    System.out.println("\nError - Invalid Input\n");

            }
        }
    }

    private void addARoom(Scanner scanner) {
        System.out.println("Please enter room number: ");
        int roomNumber = Integer.parseInt(scanner.nextLine());
        System.out.println("Please enter room price: ");
        Double roomPrice = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter room type: 1 for single, 2 for double: ");
        int roomType = Integer.parseInt(scanner.nextLine());
        IRoom room = null;
        if(roomType == 1)
            room = new Room(String.valueOf(roomNumber), roomPrice, RoomType.SINGLE);
        if(roomType == 2)
            room = new Room(String.valueOf(roomNumber), roomPrice, RoomType.DOUBLE);
        List<IRoom> roomList = new ArrayList<>();
        roomList.add(room);
        adminResource.addRoom(roomList);
    }
}

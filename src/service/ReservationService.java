package service;

import model.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReservationService {

    Set<Reservation> reservationList = new HashSet<>();
    Set<IRoom> roomList = new HashSet<>();

    private static class SingletonHolder {
        public static final ReservationService instance = new ReservationService();
    }

    public static ReservationService getInstance() {
        return ReservationService.SingletonHolder.instance;
    }

    public void addRoom(IRoom room) {
        roomList.add(room);
    }

    public IRoom getARoom(String roomId) {
        for (IRoom room : roomList) {
            if (roomId.equals(room.getRoomNumber())) {
                return room;
            }
        }
        return null;
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation newReservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservationList.add(newReservation);
        return newReservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Set<IRoom> availableRooms = new HashSet<>();
        Set<IRoom> reservedRoom = new HashSet<>();

       if(reservationList.isEmpty()){
           availableRooms.addAll(roomList);
       }else{
           for(Reservation reservation : reservationList){
               reservedRoom.add(reservation.getRoom());
               if (isRoomAvailable(reservation, checkInDate, checkOutDate)) {
                   availableRooms.add(reservation.getRoom());
               }else{
                   availableRooms.remove(reservation.getRoom());
               }
           }

           for(IRoom room : roomList){
               if(!reservedRoom.contains(room)  )
                   availableRooms.add(room);
           }
       }
        return availableRooms;
    }

    public boolean isRoomAvailable(Reservation reservation, Date checkInDate, Date checkOutDate) {
        return checkInDate.after(reservation.getCheckOutDate());
    }

    public Collection<Reservation> getCustomerReservation(Customer customer) {
        List<Reservation> list = new ArrayList<>();
        for (Reservation reservation : reservationList) {
            if (reservation.getCustomer().equals(customer))
                list.add(reservation);
        }
        return list;
    }

    public void printAllReservation() {
        if(reservationList.isEmpty()){
            System.out.println("\nThere are currently no reservations.\n");
        }else{
            for (Reservation reservation : reservationList) {
                System.out.println(reservation);
            }
        }
    }

    public Collection<IRoom> getAllRooms() {
        return roomList;
    }


    public static void main(String[] args) throws Exception {
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

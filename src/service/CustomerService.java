package service;

import model.Customer;

import java.util.*;

public class CustomerService {

    private Set<Customer> customerList = new HashSet<>();

    private CustomerService(){}

    private static class SingletonHolder {
        public static final CustomerService instance = new CustomerService();
    }

    public static CustomerService getInstance() {
        return SingletonHolder.instance;
    }



    public void addCustomer(String email, String firstName, String lastName){
        customerList.add(new Customer(firstName, lastName, email));
    }

    public Customer getCustomer(String customerEmail){
        for(Customer customer : customerList){
            if(customer.getEmail().equals(customerEmail)){
                return customer;
            }
        }
        return null;
    }

    public Collection<Customer> getAllCustomers(){
        return customerList;
    }


}

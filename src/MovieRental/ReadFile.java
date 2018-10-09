/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MovieRental;
/**
 * @author Tshepo Sepadile
 * @author Banele Mlamleli
 */
import java.io.*;
import java.util.*;
public class ReadFile{

    public static FileInputStream fileInputStreamCustomer;
    public static FileInputStream fileInputStreamMovies;
    public static FileInputStream fileInputStreamRental;
    
    public static ObjectInputStream objectInputStreamCustomer;
    public static ObjectInputStream objectInputStreamMovies;
    public static ObjectInputStream objectInputStreamRental;
    
    Customer customer;
    DVD dvd;
    Rental rental;
    
    //Arraylist to populate with data from the serialized object file
    public static ArrayList<Customer> customerArraylist = new ArrayList<>();
    public static ArrayList<DVD> dvdArraylist = new ArrayList<>();
    public static ArrayList<Rental> rentalArraylist = new ArrayList<>();
    
    //read data into array lists
    public void readCustomerSerializedData(){
        try{
            fileInputStreamCustomer = new FileInputStream("/home/shaun/Documents/PROGRAMMING/Java/Projects/MovieRental1/src/Assets/Customers.ser");
            objectInputStreamCustomer = new ObjectInputStream(fileInputStreamCustomer);
            //reading data from Customer.ser file
            while(true){
                customer = (Customer) objectInputStreamCustomer.readObject();
                customerArraylist.add(customer);
            }
        }catch(Exception e){
            System.out.println("customer file successfully read");
        }
        
        //Closing the files after being opened
        try {
            fileInputStreamCustomer.close();
            objectInputStreamCustomer.close();
        } catch (Exception error) {
            System.out.println(error.getMessage()+", customer close code");
        }
    }
    
    //read data into Dvd array lists
    public void readDvdSerializedData(){
        try{
            fileInputStreamMovies = new FileInputStream("/home/shaun/Documents/PROGRAMMING/Java/Projects/MovieRental1/src/Assets/Movies.ser");
            objectInputStreamMovies = new ObjectInputStream(fileInputStreamMovies);
            
            //reading data from Movies.ser file
            while(true){
                dvd = (DVD) objectInputStreamMovies.readObject();
                dvdArraylist.add(dvd);
            }
        }catch(Exception e){
            System.out.println("movies file successfully read");
        }
        //Closing the files after being opened
        try {
            objectInputStreamMovies.close();
            fileInputStreamMovies.close();
        } catch (Exception error) {
            System.out.println(error.getMessage()+", movies close code");
        }
    }
    
    //read data into array lists
    public void readRentalSerializedData(){
        try{
            fileInputStreamRental = new FileInputStream("/home/shaun/Documents/PROGRAMMING/Java/Projects/MovieRental1/src/Assets/Rental.ser");
            objectInputStreamRental = new ObjectInputStream(fileInputStreamRental);
            //reading data from Rental.ser file
            while(true){
                rental = (Rental) objectInputStreamRental.readObject();
                rentalArraylist.add(rental);
            }
        }catch(Exception e){
            System.out.println("rental file successfully read");
        }
        //Closing the files after being opened
        try {
            fileInputStreamRental.close();
            objectInputStreamRental.close();
        } catch (Exception error) {
            System.out.println(error.getMessage()+", rental close code");
        }
    }
}
package com.example.projectsem2;
import java.util.Date;

public class customerData {
    private Integer customerNum;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Date checkIn;
    private Date checkOut;
    private Integer total;

    public customerData(Integer customerNum, String firstName, String lastName, String phoneNumber, Date checkIn, Date checkOut,Integer total) {
        this.customerNum = customerNum;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.total = total;
    }

    public Integer getCustomerNum() {
        return customerNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public Integer getTotal() {
        return total;
    }
}

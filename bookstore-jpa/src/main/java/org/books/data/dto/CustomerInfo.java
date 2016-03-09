package org.books.data.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "number",
    "email",
    "firstName",
    "lastName"
})
@XmlRootElement(name = "customerInfo")

public class CustomerInfo implements Serializable{
    @XmlElement(required = true)
    private String number;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String firstName;
    @XmlElement(required = true)
    private String lastName;

    public CustomerInfo() {
    }

    public CustomerInfo(String number, String firstName, String lastName, String email) {
        this.number= number;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public CustomerInfo(Customer customer) {
        this.email = customer.getEmail();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString(){
        return this.number + " " + this.email + " " + this.firstName + " " + this.lastName;
    }
}

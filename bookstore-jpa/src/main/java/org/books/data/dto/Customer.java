package org.books.data.dto;

import org.books.data.entity.CustomerEntity;
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
    "lastName",
    "address",
    "creditCard"
})
@XmlRootElement(name = "customer")
public class Customer implements Serializable {
    
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String firstName;
    @XmlElement(required = true)
    private String lastName;
    @XmlElement(required = true)
    private AddressDTO address;
    @XmlElement(required = true)
    private CreditCardDTO creditCard;
    @XmlElement(required = true)
    private String number;

    public Customer() {
    }

    public Customer(String number, String email, String firstName, String lastName, AddressDTO address, CreditCardDTO creditCard) {
        
        this.number = number;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.creditCard = creditCard;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public AddressDTO getAddress() {
        if (address == null) {
            address = new AddressDTO();
        }
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public CreditCardDTO getCreditCard() {
        if (creditCard == null) {
            creditCard = new CreditCardDTO();
        }
        return creditCard;
    }

    public void setCreditCard(CreditCardDTO creditCard) {
        this.creditCard = creditCard;
    }
    @Override
    public String toString(){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(this.number + " " + this.email + " " + this.firstName + " " + this.lastName + " ");
        if (address!= null){
            strBuilder.append(this.address.toString());
        }
        if (creditCard!= null){
            strBuilder.append(this.creditCard.toString());
        }
        return strBuilder.toString();
    }
    
    public static Customer copyEntityTODTO(CustomerEntity entity){
        AddressDTO address = AddressDTO.copyEntityToDTO(entity.getAddress());
        
        CreditCardDTO creditcard = CreditCardDTO.copyEntityToDTO(entity.getCreditCard());
        return new Customer(entity.getNumber(), entity.getEmail(), entity.getFirstName(), entity.getLastName(), 
                address, creditcard);
    }
}

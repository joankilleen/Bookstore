package org.books.data.dto;

import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.books.data.entity.Address;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "street",
    "city",
    "postalCode",
    "country"
})
@XmlRootElement(name = "address")

public class AddressDTO implements Serializable {

    @XmlElement(required = true)
    private String street;
    @XmlElement(required = true)
    private String city;
    @XmlElement(required = true)
    private String postalCode;
    @XmlElement(required = true)
    private String country;

    public AddressDTO() {
    }

    public AddressDTO(String street, String city, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public AddressDTO(AddressDTO other) {
        this.street = other.street;
        this.city = other.city;
        this.postalCode = other.postalCode;
        this.country = other.country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return this.street + " " + this.city + " " + this.postalCode + " " + this.country;
    }
    public static AddressDTO copyEntityToDTO(Address entity){
        AddressDTO dto = new AddressDTO(entity.getStreet(), 
                                      entity.getCity(),
                                      entity.getPostalCode(),
                                      entity.getCountry());
        return dto;

    }
}

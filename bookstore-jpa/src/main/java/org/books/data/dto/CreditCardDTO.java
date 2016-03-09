package org.books.data.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.books.data.CreditCardType;
import org.books.data.entity.CreditCard;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "type",
    "number",
    "expirationMonth",
    "expirationYear"
})
@XmlRootElement(name = "creditCard")
public class CreditCardDTO implements Serializable {

/*    public enum Type {

        MasterCard, Visa
    } */
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    private CreditCardType type;
    @XmlElement(required = true)
    private String number;
    @XmlElement(required = true)
    private Integer expirationMonth;
    @XmlElement(required = true)
    private Integer expirationYear;

    public CreditCardDTO() {
    }

    public CreditCardDTO(CreditCardType type, String number, Integer expirationMonth, Integer expirationYear) {
        this.type = type;
        this.number = number;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public CreditCardDTO(CreditCardDTO other) {
        this.type = other.type;
        this.number = other.number;
        this.expirationMonth = other.expirationMonth;
        this.expirationYear = other.expirationYear;
    }

    public CreditCardType getType() {
        return type;
    }

    public void setType(CreditCardType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public Integer getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }

    @Override
    public String toString() {
        return this.number + " " + this.type + " " + this.expirationMonth + " " + this.expirationYear;
    }
    public static CreditCardDTO copyEntityToDTO(CreditCard entity){
        CreditCardDTO dto = new CreditCardDTO((CreditCardType)entity.getType(),
                                                    entity.getNumber(),
                                                    entity.getExpirationMonth(),
                                                    entity.getExpirationYear());
        return dto;
    }
    
}

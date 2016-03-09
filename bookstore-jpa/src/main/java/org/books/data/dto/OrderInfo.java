package org.books.data.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.books.data.OrderStatus;
import org.books.data.entity.OrderEntity;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "number",
    "date",
    "amount",
    "status"
})
@XmlRootElement(name = "orderInfo")
public class OrderInfo implements Serializable{
    @XmlElement(required = true)
    private String number;
    @XmlElement(required = true)
    private Date date;
    @XmlElement(required = true)
    private BigDecimal amount;
    @XmlElement(required = true)
    private OrderStatus status;

    public OrderInfo() {
    }

    public OrderInfo(String number, Date date, BigDecimal amount, OrderStatus status) {
        this.number = number;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }

    public OrderInfo(OrderEntity order) {
        this.number = order.getNumber();
        this.date = order.getDate();
        this.amount = order.getAmount();
        this.status = order.getStatus();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString(){
        return this.number + " " + this.status + " " + this.date + " " + this.amount;
    }
}

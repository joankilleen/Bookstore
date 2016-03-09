package org.books.data.dto;

import org.books.data.entity.OrderEntity;
import org.books.data.entity.OrderItem;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.books.data.OrderStatus;


/**
 *
 * @author guthei
 */
@XmlAccessorType(XmlAccessType.FIELD)

@XmlType(name = "order", propOrder = {
    "number",
    "date",
    "amount",
    "status",
    "customerInfo",
    "address",
    "creditCard",
    "items"
})
@XmlRootElement(name = "order")
public class OrderDTO implements Serializable {

    @XmlElement(required = true, name="number")
    protected String number;
    
    @XmlElement(required = true, name="date")
    @XmlJavaTypeAdapter(SqlDateAdapter .class)
    protected Date date;
    
    @XmlElement(required = true, name="amount")
    protected BigDecimal amount;
   
    @XmlElement(required = true, name="status")
    protected OrderStatus status;
    
    @XmlTransient
    protected Customer customer;
    @XmlElement(required = true, name="address")
    protected AddressDTO address;
    @XmlElement(required = true, name="creditCard")
    protected CreditCardDTO creditCard;
   
    @XmlTransient
    protected List<OrderItemDTO> orderItems = new ArrayList<>();

    public OrderDTO() {
    }

    public OrderDTO(String number, Date date, BigDecimal amount, OrderStatus status, 
            Customer customer, AddressDTO address, CreditCardDTO creditCard, List<OrderItemDTO> orderItems) {
        this.number = number;
        this.date = date;
        this.amount = amount;
        this.status = status;
        this.customer = customer;
        this.address = address;
        this.creditCard = creditCard;
        this.orderItems = orderItems;
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
    
    @XmlElement(required = true, name="customerInfo")
    public CustomerInfo getCustomerInfo() {
        if (customer==null) return null;
        CustomerInfo custInfo = new CustomerInfo(customer.getNumber(), customer.getFirstName(), customer.getLastName(), customer.getEmail());
        return custInfo;
    }
    @XmlElement(required = true, name="items")
    public List<OrderItemDTO> getItems() {
        return orderItems;
    }
    public void setItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
    public void setCustomerInfo(CustomerInfo customerInfo) {
        if (customer==null){
            customer = new Customer();
        }
        this.customer.setEmail(customerInfo.getEmail());
        this.customer.setFirstName(customerInfo.getFirstName());
        this.customer.setLastName(customerInfo.getLastName());
        this.customer.setNumber(customerInfo.getNumber());
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
    
    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public static OrderDTO copyEntityTODTO(OrderEntity entity){
        OrderDTO dto = new OrderDTO();
        dto.setAmount(entity.getAmount());
        dto.setDate(entity.getDate());
        dto.setNumber(entity.getNumber());
        dto.setStatus(entity.getStatus());
        
        Customer cust = Customer.copyEntityTODTO(entity.getCustomer());
        dto.setCustomer(cust);
        
        AddressDTO address = AddressDTO.copyEntityToDTO(entity.getAddress());
        dto.setAddress(address);
        
        CreditCardDTO card = CreditCardDTO.copyEntityToDTO(entity.getCreditCard());
        dto.setCreditCard(card);
        
        List<OrderItemDTO> list = new ArrayList<>(entity.getOrderItems().size());
        for (OrderItem item: entity.getOrderItems()){
            OrderItemDTO itemDTO = OrderItemDTO.copyEntityToDTO(item);
            list.add(itemDTO);         
        }
        dto.setOrderItems(list);
        return dto;
    }
    
    @Override
    public String toString(){
        return this.number + "  " + this.amount + " " + this.status + " " + this.date + " " + this.customer + " " + this.creditCard.toString() + " " + this.address;
    }
    

}

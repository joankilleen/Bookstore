package org.books.data.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.books.data.OrderStatus;
import org.books.data.entity.OrderItem;


/**
 *
 * @author guthei
 */
@XmlAccessorType(XmlAccessType.FIELD)
/*@XmlType(name = "OrderItem", propOrder = {
    "bookInfo",
    "quantity"
})*/
@XmlRootElement(name = "OrderItem")
public class OrderItemDTO implements Serializable {

    @XmlElement(name="bookInfo", required = true)
    protected BookInfo book;
    @XmlElement(name="quantity", required = true )
    protected Integer quantity;
    @XmlTransient
    protected BigDecimal price;

    public OrderItemDTO() {
    }

    public OrderItemDTO(BookInfo book, Integer quantity) {
        this.book = book;
        this.quantity = quantity;
    } 
  
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BookInfo getBook() {
        return book;
    }

    public void setBook(BookInfo book) {
        this.book = book;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public static OrderItemDTO copyEntityToDTO(OrderItem entity){
       OrderItemDTO dto = new OrderItemDTO();
       dto.setBook(new BookInfo(entity.getBook()));
       dto.setPrice(entity.getPrice());
       dto.setQuantity(entity.getQuantity());
       return dto;
    }
    
    @Override
    public String toString(){
        StringBuilder bookString = new StringBuilder();
        bookString.append(book.getIsbn() + " ");
        bookString.append(book.getTitle() + " ");
        return bookString + " " + this.price + " " + this.quantity;
    }

}

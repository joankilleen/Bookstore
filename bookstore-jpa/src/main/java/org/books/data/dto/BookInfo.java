package org.books.data.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.books.data.entity.BookEntity;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "isbn",
    "title",
    "price"
})
@XmlRootElement(name = "bookInfo")
public class BookInfo implements Serializable{

    @XmlElement(required = true)
    private String isbn;
    @XmlElement(required = true)
    private String title;
    @XmlElement(required = true)
    private BigDecimal price;

    public BookInfo() {
    }

    public BookInfo(String isbn, String title, BigDecimal price) {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
    }

    public BookInfo(BookEntity book) {
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.price = book.getPrice();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    @Override
    public String toString(){
        return this.isbn + " " + this.title + " " + this.price;
    }
}

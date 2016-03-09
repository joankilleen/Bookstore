/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.books.data.entity.BookEntity;
import org.books.data.Binding;

/**
 *
 * @author Joan
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "isbn",
    "title",
    "authors",
    "publisher",
    "publicationYear",
    "binding",
    "numberOfPages",
    "price"
})
@XmlRootElement(name = "book")
public class BookDTO implements Serializable {  
    @XmlElement(required = true)
    private String isbn;
    @XmlElement(required = true)
    private String title;
    @XmlElement(required = true)
    private String authors;
    @XmlElement(required = true)
    private String publisher;
    @XmlElement(required = true)
    private Integer publicationYear;
    @XmlElement(required = true)
    private Binding binding;
    @XmlElement(required = true)
    private Integer numberOfPages;
    @XmlElement(required = true)
    private BigDecimal price;

    public BookDTO() {
    }

    public BookDTO(String isbn, String title, String authors, String publisher,
            Integer publicationYear, Binding binding, Integer numberOfPages, BigDecimal price) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.binding = binding;
        this.numberOfPages = numberOfPages;
        this.price = price;
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

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Binding getBinding() {
        return binding;
    }

    public void setBinding(Binding binding) {
        this.binding = binding;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    @Override
    public String toString(){
        return this.isbn + " " + this.title + " " + this.authors + " " + this.publisher + this.price;
    }
    public static BookDTO copyEntitytoDTO(BookEntity entity){
        return new BookDTO(entity.getIsbn(), entity.getTitle(), entity.getAuthors(), 
                        entity.getPublisher(), entity.getPublicationYear(), 
                        entity.getBinding(), entity.getNumberOfPages(), entity.getPrice());
    }
}


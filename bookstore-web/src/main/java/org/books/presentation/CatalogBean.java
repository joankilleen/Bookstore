/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.*;
import org.books.application.exception.BookNotFoundException;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.books.data.dto.PageInfo;
import org.books.util.MessageFactory;

/**
 *
 * @author guthei
 */
@Named("catalogBean")
@SessionScoped
public class CatalogBean implements Serializable {
   
    @EJB
    private  CatalogService catalogService;
    private BookInfo selectedBook;
    private BookDTO selectedBookFullInfo = null;
    private String isbn;
    //private List <BookInfo> books;
    private PageInfo pageInfo = new PageInfo();
    private BigInteger lastPageLoaded = BigInteger.ZERO;
    private static final String NO_BOOK_FOUND_ID = "org.books.presentation.NO_BOOK_FOUND";

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public BigInteger getLastPageLoaded() {
        return lastPageLoaded;
    }

    public void setLastPageLoaded(BigInteger lastPageLoaded) {
        this.lastPageLoaded = lastPageLoaded;
    }

    
    public List<BookInfo> getBooks() {
        return pageInfo.getBookItems();
    }
    public void setBooks(List<BookInfo> books) {
        pageInfo.setBookItems(books);
    }
    private String keywords;


    public BookDTO getSelectedBook() {
        if (selectedBookFullInfo != null) return selectedBookFullInfo;
        try{
            selectedBookFullInfo = catalogService.findBook(selectedBook.getIsbn());
        }
        catch (BookNotFoundException e){
            MessageFactory.error(NO_BOOK_FOUND_ID);
        }
        return selectedBookFullInfo;
    }

    public void setSelectedBook(BookInfo selectedBook) {
        this.selectedBook = selectedBook;
    }
    
    public CatalogService getCatalogService() {
        return catalogService;
    }

    public void setCatalogService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }
 

    

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }    

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

 
    
    public String searchBook(){
        
        searchPaged(BigInteger.ONE);
        
        return null;
    }
    
    public String selectBook(BookInfo book){
        this.selectedBook = book;
        return "bookDetails";
    }
    
    public String next(){
        searchPaged(lastPageLoaded.add(BigInteger.ONE));
        return null;
    }
    public String prev(){
        searchPaged(lastPageLoaded.subtract(BigInteger.ONE));
        return null;
    }
    private void searchPaged(BigInteger page){
        pageInfo = catalogService.searchBooksPaged(this.keywords, page);
        if (pageInfo.getBookItems().isEmpty()) {
            MessageFactory.info(NO_BOOK_FOUND_ID);
        }
        lastPageLoaded = pageInfo.getLastPageLoaded();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.testng.annotations.Test;

/**
 *
 * @author Joan
 */
public class AmazonCatalogIT  extends BaseIT{
    private static final String AMAZON_CATALOG_NAME = "java:global/bookstore-app/bookstore-ejb/AmazonCatalog";
    private static AmazonCatalog amazonCatalog;
    
    
    public AmazonCatalogIT() {
    }
    @Override
    protected void lookupService() throws Exception {
        Context jndiContext = new InitialContext();
        amazonCatalog = (AmazonCatalog) jndiContext.lookup(AMAZON_CATALOG_NAME);
    }
    @Test
    public void itemSearchTest(){
        List<BookInfo> resultList = amazonCatalog.itemSearch("jpa apress");
        System.out.println();
        System.out.println("Number of books found: " + resultList.size());
        for(BookInfo book : resultList){
            log("valid book: " + book.toString());
        }
    }

    @Test
    public void itemLookupTest(){
        BookDTO book = amazonCatalog.itemLookup("0596009208");
        System.out.println();
        log("look for book: " + "ISBN: " + book.getIsbn() + 
                " | Title: " + book.getTitle() + 
                " | Authors: " + book.getAuthors() + 
                " | Publisher: " + book.getPublisher() +  
                " | Binding: " + book.getBinding() + 
                " | Number of Pages: " + book.getNumberOfPages() + 
                " | Publication Year: " + book.getPublicationYear() +
                " | Price: " + book.getPrice());
    }
}

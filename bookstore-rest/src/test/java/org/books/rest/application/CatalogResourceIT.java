/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.rest.application;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.core.GenericType;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import org.books.data.dto.BookDTO;
import org.books.rest.util.ValidationHandler;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author guthei
 */
public class CatalogResourceIT extends BaseIT {

    private ValidationHandler<BookDTO> bookDTOValidator;
    private static final String MERGED_SCHEMA_PATH = "/WEB-INF/schemas/merged.xsd";
    
   
    
    @BeforeClass
    @Override
    public void setUpClass() throws Exception {
        setResourceURI("http://localhost:8080/bookstore/rest/books");
        super.setUpClass();
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
        bookDTOValidator = new ValidationHandler<>();
    }
    
    
    @Test
    public void findBook() throws SAXException, JAXBException, IOException{
        System.out.println("findBook test");
        System.out.println("findBook: 111895145X");
        Response response = webTarget.path("111895145X").request(APPLICATION_XML).get();
        assertTrue(Response.Status.OK.getStatusCode() == response.getStatus());

        org.books.data.dto.BookDTO book = response.readEntity(org.books.data.dto.BookDTO.class);
        assertNotNull(book);
        System.out.println("bookTitle found: '" + book.getTitle() + "' | ISBN: " + book.getIsbn()); 
    }
    
    @Test
    public void notFoundBook(){
        System.out.println("notFoundBook test");
        System.out.println("notFoundBook: 111895145");
        Response response = webTarget.path("111895145").request(APPLICATION_XML).get();
        assertTrue(Response.Status.NOT_FOUND.getStatusCode() == response.getStatus());
    }
    
    @Test
    public void searchBooks(){
        String keywords = "java%2Bxml";
        System.out.println("searchBooks test for keywords: " + keywords);
        Response response = webTarget.queryParam("keywords", keywords).request(APPLICATION_JSON).get();
        List<org.books.data.dto.BookInfo> list = null;
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            list = response.readEntity(new GenericType<List<org.books.data.dto.BookInfo>>() {
            });
        }
        assertNotNull(list);
        System.out.println("size of book list: " + list.size());
        for (org.books.data.dto.BookInfo book: list){
            System.out.println("book found: " + book);
        }
    }

}

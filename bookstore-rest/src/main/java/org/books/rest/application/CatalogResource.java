/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.rest.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import org.books.application.CatalogService;
import org.books.application.exception.BookNotFoundException;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.books.data.dto.OrderDTO;
import org.books.rest.util.ValidationHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author guthei
 */
@Path("books")
@RequestScoped
public class CatalogResource {

    @EJB
    private CatalogService catalogService;
    private static final Logger LOG = Logger.getLogger(CatalogResource.class.getName());
    private ValidationHandler<BookDTO> bookDTOValidator;
    private static final String MERGED_SCHEMA_PATH = "/WEB-INF/schemas/merged.xsd";
    @Context
    ServletContext servletContext;

    @PostConstruct
    public void init() {
        bookDTOValidator = new ValidationHandler<>();
    }

    @PreDestroy
    public void destroy() {
        bookDTOValidator = null;
    }

    @GET
    @Path("{isbn}")
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response findBook(@PathParam("isbn") String isbn) {
        LOG.info("finding book: " + isbn);
        BookDTO bookDTO = null;
        Response response = null;

        try {
            bookDTO = catalogService.findBook(isbn);
            InputStream streamOutput = servletContext.getResourceAsStream(MERGED_SCHEMA_PATH);
            bookDTOValidator.validate(BookDTO.class, bookDTO, streamOutput);
            return Response.ok(bookDTO).build();

        } catch (BookNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        } catch (RuntimeException | JAXBException | IOException | SAXException ex) {
            ex.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            return response;
        }

    }

    @GET
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response searchBooks(@QueryParam("keywords") String keywords) {
        LOG.info("search book: " + keywords);
        Response response = null;
        if (keywords == null || keywords.isEmpty() || keywords.equals(" ")) {
            response = Response.status(Response.Status.BAD_REQUEST).build();
            return response;
        }

        List<BookInfo> list = catalogService.searchBooks(keywords);
        LOG.info("number of books found: " + list.size());
        GenericEntity<List<BookInfo>> outputList = new GenericEntity<List<BookInfo>>(list) {
        };      
        return Response.ok(outputList).build();

    }
}

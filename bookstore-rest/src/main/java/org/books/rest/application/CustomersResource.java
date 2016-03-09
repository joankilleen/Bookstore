/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.rest.application;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import static javax.ws.rs.client.Entity.xml;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import static javax.ws.rs.core.HttpHeaders.CONTENT_LENGTH;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.Response;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.books.application.CustomerService;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.data.dto.Customer;
import org.books.data.dto.CustomerInfo;
import org.books.data.dto.RegistrationDTO;
import org.books.rest.util.ValidationHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author Joan
 */
@Path("customers")
public class CustomersResource {

    
    private CustomerService customerService;
  
    private ValidationHandler<RegistrationDTO> registrationValidator;
    private ValidationHandler<Customer> customerValidator;
    private ValidationHandler<CustomerInfo> customerInfoValidator;
    public static final String CUSTOMER_SCHEMA_PATH="/WEB-INF/schemas/customers.xsd";
    private static final String EMPTY_REQUEST_MESSAGE = "Empty request";
    private static final String CUSTOMER_NUMBERS_DONT_MATCH = "Customer numbers in request body and URI do not match";
    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/CustomerService";
     
    @Context
    ServletContext servletContext;
    @Context
    HttpHeaders headers;

    @PostConstruct
    public void init(){
        javax.naming.Context jndiContext;
        try {
            jndiContext = new InitialContext();
            customerService = (CustomerService) jndiContext.lookup(CUSTOMER_SERVICE_NAME);
        } catch (NamingException ex) {
            Logger.getLogger(CustomersResource.class.getName()).log(Level.SEVERE, null, ex);
        }
	
        registrationValidator = new ValidationHandler<>();
        customerValidator = new ValidationHandler<>();
        customerInfoValidator = new ValidationHandler<>();
    }
    
    @PreDestroy
    public void destroy(){
        registrationValidator = null;
        customerValidator = null;
        customerInfoValidator = null;
    }
    @POST
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    @Produces({TEXT_PLAIN})
    public Response registerCustomer(RegistrationDTO registration) { 
        System.out.println("register customer: " + registration);
        System.out.println("Customer service ejb: " + customerService);
        Customer dto = null; 
                
        Response response = null;
        Customer customer = null;
        try {
            InputStream stream = servletContext.getResourceAsStream(CUSTOMER_SCHEMA_PATH);
            registrationValidator.validate(RegistrationDTO.class, registration, stream);        
            dto = customerService.registerCustomer(registration.getCustomer(), registration.getPassword());
            customer = dto;
        } catch (CustomerAlreadyExistsException ex) {
            response = Response.status(Response.Status.CONFLICT).entity(ex.getMessage()).build();
            return response;
        } catch (IllegalArgumentException | SAXException ex) {
            ex.printStackTrace();
            response = Response.status(Response.Status.BAD_REQUEST).build();
            return response;
        } catch (RuntimeException | JAXBException | IOException  ex) {
            ex.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            return response;
            
        }   
        response = Response.status(Response.Status.CREATED).entity(customer.getNumber()).build();      
        return response;

    }

    @GET
    @Path("{customerNumber}")
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response findCustomer(@PathParam("customerNumber") String number) {
        Customer dto = null;
        try {

            System.out.println("finding customer: " + number);
            dto = customerService.findCustomer(number);
            
            // validate outgoing object
            //InputStream stream = servletContext.getResourceAsStream(CUSTOMER_SCHEMA_PATH);
            //customerValidator.validate(Customer.class, dto, stream);
            
                
        } catch (CustomerNotFoundException ex) {
                return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        } /*catch (SAXException | JAXBException | IOException ex) {
            Logger.getLogger(CustomersResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }*/
        return Response.ok(dto).build();
    }
    
    @GET
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response searchCustomer(@QueryParam("name") String name){
        Response response= null;
        System.out.println("web service searching for customers....... " + name);
        if (name==null || name.isEmpty()){
            response = Response.status(Response.Status.BAD_REQUEST).build();
            return response;
        }
        System.out.println("paramater ok going to search for customers....... " + name);
        List<CustomerInfo> list = customerService.searchCustomers(name);
            
        System.out.println("number of customers found: " + list.size());
        
        GenericEntity<List<CustomerInfo>> outputList = new GenericEntity<List<CustomerInfo>>(list) {};
        /*InputStream stream = servletContext.getResourceAsStream(CUSTOMER_SCHEMA_PATH);
        for (CustomerInfo cust: list){
            try {
                customerInfoValidator.validate(CustomerInfo.class, cust, stream);
            } catch (SAXException | JAXBException | IOException ex) {
                ex.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } */
        
        return Response.ok(outputList).build();
    }
    
    @PUT
    @Path("{customerNumber}")
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    public void updateCustomer(@PathParam("customerNumber") String number, Customer customer){
        System.out.println("updating customer....... " + number + " " + customer);
        List<String> headerContentLength = headers.getRequestHeaders().get(CONTENT_LENGTH);
        if (headerContentLength.size() == 0){
            Response response = Response.status(Response.Status.NO_CONTENT).build();
            throw new WebApplicationException(EMPTY_REQUEST_MESSAGE, response);
        }
        if (!number.equals(customer.getNumber())){
           System.out.println("numbers don't match  " + number + " " + customer);
           Response response = Response.status(Response.Status.BAD_REQUEST).build();
           throw new WebApplicationException(CUSTOMER_NUMBERS_DONT_MATCH, response); 
        }
        try {
            InputStream stream = servletContext.getResourceAsStream(CUSTOMER_SCHEMA_PATH);
            customerValidator.validate(Customer.class, customer, stream);
            customerService.updateCustomer(customer);
        } catch (IllegalArgumentException | SAXException  ex) {
            System.out.println("updateCustomer rejecting customer  " + number + " " + customer);
            ex.printStackTrace();
            Response response = Response.status(Response.Status.BAD_REQUEST).build();
            throw new WebApplicationException(ex.getMessage(), response);
        } catch (RuntimeException | JAXBException | IOException  ex) {
            ex.printStackTrace();
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            throw new WebApplicationException(ex.getMessage(), response);
        } catch (CustomerNotFoundException ex) {
            Response response = Response.status(Response.Status.NOT_FOUND).build();
            throw new WebApplicationException(ex.getMessage(), response);
        } catch (CustomerAlreadyExistsException ex) {
            Response response = Response.status(Response.Status.CONFLICT).build();
            throw new WebApplicationException(ex.getMessage(), response);
        } 
    }   
        
    
    
    
}

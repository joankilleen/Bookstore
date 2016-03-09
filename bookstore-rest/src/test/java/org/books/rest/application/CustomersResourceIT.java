/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.rest.application;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.bind.util.JAXBSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.books.data.CreditCardType;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.books.data.dto.*;
import org.books.rest.util.ValidationHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author Joan
 */
public class CustomersResourceIT extends BaseIT {

    private org.books.data.dto.Customer customer = new Customer();
    private RegistrationDTO registration = new RegistrationDTO();

    public CustomersResourceIT() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @BeforeMethod
    public void setUpMethod() throws Exception {

        AddressDTO address = new AddressDTO();
        address.setCity("Berne");
        address.setCountry("CH");
        address.setStreet("Zugweg");
        address.setPostalCode("1234");
        customer.setAddress(address);

        CreditCardDTO card = new CreditCardDTO();
        card.setExpirationMonth(12);
        card.setExpirationYear(2020);
        card.setNumber("5000111122223333");
        card.setType(CreditCardType.MasterCard);
        customer.setCreditCard(card);

        customer.setFirstName("XXX");
        customer.setLastName("YYYY");
        customer.setEmail("anything");
        customer.setNumber("600100");
        registration.setCustomer(customer);
        registration.setPassword("pass_word");
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    @Test
    public void getCustomer() {
        System.out.println("getCustomer test");
        System.out.println("getCustomer: 6014");
        Response response = webTarget.path("6014").request(APPLICATION_XML).get();
        Customer customer = null;
        assert (Status.OK.getStatusCode() == response.getStatus());

        customer = response.readEntity(Customer.class);
        System.out.println("customer found: " + customer.getEmail() + " " + customer.getFirstName() + " " + customer.getNumber());

    }

    @Test
    public void noCustomerFound() {
        System.out.println("getCustomer test");
        System.out.println("getCustomer: XXXX");
        Response response = webTarget.path("XXXX").request(APPLICATION_XML).get();
        Customer customer = null;
        assert (Status.NOT_FOUND.getStatusCode() == response.getStatus());

    }

    @Test
    public void registerCustomer() throws DatatypeConfigurationException, ParseException {
        System.out.println("registerCustomer test");
        /*{"number":"6014",
         "email":"susanne@example.org","firstName":"Susanne","lastName":"Metre",
         "address":{"street":"Lausanneweg 22","city":"Lausanne","postalCode":"VD 1000","country":"CH"},
         "creditCard":{"type":"MasterCard","number":"5100000000000007","expirationMonth":1,"expirationYear":2020}}*/
        System.out.println("registerCustomer...." + registration.getCustomer());

        //registration.setPassword(null);
        Response response = webTarget.request(TEXT_PLAIN).post(Entity.json(registration));
        if (Status.OK.getStatusCode() == response.getStatus()) {

            String customerNr = response.readEntity(String.class);
            System.out.println("customer created: " + customer.getNumber());
        } else {
            System.out.println("response code: " + response.getStatusInfo());
        }
    }

    public Customer getCustomer(String number) {

        Response response = webTarget.path(number).request(APPLICATION_XML).get();
        Customer customer = null;
        if (Status.OK.getStatusCode() == response.getStatus()) {

            customer = response.readEntity(Customer.class);
            System.out.println("customer found: " + customer.toString() + " " + customer.getEmail());
        } else {
            System.out.println("response code: " + response.getStatusInfo());
        }
        return customer;

    }

    @Test
    public void updateCustomerJSON() {
        System.out.println("updateCustomerJSON test");

        String number = "6019";
        Customer customer = getCustomer(number);
        
        // email conflicts with an existing customeer
        customer.setEmail("superman@example.org");
        
        Response response = webTarget.path(number).request(MediaType.APPLICATION_JSON).put(Entity.json(customer));
        assertTrue(Status.NO_CONTENT.getStatusCode() == response.getStatus());
        System.out.println("response code: " + response.getStatusInfo());

    }
    @Test
    public void updateCustomerConflict() {
        System.out.println("updateCustomerConflict test");

        String number = "6019";
        Customer customer = getCustomer(number);
        
        // email conflicts with an existing customeer
        customer.setEmail("gisler@example.org");
        
        Response response = webTarget.path(number).request(MediaType.APPLICATION_JSON).put(Entity.json(customer));
        assertTrue(Status.CONFLICT.getStatusCode() == response.getStatus());
        System.out.println("response code: " + response.getStatusInfo());

    }
    @Test void updateCustomerbadRequest(){
        System.out.println("updateCustomerXML test");

        String number = "6019";
        Customer customer = getCustomer(number);
        customer.setEmail("");
        Response response = webTarget.path(number).request(MediaType.APPLICATION_JSON).put(Entity.json(customer));
        System.out.println("response code: " + response.getStatusInfo());
    }

    @Test
    public void searchCustomer() {
        System.out.println("searchCustomer test");

        String search = "metre";
        Response response = webTarget.queryParam("name", search).request(MediaType.APPLICATION_JSON).get();
        List<CustomerInfo> list = null;
        if (response.getStatus() == Status.OK.getStatusCode()) {
            list = response.readEntity(new GenericType<List<CustomerInfo>>() {
            });
        }
        System.out.println("size of customer list: " + list.size());
        for (CustomerInfo cust : list) {
            System.out.println("Customer found: " + cust);
        }
    }
    @Test
    public void searchCustomerNoneFound(){
        System.out.println("searchCustomer test");

        String search = "XXXXXXXX";
        Response response = webTarget.queryParam("name", search).request(MediaType.APPLICATION_JSON).get();
        List<CustomerInfo> list = null;
        if (response.getStatus() == Status.OK.getStatusCode()) {
            list = response.readEntity(new GenericType<List<CustomerInfo>>() {
            });
        }
        System.out.println("size of customer list: " + list.size());
        for (CustomerInfo cust : list) {
            System.out.println("Customer found: " + cust);
        }
    } 
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.rest.application;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.books.application.OrderService;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.OrderAlreadyShippedException;
import org.books.application.exception.OrderNotFoundException;
import org.books.application.exception.PaymentFailedException;
import org.books.data.dto.CustomerInfo;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;
import org.books.data.dto.OrderItemDTO;
import org.books.data.dto.RegistrationDTO;
import org.books.data.dto.OrderRequest;
import org.books.rest.util.ValidationHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author guthei
 */
@Path("orders")
@RequestScoped
public class OrdersResource {

    private ValidationHandler<OrderRequest> requestValidator;
    private ValidationHandler<OrderInfo> orderInfoValidator;
    private ValidationHandler<OrderDTO> orderDTOValidator;
    private static final Logger LOG = Logger.getLogger(OrdersResource.class.getName());
    private static final String ORDER_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/OrderService!org.books.application.OrderService";
    private static final String MERGED_SCHEMA_PATH = "/WEB-INF/schemas/merged.xsd";
    @Context
    ServletContext servletContext;
    @EJB
    private OrderService orderService;

    @PostConstruct
    public void init() {
        requestValidator = new ValidationHandler<>();
        orderInfoValidator = new ValidationHandler<>();
        orderDTOValidator = new ValidationHandler<>();
    }

    @PreDestroy
    public void destroy() {
        requestValidator = null;
    }

    @POST
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response placeOrder(OrderRequest orderRequest) {
        OrderDTO orderDTO = null;
        Response response = null;
        try {

            LOG.info("place order for customer: " + orderRequest.getCustomerNr());

            InputStream stream = servletContext.getResourceAsStream(MERGED_SCHEMA_PATH);
            requestValidator.validate(OrderRequest.class, orderRequest, stream);
            List<OrderItemDTO> orderInfo = orderRequest.getItems();
            orderDTO = orderService.placeOrder(orderRequest.getCustomerNr(), orderInfo);
            System.out.println("OrdersResource: order created: " + orderDTO);
            InputStream streamOutput = servletContext.getResourceAsStream(MERGED_SCHEMA_PATH);
            orderDTOValidator.validate(OrderDTO.class, orderDTO, streamOutput);
            response = Response.status(Response.Status.CREATED).entity(orderDTO).build();
            return response;
        } catch (BookNotFoundException ex) {
            // 404 "Not Found"
            response = Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
            return response;
        } catch (CustomerNotFoundException ex) {
            // 404 "Not Found"
            response = Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
            return response;
        } catch (PaymentFailedException ex) {
            // 402 "Payment Required"
            response = Response.status(Response.Status.PAYMENT_REQUIRED).entity(ex.getMessage()).build();
            return response;
        } catch (IllegalArgumentException | SAXException ex) {
            // 400 "Bad Request"
            ex.printStackTrace();
            response = Response.status(Response.Status.BAD_REQUEST).build();
            return response;
        } catch (JAXBException | IOException ex) {
            ex.printStackTrace();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            return response;
        }
        

    }

    @GET
    @Path("{number}")
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response findOrder(@PathParam("number") String orderNr) {
        LOG.info("finding order: " + orderNr);
        OrderDTO orderDTO = null;
        Response response = null;

        try {
            orderDTO = orderService.findOrder(orderNr);
        } catch (OrderNotFoundException ex) {
            // 404 "Not Found"
            response = Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
            return response;
        }
        
        response = Response.status(Response.Status.OK).entity(orderDTO).build();
        return response;
    }

    @GET
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response searchOrders(@QueryParam("customerNr") String customerNr, @QueryParam("year") Integer year) {
        LOG.info("finding order-number: " + customerNr + " order-year: " + year);
        Response response = null;
        
        List<OrderInfo> list = new ArrayList<>();
        if (customerNr == null || customerNr.isEmpty() || year == 0 | year.equals(null)) {
            response = Response.status(Response.Status.BAD_REQUEST).build();
            return response;
        }
        
        try {
            list = orderService.searchOrders(customerNr, year);
        } catch (CustomerNotFoundException ex) {
            response = Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
            return response;
        }
        InputStream stream = servletContext.getResourceAsStream(MERGED_SCHEMA_PATH);
        for (OrderInfo info : list) {
            try {
                orderInfoValidator.validate(OrderInfo.class, info, stream);
            } catch (SAXException ex) {
                Logger.getLogger(OrdersResource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JAXBException ex) {
                Logger.getLogger(OrdersResource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(OrdersResource.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        GenericEntity<List<OrderInfo>> outputList = new GenericEntity<List<OrderInfo>>(list) {};

        return Response.ok(outputList).build();
    }

    @DELETE
    @Path("{number}")
    public Response cancelOrder(@PathParam("number") String orderNr) {
        LOG.info("cancel order: " + orderNr);
        Response response = null;

        try {
            orderService.cancelOrder(orderNr);
            response = Response.status(Response.Status.NO_CONTENT).build();
        } catch (OrderNotFoundException ex) {
            response = Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        } catch (OrderAlreadyShippedException ex) {
            response = Response.status(Response.Status.FORBIDDEN).entity(ex.getMessage()).build();
        }
        return response;
    }
}

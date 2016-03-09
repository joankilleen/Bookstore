/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.rest.application;

import java.math.BigDecimal;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.Response;
import org.books.data.dto.Customer;
import org.books.data.dto.BookInfo;
import org.books.data.dto.CustomerInfo;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderItemDTO;
import org.books.data.dto.OrderRequest;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Joan
 */
public class OrdersResourceIT extends BaseIT {
    
    private OrderRequest orderRequest = new OrderRequest();
    private Customer customer = new Customer();
    private BookInfo info1 = new BookInfo();
    private OrderDTO order = null;
    @BeforeClass
    @Override
    public void setUpClass()throws Exception{
        setResourceURI("http://localhost:8080/bookstore/rest/orders");
        super.setUpClass();
    }
    
    @Test
    public void findOrder(){
        System.out.println("findOrder test");
        System.out.println("findOrder: 202023");
        Response response = webTarget.path("202023").request(APPLICATION_XML).get();
        assertTrue(Response.Status.OK.getStatusCode() == response.getStatus());

        org.books.data.dto.OrderDTO order = response.readEntity(org.books.data.dto.OrderDTO.class);
        System.out.println("order found: " + order);        
        org.books.data.dto.Customer customer = order.getCustomer();
        org.books.data.dto.BookInfo info = order.getOrderItems().get(0).getBook();
        

    }
    
    @Test()
    public void placeOrder(){
        WebTarget webTargetCust = client.target("http://localhost:8080/bookstore/rest/customers");
        Response responseCust = webTargetCust.path("6014").request(APPLICATION_XML).get();
        Customer customer = null;
        assert (Response.Status.OK.getStatusCode() == responseCust.getStatus());
        customer = responseCust.readEntity(Customer.class);
        System.out.println("placeOrder test for customer: " + customer.getNumber());
        orderRequest.setCustomerNr(customer.getNumber());
        System.out.println("customer number set: " + orderRequest.getCustomerNr());
        OrderItemDTO item = new OrderItemDTO();
        info1.setIsbn("143024626X");
        info1.setPrice(new BigDecimal("29.99"));
        info1.setTitle("XXX");
        item.setBook(info1);
        item.setQuantity(10);
        orderRequest.getItems().add(item);
        Response response = webTarget.request(APPLICATION_JSON).post(Entity.json(orderRequest));
        assertTrue(response.getStatus() == Response.Status.CREATED.getStatusCode());
        order = response.readEntity(OrderDTO.class);
        System.out.println("new order created: " + order.getNumber());
        System.out.println(response.getStatusInfo());
        
        
    }
    @Test(/*dependsOnMethods = "placeOrder"*/)
    public void searchOrders(){
        String customerNr = "6019";
        System.out.println("searchOrders test for customer: " + "6019");
        Response response = webTarget.queryParam("customerNr", customerNr).queryParam("year", 2010).request(APPLICATION_JSON).get();
        List<org.books.data.dto.OrderInfo> list = null;
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            list = response.readEntity(new GenericType<List<org.books.data.dto.OrderInfo>>() {
            });
        }
        System.out.println("size of order list: " + list.size());
        for (org.books.data.dto.OrderInfo order: list){
            System.out.println("order found: " + order);
        }
    }
    
    @Test(dependsOnMethods = "placeOrder") 
    public void cancelOrder(){
        System.out.println("cancelOrder test for customer: " + order.getNumber());
        Response response = webTarget.path(order.getNumber()).request().delete();
        assertTrue(Response.Status.NO_CONTENT.getStatusCode() == response.getStatus());
    }
    
    @Test 
    public void placeOrderbadRequest(){
        WebTarget webTargetCust = client.target("http://localhost:8080/bookstore/rest/customers");
        Response responseCust = webTargetCust.path("6014").request(APPLICATION_XML).get();
        Customer customer = null;
        assert (Response.Status.OK.getStatusCode() == responseCust.getStatus());
        customer = responseCust.readEntity(Customer.class);
        System.out.println("placeOrder test for customer, bad request: " + customer.getNumber());
        orderRequest.setCustomerNr(customer.getNumber());
        // no order items
        orderRequest.getItems().clear();
        Response response = webTarget.request(APPLICATION_JSON).post(Entity.json(orderRequest));
        assertTrue(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode());
        System.out.println(response.getStatusInfo());
    }
}

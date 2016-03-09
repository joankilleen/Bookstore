/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.util.List;
import javax.ejb.Remote;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.OrderAlreadyShippedException;
import org.books.application.exception.OrderNotFoundException;
import org.books.application.exception.PaymentFailedException;
import org.books.data.dto.CreditCardDTO;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;
import org.books.data.dto.OrderItemDTO;

/**
 *
 * @author Joan
 */
@Remote
public interface OrderService {
    public OrderDTO findOrder(String orderNr) throws OrderNotFoundException;
    
    public List<OrderInfo> searchOrders(String customerNr, Integer year) throws CustomerNotFoundException;
    
    public OrderDTO placeOrder(String customerNr, List<OrderItemDTO> items) 
            throws BookNotFoundException, CustomerNotFoundException, PaymentFailedException;
    
    public void cancelOrder(String orderNr) throws OrderNotFoundException, OrderAlreadyShippedException;
    
    public void validatePayment(CreditCardDTO creditcard, List<OrderItemDTO> items) throws PaymentFailedException;
    
}

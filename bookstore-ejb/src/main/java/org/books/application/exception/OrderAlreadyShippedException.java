/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.exception;

import javax.ejb.ApplicationException;

/**
 *
 * @author Joan
 */
@ApplicationException(rollback = true)
public class OrderAlreadyShippedException extends Exception{
    
}

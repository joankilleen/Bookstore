/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author guthei
 */
@XmlType(name = "CreditCardType")
@XmlEnum
public enum CreditCardType {
    MasterCard, Visa;  
   
}

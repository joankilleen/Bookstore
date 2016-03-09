/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.dto;

/**
 *
 * @author Joan
 */
    import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SqlDateAdapter extends XmlAdapter <String , Date>
    {

    public String marshal(java.sql.Date d) {
        //"yyyy-MM-dd'T'HH:mm:ss'Z'"
        StringBuilder returnValue = new StringBuilder();
        returnValue.append(d.toString());
        returnValue.append("T00:00:00Z");
        System.out.println("SqlDateAdapter marshalling:  " + returnValue);
        return returnValue.toString();
    }

    public Date unmarshal(String v) {
        System.out.println("SqlDateAdapter unmarshalling:  " + v);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        java.sql.Date sqlDate = null;
       try {
                 java.util.Date convertedDate = dateFormat.parse(v);
                 sqlDate = new java.sql.Date(convertedDate.getTime());
        } catch (ParseException e) {
           e.printStackTrace();
        }
        return sqlDate;
    }
    
    
}


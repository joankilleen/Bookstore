/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.rest.application;

/**
 *
 * @author Joan
 */
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.books.rest.util.LoggingFilter;

@ApplicationPath("")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(CustomersResource.class);
        classes.add(OrdersResource.class);
        classes.add(CatalogResource.class);        
        classes.add(LoggingFilter.class);
        return classes;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.rest.application;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 *
 * @author Joan
 */
public class BaseIT {
    
    private String resourceURI = "http://localhost:8080/bookstore/rest/customers";
    protected Connection con;
    protected IDatabaseConnection dbConnection;
    protected FlatXmlDataSet dataSet;
    protected static final String PERSISTENCE_UNIT = "bookstore";
    protected static final String USER = "app";
    protected static final String PASSWORD = "app";
    protected static final String CONNECTION_STRING = "jdbc:derby://localhost:1527/bookstore";
    protected static final Logger LOG = Logger.getLogger(BaseIT.class.getName());
    

    public String getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
    }
    protected Client client;
    protected WebTarget webTarget;
    
    public BaseIT() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @BeforeClass
    public void setUpClass() throws Exception {
        client = ClientBuilder.newClient();
        webTarget = client.target(resourceURI);
        
        con = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
        dbConnection = new DatabaseConnection(con);
        InputStream strm = BaseIT.class.getClassLoader().getResourceAsStream("bookstore.xml");
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        dataSet = builder.build(strm);
        DatabaseOperation.DELETE_ALL.execute(dbConnection, dataSet);
        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, dataSet);       
    
        
    }

    @AfterClass
    public void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.rest.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 *
 * @author Joan
 */
public class ValidationHandler<T> {

    public void validate(Class<T> type, T object, InputStream schemaStream) throws SAXException, JAXBException, IOException, SAXException {

        JAXBContext jc = JAXBContext.newInstance(type);
        JAXBSource source = new JAXBSource(jc, object);
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(schemaStream));
        Validator validator = schema.newValidator();
        validator.validate(source);

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.util.List;
import javax.ejb.Remote;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;

/**
 *
 * @author Joan
 */
@Remote
public interface AmazonCatalog  {
    
    public List<BookInfo> itemSearch(String keywords);
    public BookDTO itemLookup(String isbn);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.math.BigInteger;
import java.util.List;
import javax.ejb.Remote;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.books.data.dto.PageInfo;

/**
 *
 * @author Joan
 */
@Remote
public interface AmazonCatalog  {
    
    public List<BookInfo> itemSearch(String keywords);
    public PageInfo itemSearchPaged(String keywords, BigInteger pageToLoad);
    public BookDTO itemLookup(String isbn);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joan
 */
public class PageInfo implements Serializable{
    private List<BookInfo> bookItems = new ArrayList<>();
    private boolean more = false;
    private BigInteger lastPageLoaded;

    public List<BookInfo> getBookItems() {
        return bookItems;
    }

    public void setBookItems(List<BookInfo> bookItems) {
        this.bookItems = bookItems;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public BigInteger getLastPageLoaded() {
        return lastPageLoaded;
    }

    public void setLastPageLoaded(BigInteger lastPageLoaded) {
        this.lastPageLoaded = lastPageLoaded;
    }
    
    
    
    
}

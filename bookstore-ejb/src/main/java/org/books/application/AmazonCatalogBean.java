/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import org.amazon.security.AmazonSecurityFactory;
import org.amazon.security.AmazonSecurityHelper;
import org.amazon.webservice.soap.AWSECommerceService;
import org.amazon.webservice.soap.AWSECommerceServicePortType;
import org.amazon.webservice.soap.Errors;
import org.amazon.webservice.soap.Item;
import org.amazon.webservice.soap.ItemAttributes;
import org.amazon.webservice.soap.ItemLookup;
import org.amazon.webservice.soap.ItemLookupRequest;
import org.amazon.webservice.soap.ItemLookupResponse;
import org.amazon.webservice.soap.ItemSearch;
import org.amazon.webservice.soap.ItemSearchRequest;
import org.amazon.webservice.soap.ItemSearchResponse;
import org.amazon.webservice.soap.Items;
import org.amazon.webservice.soap.OperationRequest;
import org.amazon.webservice.soap.Price;
import org.books.data.Binding;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;

/**
 *
 * @author Joan
 */
@Stateless(name = "AmazonCatalog")
@Singleton
public class AmazonCatalogBean implements AmazonCatalog {

    private static final int MAX_ITEM_PAGES = 10;
    private static final int MILLSECOND_DELAY = 800;
    private static final Logger LOG = Logger.getLogger(AmazonCatalogBean.class.getName());

    //AWSECommerceService service;
    @WebServiceRef(AWSECommerceService.class)
    AWSECommerceServicePortType awseServicePort;

    @PostConstruct
    public void init() {
    }

    @PreDestroy
    public void destroy() {
    }

    @Override
    public List<BookInfo> itemSearch(String keywords) {
        List<BookInfo> resultList = new ArrayList<>();

        int totalPages = 10;
        for (int i = 1; i <= totalPages && i <= MAX_ITEM_PAGES; i++) {
            log("going to request page " + i);

            ItemSearchResponse searchResponse = sendSearchRequest(keywords, new BigInteger(new Integer(i).toString()));

            try {
                totalPages = processSearchResults(searchResponse, resultList);
                log("Total pages found:  " + totalPages);
            } catch (InternalNoResultsException ex) {
                log(ex.getMessage());

                return resultList;
            }
            try {
                Thread.sleep(MILLSECOND_DELAY);
            } catch (InterruptedException ex) {
                log(ex.getLocalizedMessage());
                return resultList;
            }
        }
        return resultList;

    }

    private ItemSearchRequest createSearchRequest(String keywords, BigInteger itemPage) {
        ItemSearchRequest searchRequest = new ItemSearchRequest();
        searchRequest.setSearchIndex("Books");
        searchRequest.setKeywords(keywords);
        searchRequest.setItemPage(itemPage);
        searchRequest.getResponseGroup().add("ItemAttributes");
        return searchRequest;
    }

    private ItemSearchResponse sendSearchRequest(String keywords, BigInteger itemPage) {
        AmazonSecurityHelper helper = new AmazonSecurityFactory().createHelper("ItemSearch");
        ItemSearch itemSearch = new ItemSearch();

        ItemSearchRequest searchRequest = createSearchRequest(keywords, itemPage);

        itemSearch.setAssociateTag(helper.getAssociatekey());
        itemSearch.getRequest().add(searchRequest);

        ItemSearchResponse searchResponse = awseServicePort.itemSearch(itemSearch);
        return searchResponse;
    }

    private int processSearchResults(ItemSearchResponse searchResponse, List<BookInfo> resultList) throws InternalNoResultsException {
        List<Items> listItems = searchResponse.getItems();
        OperationRequest request = searchResponse.getOperationRequest();

        int totalPages = 0;
        int currentPage = 0;
        for (Items items : listItems) {
            Errors errors = items.getRequest().getErrors();
            if (errors != null && errors.getError().size() > 0) {
                log("size of errors: " + errors.getError().size());
                log(errors.getError().get(0).getCode() + " " + errors.getError().get(0).getMessage());
                throw new InternalNoResultsException(errors.getError().get(0).getMessage());

            }
            totalPages = items.getTotalPages().intValue();
            log("Size of list ITEMS: " + listItems.size());

            List<Item> listITEM = items.getItem();
            log("Size of list ITEM: " + listITEM.size());
            for (Item item : listITEM) {
                
                ItemAttributes attributes = item.getItemAttributes();

                BookInfo nextBook = new BookInfo();
                boolean isValidBook = validateBook(nextBook, attributes);
                if (isValidBook) {
                    log("adding book to list: " + nextBook);
                    resultList.add(nextBook);
                }
            }
        }
        log("processSearchResults returning total pages: " + totalPages);
        return totalPages;
    }

    private void log(String message) {
        LOG.info(this.getClass().getSimpleName() + " " + message);
    }

    private boolean validateBook(BookInfo nextBook, ItemAttributes attributes) {
        log("validation book: " + nextBook);
        log("Book binding found: " + attributes.getBinding());
        if (attributes.getListPrice() == null || attributes.getListPrice().getFormattedPrice().isEmpty()) {
            log("rejecting book because pirce is empty: " + nextBook);
            return false;
        }
        nextBook.setPrice(convertPrice(attributes.getListPrice()));

        if (attributes.getISBN() == null || attributes.getISBN().isEmpty()) {
            log("rejecting book because ISBN is empty: " + nextBook);
            return false;
        }
        nextBook.setIsbn(attributes.getISBN());

        if (attributes.getTitle() == null || attributes.getTitle().isEmpty()) {
            log("rejecting book because title is empty: " + nextBook);
            return false;
        }
        nextBook.setTitle(attributes.getTitle());

        if (attributes.getAuthor() == null || attributes.getAuthor().size() == 0) {
            log("rejecting book because author is empty: " + nextBook);
            return false;
        }
        
        if (attributes.getBinding() == null || attributes.getBinding().isEmpty()) {
            log("rejecting book because binding is empty: " + nextBook);
            return false;
        }
        if (attributes.getNumberOfPages() == null || attributes.getNumberOfPages().equals(0)) {
            log("rejecting book because number of pages is empty: " + nextBook);
            return false;
        }
        if (attributes.getPublicationDate() == null || attributes.getPublicationDate().isEmpty()) {
            log("rejecting book because publication date is empty: " + nextBook);
            return false;
        }
        log("Book binding found: " + attributes.getBinding());
        return true;

    }

    @Override
    public BookDTO itemLookup(String isbn) {
        BookDTO bookDTO = null;
        ItemLookupResponse itemLookupResponse = sendLookupRequest(isbn);
        try {
            bookDTO = processLookupResult(itemLookupResponse);
        } catch (InternalNoResultsException ex) {
            log(ex.getMessage());
            return bookDTO;
        }
        return bookDTO;
    }

    private static ItemLookupRequest createLookupRequest(String isbn) {
        ItemLookupRequest searchRequest = new ItemLookupRequest();
        searchRequest.setSearchIndex("Books");
        searchRequest.setIdType("ISBN");
        searchRequest.getItemId().add(isbn);
        searchRequest.getResponseGroup().add("ItemAttributes");
        return searchRequest;
    }

    private ItemLookupResponse sendLookupRequest(String isbn) {
        AmazonSecurityHelper helper = new AmazonSecurityFactory().createHelper("ItemLookup");
        ItemLookup itemLookup = new ItemLookup();

        ItemLookupRequest lookupRequest = createLookupRequest(isbn);

        itemLookup.setAssociateTag(helper.getAssociatekey());
        itemLookup.getRequest().add(lookupRequest);

        ItemLookupResponse lookupResponse = awseServicePort.itemLookup(itemLookup);
        return lookupResponse;
    }

    private BookDTO processLookupResult(ItemLookupResponse itemLookupResponse) throws InternalNoResultsException {
        BookDTO bookDTO = null;
        String authors = "";
        Binding binding = null;
        Integer publicationDate = 0;
        List<Items> listItems = itemLookupResponse.getItems();

        for (Iterator<Items> it = listItems.iterator(); it.hasNext();) {
            Items items = it.next();
            Errors errors = items.getRequest().getErrors();
            if (errors != null && errors.getError().size() > 0) {
                log("size of errors: " + errors.getError().size());
                log(errors.getError().get(0).getCode() + " " + errors.getError().get(0).getMessage());
                throw new InternalNoResultsException(errors.getError().get(0).getMessage());
            }
            
            Item bookItem = items.getItem().get(0);
            ItemAttributes attributes = bookItem.getItemAttributes();

            if (!attributes.getAuthor().isEmpty()) {
                for (String author : attributes.getAuthor()) {
                    authors += author + ", ";
                }
                authors = authors.substring(0, authors.length() - 2);
            }
            if (attributes.getBinding() != null) {
                if (attributes.getBinding().equals("Ebook")
                        | attributes.getBinding().equals("Hardcover")
                        | attributes.getBinding().equals("Paperback")) {
                    binding = Binding.valueOf(attributes.getBinding());
                } else if (attributes.getBinding().equals("Kindle Edition")) {
                    binding = Binding.Ebook;
                
                } else {
                    binding = Binding.valueOf("Unknown");
                }
            }
            if (!attributes.getPublicationDate().equals(null)) {
                publicationDate = Integer.parseInt(attributes.getPublicationDate().substring(0, 4));
            }

            bookDTO = new BookDTO(attributes.getISBN(), attributes.getTitle(),
                    authors, attributes.getPublisher(), publicationDate, binding,
                    attributes.getNumberOfPages().intValue(), convertPrice(attributes.getListPrice()));
            try {
                Thread.sleep(MILLSECOND_DELAY);
            } catch (InterruptedException ex) {
                log(ex.getLocalizedMessage());
            }
        }
        return bookDTO;
    }

    private class InternalNoResultsException extends Exception {

        private InternalNoResultsException(String message) {
            super(message);
        }

    }

    private BigDecimal convertPrice(Price listPrice) {
        BigDecimal amount = new BigDecimal(listPrice.getAmount().toString());
        BigDecimal divisor = new BigDecimal("100");
        return amount.divide(divisor, 2, RoundingMode.CEILING);
    }
}

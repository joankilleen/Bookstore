package org.books.data.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.books.data.Binding;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-03-09T17:33:57")
@StaticMetamodel(BookEntity.class)
public class BookEntity_ { 

    public static volatile SingularAttribute<BookEntity, Integer> numberOfPages;
    public static volatile SingularAttribute<BookEntity, BigDecimal> price;
    public static volatile SingularAttribute<BookEntity, String> isbn;
    public static volatile SingularAttribute<BookEntity, String> publisher;
    public static volatile SingularAttribute<BookEntity, Integer> publicationYear;
    public static volatile SingularAttribute<BookEntity, Binding> binding;
    public static volatile SingularAttribute<BookEntity, Long> id;
    public static volatile SingularAttribute<BookEntity, String> title;
    public static volatile SingularAttribute<BookEntity, String> authors;

}
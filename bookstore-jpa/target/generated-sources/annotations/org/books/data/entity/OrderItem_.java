package org.books.data.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.books.data.entity.BookEntity;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-03-09T17:33:57")
@StaticMetamodel(OrderItem.class)
public class OrderItem_ { 

    public static volatile SingularAttribute<OrderItem, Integer> quantity;
    public static volatile SingularAttribute<OrderItem, BigDecimal> price;
    public static volatile SingularAttribute<OrderItem, BookEntity> book;
    public static volatile SingularAttribute<OrderItem, Integer> id;

}
package org.books.data.entity;

import java.math.BigDecimal;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.books.data.OrderStatus;
import org.books.data.entity.Address;
import org.books.data.entity.CreditCard;
import org.books.data.entity.CustomerEntity;
import org.books.data.entity.OrderItem;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-03-09T17:33:57")
@StaticMetamodel(OrderEntity.class)
public class OrderEntity_ { 

    public static volatile SingularAttribute<OrderEntity, Date> date;
    public static volatile SingularAttribute<OrderEntity, String> number;
    public static volatile SingularAttribute<OrderEntity, BigDecimal> amount;
    public static volatile SingularAttribute<OrderEntity, Address> address;
    public static volatile SingularAttribute<OrderEntity, Long> id;
    public static volatile SingularAttribute<OrderEntity, CreditCard> creditCard;
    public static volatile ListAttribute<OrderEntity, OrderItem> orderItems;
    public static volatile SingularAttribute<OrderEntity, OrderStatus> status;
    public static volatile SingularAttribute<OrderEntity, CustomerEntity> customer;

}
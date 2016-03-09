package org.books.data.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.books.data.entity.Address;
import org.books.data.entity.CreditCard;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-03-09T17:33:57")
@StaticMetamodel(CustomerEntity.class)
public class CustomerEntity_ { 

    public static volatile SingularAttribute<CustomerEntity, String> number;
    public static volatile SingularAttribute<CustomerEntity, String> firstName;
    public static volatile SingularAttribute<CustomerEntity, String> lastName;
    public static volatile SingularAttribute<CustomerEntity, Address> address;
    public static volatile SingularAttribute<CustomerEntity, Long> id;
    public static volatile SingularAttribute<CustomerEntity, CreditCard> creditCard;
    public static volatile SingularAttribute<CustomerEntity, String> email;

}
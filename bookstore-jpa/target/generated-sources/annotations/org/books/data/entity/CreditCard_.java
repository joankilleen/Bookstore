package org.books.data.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.books.data.CreditCardType;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-03-09T17:33:57")
@StaticMetamodel(CreditCard.class)
public class CreditCard_ { 

    public static volatile SingularAttribute<CreditCard, Integer> expirationYear;
    public static volatile SingularAttribute<CreditCard, String> number;
    public static volatile SingularAttribute<CreditCard, Integer> expirationMonth;
    public static volatile SingularAttribute<CreditCard, CreditCardType> type;

}
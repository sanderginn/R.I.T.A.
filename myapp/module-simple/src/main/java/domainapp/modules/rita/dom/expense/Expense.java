package domainapp.modules.rita.dom.expense;

import java.math.BigDecimal;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainObject;

import org.incode.module.base.dom.utils.TitleBuilder;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.driver.Driver;
import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        schema = "rita"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column = "id")
@javax.jdo.annotations.Version(
        strategy = VersionStrategy.DATE_TIME,
        column = "version")
@Queries({
        @Query(
                name = "findByCar",
                value = "SELECT "
                        + "FROM domainapp.modules.rita.dom.expense.Expense "
                        + "WHERE car == :car")
})
@DomainObject()
public class Expense implements Comparable<Expense> {

    public String title() {
        return TitleBuilder.start()
                .toString();
    }

    public Expense(
            final ExpenseType type,
            final BigDecimal cost,
            final LocalDate datePaid,
            final Driver paidBy,
            final Car car) {
        setType(type);
        setCost(cost);
        setDatePaid(datePaid);
        setPaidBy(paidBy);
        setCar(car);
        setSettled(Boolean.FALSE);
    }

    @Column(allowsNull = "false")
    @Getter @Setter
    private ExpenseType type;

    @Column(allowsNull = "false", scale = 2)
    @Getter @Setter
    private BigDecimal cost;

    @Column(allowsNull = "false")
    @Getter @Setter
    private LocalDate datePaid;

    @Column(allowsNull = "false")
    @Getter @Setter
    private Driver paidBy;

    @Column(allowsNull = "false")
    @Getter @Setter
    private Car car;

    @Column(allowsNull = "false")
    @Getter @Setter
    private Boolean settled;

    @Override
    public int compareTo(final Expense o) {
        return getDatePaid().compareTo(o.getDatePaid());
    }
}

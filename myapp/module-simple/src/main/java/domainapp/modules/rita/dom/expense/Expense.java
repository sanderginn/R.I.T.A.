package domainapp.modules.rita.dom.expense;

import java.math.BigDecimal;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;

import org.incode.module.base.dom.utils.TitleBuilder;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.invoice.Invoice;
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
                        + "WHERE car == :car"),
        @Query(
                name = "findByInvoice",
                value = "SELECT "
                        + "FROM domainapp.modules.rita.dom.expense.Expense "
                        + "WHERE settledOnInvoice == :invoice"),
        @Query(
                name = "findByCarAndSettledStatus",
                value = "SELECT "
                        + "FROM domainapp.modules.rita.dom.expense.Expense "
                        + "WHERE car == :car"
                        + "&& settled == :settled")
})
@DomainObject
@DomainObjectLayout(cssClassFa = "fa-eur")
public class Expense implements Comparable<Expense> {

    public String title() {
        return TitleBuilder.start()
                .withName(getPaidBy().title())
                .withReference(getDatePaid().toString() + ", " + settledToString())
                .withParent(getType().toNormalCase())
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
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Getter @Setter
    private Car car;

    @Column(allowsNull = "false")
    @Getter @Setter
    private Boolean settled;

    @Column(allowsNull = "true")
    @Getter @Setter
    private Invoice settledOnInvoice;

    public boolean hideSettledOnInvoice() {
        return !getSettled();
    }

    @Override
    public int compareTo(final Expense o) {
        return -getCost().compareTo(o.getCost());
    }

    @Programmatic
    private String settledToString() {
        return getSettled() ? "settled" : "not settled";
    }
}

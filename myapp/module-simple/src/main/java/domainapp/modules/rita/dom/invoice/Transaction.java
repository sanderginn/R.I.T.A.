package domainapp.modules.rita.dom.invoice;

import java.math.BigDecimal;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.VersionStrategy;

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
                name = "findByInvoice",
                value = "SELECT "
                        + "FROM domainapp.modules.rita.dom.invoice.Transaction "
                        + "WHERE invoice == :invoice")
})
public class Transaction implements Comparable<Transaction> {

    public Transaction(
            final Driver debtor,
            final Driver creditor,
            final BigDecimal amount,
            final Invoice invoice) {
        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
        this.invoice = invoice;
        this.settled = Boolean.FALSE;
    }

    @Column(allowsNull = "false")
    @Getter @Setter
    private Driver debtor;

    @Column(allowsNull = "false")
    @Getter @Setter
    private Driver creditor;

    @Column(allowsNull = "false", scale = 2)
    @Getter @Setter
    private BigDecimal amount;

    @Column(allowsNull = "false")
    @Getter @Setter
    private Invoice invoice;

    @Column(allowsNull = "false")
    @Getter @Setter
    private Boolean settled;

    @Override
    public int compareTo(final Transaction o) {
        return getAmount().compareTo(o.getAmount());
    }
}

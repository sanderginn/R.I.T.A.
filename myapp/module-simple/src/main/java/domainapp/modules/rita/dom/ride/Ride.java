package domainapp.modules.rita.dom.ride;

import java.math.BigInteger;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
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
                        + "FROM domainapp.modules.rita.dom.ride.Ride "
                        + "WHERE car == :car"),
        @Query(
                name = "findByInvoice",
                value = "SELECT "
                        + "FROM domainapp.modules.rita.dom.ride.Ride "
                        + "WHERE settledOnInvoice == :invoice"),
        @Query(
                name = "findByCarAndSettledStatus",
                value = "SELECT "
                        + "FROM domainapp.modules.rita.dom.ride.Ride "
                        + "WHERE car == :car"
                        + "&& settled == :settled")
})
@DomainObject
@DomainObjectLayout(cssClassFa = "fa-road")
public class Ride implements Comparable<Ride> {

    public String title() {
        return TitleBuilder.start()
                .withName(getDescription())
                .withReference(date.toString())
                .toString();
    }

    public Ride(
            final String description,
            final LocalDate date,
            final BigInteger oldMileage,
            final BigInteger newMileage,
            final Driver driver,
            final Car car) {
        setDescription(description);
        setDate(date);
        setOldMileage(oldMileage);
        setNewMileage(newMileage);
        setDistanceTraveled(newMileage.subtract(oldMileage));
        setDriver(driver);
        setCar(car);
        setSettled(Boolean.FALSE);
    }

    @Column(allowsNull = "false")
    @Getter @Setter
    private String description;

    @Column(allowsNull = "false")
    @Getter @Setter
    private LocalDate date;

    @Column(allowsNull = "false")
    @Getter @Setter
    private BigInteger oldMileage;

    @Column(allowsNull = "false")
    @Getter @Setter
    private BigInteger newMileage;

    @Column(allowsNull = "false")
    @Getter @Setter
    private BigInteger distanceTraveled;

    @Column(allowsNull = "false")
    @Getter @Setter
    private Driver driver;

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
    public int compareTo(final Ride o) {
        return -getNewMileage().compareTo(o.getNewMileage());
    }
}

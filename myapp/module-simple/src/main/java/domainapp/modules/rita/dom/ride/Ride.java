package domainapp.modules.rita.dom.ride;

import java.math.BigInteger;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainObject;

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
@DomainObject()
public class Ride implements Comparable<Ride> {

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
        setPaid(Boolean.FALSE);
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
    @Getter @Setter
    private Car car;

    @Column(allowsNull = "false")
    @Getter @Setter
    private Boolean paid;

    @Override
    public int compareTo(final Ride o) {
        return this.getDate().compareTo(o.getDate());
    }
}

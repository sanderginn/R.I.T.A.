package domainapp.modules.rita.dom.car;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;
import org.json.JSONObject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.incode.module.base.dom.utils.TitleBuilder;

import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.driver.DriverRepository;
import domainapp.modules.rita.dom.ride.RideRepository;
import domainapp.modules.rita.dom.util.DateUtil;
import domainapp.modules.rita.dom.util.LicenseScanner;
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
                name = "findByLicensePlate",
                value = "SELECT "
                        + "FROM domainapp.modules.rita.dom.car.Car "
                        + "WHERE licensePlate == :licensePlate")
})
@DomainObject
@DomainObjectLayout(cssClassFa = "fa-car")
public class Car {

    public String title() {
        return TitleBuilder.start()
                .withName(getBrand())
                .withName(getModel())
                .toString();
    }

    public Car(final String licensePlate, final BigInteger mileage) {
        setLicensePlate(licensePlate);
        setMileage(mileage);
        JSONObject carData = LicenseScanner.requestWebService(licensePlate).getJSONObject("d");
        setBrand(carData.getString("Merk"));
        setModel(carData.getString("Handelsbenaming"));
        setApkExpirationDate(DateUtil.parseRdwDate(carData.getString("VervaldatumAPK")));
    }

    // //////////////////////////////////

    @Column(allowsNull = "false")
    @Getter @Setter
    private String licensePlate;

    // //////////////////////////////////

    @Column(allowsNull = "false")
    @Getter @Setter
    private BigInteger mileage;

    // //////////////////////////////////

    @Column(allowsNull = "false")
    @Getter @Setter
    private String brand;

    // //////////////////////////////////

    @Column(allowsNull = "false")
    @Getter @Setter
    private String model;

    // //////////////////////////////////

    @Column(allowsNull = "false")
    @Getter @Setter
    private LocalDate apkExpirationDate;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public Car updateApkExpirationDate() {
        JSONObject carData = LicenseScanner.requestWebService(getLicensePlate()).getJSONObject("d");
        setApkExpirationDate(DateUtil.parseRdwDate(carData.getString("VervaldatumAPK")));
        return this;
    }

    // //////////////////////////////////

    @Getter @Setter
    private List<Driver> drivers = new ArrayList<>();

    public Car addDriver(final Driver driver) {
        drivers.add(driver);
        return this;
    }

    public List<Driver> choices0AddDriver() {
        return driverRepository.listAll();
    }

    // //////////////////////////////////

    @Inject
    DriverRepository driverRepository;

    @Inject
    RideRepository rideRepository;

}

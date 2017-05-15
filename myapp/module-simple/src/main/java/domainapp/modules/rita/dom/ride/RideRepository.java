package domainapp.modules.rita.dom.ride;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.invoice.Invoice;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Ride.class)
public class RideRepository {

    public Ride newRide(
            final String description,
            final LocalDate date,
            final BigInteger oldMileage,
            final BigInteger newMileage,
            final Driver driver,
            final Car car) {
        final Ride ride = new Ride(description, date, oldMileage, newMileage, driver, car);
        serviceRegistry.injectServicesInto(ride);
        repositoryService.persist(ride);

        return ride;
    }

    public List<Ride> findByCar(final Car car) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Ride.class,
                        "findByCar",
                        "car", car
                ));
    }

    public List<Ride> findByInvoice(final Invoice invoice) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Ride.class,
                        "findByInvoice",
                        "invoice", invoice
                ));
    }

    public List<Ride> findByCarAndSettledStatus(final Car car, final Boolean settledStatus) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Ride.class,
                        "findByCarAndSettledStatus",
                        "car", car, "settled", settledStatus
                ));
    }

    @Inject
    RepositoryService repositoryService;

    @Inject
    ServiceRegistry2 serviceRegistry;
}

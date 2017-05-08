package domainapp.modules.rita.dom.ride;

import java.math.BigInteger;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.driver.Driver;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Ride.class)
public class RideRepository {

    public void newRide(
            final String description,
            final LocalDate date,
            final BigInteger newMileage,
            final Driver driver,
            final Car car) {
        final Ride ride = new Ride(description, date, newMileage, driver, car);
        serviceRegistry.injectServicesInto(ride);
        repositoryService.persist(ride);
    }

    @Inject
    RepositoryService repositoryService;

    @Inject
    ServiceRegistry2 serviceRegistry;
}

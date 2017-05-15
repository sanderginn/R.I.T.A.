package domainapp.modules.rita.dom.car;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Car.class
)
public class CarRepository {

    public List<Car> listAll() {
        return repositoryService.allInstances(Car.class);
    }

    public Car findOrCreateCar(final String licensePlate, final BigInteger mileage) {
        if (findByLicensePlate(licensePlate) == null) {
            final Car car = new Car(licensePlate, mileage);
            serviceRegistry.injectServicesInto(car);
            repositoryService.persist(car);
            return car;
        } else {
            return findByLicensePlate(licensePlate);
        }
    }

    public Car findByLicensePlate(final String licensePlate) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        Car.class,
                        "findByLicensePlate",
                        "licensePlate", licensePlate
                ));
    }

    @Inject
    ServiceRegistry2 serviceRegistry;

    @Inject
    RepositoryService repositoryService;
}

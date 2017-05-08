package domainapp.modules.rita.dom.car;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
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

    public Car newCar(final String licensePlate, final BigInteger mileage) {
        final Car car = new Car(licensePlate, mileage);
        serviceRegistry.injectServicesInto(car);
        repositoryService.persist(car);
        return car;
    }

    @Inject
    ServiceRegistry2 serviceRegistry;

    @Inject
    RepositoryService repositoryService;
}

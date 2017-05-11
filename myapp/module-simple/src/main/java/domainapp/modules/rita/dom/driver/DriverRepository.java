package domainapp.modules.rita.dom.driver;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.rita.dom.util.IBANValidator;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Driver.class
)
public class DriverRepository {

    public List<Driver> listAll() {
        return repositoryService.allInstances(Driver.class);
    }

    public List<Driver> findByLastName(final String lastName) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Driver.class,
                        "findByLastName",
                        "lastName", lastName
                ));
    }

    public Driver newDriver(
            final String firstName,
            final String lastName,
            final String email,
            final String iban) {
        final Driver driver = new Driver(firstName, lastName, email, iban);
        serviceRegistry.injectServicesInto(driver);
        repositoryService.persist(driver);
        return driver;
    }

    @Inject
    RepositoryService repositoryService;

    @Inject
    ServiceRegistry2 serviceRegistry;
}

package domainapp.modules.rita.fixture.driver;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.driver.DriverRepository;

public abstract class DriverAbstract extends FixtureScript {

    protected Driver createDriver(
            final String firstName,
            final String lastName,
            final String email,
            final String iban,
            final ExecutionContext fixtureResults) {
        Driver driver = driverRepository.newDriver(firstName, lastName, email, iban);
        fixtureResults.addResult(this, driver);

        return driver;
    }

    @Inject
    protected DriverRepository driverRepository;

}

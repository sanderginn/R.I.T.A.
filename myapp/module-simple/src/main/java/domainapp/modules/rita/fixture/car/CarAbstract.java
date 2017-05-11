package domainapp.modules.rita.fixture.car;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.car.CarRepository;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.driver.DriverRepository;

public abstract class CarAbstract extends FixtureScript {

    protected Car createCar(
            final String licensePlate,
            final BigInteger mileage,
            final ExecutionContext fixtureResults) {
        Car car = carRepository.newCar(licensePlate, mileage);
        fixtureResults.addResult(this, car);

        return car;
    }

    protected void addDrivers(Car car, List<String> lastNames) {
        for (String lastName : lastNames) {
            Driver driver = driverRepository.findByLastName(lastName).get(0);
            car.addDriver(driver);
        }
    }

    @Inject
    protected CarRepository carRepository;

    @Inject
    protected DriverRepository driverRepository;
}

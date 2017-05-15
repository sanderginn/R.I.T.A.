package domainapp.modules.rita.fixture.ride;

import java.math.BigInteger;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.car.CarRepository;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.driver.DriverRepository;
import domainapp.modules.rita.dom.ride.Ride;
import domainapp.modules.rita.dom.ride.RideRepository;

public abstract class RideAbstract extends FixtureScript {

    protected Ride createRide(
            final String description,
            final LocalDate date,
            final BigInteger oldMileage,
            final BigInteger newMileage,
            final Driver driver,
            final Car car,
            final ExecutionContext fixtureResults) {
        Ride ride = rideRepository.newRide(description, date, oldMileage, newMileage, driver, car);
        car.setMileage(newMileage);
        fixtureResults.addResult(this, ride);

        return ride;
    }

    @Inject
    protected RideRepository rideRepository;

    @Inject
    protected DriverRepository driverRepository;

    @Inject
    protected CarRepository carRepository;

}

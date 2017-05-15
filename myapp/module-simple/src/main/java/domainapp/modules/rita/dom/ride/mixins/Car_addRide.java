package domainapp.modules.rita.dom.ride.mixins;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.clock.ClockService;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.ride.RideRepository;

@Mixin
public class Car_addRide {

    private final Car car;

    public Car_addRide(final Car car) {
        this.car = car;
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public Car $$(
            final String description,
            final LocalDate date,
            final BigInteger newMileage,
            final Driver driver) {
        rideRepository.newRide(description, date, car.getMileage(), newMileage, driver, car);
        car.setMileage(newMileage);

        return car;
    }

    public LocalDate default1$$() {
        return clockService.now();
    }

    public List<Driver> choices3$$() {
        return car.getDrivers();
    }

    public String validate$$(
            final String description,
            final LocalDate date,
            final BigInteger newMileage,
            final Driver driver) {
        return newMileage.compareTo(car.getMileage()) != 1 ? "New mileage must be more than current mileage (" + car.getMileage().toString() + ")" : null;
    }

    @Inject
    RideRepository rideRepository;

    @Inject
    ClockService clockService;
}

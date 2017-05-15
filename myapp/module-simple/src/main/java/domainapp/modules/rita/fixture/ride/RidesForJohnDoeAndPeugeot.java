package domainapp.modules.rita.fixture.ride;

import java.math.BigInteger;

import org.joda.time.LocalDate;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.fixture.car.CarForPeugeotAndJohnAndJan;
import domainapp.modules.rita.fixture.driver.DriverForJohnDoe;

public class RidesForJohnDoeAndPeugeot extends RideAbstract {

    public static final BigInteger NEW_MILEAGE_RIDE_ONE = BigInteger.valueOf(1100);
    public static final BigInteger NEW_MILEAGE_RIDE_TWO = BigInteger.valueOf(1250);

    @Override
    protected void execute(final ExecutionContext executionContext) {
        // prereqs
        executionContext.executeChild(this, new CarForPeugeotAndJohnAndJan());

        final Car car = carRepository.findByLicensePlate(CarForPeugeotAndJohnAndJan.LICENSE_PLATE);
        final Driver driverForJohn = driverRepository.findByLastName(DriverForJohnDoe.LAST_NAME).get(0);

        createRide("Ride 1",
                new LocalDate(2017, 1, 1),
                car.getMileage(),
                NEW_MILEAGE_RIDE_ONE,
                driverForJohn,
                car,
                executionContext);

        createRide("Ride 2",
                new LocalDate(2017, 1, 2),
                car.getMileage(),
                NEW_MILEAGE_RIDE_TWO,
                driverForJohn,
                car,
                executionContext);

    }

}

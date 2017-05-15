package domainapp.modules.rita.fixture.ride;

import java.math.BigInteger;

import org.joda.time.LocalDate;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.fixture.car.CarForPeugeotAndJohnAndJan;
import domainapp.modules.rita.fixture.driver.DriverForJanModaal;

public class RidesForJanModaalAndPeugeot extends RideAbstract {

    public static final BigInteger NEW_MILEAGE_RIDE_THREE = BigInteger.valueOf(1350);
    public static final BigInteger NEW_MILEAGE_RIDE_FOUR = BigInteger.valueOf(1475);

    @Override
    protected void execute(final ExecutionContext executionContext) {
        // prereqs
        executionContext.executeChild(this, new CarForPeugeotAndJohnAndJan());

        final Car car = carRepository.findByLicensePlate(CarForPeugeotAndJohnAndJan.LICENSE_PLATE);
        final Driver driverForJan = driverRepository.findByLastName(DriverForJanModaal.LAST_NAME).get(0);

        createRide("Ride 3",
                new LocalDate(2017, 1, 3),
                car.getMileage(),
                NEW_MILEAGE_RIDE_THREE,
                driverForJan,
                car,
                executionContext);

        createRide("Ride 4",
                new LocalDate(2017, 1, 4),
                car.getMileage(),
                NEW_MILEAGE_RIDE_FOUR,
                driverForJan,
                car,
                executionContext);
    }
}

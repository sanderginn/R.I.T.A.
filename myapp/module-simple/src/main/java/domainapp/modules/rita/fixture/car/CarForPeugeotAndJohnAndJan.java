package domainapp.modules.rita.fixture.car;

import java.math.BigInteger;
import java.util.Arrays;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.fixture.driver.DriverForJanModaal;
import domainapp.modules.rita.fixture.driver.DriverForJohnDoe;

public class CarForPeugeotAndJohnAndJan extends CarAbstract {

    public static final String LICENSE_PLATE = "PVLF69";
    public static final BigInteger MILEAGE = BigInteger.valueOf(1000);

    @Override
    protected void execute(final ExecutionContext executionContext) {

        // prereqs
        executionContext.executeChild(this, new DriverForJohnDoe());
        executionContext.executeChild(this, new DriverForJanModaal());

        Car car = createCar(LICENSE_PLATE, MILEAGE, executionContext);
        addDrivers(car, Arrays.asList(DriverForJohnDoe.LAST_NAME, DriverForJanModaal.LAST_NAME));
    }

}

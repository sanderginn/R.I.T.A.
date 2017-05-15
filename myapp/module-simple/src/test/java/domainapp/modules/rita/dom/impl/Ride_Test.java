package domainapp.modules.rita.dom.impl;

import java.math.BigInteger;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.invoice.InvoiceRepository;
import domainapp.modules.rita.dom.ride.Ride;
import static org.assertj.core.api.Assertions.assertThat;

public class Ride_Test {
    private static Driver driver1;
    private static Driver driver2;
    private static Driver driver3;
    private static SortedSet<Ride> rides;

    @Before
    public void setUp() throws Exception {
        driver1 = new Driver("John", "Doe", "john@doe.io", "iban1");
        driver2 = new Driver("Mary", "Appleseed", "mary@appleseed.io", "iban2");
        driver3 = new Driver("Juan", "Perez", "juan@perez.io", "iban3");
        final Car car = new Car("PVLF69", BigInteger.valueOf(100));
        car.addDriver(driver1);
        car.addDriver(driver2);
        car.addDriver(driver3);

        final Ride ride1 = new Ride("ride 1",
                new LocalDate(2017, 1, 1),
                BigInteger.valueOf(100),
                BigInteger.valueOf(150),
                driver1,
                car);
        final Ride ride2 = new Ride("ride 2",
                new LocalDate(2017, 1, 2),
                BigInteger.valueOf(150),
                BigInteger.valueOf(175),
                driver2,
                car);
        final Ride ride3 = new Ride("ride 3",
                new LocalDate(2017, 1, 3),
                BigInteger.valueOf(175),
                BigInteger.valueOf(225),
                driver3,
                car);

        rides = new TreeSet<>();
        rides.add(ride1);
        rides.add(ride2);
        rides.add(ride3);

        assertThat(rides).hasSize(3);
    }

    public static class Ordering extends Ride_Test {

        @Test
        public void firstReturnsRide3() throws Exception {
            // then
            assertThat(rides.first().getDescription()).isEqualTo("ride 3");
        }

        @Test
        public void lastReturnsRide3() throws Exception {
            // then
            assertThat(rides.last().getDescription()).isEqualTo("ride 1");
        }

    }

    public static class CalculateKilometers extends Ride_Test {

        @Test
        public void kilometersPerDriver() throws Exception {
            // given
            final InvoiceRepository invoiceRepository = new InvoiceRepository();

            // when
            final Map<Driver, BigInteger> kilometersPerDriver = invoiceRepository.calculateKilometersPerDriver(rides);

            // then
            assertThat(kilometersPerDriver.get(driver1)).isEqualTo(BigInteger.valueOf(50));
            assertThat(kilometersPerDriver.get(driver2)).isEqualTo(BigInteger.valueOf(25));
            assertThat(kilometersPerDriver.get(driver3)).isEqualTo(BigInteger.valueOf(50));
        }

        @Test
        public void kilometersDrivenEquals125() throws Exception {
            // given
            final InvoiceRepository invoiceRepository = new InvoiceRepository();

            // when
            final BigInteger kilometersDriven = invoiceRepository.calculateTotalKilometersDriven(rides.first(), rides.last());

            // then
            assertThat(kilometersDriven).isEqualTo(BigInteger.valueOf(125));
        }
    }
}

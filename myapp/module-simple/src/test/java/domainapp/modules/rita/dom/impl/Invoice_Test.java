package domainapp.modules.rita.dom.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.invoice.InvoiceRepository;
import static org.assertj.core.api.Assertions.assertThat;

public class Invoice_Test {

    public static class CalculateBalancePerDriverSorted extends Invoice_Test {

        private Map<Driver, BigInteger> kilometersPerDriver;
        private Map<Driver, BigDecimal> expensesPerDriver;
        private BigDecimal costPerKilometer;
        private Driver driver1;
        private Driver driver2;
        private Driver driver3;

        @Before
        public void setUp() throws Exception {
            kilometersPerDriver = new HashMap<>();
            expensesPerDriver = new HashMap<>();

            driver1 = new Driver("John", "Doe", "john@doe.io", "iban1");
            driver2 = new Driver("Mary", "Appleseed", "mary@appleseed.io", "iban2");
            driver3 = new Driver("Juan", "Perez", "juan@perez.io", "iban3");

            kilometersPerDriver.put(driver1, BigInteger.valueOf(100));
            kilometersPerDriver.put(driver2, BigInteger.valueOf(300));
            kilometersPerDriver.put(driver3, BigInteger.valueOf(200));

            expensesPerDriver.put(driver1, BigDecimal.valueOf(0));
            expensesPerDriver.put(driver2, BigDecimal.valueOf(50));
            expensesPerDriver.put(driver3, BigDecimal.valueOf(25));

            costPerKilometer = BigDecimal.valueOf(75).divide(BigDecimal.valueOf(600), 2, BigDecimal.ROUND_CEILING);
            assertThat(costPerKilometer).isEqualTo(BigDecimal.valueOf(0.13));
        }

        @Test
        public void sortedCorrectly() throws Exception {
            // given
            final InvoiceRepository invoiceRepository = new InvoiceRepository();

            // when
            final Map<Driver, BigDecimal> sorted = invoiceRepository.calculateBalancePerDriverSorted(costPerKilometer, kilometersPerDriver, expensesPerDriver);

            // then
            assertThat(sorted.values()).containsExactly(
                    BigDecimal.valueOf(-1100, 2),
                    BigDecimal.valueOf(100, 2),
                    BigDecimal.valueOf(1300, 2));
            assertThat(sorted.keySet()).containsExactly(driver2, driver3, driver1);
        }

    }
}

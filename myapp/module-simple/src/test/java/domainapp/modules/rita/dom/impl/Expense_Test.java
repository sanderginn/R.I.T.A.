package domainapp.modules.rita.dom.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.expense.Expense;
import domainapp.modules.rita.dom.expense.ExpenseType;
import domainapp.modules.rita.dom.invoice.InvoiceRepository;
import static org.assertj.core.api.Assertions.assertThat;

public class Expense_Test {

    public static class Title extends Expense_Test {
        private Driver driver;
        private Car car;
        private Expense expense;

        @Before
        public void setUp() throws Exception {
            driver = new Driver("John", "Doe", "john@doe.io", "iban");
            car = new Car("PVLF69", BigInteger.valueOf(100));
            car.addDriver(driver);
            expense = new Expense(
                    ExpenseType.PETROL,
                    BigDecimal.valueOf(100),
                    new LocalDate(2017, 1, 1),
                    driver,
                    car);
        }

        @Test
        public void notSettled() throws Exception {
            // then
            assertThat(expense.title()).isEqualTo("Petrol > John Doe [2017-01-01, not settled]");
        }

        @Test
        public void settled() throws Exception {
            // when
            expense.setSettled(Boolean.TRUE);

            // then
            assertThat(expense.title()).isEqualTo("Petrol > John Doe [2017-01-01, settled]");
        }
    }

    public static class CalculateExpenses extends Expense_Test {
        private Driver driver1;
        private Driver driver2;
        private Driver driver3;
        private Car car;
        private List<Expense> expenses;

        @Before
        public void setUp() throws Exception {
            expenses = new ArrayList<>();

            driver1 = new Driver("John", "Doe", "john@doe.io", "iban1");
            driver2 = new Driver("Mary", "Appleseed", "mary@appleseed.io", "iban2");
            driver3 = new Driver("Juan", "Perez", "juan@perez.io", "iban3");
            car = new Car("PVLF69", BigInteger.valueOf(100));
            car.addDriver(driver1);
            car.addDriver(driver2);
            car.addDriver(driver3);

            Expense expense1 = new Expense(
                    ExpenseType.PETROL,
                    BigDecimal.valueOf(100),
                    new LocalDate(2017, 1, 1),
                    driver1,
                    car);

            Expense expense2 = new Expense(
                    ExpenseType.PETROL,
                    BigDecimal.valueOf(200),
                    new LocalDate(2017, 1, 2),
                    driver2,
                    car);

            Expense expense3 = new Expense(
                    ExpenseType.PETROL,
                    BigDecimal.valueOf(150),
                    new LocalDate(2017, 1, 3),
                    driver3,
                    car);

            expenses.add(expense1);
            expenses.add(expense2);
            expenses.add(expense3);

            assertThat(expenses).hasSize(3);
        }

        @Test
        public void expensesPerDriver() throws Exception {
            // given
            final InvoiceRepository invoiceRepository = new InvoiceRepository();

            // when
            final Map<Driver, BigDecimal> expensesPerDriver = invoiceRepository.calculateExpensesPerDriver(expenses);

            // then
            assertThat(expensesPerDriver.get(driver1)).isEqualTo(BigDecimal.valueOf(100));
            assertThat(expensesPerDriver.get(driver2)).isEqualTo(BigDecimal.valueOf(200));
            assertThat(expensesPerDriver.get(driver3)).isEqualTo(BigDecimal.valueOf(150));
        }

        @Test
        public void totalExpensesEquals450() throws Exception {
            // given
            final InvoiceRepository invoiceRepository = new InvoiceRepository();

            // when
            final BigDecimal totalExpenses = invoiceRepository.calculateTotalExpenses(expenses);

            // then
            assertThat(totalExpenses).isEqualTo(BigDecimal.valueOf(450));
        }
    }
}

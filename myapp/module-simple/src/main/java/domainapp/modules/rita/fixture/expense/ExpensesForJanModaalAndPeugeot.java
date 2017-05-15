package domainapp.modules.rita.fixture.expense;

import java.math.BigDecimal;

import org.joda.time.LocalDate;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.expense.ExpenseType;
import domainapp.modules.rita.fixture.car.CarForPeugeotAndJohnAndJan;
import domainapp.modules.rita.fixture.driver.DriverForJanModaal;

public class ExpensesForJanModaalAndPeugeot extends ExpenseAbstract {

    public static final BigDecimal COST_EXPENSE_THREE = BigDecimal.valueOf(50);

    @Override
    protected void execute(final ExecutionContext executionContext) {
        // prereqs
        executionContext.executeChild(this, new CarForPeugeotAndJohnAndJan());
        final Car car = carRepository.findByLicensePlate(CarForPeugeotAndJohnAndJan.LICENSE_PLATE);
        final Driver driver = driverRepository.findByLastName(DriverForJanModaal.LAST_NAME).get(0);

        createExpense(ExpenseType.PETROL,
                COST_EXPENSE_THREE,
                new LocalDate(2017, 1, 3),
                driver,
                car,
                executionContext);
    }
}

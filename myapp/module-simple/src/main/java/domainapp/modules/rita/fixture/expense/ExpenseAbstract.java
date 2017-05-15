package domainapp.modules.rita.fixture.expense;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.car.CarRepository;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.driver.DriverRepository;
import domainapp.modules.rita.dom.expense.Expense;
import domainapp.modules.rita.dom.expense.ExpenseRepository;
import domainapp.modules.rita.dom.expense.ExpenseType;

public abstract class ExpenseAbstract extends FixtureScript {

    protected Expense createExpense(
            final ExpenseType type,
            final BigDecimal cost,
            final LocalDate datePaid,
            final Driver paidBy,
            final Car car,
            final ExecutionContext fixtureResults) {
        Expense expense = expenseRepository.newExpense(type, cost, datePaid, paidBy, car);
        fixtureResults.addResult(this, expense);

        return expense;
    }

    @Inject
    protected CarRepository carRepository;

    @Inject
    protected DriverRepository driverRepository;

    @Inject
    protected ExpenseRepository expenseRepository;
}

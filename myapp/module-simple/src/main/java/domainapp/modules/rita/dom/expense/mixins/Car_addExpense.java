package domainapp.modules.rita.dom.expense.mixins;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.expense.ExpenseRepository;
import domainapp.modules.rita.dom.expense.ExpenseType;

@Mixin
public class Car_addExpense {

    private final Car car;

    public Car_addExpense(final Car car) {
        this.car = car;
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(contributed = Contributed.AS_ACTION)
    public Car $$(
            final ExpenseType type,
            final BigDecimal cost,
            final LocalDate datePaid,
            final Driver paidBy) {
        expenseRepository.newExpense(type, cost, datePaid, paidBy, car);

        return car;
    }

    public List<Driver> choices3$$(
            final ExpenseType type,
            final BigDecimal cost,
            final LocalDate datePaid,
            final Driver paidBy) {
        return car.getDrivers();
    }

    @Inject
    ExpenseRepository expenseRepository;
}

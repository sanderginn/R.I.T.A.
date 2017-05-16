package domainapp.modules.rita.dom.expense.mixins;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.expense.Expense;
import domainapp.modules.rita.dom.expense.ExpenseRepository;

@Mixin
public class Car_expenses {

    private final Car car;

    public Car_expenses(final Car car) {
        this.car = car;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION)
    public List<Expense> $$() {
        List<Expense> expenses = expenseRepository.findByCar(car);
        expenses.sort(Expense::compareTo);
        return expenses;
    }

    @Inject
    ExpenseRepository expenseRepository;

}

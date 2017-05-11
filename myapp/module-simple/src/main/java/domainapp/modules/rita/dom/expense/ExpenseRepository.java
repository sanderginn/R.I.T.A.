package domainapp.modules.rita.dom.expense;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.driver.Driver;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Expense.class)
public class ExpenseRepository {

    public Expense newExpense(
            final ExpenseType type,
            final BigDecimal cost,
            final LocalDate datePaid,
            final Driver paidBy,
            final Car car) {
        final Expense expense = new Expense(type, cost, datePaid, paidBy, car);
        serviceRegistry.injectServicesInto(expense);
        repositoryService.persist(expense);

        return expense;
    }

    public List<Expense> findByCar(final Car car) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Expense.class,
                        "findByCar",
                        "car", car
                ));
    }

    @Inject
    RepositoryService repositoryService;

    @Inject
    ServiceRegistry2 serviceRegistry;

}

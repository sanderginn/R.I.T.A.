package domainapp.modules.rita.dom.invoice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.expense.Expense;
import domainapp.modules.rita.dom.expense.ExpenseRepository;
import domainapp.modules.rita.dom.ride.Ride;
import domainapp.modules.rita.dom.ride.RideRepository;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Invoice.class)
public class InvoiceRepository {

    public Invoice newInvoice(
            final LocalDate invoiceDate,
            final Car car) {
        final Invoice invoice = new Invoice(invoiceDate, car);
        final List<Expense> expenses = expenseRepository.findByCarAndSettledStatus(car, Boolean.FALSE);
        final SortedSet<Ride> rides = new TreeSet<>(rideRepository.findByCarAndSettledStatus(car, Boolean.FALSE));
        final SortedSet<Transaction> transactions = createTransactions(expenses, rides, invoice);

        serviceRegistry.injectServicesInto(invoice);
        repositoryService.persist(invoice);

        expenses.forEach(expense -> {
            expense.setSettled(Boolean.TRUE);
            expense.setSettledOnInvoice(invoice);
        });

        rides.forEach(ride -> {
            ride.setSettled(Boolean.TRUE);
            ride.setSettledOnInvoice(invoice);
        });

        return invoice;
    }

    @Programmatic
    private SortedSet<Transaction> createTransactions(
            final List<Expense> expenses,
            final SortedSet<Ride> rides,
            final Invoice invoice) {
        final SortedSet<Transaction> transactions = new TreeSet<>();

        final BigInteger totalAmountOfKilometers = calculateTotalKilometersDriven(rides.first(), rides.last());
        final BigDecimal totalAmountOfExpenses = calculateTotalExpenses(expenses);
        final BigDecimal costPerKilometer = totalAmountOfExpenses.divide(new BigDecimal(totalAmountOfKilometers), 6, BigDecimal.ROUND_CEILING);

        final Map<Driver, BigInteger> kilometersPerDriver = calculateKilometersPerDriver(rides);
        final Map<Driver, BigDecimal> expensesPerDriver = calculateExpensesPerDriver(expenses);
        final TreeMap<Driver, BigDecimal> balancePerDriver = calculateBalancePerDriverSorted(costPerKilometer, kilometersPerDriver, expensesPerDriver);

        Set<Driver> keys = balancePerDriver.keySet();
        for (Driver key : keys) {
            while (balancePerDriver.get(key).compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal currentBalance = balancePerDriver.get(key);
                Driver highestDebtDriver = balancePerDriver.lastKey();
                BigDecimal highestDebt = balancePerDriver.get(highestDebtDriver);

                // highest debt is lower than current credit
                if (currentBalance.abs().compareTo(highestDebt) > 0) {
                    transactions.add(transactionRepository.newTransaction(highestDebtDriver, key, highestDebt, invoice));

                    balancePerDriver.replace(key, currentBalance.add(highestDebt));
                    balancePerDriver.replace(highestDebtDriver, BigDecimal.ZERO);
                } else { // highest debt is higher than or equal to current credit
                    transactions.add(transactionRepository.newTransaction(highestDebtDriver, key, currentBalance.negate(), invoice));

                    balancePerDriver.replace(key, BigDecimal.ZERO);
                    balancePerDriver.replace(highestDebtDriver, highestDebt.subtract(currentBalance.negate()));
                }
            }
        }

        return transactions;
    }

    @Programmatic
    public BigInteger calculateTotalKilometersDriven(final Ride end, final Ride start) {
        return end.getNewMileage()
                .subtract(start.getOldMileage());
    }

    @Programmatic
    public BigDecimal calculateTotalExpenses(final List<Expense> expenses) {
        return expenses.stream()
                .map(Expense::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Programmatic
    public Map<Driver, BigInteger> calculateKilometersPerDriver(SortedSet<Ride> rides) {
        final Map<Driver, BigInteger> kilometersPerDriver = new HashMap<>();
        rides.forEach(ride -> kilometersPerDriver.compute(
                ride.getDriver(),
                (k, v) -> v == null ? ride.getDistanceTraveled() : v.add(ride.getDistanceTraveled()))
        );

        return kilometersPerDriver;
    }

    @Programmatic
    public Map<Driver, BigDecimal> calculateExpensesPerDriver(final List<Expense> expenses) {
        final Map<Driver, BigDecimal> expensesPerDriver = new HashMap<>();
        expenses.forEach(expense -> expensesPerDriver.compute(
                expense.getPaidBy(),
                (k, v) -> v == null ? expense.getCost() : v.add(expense.getCost()))
        );

        return expensesPerDriver;
    }

    // TODO: test
    @Programmatic
    public TreeMap<Driver, BigDecimal> calculateBalancePerDriverSorted(
            final BigDecimal costPerKilometer,
            final Map<Driver, BigInteger> kilometersPerDriver,
            final Map<Driver, BigDecimal> expensesPerDriver) {
        final Map<Driver, BigDecimal> balancePerDriver = new HashMap<>();

        kilometersPerDriver.entrySet()
                .forEach(entry -> balancePerDriver.put(
                        entry.getKey(),
                        new BigDecimal(entry.getValue()).multiply(costPerKilometer).setScale(2, BigDecimal.ROUND_HALF_EVEN))
                );

        expensesPerDriver.entrySet()
                .forEach(entry -> {
                    Driver driver = entry.getKey();

                    if (!balancePerDriver.containsKey(driver)) {
                        balancePerDriver.put(
                                driver,
                                entry.getValue().negate());
                    } else {
                        BigDecimal oldValue = balancePerDriver.get(driver);
                        balancePerDriver.put(
                                driver,
                                oldValue.subtract(entry.getValue()));
                    }
                });

        return sortByValue(balancePerDriver);
    }

    @Programmatic
    private TreeMap sortByValue(Map unsortedMap) {
        TreeMap sortedMap = new TreeMap(new ValueComparator(unsortedMap));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }

    class ValueComparator implements Comparator {
        Map map;

        public ValueComparator(Map map) {
            this.map = map;
        }

        public int compare(Object keyA, Object keyB) {
            Comparable valueA = (Comparable) map.get(keyA);
            Comparable valueB = (Comparable) map.get(keyB);
            return -valueB.compareTo(valueA);
        }
    }

    @Inject
    RepositoryService repositoryService;

    @Inject
    ServiceRegistry2 serviceRegistry;

    @Inject
    RideRepository rideRepository;

    @Inject
    ExpenseRepository expenseRepository;

    @Inject
    TransactionRepository transactionRepository;

}

package domainapp.modules.rita.dom.invoice;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.rita.dom.driver.Driver;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Transaction.class)
public class TransactionRepository {

    public Transaction newTransaction(
            final Driver debtor,
            final Driver creditor,
            final BigDecimal amount,
            final Invoice invoice) {
        final Transaction transaction = new Transaction(debtor, creditor, amount, invoice);
        serviceRegistry.injectServicesInto(transaction);
        repositoryService.persist(transaction);

        return transaction;
    }

    public List<Transaction> findByInvoice(final Invoice invoice) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Transaction.class,
                        "findByInvoice",
                        "invoice", invoice
                ));
    }

    @Inject
    ServiceRegistry2 serviceRegistry;

    @Inject
    RepositoryService repositoryService;

}

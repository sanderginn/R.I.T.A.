package domainapp.modules.rita.dom.invoice.mixins;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.modules.rita.dom.invoice.Invoice;
import domainapp.modules.rita.dom.invoice.Transaction;
import domainapp.modules.rita.dom.invoice.TransactionRepository;

@Mixin
public class Invoice_transactions {

    private final Invoice invoice;

    public Invoice_transactions(final Invoice invoice) {
        this.invoice = invoice;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION)
    public SortedSet<Transaction> $$() {
        return new TreeSet<>(transactionRepository.findByInvoice(invoice));
    }

    @Inject
    TransactionRepository transactionRepository;

}

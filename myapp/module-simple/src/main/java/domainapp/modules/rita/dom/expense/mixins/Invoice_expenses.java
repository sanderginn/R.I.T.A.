package domainapp.modules.rita.dom.expense.mixins;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.modules.rita.dom.expense.Expense;
import domainapp.modules.rita.dom.expense.ExpenseRepository;
import domainapp.modules.rita.dom.invoice.Invoice;

@Mixin
public class Invoice_expenses {

    private final Invoice invoice;

    public Invoice_expenses(final Invoice invoice) {
        this.invoice = invoice;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION)
    public SortedSet<Expense> $$() {
        return new TreeSet<>(expenseRepository.findByInvoice(invoice));
    }

    @Inject
    ExpenseRepository expenseRepository;

}

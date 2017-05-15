package domainapp.modules.rita.dom.ride.mixins;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.modules.rita.dom.invoice.Invoice;
import domainapp.modules.rita.dom.ride.Ride;
import domainapp.modules.rita.dom.ride.RideRepository;

@Mixin
public class Invoice_rides {

    private final Invoice invoice;

    public Invoice_rides(final Invoice invoice) {
        this.invoice = invoice;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION)
    public SortedSet<Ride> $$() {
        return new TreeSet<>(rideRepository.findByInvoice(invoice));
    }

    @Inject
    RideRepository rideRepository;

}

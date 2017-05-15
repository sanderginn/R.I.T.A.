package domainapp.modules.rita.dom.invoice.mixins;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.clock.ClockService;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.invoice.Invoice;
import domainapp.modules.rita.dom.invoice.InvoiceRepository;

@Mixin
public class Car_createInvoice {

    private final Car car;

    public Car_createInvoice(final Car car) {
        this.car = car;
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
    public Invoice $$() {
        return invoiceRepository.newInvoice(clockService.now(), car);
    }

    @Inject
    InvoiceRepository invoiceRepository;

    @Inject
    ClockService clockService;
}

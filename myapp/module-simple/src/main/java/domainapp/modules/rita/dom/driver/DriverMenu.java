package domainapp.modules.rita.dom.driver;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "rita.DriverMenu",
        repositoryFor = Driver.class)
@DomainServiceLayout(
        named = "Drivers",
        menuOrder = "10")
public class DriverMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @MemberOrder(sequence = "1")
    public List<Driver> listAll() {
        return driverRepository.listAll();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @MemberOrder(sequence = "2")
    public List<Driver> findByLastName(final String lastName) {
        return driverRepository.findByLastName(lastName);
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @MemberOrder(sequence = "3")
    public Driver newDriver(
            final String firstName,
            final String lastName,
            final String email,
            final String iban) {
        return driverRepository.newDriver(firstName, lastName, email, iban);
    }

    @Inject
    DriverRepository driverRepository;
}

package domainapp.modules.rita.dom.invoice;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;

import org.incode.module.base.dom.utils.TitleBuilder;

import domainapp.modules.rita.dom.car.Car;
import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        schema = "rita"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column = "id")
@javax.jdo.annotations.Version(
        strategy = VersionStrategy.DATE_TIME,
        column = "version")
@Queries({

})
@DomainObject
@DomainObjectLayout(cssClassFa = "fa-file-text")
public class Invoice {

    public String title() {
        return TitleBuilder.start()
                .withName(getInvoiceDate())
                .toString();
    }

    public Invoice(
            final LocalDate invoiceDate,
            final Car car) {
        this.invoiceDate = invoiceDate;
        this.car = car;
    }

    @Column(allowsNull = "false")
    @Getter @Setter
    private LocalDate invoiceDate;

    @Column(allowsNull = "false")
    @Getter @Setter
    private Car car;

}

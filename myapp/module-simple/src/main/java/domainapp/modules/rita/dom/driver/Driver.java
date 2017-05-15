package domainapp.modules.rita.dom.driver;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;

import org.incode.module.base.dom.utils.TitleBuilder;

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
        @Query(
                name = "findByLastName",
                value = "SELECT "
                        + "FROM domainapp.modules.rita.dom.driver.Driver "
                        + "WHERE lastName.indexOf(:lastName) >= 0"),
        @Query(
                name = "findByEmail",
                value = "SELECT "
                        + "FROM domainapp.modules.rita.dom.driver.Driver "
                        + "WHERE email == :email")
})
//TODO: add uniques
@Uniques({
        @Unique()
})
@DomainObject
@DomainObjectLayout(cssClassFa = "fa-user")
public class Driver {

    public String title() {
        return TitleBuilder.start()
                .withName(getFirstName())
                .withName(getLastName())
                .toString();
    }

    public Driver(
            final String firstName,
            final String lastName,
            final String email,
            final String iban) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setIban(iban);
    }

    @Column(allowsNull = "false")
    @Getter @Setter
    private String firstName;

    @Column(allowsNull = "false")
    @Getter @Setter
    private String lastName;

    @Column(allowsNull = "false")
    @Getter @Setter
    private String email;

    @Column(allowsNull = "false")
    @Getter @Setter
    private String iban;

    private Driver change(
            @Parameter(optionality = Optionality.OPTIONAL) final String firstName,
            @Parameter(optionality = Optionality.OPTIONAL) final String lastName,
            @Parameter(optionality = Optionality.OPTIONAL) final String email,
            @Parameter(optionality = Optionality.OPTIONAL) final String iban) {
        if (firstName != null) {
            setFirstName(firstName);
        }

        if (lastName != null) {
            setLastName(lastName);
        }

        if (email != null) {
            setEmail(email);
        }

        if (iban != null) {
            setIban(iban);
        }

        return this;
    }

    @Override
    public String toString() {
        return firstName + ' ' + lastName;
    }
}

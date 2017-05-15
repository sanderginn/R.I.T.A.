package domainapp.modules.rita.dom.car;

import java.math.BigInteger;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "rita.CarMenu",
        repositoryFor = Car.class)
@DomainServiceLayout(
        named = "Cars",
        menuOrder = "20")
public class CarMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @MemberOrder(sequence = "1")
    public List<Car> listAll() {
        return carRepository.listAll();
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @MemberOrder(sequence = "2")
    public Car findOrCreateCar(final String licensePlate, final BigInteger mileage) {
        return carRepository.findOrCreateCar(licensePlate, mileage);
    }

    public String validateFindOrCreateCar(final String licensePlate, final BigInteger mileage) {
        String licensePlateFiltered = licensePlate.replace("-", "").toUpperCase();

        Pattern[] licensePatterns = new Pattern[14];
        licensePatterns[0] = Pattern.compile("^[a-zA-Z]{2}[\\d]{2}[\\d]{2}$"); // 1 XX-99-99
        licensePatterns[1] = Pattern.compile("^[\\d]{2}[\\d]{2}[a-zA-Z]{2}$"); // 2 99-99-XX
        licensePatterns[2] = Pattern.compile("^[\\d]{2}[a-zA-Z]{2}[\\d]{2}$"); // 3 99-XX-99
        licensePatterns[3] = Pattern.compile("^[a-zA-Z]{2}[\\d]{2}[a-zA-Z]{2}$"); // 4 XX-99-XX
        licensePatterns[4] = Pattern.compile("^[a-zA-Z]{2}[a-zA-Z]{2}[\\d]{2}$"); // 5 XX-XX-99
        licensePatterns[5] = Pattern.compile("^[\\d]{2}[a-zA-Z]{2}[a-zA-Z]{2}$"); // 6 99-XX-XX
        licensePatterns[6] = Pattern.compile("^[\\d]{2}[a-zA-Z]{3}[\\d]{1}$"); // 7 99-XXX-9
        licensePatterns[7] = Pattern.compile("^[\\d]{1}[a-zA-Z]{3}[\\d]{2}$"); // 8 9-XXX-99
        licensePatterns[8] = Pattern.compile("^[a-zA-Z]{2}[\\d]{3}[a-zA-Z]{1}$"); // 9 XX-999-X
        licensePatterns[9] = Pattern.compile("^[a-zA-Z]{1}[\\d]{3}[a-zA-Z]{2}$"); // 10 X-999-XX
        licensePatterns[10] = Pattern.compile("/^[a-zA-Z]{3}[\\d]{2}[a-zA-Z]{1}$"); // 11 XXX-99-X
        licensePatterns[11] = Pattern.compile("/^[a-zA-Z]{1}[\\d]{2}[a-zA-Z]{3}$"); // 12 X-99-XXX
        licensePatterns[12] = Pattern.compile("/^[\\d]{1}[a-zA-Z]{2}[\\d]{3}$"); // 13 9-XX-999
        licensePatterns[13] = Pattern.compile("/^[\\d]{3}[a-zA-Z]{2}[\\d]{1}$"); // 14 999-XX-9

        for (Pattern pattern : licensePatterns) {
            if (pattern.matcher(licensePlateFiltered).matches()) {
                return null;
            }
        }

        return "Incorrect license plate";
    }

    @Inject
    CarRepository carRepository;
}

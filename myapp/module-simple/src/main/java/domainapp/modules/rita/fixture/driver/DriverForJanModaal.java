package domainapp.modules.rita.fixture.driver;

public class DriverForJanModaal extends DriverAbstract {

    public static final String FIRST_NAME = "Jan";
    public static final String LAST_NAME = "Modaal";
    public static final String EMAIL = "jan@modaal.nl";
    public static final String IBAN = "NL44RABO0123456789";

    @Override
    protected void execute(final ExecutionContext executionContext) {
        createDriver(FIRST_NAME, LAST_NAME, EMAIL, IBAN, executionContext);
    }
}

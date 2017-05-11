package domainapp.modules.rita.fixture.driver;

public class DriverForJohnDoe extends DriverAbstract {

    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String EMAIL = "john@doe.io";
    public static final String IBAN = "NL91ABNA0417164300";

    @Override
    protected void execute(final ExecutionContext executionContext) {
        createDriver(FIRST_NAME, LAST_NAME, EMAIL, IBAN, executionContext);
    }
}

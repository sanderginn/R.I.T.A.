package domainapp.modules.rita.dom.expense;

public enum ExpenseType {

    PETROL,
    MAINTENANCE,
    TAXES,
    INSURANCE;

    public String toNormalCase() {
        return name().substring(0, 1) + name().toLowerCase().substring(1);
    }
}

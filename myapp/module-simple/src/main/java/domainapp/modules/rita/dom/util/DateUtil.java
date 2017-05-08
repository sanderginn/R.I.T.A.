package domainapp.modules.rita.dom.util;

import org.joda.time.LocalDate;

public class DateUtil {

    public static LocalDate parseRdwDate(final String unparsed) {
        CharSequence filteredDate = unparsed.subSequence(6, unparsed.length() - 2);
        return new LocalDate(Long.parseLong(filteredDate.toString()));
    }
}

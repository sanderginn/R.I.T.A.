package domainapp.modules.rita.dom.util;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public final class IBANValidator {

    private IBANValidator() {
    }

    private static final int IBAN_LENGTH_MAX = 32;

    private static final int IBAN_LENGTH_MIN = 15;

    private static final int SUFFIX_OFFSET = 4;

    private static final int A_ASCII = 97;
    private static final int BASE_TEN = 10;

    public static int checksum(final String iban) {
        String tmp = (iban.substring(SUFFIX_OFFSET) + iban.substring(0, SUFFIX_OFFSET)).toUpperCase();
        StringBuffer digits = new StringBuffer();
        for (int i = 0; i < tmp.length(); i++) {
            char c = tmp.charAt(i);
            if (c >= '0' && c <= '9') {
                digits.append(c);
            } else if (c >= 'A' && c <= 'Z') {
                int n = c - 'A' + BASE_TEN;
                digits.append((char) ('0' + n / BASE_TEN));
                digits.append((char) ('0' + (n % BASE_TEN)));
            } else {
                return -1;
            }
        }
        BigDecimal n = new BigDecimal(digits.toString());
        return n.remainder(BigDecimal.valueOf(A_ASCII)).intValue();
    }

    public static String fixChecksum(final String ibanTemplate) {
        int remainder = checksum(ibanTemplate);
        String pp = StringUtils.leftPad(String.valueOf(1 + A_ASCII - remainder), 2, '0');
        return ibanTemplate.substring(0, 2) + pp + ibanTemplate.substring(SUFFIX_OFFSET);
    }

    public static boolean valid(final String iban) {
        if (iban == null ||
                iban.length() < IBAN_LENGTH_MIN ||
                iban.length() > IBAN_LENGTH_MAX) {
            return false;
        }

        final int checksum = checksum(iban);
        return (checksum == 1);
    }

}
package biblioteca;

public class ISBNValidator {

	// https://github.com/SenhW/isbn-validator/blob/master/ISBNValidator.java
	// (10*x1+9*x2+8*x3+7*x4+6*x5+5*x6+4*x7+3*x8+2*x9+x10)* mod 11 = 0.
	private static boolean validadorISBN10(String ISBN10) {
		ISBN10 = ISBN10.replace("-", "").trim();
		if (ISBN10.length() == 10 && ISBN10.matches("[0-9]+")) {
			int soma = 0;
			for (int i = 10; i > 0; i--) {
				if (ISBN10.charAt(9) == 'X' && i == 1) {
					soma += i * 10;
				} else {
					soma += i * Character.getNumericValue(ISBN10.charAt(10 - i));
				}
			}
			if (soma % 11 == 0) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	// https://www.moreofless.co.uk/validate-isbn-13-java
	// x13 = 10-((x1+3*x2+x3+3*x4+x5+3*x6+x7+3*x8+x9+3*x10+x11+3x12 ) mod 10)
	private static boolean validadorISBN13(String ISBN13) {
		if (ISBN13 == null) {
			return false;
		} // remove hifes
		ISBN13 = ISBN13.replaceAll("-", ""); // deve ter 13 digitos ISBN13
		if (ISBN13.length() != 13) {
			// Chama o validador do 10. Caso tambem nao sejam retorna falso.
			return validadorISBN10(ISBN13);
		}
		try {
			int tot = 0;
			for (int i = 0; i < 12; i++) {
				int digit = Integer.parseInt(ISBN13.substring(i, i + 1));
				tot += (i % 2 == 0) ? digit * 1 : digit * 3;
			} // checksum must be 0-9. If calculated as 10 then = 0
			int checksum = 10 - (tot % 10);
			if (checksum == 10) {
				checksum = 0;
			}
			return checksum == Integer.parseInt(ISBN13.substring(12));
		} catch (NumberFormatException nfe) {
			// to catch invalid ISBNs that have non-numeric characters in them
			return false;
		}
	}

	public static boolean validadorISBN(String ISBN) {
		return validadorISBN13(ISBN);
	}
}
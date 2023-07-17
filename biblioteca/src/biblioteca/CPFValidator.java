package biblioteca;

public class CPFValidator {

	public static boolean validarCPF(String cpf) {
		cpf = cpf.replace(".", "").replace("-", "").trim();
		int soma = 0;

		for (int i = 0; i < 9; i++) {
			soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
		}
		int resto = soma % 11;
		if (resto < 2) {
			if (Character.getNumericValue(cpf.charAt(9)) != 0)
				return false;
		} else {
			if (Character.getNumericValue(cpf.charAt(9)) != 11 - resto)
				return false;
		}

		soma = 0;

		for (int i = 0; i < 10; i++) {
			soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
		}

		resto = soma % 11;

		if (resto < 2) {
			if (Character.getNumericValue(cpf.charAt(10)) != 0) {
				return false;
			}
		} else {
			if (Character.getNumericValue(cpf.charAt(10)) != 11 - resto) {
				return false;
			}
		}
		return true;
	}

}
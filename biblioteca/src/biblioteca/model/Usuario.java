package biblioteca.model;

public class Usuario {

	private int id;

	private String nome;

	private String CPF;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCPF() {
		return CPF;
	}

	public void setCPF(String cPF) {
		this.CPF = cPF;
	}

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

	// Yan
	public static boolean validacaoCPF(String cpf) {
		int cont = 10, cont2 = 11;
		int resto1, soma1 = 0;
		int resto2, soma2 = 0;

		if (cpf == null) {
			return true;
		}
		for (int i = 0; i < 14; i++) {
			cpf = cpf.replace(".", "").replace("-", "").trim();
			// cpf = cpf.replace(".", "");
		}

		for (int i = 0; i < 9; i++) {
			soma1 += cont * Character.getNumericValue(cpf.charAt(i));
			cont--; // ao inves de usar cont, poderia usar (10-i)
		}

		resto1 = soma1 % 11;
		if (resto1 < 2) {
			if (Character.getNumericValue(cpf.charAt(9)) != 0) {
				return false;
			}
		} else {
			if (Character.getNumericValue(cpf.charAt(9)) != (11 - resto1)) {
				return false;
			}
		}

		for (int i = 0; i < 10; i++) {
			soma2 += cont2 * Character.getNumericValue(cpf.charAt(i)); // TODO: O CONT2 ERA CONT APENAS
			cont2--; // ao inves de usar cont, poderia usar (11-i)
		}

		resto2 = soma2 % 11;
		if (resto2 < 2) {
			if (Character.getNumericValue(cpf.charAt(10)) != 0) {
				return false;
			}
		} else {
			if (Character.getNumericValue(cpf.charAt(10)) != (11 - resto2)) {
				return false;
			}
		}

		return true;
		// TODO: REFATORAR AS FUNÇÕES JOGANDO NUMA FUNÇÃO PRIVADA
	}

}
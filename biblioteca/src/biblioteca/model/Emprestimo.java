package biblioteca.model;

import java.time.LocalDate;

public class Emprestimo {
	// https://www.w3schools.com/java/java_date.asp

	private int id;
	private int prazoEmprestimo = 7;
	private LocalDate dataEmprestimo;
	private LocalDate dataDevolucao;
	private Usuario usuario;
	private Livro livro;
	private Boolean emprestado = true;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(LocalDate data) {
		this.dataEmprestimo = data;
	}

	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public Boolean getEmprestado() {
		return emprestado;
	}

	public void setEmprestado(Boolean emprestado) {
		this.emprestado = emprestado;
	}

	public int getPrazoEmprestimo() {
		return prazoEmprestimo;
	}

	public void setPrazoEmprestimo(int prazoEmprestimo) {
		this.prazoEmprestimo = prazoEmprestimo;
	}

}

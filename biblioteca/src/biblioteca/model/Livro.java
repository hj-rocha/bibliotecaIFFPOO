package biblioteca.model;

public class Livro {

	private int id;
	private String ISBN;
	private String titulo;
	private String Categoria;
	private Editora editora;
	private Boolean disponivelParaEmprestimo = true;

	public Livro() {

	}

	public Livro(String iSBN, String titulo, String categoria, Editora editora) {
		super();
		ISBN = iSBN;
		this.titulo = titulo;
		Categoria = categoria;
		this.editora = editora;
	}

	public Livro(String iSBN, String titulo, String categoria, Editora editora, Boolean disponivelParaEmprestimo) {
		super();
		ISBN = iSBN;
		this.titulo = titulo;
		Categoria = categoria;
		this.editora = editora;
		this.disponivelParaEmprestimo = disponivelParaEmprestimo;
	}

	public Livro(int id, String iSBN, String titulo, String categoria, Editora editora,
			Boolean disponivelParaEmprestimo) {
		super();
		this.id = id;
		this.ISBN = iSBN;
		this.titulo = titulo;
		this.Categoria = categoria;
		this.editora = editora;
		this.disponivelParaEmprestimo = disponivelParaEmprestimo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEditora(String nomeEditora) {
		Editora editora = new Editora();
		editora.setNome(nomeEditora);
		this.editora = editora;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		this.ISBN = iSBN;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCategoria() {
		return Categoria;
	}

	public void setCategoria(String categoria) {
		Categoria = categoria;
	}

	public Editora getEditora() {
		return editora;
	}

	public void setEditora(Editora editora) {
		this.editora = editora;
	}

	public Boolean getDisponivelParaEmprestimo() {
		return disponivelParaEmprestimo;
	}

	public void setDisponivelParaEmprestimo(Boolean disponivelParaEmprestimo) {
		this.disponivelParaEmprestimo = disponivelParaEmprestimo;
	}

}
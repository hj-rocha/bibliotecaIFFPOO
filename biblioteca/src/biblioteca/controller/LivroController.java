package biblioteca.controller;

import java.util.List;

import biblioteca.dao.EditoraDAO;
import biblioteca.dao.LivroDAO;
import biblioteca.model.Editora;
import biblioteca.model.Livro;

public class LivroController {

	LivroDAO livroDAO = new LivroDAO();
	EditoraDAO eDAO = new EditoraDAO();

	public Livro cadastrar(String titulo, String nomeEditora, String iSBN, String categoria) throws Exception {

		try {
			Editora editora = eDAO.consultarNome(nomeEditora);
			if (editora == null) {
				editora = new Editora(nomeEditora);
				eDAO.cadastrar(editora);
				editora = eDAO.consultarNome(nomeEditora);
			}
			if (livroDAO.consultarLivroPorIsbn(iSBN) != null) {
				throw new Exception("ISBN já cadastrado!");
			} else {
				Livro livro = new Livro(iSBN, titulo, categoria, editora);
				livroDAO.cadastrar(livro);
				return livro;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public Livro consultarLivroPorIsbn(String isbn) {
		return livroDAO.consultarLivroPorIsbn(isbn);
	}

	public void atualizar(Livro livro, String ISBNAtual) {
		livroDAO.atualizar(livro, ISBNAtual);
	}

	public void apagar(String isbn) {
		livroDAO.apagar(isbn);
	}

	public List<Livro> listar() {
		return listar();
	}
}

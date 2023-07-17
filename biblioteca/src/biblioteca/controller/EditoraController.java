package biblioteca.controller;

import biblioteca.dao.EditoraDAO;
import biblioteca.model.Editora;

public class EditoraController {

	EditoraDAO editoraDAO = new EditoraDAO();

	public Editora cadastrar(String editora) {
		return editoraDAO.cadastrar(editora);
	}
}

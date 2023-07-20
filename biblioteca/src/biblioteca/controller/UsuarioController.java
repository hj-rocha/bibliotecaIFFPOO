package biblioteca.controller;

import java.util.List;

import biblioteca.dao.UsuarioDAO;
import biblioteca.model.Usuario;

public class UsuarioController {

	UsuarioDAO usuarioDAO = new UsuarioDAO();

	public Usuario cadastrar(Usuario usuario) throws Exception {

		if (usuarioDAO.consultarPorCPF(usuario.getCPF()) != null) {
			usuario = usuarioDAO.cadastrar(usuario);
		} else {
			throw new Exception("CPF já cadastrado!");
		}
		return usuario;
	}

	public Usuario consultarPorCPF(String cpf) {
		return usuarioDAO.consultarPorCPF(cpf);
	}

	public void apagar(String cpf) {
		usuarioDAO.apagar(cpf);
	}

	public void atualizar(String CPFAtual, String nomeNovo, String CPFNovo) throws Exception {
		Usuario usuario = new Usuario();
		usuario.setCPF(CPFNovo);
		usuario.setNome(nomeNovo);
		usuarioDAO.atualizar(usuario, CPFAtual);
	}

	public List<Usuario> listar() {
		return usuarioDAO.listar();
	}
}
package biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.CPFValidator;
import biblioteca.JDBCConnection;
import biblioteca.model.Usuario;

public class UsuarioDAO {

	private Connection connection;

	public UsuarioDAO() {
		connection = (new JDBCConnection()).getConnection();
	}

	public Usuario cadastrar(Usuario usuario) throws Exception {
		if (!CPFValidator.validarCPF(usuario.getCPF())) {
			throw new Exception("CPF errado!");
		}
		if (existeUsuario(usuario)) {
			throw new Exception("CPF a cadastrado!");
		}
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO usuario (nome, cpf) VALUES (?, ?)");
			ps.setString(1, usuario.getNome());
			ps.setString(2, usuario.getCPF());
			ps.execute();
		} catch (Exception e) {

		}
		return usuario;
	}

	public Usuario consultarPorCPF(String cpf) {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM usuario WHERE cpf=?");
			ps.setString(1, cpf);
			ResultSet result = ps.executeQuery();
			if (!result.next()) {
				throw new Exception("Nenhum usuario cadastrada.");
			}
			List<Usuario> usuarios = new ArrayList<Usuario>();
			do {
				Usuario usuario = new Usuario();
				usuario.setId(result.getInt(1));
				usuario.setCPF(result.getString(3));
				usuario.setNome(result.getString(2));
				usuarios.add(usuario);
			} while (result.next());
			return usuarios.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void apagar(String cpf) {
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM usuario WHERE cpf = ?");
			ps.setString(1, cpf);
			ps.executeUpdate();
			System.out.println("Usuário removido com sucesso.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void atualizar(Usuario usuario, String CPFAtual) {

		try {
			if (Usuario.validarCPF(usuario.getCPF())) {
				PreparedStatement ps = connection
						.prepareStatement("UPDATE usuario SET cpf = ?, nome = ? WHERE cpf = ? ");
				ps.setString(1, usuario.getCPF());
				ps.setString(2, usuario.getNome());
				ps.setString(3, CPFAtual);
				System.out.println("Atualização concluida");
				ps.executeUpdate();
			} else {
				System.out.println("CPF inválido!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Boolean existeUsuario(Usuario u) {
		Usuario usuario = this.consultarPorCPF(u.getCPF());
		if (usuario.getCPF().equals(u.getCPF())) {
			return true;
		}
		return false;
	}

	public List<Usuario> listar() {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM usuario");
			ResultSet result = ps.executeQuery();
			if (!result.next()) {
				throw new Exception("Nenhum usuario cadastrada.");
			}
			List<Usuario> usuarios = new ArrayList<Usuario>();
			do {
				Usuario usuario = new Usuario();
				usuario.setCPF(result.getString(3));
				usuario.setNome(result.getString(2));
				usuarios.add(usuario);
			} while (result.next());
			return usuarios;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}

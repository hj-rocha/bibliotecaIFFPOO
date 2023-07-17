package biblioteca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.JDBCConnection;
import biblioteca.model.Editora;
import biblioteca.model.Livro;
import biblioteca.model.Usuario;

public class EditoraDAO {

	private Connection connection;

	public EditoraDAO() {
		connection = (new JDBCConnection()).getConnection();
	}

	public Editora cadastrar(Editora editora) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO editora (nome) VALUES (?)");
			ps.setString(1, editora.getNome());
			ps.execute();
		} catch (Exception e) {

		}
		return editora;
	}

	public Editora cadastrar(String nome) {
		Editora ed = new Editora(nome);
		return this.cadastrar(ed);
	}

	public Editora consultarNome(String nome) {

		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM editora WHERE nome = ?");
			ps.setString(1, nome);
			ResultSet rs = ps.executeQuery();

			rs.next();
			Editora ed = new Editora();
			ed.setNome(rs.getString("nome"));
			return ed;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}

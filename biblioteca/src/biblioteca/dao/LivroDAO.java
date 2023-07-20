package biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.ISBNValidator;
import biblioteca.JDBCConnection;
import biblioteca.model.Editora;
import biblioteca.model.Livro;

public class LivroDAO {

	private Connection connection;

	public LivroDAO() {
		connection = (new JDBCConnection()).getConnection();
	}

	public Livro cadastrar(Livro livro) {
		try {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO livro (titulo, ISBN, categoria, editora_id, disponivel_para_emprestimo) VALUES (?, ?, ?, ?, ?)");
			ps.setString(1, livro.getTitulo());
			ps.setString(1, livro.getISBN());
			ps.setString(1, livro.getCategoria());
			ps.setInt(1, livro.getEditora().getId());
			ps.setInt(1, 1);
			ps.execute();
		} catch (Exception e) {

		}
		return livro;
	}

	public Livro consultarLivroPorIsbn(String isbn) {
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement("SELECT * FROM livro WHERE ISBN = ? ");
			ps.setString(1, isbn);
			ResultSet rs = ps.executeQuery();
			rs.next();
			String livroISBN = rs.getString("ISBN");
			String livroTitulo = rs.getString("titulo");
			String livroCategoria = rs.getString("categoria");
			boolean livroDisponibilidade = (boolean) rs.getBoolean("disponivel_para_emprestimo");
			Editora ed = new Editora();
			ed.setId(rs.getInt("id"));
			Livro livro = new Livro(rs.getInt(1), livroISBN, livroTitulo, livroCategoria, ed, livroDisponibilidade);
			return livro;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public void atualizar(Livro livro, String ISBNAtual) throws Exception {
		PreparedStatement ps;
		try {
			// TODO: fazer a validação do ISBN antes da atualização dos dados
			if (ISBNValidator.validadorISBN(livro.getISBN())) {
				ps = connection.prepareStatement("UPDATE livro SET titulo = ?, ISBN= ?"
						+ "categoria = ?, editora_id = ?, dis3ponivel_para_emprestimo = ? WHERE ISBN = ? ");
				ps.setString(1, livro.getTitulo());
				ps.setString(2, livro.getISBN());
				ps.setString(3, livro.getCategoria());
				ps.setInt(4, livro.getEditora().getId());
				ps.setBoolean(5, livro.getDisponivelParaEmprestimo());
				ps.executeUpdate();
			} else {
				throw new Exception("ISBN inválido!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Boolean atualizarDisponibilidadeDoLivro(Boolean d, String ISBNAtual) throws Exception {
		PreparedStatement ps;
		try {
			if (ISBNValidator.validadorISBN(ISBNAtual)) {
				throw new Exception("ISBN inválido!");			
			}
			// TODO: fazer a validação do ISBN antes da atualização dos dados
			ps = connection.prepareStatement("UPDATE livro SET " + "disponivel_para_emprestimo = ? WHERE ISBN = ? ");
			ps.setBoolean(1, d);
			ps.setString(2, ISBNAtual);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void apagar(String isbn) {
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM livro WHERE ISBN = ?");
			ps.setString(1, isbn);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Livro> listar() {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM livro");
			ResultSet result = ps.executeQuery();
			if (!result.next()) {
				throw new Exception("Nenhum livro cadastrado.");
			}
			List<Livro> livros = new ArrayList<Livro>();
			do {
				Livro livro = new Livro();
				livro.setId(result.getInt(1));
				livro.setTitulo(result.getString(2));
				livro.setISBN(result.getString(3));
				livro.setCategoria(result.getString(4));
				Editora editora = new Editora();
				editora.setId(result.getInt(5));
				livro.setEditora(editora);
				livro.setDisponivelParaEmprestimo(result.getBoolean(6));
				livros.add(livro);
			} while (result.next());
			return livros;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}

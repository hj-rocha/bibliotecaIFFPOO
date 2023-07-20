package biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import biblioteca.JDBCConnection;
import biblioteca.model.Emprestimo;
import biblioteca.model.Livro;
import biblioteca.model.Usuario;

public class EmprestimoDAO {

	private Connection connection;

	private LivroDAO livroDAO = new LivroDAO();

	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	public EmprestimoDAO() {
		connection = (new JDBCConnection()).getConnection();
	}

	private Emprestimo cadastrar(Emprestimo emprestimo) {
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO emprestimo (prazo_emprestimo, data_emprestimo,"
							+ " usuario_id, livro_id, emprestado)" + " VALUES (?,?,?,?,?)");
			ps.setInt(1, emprestimo.getPrazoEmprestimo());
			ps.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
			ps.setInt(3, emprestimo.getUsuario().getId());
			ps.setInt(4, emprestimo.getLivro().getId());
			ps.setBoolean(5, emprestimo.getEmprestado());
			ps.execute();
		} catch (Exception e) {

		}
		return emprestimo;
	}

	public List<Emprestimo> consultarPorCPF(String cpf) {
		List<Emprestimo> listaEmprestimos = new ArrayList<Emprestimo>();

		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM emprestimo WHERE cpf = ?");
			ps.setString(1, cpf);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Emprestimo emprestimo = new Emprestimo();
				emprestimo.setId(rs.getInt("id"));
				emprestimo.setPrazoEmprestimo(rs.getInt("prazo_emprestimo"));
				emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
				emprestimo.setDataDevolucao(rs.getDate("data_deveolucao").toLocalDate());
				Usuario usuario = new Usuario();
				usuario.setId(rs.getInt("usuario_id"));
				emprestimo.setUsuario(usuario);
				Livro livro = new Livro();
				livro.setId(rs.getInt("livro_id"));
				emprestimo.setLivro(livro);
				emprestimo.setEmprestado(rs.getBoolean("emprestado"));
				listaEmprestimos.add(emprestimo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaEmprestimos;
	}

	public void apagar(String id) {
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM emprestimo WHERE id = ?");
			ps.setString(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Emprestimo> listar() {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM emprestimo");
			ResultSet result = ps.executeQuery();
			if (!result.next()) {
				throw new Exception("Nenhum emprestimo cadastrada.");
			}
			List<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
			do {
				Emprestimo emprestimo = new Emprestimo();
				emprestimo.setId(result.getInt(1));
				emprestimo.setPrazoEmprestimo(result.getInt(2));
				// toLocalDate converte a Date trazido do resultset para inserir o campo Date.
				emprestimo.setDataEmprestimo(result.getDate(3).toLocalDate());
				if (result.getDate(4) != null) {
					emprestimo.setDataDevolucao(result.getDate(4).toLocalDate());
				}
				Usuario u = new Usuario();
				u.setId(result.getInt(5));
				emprestimo.setUsuario(u);
				Livro l = new Livro();
				l.setId(result.getInt(6));
				emprestimo.setLivro(l);
				emprestimo.setEmprestado(result.getBoolean(7));
				emprestimos.add(emprestimo);
			} while (result.next());
			return emprestimos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String emprestarLivro(String ISBN, String CPF) throws Exception {

		if (usuarioTemEmprestimoAtrasado(CPF)) {
			return "Nao emprestado. Usuario com atrasos.";
		}
		if (!livroEstaDisponivelParaEmprestimo(ISBN)) {
			return "Nao emprestado. Livro nao disponivel para emprestimo.";
		}
		Livro livro = livroDAO.consultarLivroPorIsbn(ISBN);
		livro.setDisponivelParaEmprestimo(false);
		Usuario usuario = usuarioDAO.consultarPorCPF(CPF);
		Emprestimo emprestimo = new Emprestimo();
		emprestimo.setDataEmprestimo(LocalDate.now());
		emprestimo.setEmprestado(true);
		emprestimo.setLivro(livro);
		emprestimo.setUsuario(usuario);
		this.cadastrar(emprestimo);
		this.livroDAO.atualizarDisponibilidadeDoLivro(false, ISBN);
		return "Livro emprestado";
	}

	public String devolverLivro(String CPF, String ISBN) {

		try {
			PreparedStatement ps = connection.prepareStatement(
					"SELECT * FROM emprestimo" + " WHERE livro_id = ? AND usuario_id = ? AND emprestado = 1");
			int usuario_id = usuarioDAO.consultarPorCPF(CPF).getId();
			int livro_id = livroDAO.consultarLivroPorIsbn(ISBN).getId();
			ps.setInt(1, livro_id);
			ps.setInt(2, usuario_id);
			ResultSet result = ps.executeQuery();
			if (!result.next()) {
				throw new Exception("Nenhum emprestimo encontrado.");
			} else {
				ps = connection
						.prepareStatement("UPDATE emprestimo SET emprestado = 0, data_devolucao = ? " + "WHERE id = ?");
				ps.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
				ps.setInt(2, result.getInt(1));
				ps.executeUpdate();
				this.livroDAO.atualizarDisponibilidadeDoLivro(true, ISBN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "Livro devolvido";
	}

	private Boolean usuarioTemEmprestimoAtrasado(String CPF) {

		UsuarioDAO uDAO = new UsuarioDAO();
		try {
			Usuario u = uDAO.consultarPorCPF(CPF);
			PreparedStatement ps = connection
					.prepareStatement("SELECT * FROM emprestimo WHERE usuario_id=? AND emprestado=1");
			ps.setInt(1, u.getId());
			ps.executeQuery();
			ResultSet result = ps.executeQuery();
			List<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
			while (result.next()) {
				Emprestimo emp = new Emprestimo();
				emp.setId(result.getInt("id"));
				emp.setDataEmprestimo(result.getDate("data_emprestimo").toLocalDate());
				emprestimos.add(emp);
			}
			for (Emprestimo emprestimo2 : emprestimos) {
				Period periodo = Period.between(emprestimo2.getDataEmprestimo(), LocalDate.now());
				if (periodo.getDays() > emprestimo2.getPrazoEmprestimo()) {
					// Retorna true se o usuario tem um emprestimo atrasado.
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private Boolean livroEstaDisponivelParaEmprestimo(String ISBN) {
		Livro livro = livroDAO.consultarLivroPorIsbn(ISBN);
		if (livro.getDisponivelParaEmprestimo()) {
			return true;
		}
		return false;
	}

	public List<Emprestimo> consultarEmprestimosAtivos() {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM emprestimo WHERE emprestado = 1");
			ResultSet result = ps.executeQuery();
			if (!result.next()) {
				throw new Exception("Nenhum emprestimo cadastrado.");
			}
			List<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
			do {
				Emprestimo emprestimo = new Emprestimo();
				emprestimo.setId(result.getInt(1));
				emprestimo.setPrazoEmprestimo(result.getInt(2));
				// toLocalDate converte a Date trazido do resultset para inserir o campo Date.
				emprestimo.setDataEmprestimo(result.getDate(3).toLocalDate());
				if (result.getDate(4) != null) {
					emprestimo.setDataDevolucao(result.getDate(4).toLocalDate());
				}
				Usuario u = new Usuario();
				u.setId(result.getInt(5));
				emprestimo.setUsuario(u);
				Livro l = new Livro();
				l.setId(result.getInt(6));
				emprestimo.setLivro(l);
				emprestimo.setEmprestado(result.getBoolean(7));

				emprestimos.add(emprestimo);
			} while (result.next());
			return emprestimos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Emprestimo> consultarEmprestimosConcluidos() {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM emprestimo WHERE emprestado = 0");
			ResultSet result = ps.executeQuery();
			if (!result.next()) {
				throw new Exception("Nenhum emprestimo cadastrada.");
			}
			List<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
			do {
				Emprestimo emprestimo = new Emprestimo();
				emprestimo.setId(result.getInt(1));
				emprestimo.setPrazoEmprestimo(result.getInt(2));
				// toLocalDate converte a Date trazido do resultset para inserir o campo Date.
				emprestimo.setDataEmprestimo(result.getDate(3).toLocalDate());
				if (result.getDate(4) != null) {
					emprestimo.setDataDevolucao(result.getDate(4).toLocalDate());
				}
				Usuario u = new Usuario();
				u.setId(result.getInt(5));
				emprestimo.setUsuario(u);
				Livro l = new Livro();
				l.setId(result.getInt(6));
				emprestimo.setLivro(l);
				emprestimo.setEmprestado(result.getBoolean(7));

				emprestimos.add(emprestimo);
			} while (result.next());
			return emprestimos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Emprestimo> consultarEmprestimosAtrasados() {

		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM emprestimo");
			ResultSet result = ps.executeQuery();
			if (!result.next()) {
				throw new Exception("Nenhum emprestimo cadastrada.");
			}
			List<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
			do {
				Emprestimo emprestimo = new Emprestimo();
				emprestimo.setId(result.getInt(1));
				emprestimo.setPrazoEmprestimo(result.getInt(2));
				// toLocalDate converte a Date trazido do resultset para inserir o campo Date.
				emprestimo.setDataEmprestimo(result.getDate(3).toLocalDate());
				if (result.getDate(4) != null) {
					emprestimo.setDataDevolucao(result.getDate(4).toLocalDate());
				}
				Usuario u = new Usuario();
				u.setId(result.getInt(5));
				emprestimo.setUsuario(u);
				Livro l = new Livro();
				l.setId(result.getInt(6));
				emprestimo.setLivro(l);
				emprestimo.setEmprestado(result.getBoolean(7));

				emprestimos.add(emprestimo);
			} while (result.next());
			List<Emprestimo> emp = new ArrayList<Emprestimo>();
			for (Emprestimo emprestimo : emprestimos) {
				if (emprestimo.getEmprestado().equals(true)) {
					// https://www.devmedia.com.br/como-manipular-datas-com-o-java-8/34135
					Period periodo = Period.between(emprestimo.getDataEmprestimo(), LocalDate.now());
					if (periodo.getDays() > emprestimo.getPrazoEmprestimo()) {
						emp.add(emprestimo);
					}
				}
			}
			return emp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void atualizar(Emprestimo emprestimo) {

		try {
			PreparedStatement ps = connection.prepareStatement("UPDATE emprestimo SET prazo_emprestimo = ?"
					+ "data_emprestimo = ?, data_devolucao = ?, usuario_id = ?, livro_id = ?, emprestado = ? "
					+ " WHERE id = ? ");
			ps.setInt(1, emprestimo.getPrazoEmprestimo());
			ps.setDate(2, java.sql.Date.valueOf(emprestimo.getDataEmprestimo()));
			ps.setDate(3, java.sql.Date.valueOf(emprestimo.getDataDevolucao()));
			ps.setInt(4, emprestimo.getUsuario().getId());
			ps.setInt(5, emprestimo.getLivro().getId());
			ps.setBoolean(6, emprestimo.getEmprestado());
			ps.executeUpdate();

		} catch (

		SQLException e) {
			e.printStackTrace();
		}
	}

}

package biblioteca;

import java.util.List;
import java.util.Scanner;

import biblioteca.controller.EditoraController;
import biblioteca.controller.EmprestimoController;
import biblioteca.controller.LivroController;
import biblioteca.controller.UsuarioController;
import biblioteca.model.Emprestimo;
import biblioteca.model.Livro;
import biblioteca.model.Usuario;

public class Principal {

	public static void main(String[] args) throws Exception {

		Boolean continua = true;

		UsuarioController usuarioController = new UsuarioController();
		LivroController livroController = new LivroController();
		EditoraController editoraController = new EditoraController();
		EmprestimoController emprestimoController = new EmprestimoController();

		while (continua) {

			System.out.println("#### Sistema de Gerenciamento de Biblioteca #### \n");
			System.out.println("1 Consultar livro por ISBN");
			System.out.println("2 Consultar Emprestimos Ativos");
			System.out.println("3 Consultar Emprestimos Concluidos");
			System.out.println("4 Consultar Emprestimos Atrasados");
			System.out.println("5 Emprestar livro");
			System.out.println("6 Devolver livro");
			System.out.println("7 Cadastrar usuario");
			System.out.println("8 Listar usuarios");
			System.out.println("9 Cadastrar livro");
			System.out.println("10 Consultar Usuario");
			System.out.println("11 Listar Emprestimos");
			System.out.println("12 Apagar Usuario");
			System.out.println("13 Atualizar Usuario");
			System.out.println("14 Cadastrar editora.");
			System.out.println("15 Atualizar Livro");
			System.out.println("16 Apagar Livro");
			System.out.println("17 Listar Livros");
			System.out.println("16 Apagar Emprestimo");
			System.out.println("20 Sair");

			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			String opcao = scan.next();
			System.out.println("Sua opcao foi, " + opcao);

			if (opcao.equals("1")) {
				System.out.println("Digite o ISBN)");
				String isbn = scan.next();
				System.out.println("O livro buscado eh: " + livroController.consultarLivroPorIsbn(isbn).getTitulo());
			}
			if (opcao.equals("2")) {
				System.out.println("Emprestimos ativo");
				List<Emprestimo> emprestimos = emprestimoController.consultarEmprestimosAtivos();
				for (Emprestimo emprestimo : emprestimos) {
					System.out.println(emprestimo.getId() + " : " + emprestimo.getDataEmprestimo());
				}
			}
			if (opcao.equals("3")) {
				System.out.println("Emprestimos Concluidos");
				List<Emprestimo> emprestimos = emprestimoController.consultarEmprestimosConcluidos();
				for (Emprestimo emprestimo : emprestimos) {
					System.out.println(emprestimo.getId() + " : " + emprestimo.getDataEmprestimo());
				}
			}
			if (opcao.equals("4")) {
				System.out.println("Emprestimos Atrasados");
				List<Emprestimo> emprestimos = emprestimoController.consultarEmprestimosAtrasados();
				for (Emprestimo emprestimo : emprestimos) {
					System.out.println(emprestimo.getId() + " : " + emprestimo.getDataEmprestimo());
				}
			}

			if (opcao.equals("5")) {
				System.out.println("Digite o CPF do usuario");
				String CPF = scan.next();
				System.out.println("Digite o ISBN do livro");
				String ISBN = scan.next();
				System.out.println(emprestimoController.emprestarLivro(ISBN, CPF));
			}
			if (opcao.equals("6")) {
				System.out.println("Digite o CPF do usuario");
				String CPF = scan.next();
				System.out.println("Digite o ISBN do livro");
				String ISBN = scan.next();
				System.out.println(emprestimoController.devolverLivro(CPF, ISBN));
			}
			if (opcao.equals("7")) {
				System.out.println("Digite o nome do usuario");
				String nome = scan.next();
				System.out.println("Digite o CPF do usuario");
				String cpf = scan.next();
				Usuario u = new Usuario();
				u.setCPF(cpf);
				u.setNome(nome);
				u = usuarioController.cadastrar(u);
				System.out.println("Usuario cadastrado: " + u.getNome() + " " + u.getCPF());

			}
			if (opcao.equals("8")) {
				List<Usuario> us = usuarioController.listar();
				for (Usuario usuario : us) {
					System.out.println(usuario.getNome() + " : " + usuario.getCPF());
				}
			}
			if (opcao.equals("9")) {
				System.out.println("Digite o titulo do livro.");
				String titulo = scan.next();
				System.out.println("Digite o nome da editora.");
				String nomeEditora = scan.next();
				System.out.println("Digite o ISBN do livro.");
				String isbn = scan.next();
				System.out.println("Digite o Categoria do livro.");
				String categoria = scan.next();
				System.out.println(livroController.cadastrar(titulo, nomeEditora, isbn, categoria));
			}
			if (opcao.equals("10")) {
				// System.out.println("Digite o tipo de buscca (nome, CPF)");
				// String tipo = scan.next();
				System.out.println("Digite o CPF da busca");
				String valor = scan.next();
				Usuario u = usuarioController.consultarPorCPF(valor);
				System.out.println("Usuario encontrado:" + u.getCPF() + " " + u.getNome());
			}
			if (opcao.equals("20")) {
				System.out.println("Programa terminado.");
				continua = false;
			}
			if (opcao.equals("12")) {
				System.out.println("Digite o CPF sem pontos ou traços.");
				String cpf = scan.next();
				usuarioController.apagar(cpf);
				System.out.println("Usuário apgadao com sucesso.");
			}
			if (opcao.equals("13")) {
				System.out.println("Digite o CPF atual sem pontos ou traços.");
				String cpfAtual = scan.next();
				System.out.println("Digite o CPF novo sem pontos ou traços.");
				String cpfNovo = scan.next();
				System.out.println("Digite o novo nome.");
				String nomeNovo = scan.next();
				usuarioController.atualizar(cpfAtual, nomeNovo, cpfNovo);
				System.out.println("Usuário atualizado com sucesso.");

			}
			if (opcao.equals("14")) {
				System.out.println("Digite o nome da editora.");
				String nome = scan.next();
				System.out.println(editoraController.cadastrar(nome).getNome());
			}
			if (opcao.equals("11")) {
				List<Emprestimo> emprestimos = emprestimoController.listar();
				for (Emprestimo emprestimo : emprestimos) {
					System.out.println(emprestimo.getDataEmprestimo() + " : " + emprestimo.getLivro().getId() + " : "
							+ emprestimo.getUsuario().getId());
				}
			}
			if (opcao.equals("15")) {
				Livro livro = new Livro();
				String ISBNAtual = "";
				livroController.atualizar(livro, ISBNAtual);
			}
			if (opcao.equals("16")) {
				System.out.println("Digite o ISBN sem pontos ou traços.");
				String isbn = scan.next();
				livroController.apagar(isbn);
				System.out.println("Livro apagado com sucesso.");
			}
			if (opcao.equals("17")) {
				List<Livro> livros = livroController.listar();
				for (Livro livro : livros) {
					System.out.println(livro.getCategoria() + " : " + livro.getISBN() + " : " + livro.getTitulo());
				}
			}
			if (opcao.equals("18")) {
				System.out.println("Digite o id sem pontos ou traços.");
				String id = scan.next();
				emprestimoController.apagar(id);
				System.out.println("Emprestimo apagado com sucesso.");
			}

		}
	}
}

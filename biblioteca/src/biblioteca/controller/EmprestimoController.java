package biblioteca.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import biblioteca.dao.EmprestimoDAO;
import biblioteca.model.Emprestimo;

public class EmprestimoController {

	EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

	public String emprestarLivro(String ISBN, String CPF) throws Exception {
		return emprestimoDAO.emprestarLivro(ISBN, CPF);
	}

	public String devolverLivro(String CPF, String ISBN) {
		return emprestimoDAO.devolverLivro(CPF, ISBN);
	}

	public List<Emprestimo> consultarPorCPF(String cpf) {
		return emprestimoDAO.consultarPorCPF(cpf);
	}

	public void apagar(String cpf) {
		emprestimoDAO.apagar(cpf);
	}

	public void atualizar(Emprestimo emprestimo) {
		emprestimoDAO.atualizar(emprestimo);
	}

	public List<Emprestimo> listar() {
		return emprestimoDAO.listar();
	}

	public List<Emprestimo> consultarEmprestimosAtivos() {
		return emprestimoDAO.consultarEmprestimosAtivos();
	}

	public List<Emprestimo> consultarEmprestimosConcluidos() {
		return emprestimoDAO.consultarEmprestimosConcluidos();
	}

	public List<Emprestimo> consultarEmprestimosAtrasados() {
		return emprestimoDAO.consultarEmprestimosAtrasados();
	}

}
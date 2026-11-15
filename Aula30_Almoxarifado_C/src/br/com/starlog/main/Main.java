package br.com.starlog.main;

import br.com.starlog.dao.ProdutoDAO;
import br.com.starlog.dao.ProdutoDAOSqlite;
import br.com.starlog.factory.ConexaoFactory;
import br.com.starlog.model.Produto;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        new File(ConexaoFactory.ARQUIVO).delete();
        ProdutoDAO dao = new ProdutoDAOSqlite();

        System.out.println("### [Create] ###");
        Produto teclado = new Produto("Teclado Mecânico RGB", 359.00, 6);
        Produto mousepad = new Produto("Mousepad Speed XL", 49.90, 30);
        dao.salvar(teclado);
        dao.salvar(mousepad);
        System.out.println("Teclado recebeu ID " + teclado.getId() + " e Mousepad recebeu ID " + mousepad.getId());

        System.out.println("\n### [Read] ###");
        mostrar(dao.listarTodos());

        System.out.println("\n### [Update] ###");
        teclado.setEstoque(15);
        teclado.setPreco(289.90);
        if (dao.atualizar(teclado)) {
            System.out.println("Update OK (true). Agora: " + dao.buscarPorId(teclado.getId()).orElseThrow());
        } else {
            System.out.println("Update não afetou nenhuma linha.");
        }

        System.out.println("\n### [Delete] ###");
        System.out.println("Excluir Mousepad: " + dao.excluir(mousepad.getId()));
        boolean apagou = dao.excluir(888L);
        System.out.println("Excluir ID 888: " + apagou + (apagou ? "" : " (não existe, tudo certo)"));

        System.out.println("\n### [Final State] ###");
        List<Produto> restantes = dao.listarTodos();
        mostrar(restantes);
        System.out.println("Total no banco: " + restantes.size());
    }

    private static void mostrar(List<Produto> itens) {
        System.out.printf("%-3s %-24s %10s %8s%n", "ID", "NOME", "PREÇO", "ESTOQUE");
        System.out.println("=".repeat(48));
        itens.forEach(p -> System.out.printf("%-3d %-24s %10.2f %8d%n",
                p.getId(), p.getNome(), p.getPreco(), p.getEstoque()));
    }
}

package br.com.starlog.dao;

import br.com.starlog.factory.ConexaoFactory;
import br.com.starlog.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** CRUD SQLite com SQL em constantes. */
public class ProdutoDAOSqlite implements ProdutoDAO {

    private static final String CRIAR =
            "CREATE TABLE IF NOT EXISTS produtos (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nome TEXT NOT NULL, preco REAL NOT NULL, estoque INTEGER NOT NULL);";
    private static final String INSERIR = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?);";
    private static final String POR_ID = "SELECT id, nome, preco, estoque FROM produtos WHERE id = ?;";
    private static final String TODOS = "SELECT id, nome, preco, estoque FROM produtos ORDER BY nome ASC;";
    private static final String ATUALIZAR = "UPDATE produtos SET nome = ?, preco = ?, estoque = ? WHERE id = ?;";
    private static final String EXCLUIR = "DELETE FROM produtos WHERE id = ?;";

    public ProdutoDAOSqlite() {
        try (Connection conexao = ConexaoFactory.obterConexao();
             Statement comando = conexao.createStatement()) {
            comando.execute(CRIAR);
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao criar a tabela produtos: " + erro.getMessage(), erro);
        }
    }

    private Produto mapearLinha(ResultSet rs) throws SQLException {
        return new Produto(rs.getLong("id"), rs.getString("nome"), rs.getDouble("preco"), rs.getInt("estoque"));
    }

    @Override
    public void salvar(Produto produto) {
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(INSERIR, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPreco());
            ps.setInt(3, produto.getEstoque());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    produto.setId(chaves.getLong(1));
                }
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao salvar produto: " + erro.getMessage(), erro);
        }
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(POR_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearLinha(rs)) : Optional.empty();
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao buscar produto: " + erro.getMessage(), erro);
        }
    }

    @Override
    public List<Produto> listarTodos() {
        List<Produto> itens = new ArrayList<>();
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(TODOS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                itens.add(mapearLinha(rs));
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao listar catálogo: " + erro.getMessage(), erro);
        }
        return itens;
    }

    @Override
    public boolean atualizar(Produto produto) {
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(ATUALIZAR)) {
            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPreco());
            ps.setInt(3, produto.getEstoque());
            ps.setLong(4, produto.getId());
            int afetadas = ps.executeUpdate();
            return afetadas > 0;
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao atualizar produto: " + erro.getMessage(), erro);
        }
    }

    @Override
    public boolean excluir(Long id) {
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(EXCLUIR)) {
            ps.setLong(1, id);
            int afetadas = ps.executeUpdate();
            return afetadas > 0;
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao excluir produto: " + erro.getMessage(), erro);
        }
    }
}

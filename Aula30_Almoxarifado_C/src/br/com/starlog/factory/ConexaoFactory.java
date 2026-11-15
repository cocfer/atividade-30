package br.com.starlog.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexaoFactory {
    public static final String ARQUIVO = "almoxarifado.db";

    private ConexaoFactory() {
    }

    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + ARQUIVO);
    }
}

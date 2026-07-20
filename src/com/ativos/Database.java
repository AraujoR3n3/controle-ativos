package com.ativos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    // Caminho do banco SQLite
    private static final String URL = "jdbc:sqlite:ativos.db";

    /**
     * Conecta ao banco SQLite.
     * Caso as tabelas não existam, elas são criadas automaticamente.
     */
    public static Connection connect() {

        try {

            // Carrega o driver SQLite
            Class.forName("org.sqlite.JDBC");

            // Abre conexão
            Connection conn = DriverManager.getConnection(URL);

            Statement stmt = conn.createStatement();

            // Tabela principal dos ativos
            String sql = """
                    CREATE TABLE IF NOT EXISTS ativos (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                     
                         empresa TEXT,
                         cd TEXT,
                     
                         equipamento TEXT,
                         marca TEXT,
                         modelo TEXT,
                     
                         serial TEXT UNIQUE,
                         host TEXT,
                     
                         patrimonio TEXT UNIQUE,
                         local TEXT,
                     
                         status TEXT,
                         condicao TEXT,
                         situacao TEXT,
                     
                         observacoes TEXT,
                         responsavel TEXT
                     )
                    """;

            stmt.execute(sql);

            // Tabela de unidades
            String sqlUnidades = """
        CREATE TABLE IF NOT EXISTS unidades (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nome TEXT NOT NULL,
            empresa_id INTEGER,
            FOREIGN KEY (empresa_id)
                REFERENCES empresas(id)
        )
        """;

            stmt.execute(sqlUnidades);



            // Tabela de empresas
            String sqlEmpresas = """
        CREATE TABLE IF NOT EXISTS empresas (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nome TEXT NOT NULL UNIQUE
        )
        """;

            stmt.execute(sqlEmpresas);


            System.out.println("✅ Banco conectado");

            return conn;

        } catch (Exception e) {

            System.out.println("❌ Erro no banco");
            System.out.println("TIPO: " + e.getClass().getName());
            System.out.println("MSG: " + e.getMessage());

            e.printStackTrace();

            return null;
        }
    }
}
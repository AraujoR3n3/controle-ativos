package dao;

import com.ativos.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// ===============================
// DAO de Empresas
// ===============================
public class EmpresaDAO {

    // ===============================
    // Listar empresas
    // ===============================
    public List<String> listar() {

        List<String> empresas =
                new ArrayList<>();

        try {

            Connection conn =
                    Database.connect();

            Statement stmt =
                    conn.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                            """
                            SELECT nome
                            FROM empresas
                            ORDER BY nome
                            """
                    );

            while (rs.next()) {

                empresas.add(
                        rs.getString("nome")
                );
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return empresas;
    }

    // ===============================
    // Salvar empresa
    // ===============================
    public void salvar(
            String nome
    ) {

        try {

            Connection conn =
                    Database.connect();

            PreparedStatement ps =
                    conn.prepareStatement(
                            """
                            INSERT INTO empresas(nome)
                            VALUES(?)
                            """
                    );

            ps.setString(
                    1,
                    nome
            );

            ps.executeUpdate();

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    // ===============================
    // Excluir empresa
    // ===============================
    public void excluir(
            String nome
    ) {

        try {

            Connection conn =
                    Database.connect();

            PreparedStatement ps =
                    conn.prepareStatement(
                            """
                            DELETE FROM empresas
                            WHERE nome = ?
                            """
                    );

            ps.setString(
                    1,
                    nome
            );

            int linhas =
                    ps.executeUpdate();

            System.out.println(
                    "EMPRESAS EXCLUIDAS = "
                            + linhas
            );

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
}
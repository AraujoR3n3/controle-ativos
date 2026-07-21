package dao;

import com.ativos.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalidadeDAO {

    // ===============================
    // Listar localidades
    // ===============================
    public List<String[]> listar() {

        List<String[]> localidades =
                new ArrayList<>();

        try {

            Connection conn =
                    Database.connect();

            Statement stmt =
                    conn.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                            """
                            SELECT
                                e.nome AS empresa,
                                u.nome AS localidade
                            FROM unidades u
                            LEFT JOIN empresas e
                                ON e.id = u.empresa_id
                            ORDER BY e.nome, u.nome
                            """
                    );

            while (rs.next()) {

                localidades.add(
                        new String[]{
                                rs.getString("empresa"),
                                rs.getString("localidade")
                        }
                );
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return localidades;
    }

    // ===============================
    // Salvar localidade
    // ===============================
    public void salvar(
            String localidade,
            String empresa
    ) {

        try {

            Connection conn =
                    Database.connect();

            PreparedStatement ps =
                    conn.prepareStatement(
                            """
                            INSERT INTO unidades
                            (
                                nome,
                                empresa_id
                            )
                            VALUES
                            (
                                ?,
                                (
                                    SELECT id
                                    FROM empresas
                                    WHERE nome = ?
                                )
                            )
                            """
                    );

            ps.setString(1, localidade);
            ps.setString(2, empresa);
            System.out.println(
                    "SALVANDO LOCALIDADE = "
                            + localidade
            );

            System.out.println(
                    "EMPRESA = "
                            + empresa
            );

            ps.executeUpdate();

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    // ===============================
    // Excluir localidade
    // ===============================
    public void excluir(
            String localidade
    ) {

        try {

            Connection conn =
                    Database.connect();

            PreparedStatement ps =
                    conn.prepareStatement(
                            """
                            DELETE FROM unidades
                            WHERE nome = ?
                            """
                    );

            ps.setString(
                    1,
                    localidade
            );

            ps.executeUpdate();

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
}
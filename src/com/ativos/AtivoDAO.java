package com.ativos;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AtivoDAO {

    public List<Ativo> listarTodos() {

        List<Ativo> ativos =
                new ArrayList<>();

        try {

            Connection conn =
                    Database.connect();

            Statement stmt =
                    conn.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                            "SELECT * FROM ativos"
                    );

            while (rs.next()) {

                Ativo ativo = new Ativo(
                        rs.getString("empresa"),
                        rs.getString("cd"),
                        rs.getString("equipamento"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("serial"),
                        rs.getString("host"),
                        rs.getString("patrimonio"),
                        rs.getString("local"),
                        rs.getString("status"),
                        rs.getString("condicao"),
                        rs.getString("situacao"),
                        rs.getString("responsavel"),
                        rs.getString("observacoes")
                );

                ativos.add(ativo);
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return ativos;
    }

    public int contarAtivos() {

        return listarTodos().size();
    }

    public Ativo buscarPorPatrimonio(
            String patrimonio
    ) {

        try {

            Connection conn =
                    Database.connect();

            PreparedStatement ps =
                    conn.prepareStatement(
                            """
                            SELECT *
                            FROM ativos
                            WHERE patrimonio = ?
                            """
                    );

            ps.setString(
                    1,
                    patrimonio
            );

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                Ativo ativo = new Ativo(
                        rs.getString("empresa"),
                        rs.getString("cd"),
                        rs.getString("equipamento"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("serial"),
                        rs.getString("host"),
                        rs.getString("patrimonio"),
                        rs.getString("local"),
                        rs.getString("status"),
                        rs.getString("condicao"),
                        rs.getString("situacao"),
                        rs.getString("responsavel"),
                        rs.getString("observacoes")
                );

                conn.close();

                return ativo;
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return null;
    }

    public void excluir(
            String patrimonio
    ) {

        try {

            Connection conn =
                    Database.connect();

            PreparedStatement ps =
                    conn.prepareStatement(
                            """
                            DELETE FROM ativos
                            WHERE patrimonio = ?
                            """
                    );

            ps.setString(
                    1,
                    patrimonio
            );

            ps.executeUpdate();

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

}
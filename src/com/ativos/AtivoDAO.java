package com.ativos;

// ===============================
// Imports
// ===============================

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

// ===============================
// DAO de Ativos
// ===============================

public class AtivoDAO {

    // ===============================
    // Listar todos os ativos
    // ===============================
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

    // ===============================
    // Contar ativos
    // ===============================
    public int contarAtivos() {

        return listarTodos().size();
    }

    // ===============================
    // Buscar ativo por patrimônio
    // ===============================
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

    // ===============================
    // Excluir ativo
    // ===============================
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

    // ===============================
    // Salvar novo ativo
    // ===============================
    public void salvar(
            Ativo ativo
    ) {

        try {

            Connection conn =
                    Database.connect();

            PreparedStatement ps =
                    conn.prepareStatement(
                            """
                            INSERT INTO ativos
                            (
                                empresa,
                                cd,
                                equipamento,
                                marca,
                                modelo,
                                serial,
                                host,
                                patrimonio,
                                local,
                                status,
                                condicao,
                                situacao,
                                responsavel,
                                observacoes
                            )
                            VALUES
                            (
                                ?,?,?,?,?,?,?,?,?,?,?,?,?,?
                            )
                            """
                    );

            ps.setString(1, ativo.getEmpresa());
            ps.setString(2, ativo.getCd());
            ps.setString(3, ativo.getEquipamento());
            ps.setString(4, ativo.getMarca());
            ps.setString(5, ativo.getModelo());
            ps.setString(6, ativo.getSerial());
            ps.setString(7, ativo.getHost());
            ps.setString(8, ativo.getPatrimonio());
            ps.setString(9, ativo.getLocal());
            ps.setString(10, ativo.getStatus());
            ps.setString(11, ativo.getCondicao());
            ps.setString(12, ativo.getSituacao());
            ps.setString(13, ativo.getResponsavel());
            ps.setString(14, ativo.getObservacoes());

            ps.executeUpdate();

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
    // ===============================
// Atualizar ativo
// ===============================
    public void atualizar(
            Ativo ativo,
            String patrimonioOriginal
    ) {

        try {

            Connection conn =
                    Database.connect();

            if (conn == null) {
                return;
            }

            PreparedStatement ps =
                    conn.prepareStatement(
                            """
                            UPDATE ativos
                            SET
                                empresa = ?,
                                cd = ?,
                                equipamento = ?,
                                marca = ?,
                                modelo = ?,
                                serial = ?,
                                host = ?,
                                patrimonio = ?,
                                local = ?,
                                status = ?,
                                condicao = ?,
                                situacao = ?,
                                responsavel = ?,
                                observacoes = ?
                            WHERE patrimonio = ?
                            """
                    );

            ps.setString(1, ativo.getEmpresa());
            ps.setString(2, ativo.getCd());
            ps.setString(3, ativo.getEquipamento());
            ps.setString(4, ativo.getMarca());
            ps.setString(5, ativo.getModelo());
            ps.setString(6, ativo.getSerial());
            ps.setString(7, ativo.getHost());
            ps.setString(8, ativo.getPatrimonio());
            ps.setString(9, ativo.getLocal());
            ps.setString(10, ativo.getStatus());
            ps.setString(11, ativo.getCondicao());
            ps.setString(12, ativo.getSituacao());
            ps.setString(13, ativo.getResponsavel());
            ps.setString(14, ativo.getObservacoes());

            ps.setString(
                    15,
                    patrimonioOriginal
            );

            ps.executeUpdate();

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
}
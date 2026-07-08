package com.ativos.dao;

import com.ativos.Database;
import com.ativos.model.Ativo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AtivoDAO {

    public List<Ativo> listarTodos() {

        List<Ativo> lista = new ArrayList<>();

        try {
            Connection conn = Database.connect();

            if (conn == null) {
                System.out.println("Erro na conexão ❌");
                return lista;
            }

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ativos");

            while (rs.next()) {

                Ativo ativo = new Ativo(
                        rs.getInt("id"),
                        rs.getString("equipamento"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("serial"),
                        rs.getString("patrimonio"),
                        rs.getString("status")
                );

                lista.add(ativo);
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
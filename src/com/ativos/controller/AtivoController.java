package com.ativos.controller;

import com.ativos.AtivoDAO;
import com.ativos.Database;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AtivoController {

    // ======================================================
    // EXCLUIR ATIVO
    // ======================================================

    public void excluirAtivo(
            TableView<String[]> tabela,
            ComboBox<String> cbFiltroUnidade,
            Label lblTotal
    ) {

        // Recupera o ativo selecionado na tabela

        String[] ativo =
                tabela.getSelectionModel()
                        .getSelectedItem();

        // Valida se existe um ativo selecionado

        if (ativo == null) {

            new Alert(
                    Alert.AlertType.WARNING,
                    "Selecione um ativo para excluir."
            ).show();

            return;
        }

        // Índice 4 = Patrimônio

        String patrimonioSelecionado =
                ativo[4];

        // Solicita confirmação do usuário

        Alert confirmacao =
                new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "Deseja realmente excluir o patrimônio "
                                + patrimonioSelecionado
                                + " ?"
                );

        confirmacao.showAndWait();

        if (confirmacao.getResult() != ButtonType.OK) {
            return;
        }

        try {

            // Executa exclusão

            AtivoDAO dao =
                    new AtivoDAO();

            dao.excluir(
                    patrimonioSelecionado
            );

            // TODO:
            // Recarregar tabela após a exclusão
            // Atualmente esse método ainda está no App.java

            System.out.println(
                    "Tabela deve ser recarregada aqui"
            );

            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Registro excluído com sucesso ✅"
            ).show();

        } catch (Exception ex) {

            ex.printStackTrace();

            new Alert(
                    Alert.AlertType.ERROR,
                    "Erro ao excluir ativo ❌"
            ).show();
        }
    }

// ======================================================
// EDITAR ATIVO
// ======================================================

    public void editarAtivo(
            TableView<String[]> tabela,
            ComboBox<String> cbEmpresa,
            ComboBox<String> cbUnidade,
            TextField txtEquipamento,
            TextField txtMarca,
            TextField txtModelo,
            TextField txtSerial,
            TextField txtHost,
            TextField txtPatrimonio,
            TextField txtLocal,
            TextField txtResponsavel,
            TextArea txtObs,
            ComboBox<String> cbStatus,
            ComboBox<String> cbCondicao,
            ComboBox<String> cbSituacao,
            boolean[] modoEdicao,
            String[] patrimonioOriginal
    ) {

        // Recupera item selecionado

        String[] ativo =
                tabela.getSelectionModel()
                        .getSelectedItem();

        if (ativo == null) {

            new Alert(
                    Alert.AlertType.WARNING,
                    "Selecione um ativo."
            ).show();

            return;
        }

        // Índice 4 = patrimônio

        String patrimonioSelecionado =
                ativo[4];

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
                    patrimonioSelecionado
            );

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                cbEmpresa.setValue(
                        rs.getString("empresa")
                );

                cbUnidade.setValue(
                        rs.getString("cd")
                );

                txtEquipamento.setText(
                        rs.getString("equipamento")
                );

                txtMarca.setText(
                        rs.getString("marca")
                );

                txtModelo.setText(
                        rs.getString("modelo")
                );

                txtSerial.setText(
                        rs.getString("serial")
                );

                txtHost.setText(
                        rs.getString("host")
                );

                txtPatrimonio.setText(
                        rs.getString("patrimonio")
                );

                patrimonioOriginal[0] =
                        rs.getString("patrimonio");

                txtLocal.setText(
                        rs.getString("local")
                );

                txtResponsavel.setText(
                        rs.getString("responsavel")
                );

                txtObs.setText(
                        rs.getString("observacoes")
                );

                cbStatus.setValue(
                        rs.getString("status")
                );

                cbCondicao.setValue(
                        rs.getString("condicao")
                );

                cbSituacao.setValue(
                        rs.getString("situacao")
                );

                // Ativa modo edição

                modoEdicao[0] = true;
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();

            new Alert(
                    Alert.AlertType.ERROR,
                    "Erro ao carregar ativo ❌"
            ).show();
        }
    }
// ======================================================
// SALVAR ATIVO
// ======================================================

    public void salvarAtivo(
            ComboBox<String> cbEmpresa,
            ComboBox<String> cbUnidade,
            TextField txtEquipamento,
            TextField txtMarca,
            TextField txtModelo,
            TextField txtSerial,
            TextField txtHost,
            TextField txtPatrimonio,
            TextField txtLocal,
            TextField txtResponsavel,
            TextArea txtObs,
            ComboBox<String> cbStatus,
            ComboBox<String> cbCondicao,
            ComboBox<String> cbSituacao,
            TableView<String[]> tabela,
            ComboBox<String> cbFiltroUnidade,
            Label lblTotal,
            boolean[] modoEdicao,
            String[] patrimonioOriginal
    ) {

        System.out.println(
                "Controller: salvarAtivo()"
        );
    }

}

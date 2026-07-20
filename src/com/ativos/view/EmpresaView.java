package com.ativos.view;

import com.ativos.Database;
import com.ativos.EmpresaDAO;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class EmpresaView {

    private static Stage janela;

    public void show() {

        if (janela != null && janela.isShowing()) {

            janela.toFront();

            return;
        }

        janela = new Stage();

        janela.setTitle(
                "Cadastro de Empresas"
        );

        Label lblEmpresa =
                new Label("Empresa:");

        TextField txtEmpresa =
                new TextField();

        Button btnSalvar =
                new Button("Salvar");

        Button btnExcluir =
                new Button("Excluir");

        TableView<String> tabelaEmpresas =
                new TableView<>();

        TableColumn<String, String> colEmpresa =
                new TableColumn<>("Empresa");

        colEmpresa.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue()
                )
        );

        tabelaEmpresas.getColumns().add(
                colEmpresa
        );

        carregarEmpresas(
                tabelaEmpresas
        );

        btnSalvar.setOnAction(e -> {

            if (txtEmpresa.getText().trim().isEmpty()) {

                new Alert(
                        Alert.AlertType.WARNING,
                        "Informe o nome da empresa."
                ).showAndWait();

                return;
            }

            try {

                EmpresaDAO dao =
                        new EmpresaDAO();

                dao.salvar(
                        txtEmpresa.getText().trim()
                );

                tabelaEmpresas.getItems().add(
                        txtEmpresa.getText().trim()
                );

                new Alert(
                        Alert.AlertType.INFORMATION,
                        "Empresa cadastrada com sucesso ✅"
                ).showAndWait();

                txtEmpresa.clear();

            } catch (Exception ex) {

                ex.printStackTrace();

                new Alert(
                        Alert.AlertType.ERROR,
                        "Empresa já cadastrada ou erro ao salvar."
                ).showAndWait();
            }
        });

        btnExcluir.setOnAction(e -> {

            String empresaSelecionada =
                    tabelaEmpresas
                            .getSelectionModel()
                            .getSelectedItem();

            if (empresaSelecionada == null) {

                new Alert(
                        Alert.AlertType.WARNING,
                        "Selecione uma empresa."
                ).showAndWait();

                return;
            }

            Alert confirmacao =
                    new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Deseja realmente excluir esta empresa?"
                    );

            if (confirmacao.showAndWait().get()
                    != ButtonType.OK) {

                return;
            }

            try {

                Connection conn =
                        Database.connect();

                PreparedStatement verificaEmpresa =
                        conn.prepareStatement(
                                """
                                SELECT COUNT(*)
                                FROM unidades u
                                INNER JOIN empresas e
                                    ON e.id = u.empresa_id
                                WHERE e.nome = ?
                                """
                        );

                verificaEmpresa.setString(
                        1,
                        empresaSelecionada
                );

                ResultSet rsVerifica =
                        verificaEmpresa.executeQuery();

                if (rsVerifica.next()
                        && rsVerifica.getInt(1) > 0) {

                    new Alert(
                            Alert.AlertType.WARNING,
                            "Não é possível excluir esta empresa.\nExistem localidades vinculadas."
                    ).showAndWait();

                    conn.close();

                    return;
                }
                rsVerifica.close();
                verificaEmpresa.close();
                conn.close();

                EmpresaDAO dao =
                        new EmpresaDAO();

                dao.excluir(
                        empresaSelecionada
                );

                tabelaEmpresas.getItems()
                        .remove(
                                empresaSelecionada
                        );

                conn.close();

                new Alert(
                        Alert.AlertType.INFORMATION,
                        "Empresa excluída com sucesso ✅"
                ).showAndWait();

            } catch (Exception ex) {

                ex.printStackTrace();

                new Alert(
                        Alert.AlertType.ERROR,
                        "Erro ao excluir empresa ❌"
                ).showAndWait();
            }
        });

        tabelaEmpresas.setPrefHeight(
                250
        );

        VBox layout =
                new VBox(
                        10,
                        lblEmpresa,
                        txtEmpresa,

                        new HBox(
                                10,
                                btnSalvar,
                                btnExcluir
                        ),

                        tabelaEmpresas
                );

        layout.setPadding(
                new Insets(15)
        );

        janela.setScene(
                new Scene(
                        layout,
                        600,
                        500
                )
        );

        janela.show();
    }

    private void carregarEmpresas(
            TableView<String> tabela
    ) {

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

                tabela.getItems().add(
                        rs.getString("nome")
                );
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
}
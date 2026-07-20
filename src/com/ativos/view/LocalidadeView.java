package com.ativos.view;

import com.ativos.Database;
import com.ativos.LocalidadeDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LocalidadeView {

    private static Stage janela;

    public void show() {

        // =====================================
        // Janela única
        // Evita abrir várias telas iguais
        // =====================================

        if (janela != null && janela.isShowing()) {

            janela.toFront();
            return;
        }

        janela = new Stage();

        janela.setTitle(
                "Cadastro de Localidades"
        );

        // =====================================
        // Empresa
        // =====================================

        Label lblEmpresa =
                new Label("Empresa:");

        ComboBox<String> cbEmpresa =
                new ComboBox<>();

        // =====================================
        // Carregar empresas no ComboBox
        // =====================================

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

                cbEmpresa.getItems().add(
                        rs.getString("nome")
                );
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        // =====================================
        // Localidade
        // =====================================

        Label lblNome =
                new Label("Localidade:");

        TextField txtNome =
                new TextField();

        // =====================================
        // Botões
        // =====================================

        Button btnSalvar =
                new Button("Salvar");

        Button btnExcluir =
                new Button("Excluir");

        // =====================================
        // Tabela
        // =====================================

        TableView<String[]> tabelaLocalidades =
                new TableView<>();

        TableColumn<String[], String> colEmpresa =
                new TableColumn<>("Empresa");

        colEmpresa.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue()[0]
                )
        );

        TableColumn<String[], String> colLocalidade =
                new TableColumn<>("Localidade");

        colLocalidade.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue()[1]
                )
        );

        tabelaLocalidades.getColumns().addAll(
                colEmpresa,
                colLocalidade
        );

        // =====================================
        // Carregar localidades da base
        // =====================================

        try {

            LocalidadeDAO dao =
                    new LocalidadeDAO();

            for (String[] localidade : dao.listar()) {

                tabelaLocalidades.getItems().add(
                        localidade
                );
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        tabelaLocalidades.setPrefHeight(200);

        // =====================================
        // Salvar Localidade
        // =====================================

        btnSalvar.setOnAction(e -> {

            if (cbEmpresa.getValue() == null
                    || txtNome.getText().trim().isEmpty()) {

                new Alert(
                        Alert.AlertType.WARNING,
                        "Selecione uma empresa e informe a localidade."
                ).showAndWait();

                return;
            }

            try {

                LocalidadeDAO dao =
                        new LocalidadeDAO();

                dao.salvar(
                        txtNome.getText().trim(),
                        cbEmpresa.getValue()
                );

                tabelaLocalidades.getItems().add(
                        new String[]{
                                cbEmpresa.getValue(),
                                txtNome.getText().trim()
                        }
                );

                new Alert(
                        Alert.AlertType.INFORMATION,
                        "Localidade cadastrada com sucesso ✅"
                ).showAndWait();

                txtNome.clear();

            } catch (Exception ex) {

                ex.printStackTrace();

                new Alert(
                        Alert.AlertType.ERROR,
                        "Erro ao cadastrar localidade ❌"
                ).showAndWait();
            }
        });

        // =====================================
        // Excluir Localidade
        // =====================================

        btnExcluir.setOnAction(e -> {

            String[] localSelecionado =
                    tabelaLocalidades
                            .getSelectionModel()
                            .getSelectedItem();

            if (localSelecionado == null) {

                new Alert(
                        Alert.AlertType.WARNING,
                        "Selecione uma localidade."
                ).showAndWait();

                return;
            }

            Alert confirmacao =
                    new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Deseja realmente excluir esta localidade?"
                    );

            if (confirmacao.showAndWait().get()
                    != ButtonType.OK) {

                return;
            }

            try {

                LocalidadeDAO dao =
                        new LocalidadeDAO();

                dao.excluir(
                        localSelecionado[1]
                );

                tabelaLocalidades.getItems()
                        .remove(localSelecionado);

                new Alert(
                        Alert.AlertType.INFORMATION,
                        "Localidade excluída com sucesso ✅"
                ).showAndWait();

            } catch (Exception ex) {

                ex.printStackTrace();

                new Alert(
                        Alert.AlertType.ERROR,
                        "Erro ao excluir localidade ❌"
                ).showAndWait();
            }
        });

        // =====================================
        // Layout
        // =====================================

        VBox layout =
                new VBox(
                        10,
                        lblEmpresa,
                        cbEmpresa,
                        lblNome,
                        txtNome,
                        new HBox(
                                10,
                                btnSalvar,
                                btnExcluir
                        ),
                        tabelaLocalidades
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
 }
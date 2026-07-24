package com.ativos.view;

import com.ativos.Database;
import dao.LocalidadeDAO;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LocalidadesView {

    private final VBox view;

    private final ComboBox<String> cbEmpresa;

    private final TextField txtNome;

    private final TableView<String[]> tabelaLocalidades;

    public LocalidadesView() {

        view = new VBox(15);

        view.setPadding(
                new Insets(20)
        );

        Label titulo =
                new Label("📍 Localidades");

        titulo.getStyleClass()
                .add("card-title");

        // =====================================
        // Empresa
        // =====================================

        Label lblEmpresa =
                new Label("Empresa:");

        cbEmpresa =
                new ComboBox<>();

        carregarEmpresas();

        // =====================================
        // Localidade
        // =====================================

        Label lblNome =
                new Label("Localidade:");

        txtNome =
                new TextField();

        txtNome.setPromptText(
                "Nome da localidade"
        );

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

        tabelaLocalidades =
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

        tabelaLocalidades.getColumns()
                .addAll(
                        colEmpresa,
                        colLocalidade
                );

        tabelaLocalidades.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        VBox.setVgrow(
                tabelaLocalidades,
                Priority.ALWAYS
        );

        carregarLocalidades();

        // =====================================
        // Eventos
        // =====================================

        btnSalvar.setOnAction(
                e -> salvarLocalidade()
        );

        btnExcluir.setOnAction(
                e -> excluirLocalidade()
        );

        HBox linhaBotoes =
                new HBox(
                        10,
                        btnSalvar,
                        btnExcluir
                );

        view.getChildren().addAll(
                titulo,
                lblEmpresa,
                cbEmpresa,
                lblNome,
                txtNome,
                linhaBotoes,
                tabelaLocalidades
        );
    }

    // =====================================
    // Carregar Empresas
    // =====================================

    private void carregarEmpresas() {

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

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    // =====================================
    // Carregar Localidades
    // =====================================

    private void carregarLocalidades() {

        tabelaLocalidades.getItems()
                .clear();

        try {

            LocalidadeDAO dao =
                    new LocalidadeDAO();

            tabelaLocalidades.getItems()
                    .addAll(
                            dao.listar()
                    );

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    // =====================================
    // Salvar
    // =====================================

    private void salvarLocalidade() {

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

            carregarLocalidades();

            txtNome.clear();

            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Localidade cadastrada com sucesso ✅"
            ).showAndWait();

        } catch (Exception ex) {

            ex.printStackTrace();

            new Alert(
                    Alert.AlertType.ERROR,
                    "Erro ao salvar localidade."
            ).showAndWait();
        }
    }

    // =====================================
    // Excluir
    // =====================================

    private void excluirLocalidade() {

        String[] selecionada =
                tabelaLocalidades
                        .getSelectionModel()
                        .getSelectedItem();

        if (selecionada == null) {

            new Alert(
                    Alert.AlertType.WARNING,
                    "Selecione uma localidade."
            ).showAndWait();

            return;
        }

        try {

            LocalidadeDAO dao =
                    new LocalidadeDAO();

            dao.excluir(
                    selecionada[1]
            );

            carregarLocalidades();

            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Localidade excluída com sucesso ✅"
            ).showAndWait();

        } catch (Exception ex) {

            ex.printStackTrace();

            new Alert(
                    Alert.AlertType.ERROR,
                    "Erro ao excluir localidade."
            ).showAndWait();
        }
    }

    // =====================================
    // Retornar View
    // =====================================

    public VBox getView() {

        return view;
    }
}
package com.ativos.view;

import com.ativos.Database;
import dao.EmpresaDAO;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class EmpresasView {

    private final VBox view;

    private final TextField txtEmpresa;

    private final TableView<String> tabelaEmpresas;

    public EmpresasView() {

        view = new VBox(15);

        view.setPadding(
                new Insets(20)
        );

        // =====================================
        // Título
        // =====================================

        Label titulo =
                new Label("🏢 Empresas");

        titulo.getStyleClass()
                .add("card-title");

        // =====================================
        // Campo Empresa
        // =====================================

        Label lblEmpresa =
                new Label("Empresa:");

        txtEmpresa =
                new TextField();

        txtEmpresa.setPromptText(
                "Nome da empresa"
        );
        txtEmpresa.setMaxWidth(
                Double.MAX_VALUE
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

        tabelaEmpresas =
                new TableView<>();

        TableColumn<String, String> colEmpresa =
                new TableColumn<>("Empresa");

        colEmpresa.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue()
                )
        );

        tabelaEmpresas.getColumns()
                .add(colEmpresa);

        tabelaEmpresas.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        VBox.setVgrow(
                tabelaEmpresas,
                Priority.ALWAYS
        );

        carregarEmpresas();

        // =====================================
        // Salvar
        // =====================================

        btnSalvar.setOnAction(e -> salvarEmpresa());

        // =====================================
        // Excluir
        // =====================================

        btnExcluir.setOnAction(e -> excluirEmpresa());

        HBox linhaBotoes =
                new HBox(
                        10,
                        btnSalvar,
                        btnExcluir
                );

        view.getChildren().addAll(
                titulo,
                lblEmpresa,
                txtEmpresa,
                linhaBotoes,
                tabelaEmpresas
        );
    }

    // =====================================
    // Salvar Empresa
    // =====================================

    private void salvarEmpresa() {

        if (txtEmpresa.getText()
                .trim()
                .isEmpty()) {

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

            carregarEmpresas();

            txtEmpresa.clear();

            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Empresa cadastrada com sucesso ✅"
            ).showAndWait();

        } catch (Exception ex) {

            ex.printStackTrace();

            new Alert(
                    Alert.AlertType.ERROR,
                    "Erro ao salvar empresa."
            ).showAndWait();
        }
    }

    // =====================================
    // Excluir Empresa
    // =====================================

    private void excluirEmpresa() {

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

        try {

            EmpresaDAO dao =
                    new EmpresaDAO();

            dao.excluir(
                    empresaSelecionada
            );

            carregarEmpresas();

            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Empresa excluída com sucesso ✅"
            ).showAndWait();

        } catch (Exception ex) {

            ex.printStackTrace();

            new Alert(
                    Alert.AlertType.ERROR,
                    "Erro ao excluir empresa."
            ).showAndWait();
        }
    }

    // =====================================
    // Carregar Empresas
    // =====================================

    private void carregarEmpresas() {

        tabelaEmpresas.getItems()
                .clear();

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

                tabelaEmpresas.getItems()
                        .add(
                                rs.getString("nome")
                        );
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    // =====================================
    // Retorna a View
    // =====================================

    public VBox getView() {

        return view;
    }
}
package com.ativos;

import dao.EmpresaDAO;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

import com.ativos.view.EmpresasView;
import com.ativos.controller.AtivoController;
import com.ativos.view.LocalidadesView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App extends Application {

// ======================================================
// CONTROLLER PRINCIPAL
// ======================================================

    private AtivoController controller =
            new AtivoController();
    @Override
    public void start(Stage stage) {

// ======================================================
// TELA PRINCIPAL
// ======================================================
        Label titulo =
                new Label("🖥 Controle de Ativos");

        titulo.setStyle(
                "-fx-font-size:22px;" +
                        "-fx-font-weight:bold;"
        );
        titulo.setMaxWidth(Double.MAX_VALUE);
        titulo.setAlignment(Pos.CENTER);
// ======================================================
// CARREGAMENTO DE EMPRESAS
// ======================================================

        ComboBox<String> cbEmpresa =
                new ComboBox<>();

        carregarEmpresas(cbEmpresa);

        cbEmpresa.setPromptText(
                "Empresa"
        );

// ======================================================
// CARREGAMENTO DE LOCALIDADES
// ======================================================

        ComboBox<String> cbUnidade =
                new ComboBox<>();

        carregarLocalidades(cbUnidade);

        cbUnidade.setPromptText(
                "Localidade"
        );

        cbEmpresa.setPrefWidth(260);
        cbUnidade.setPrefWidth(300);

        cbEmpresa.setOnAction(e -> {

            cbUnidade.getItems().clear();

            try {

                Connection conn =
                        Database.connect();

                PreparedStatement ps =
                        conn.prepareStatement(
                                """
                                SELECT u.nome
                                FROM unidades u
                                INNER JOIN empresas e
                                    ON e.id = u.empresa_id
                                WHERE e.nome = ?
                                ORDER BY u.nome
                                """
                        );

                ps.setString(
                        1,
                        cbEmpresa.getValue()
                );


                ResultSet rs =
                        ps.executeQuery();

                while (rs.next()) {

                    cbUnidade.getItems().add(
                            rs.getString("nome")
                    );
                }

                conn.close();

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

// ======================================================
// COMPONENTES DO FORMULÁRIO
// ======================================================

        TextField txtEquipamento = new TextField();
        txtEquipamento.setPromptText("Equipamento");

        TextField txtMarca = new TextField();
        txtMarca.setPromptText("Marca");

        TextField txtModelo = new TextField();
        txtModelo.setPromptText("Modelo");

        TextField txtSerial = new TextField();
        txtSerial.setPromptText("Serial Number");

        TextField txtPatrimonio = new TextField();
        txtPatrimonio.setPromptText("Patrimônio");
        TextField txtHost = new TextField();
        txtHost.setPromptText("Host");

        TextField txtLocal = new TextField();
        txtLocal.setPromptText("Local");

        ComboBox<String> cbCondicao = new ComboBox<>();
        cbCondicao.getItems().addAll(
                "Novo",
                "Usado"
        );
        cbCondicao.setPromptText("Condição");

        ComboBox<String> cbSituacao = new ComboBox<>();
        cbSituacao.getItems().addAll(
                "Ativo",
                "Estoque",
                "Retirado",
                "Manutenção"
        );
        cbSituacao.setPromptText("Situação");

        TextField txtResponsavel = new TextField();
        txtResponsavel.setPromptText("Responsável");
        ComboBox<String> cbStatus = new ComboBox<>();
        cbStatus.getItems().addAll(
                "Ativo",
                "Estoque",
                "Manutenção"
        );
        cbStatus.setPromptText("Status");

        TextArea txtObs = new TextArea();
        txtObs.setPromptText("Observações");
        Button btnSalvar = new Button("Salvar");
        Button btnExportar = new Button("Exportar Excel");
        Button btnExcluir = new Button("Excluir");

        final boolean[] modoEdicao = {false};
        final String[] patrimonioOriginal = {""};
        Button btnEditar = new Button("Editar");
        Label lblFiltro = new Label("Local:");
        Label lblTotal = new Label("Total de Ativos: 0");
        btnSalvar.setStyle(
                "-fx-background-color:#2E7D32;" +
                        "-fx-text-fill:white;"
        );

        btnEditar.setStyle(
                "-fx-background-color:#1976D2;" +
                        "-fx-text-fill:white;"
        );

        btnExcluir.setStyle(
                "-fx-background-color:#C62828;" +
                        "-fx-text-fill:white;"
        );


        btnExportar.setStyle(
                "-fx-background-color:#455A64;" +
                        "-fx-text-fill:white;"
        );
        TextField txtBusca = new TextField();

        txtBusca.setPromptText(
                "Buscar patrimônio, host, equipamento ou responsável"
        );

        txtBusca.setPrefWidth(300);

        Button btnBuscar = new Button("Buscar");

        btnBuscar.setPrefWidth(120);
        btnBuscar.setPrefHeight(35);

        btnBuscar.setStyle(
                "-fx-background-color:#1976D2;" +
                        "-fx-text-fill:white;" +
                        "-fx-font-weight:bold;"
        );


        ComboBox<String> cbFiltroUnidade = new ComboBox<>();

        cbFiltroUnidade.getItems().add("Todos");

        try {

            Connection conn = Database.connect();

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(
                    """
                    SELECT DISTINCT cd
                    FROM ativos
                    ORDER BY cd
                    """
            );

            while (rs.next()) {

                cbFiltroUnidade.getItems().add(
                        rs.getString("cd")
                );
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        cbFiltroUnidade.setValue("Todos");

// ======================================================
// TABELA DE ATIVOS
// ======================================================

        TableView<String[]> tabela = new TableView<>();
        txtObs.setPrefHeight(40);
        TableColumn<String[], String> colEmpresa =
                new TableColumn<>("Empresa");

        colEmpresa.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue()[0])
        );

        TableColumn<String[], String> colUnidade =
                new TableColumn<>("Localidade");

        colUnidade.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue()[1])
        );

        TableColumn<String[], String> colEquipamento =
                new TableColumn<>("Equipamento");

        colEquipamento.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue()[2])
        );

        TableColumn<String[], String> colHost =
                new TableColumn<>("Host");

        colHost.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue()[3])
        );

        TableColumn<String[], String> colPatrimonio =
                new TableColumn<>("Patrimônio");

        colPatrimonio.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue()[4])
        );

        TableColumn<String[], String> colLocal =
                new TableColumn<>("Local Físico");

        colLocal.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue()[5])
        );

        TableColumn<String[], String> colResponsavel =
                new TableColumn<>("Responsável");

        colResponsavel.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue()[6])
        );

        tabela.getColumns().addAll(
                colEmpresa,
                colUnidade,
                colEquipamento,
                colHost,
                colPatrimonio,
                colLocal,
                colResponsavel
        );

        cbUnidade.setPrefWidth(140);
        colEquipamento.setPrefWidth(180);
        colHost.setPrefWidth(140);
        colPatrimonio.setPrefWidth(140);
        colLocal.setPrefWidth(140);
        colResponsavel.setPrefWidth(220);

// ======================================================
// CARREGAMENTO DA TABELA
// ======================================================
        controller.carregarTabela(
                tabela,
                cbFiltroUnidade,
                lblTotal
        );

// ======================================================
// EVENTO BUSCAR
// ======================================================

        btnBuscar.setOnAction(
                e -> controller.buscarAtivos(
                        tabela,
                        cbEmpresa,
                        cbUnidade,
                        txtBusca
                )
        );

        txtBusca.textProperty().addListener(
                (obs, antigo, novo) ->
                        controller.buscarAtivos(
                                tabela,
                                cbEmpresa,
                                cbUnidade,
                                txtBusca
                        )
        );

        cbFiltroUnidade.setOnAction(e ->
                controller.carregarTabela(
                        tabela,
                        cbFiltroUnidade,
                        lblTotal
                )
        );

// ======================================================
// EVENTO EDITAR
// ======================================================

        btnEditar.setOnAction(
                e -> controller.editarAtivo(
                        tabela,
                        cbEmpresa,
                        cbUnidade,
                        txtEquipamento,
                        txtMarca,
                        txtModelo,
                        txtSerial,
                        txtHost,
                        txtPatrimonio,
                        txtLocal,
                        txtResponsavel,
                        txtObs,
                        cbStatus,
                        cbCondicao,
                        cbSituacao,
                        modoEdicao,
                        patrimonioOriginal
                )
        );

// ======================================================
// EVENTO EXCLUIR
// ======================================================
        btnExcluir.setOnAction(
                e -> controller.excluirAtivo(
                        tabela,
                        cbFiltroUnidade,
                        lblTotal
                )
        );

// ======================================================
// EVENTO SALVAR
// ======================================================

        btnSalvar.setOnAction(
                e -> controller.salvarAtivo(
                        cbEmpresa,
                        cbUnidade,
                        txtEquipamento,
                        txtMarca,
                        txtModelo,
                        txtSerial,
                        txtHost,
                        txtPatrimonio,
                        txtLocal,
                        txtResponsavel,
                        txtObs,
                        cbStatus,
                        cbCondicao,
                        cbSituacao,
                        tabela,
                        cbFiltroUnidade,
                        lblTotal,
                        modoEdicao,
                        patrimonioOriginal
                )
        );

// ======================================================
// EXPORTAÇÃO EXCEL
// ======================================================

        btnExportar.setOnAction(
                e -> exportarExcel()
        );

        txtEquipamento.setPrefWidth(300);
        txtMarca.setPrefWidth(180);
        txtModelo.setPrefWidth(180);

        txtSerial.setPrefWidth(250);
        txtHost.setPrefWidth(250);

        txtPatrimonio.setPrefWidth(250);
        txtLocal.setPrefWidth(250);

        txtResponsavel.setPrefWidth(500);

// Linhas da interface

        HBox linhaEquipamento = new HBox(
                10,
                txtEquipamento,
                txtMarca,
                txtModelo
        );

        HBox linhaSerial = new HBox(
                10,
                txtSerial,
                txtHost
        );

        HBox linhaPatrimonio = new HBox(
                10,
                txtPatrimonio,
                txtLocal
        );

        HBox linhaStatus = new HBox(
                10,
                cbStatus,
                cbCondicao,
                cbSituacao
        );

// ======================================================
// CARD DADOS DO ATIVO
// ======================================================

        Label lblCadastro =
                new Label("📝 Dados do Ativo");

        lblCadastro.getStyleClass()
                .add("card-title");

        HBox linhaBotoes = new HBox(
                10,
                btnSalvar,
                btnEditar,
                btnExcluir,
                btnExportar
        );
        VBox cardCadastro =
                new VBox(
                        5,
                        lblCadastro,
                        linhaEquipamento,
                        linhaSerial,
                        linhaPatrimonio,
                        linhaStatus,
                        txtResponsavel,
                        txtObs,
                        linhaBotoes
                );

        cardCadastro.getStyleClass()
                .add("card");

        cbEmpresa.setMaxWidth(Double.MAX_VALUE);
        cbUnidade.setMaxWidth(Double.MAX_VALUE);

        HBox linhaPesquisa = new HBox(
                10,
                cbEmpresa,
                cbUnidade,
                txtBusca,
                btnBuscar
        );

        HBox.setHgrow(txtBusca,
                Priority.ALWAYS
        );

        txtBusca.setMaxWidth(
                Double.MAX_VALUE
        );

        linhaPesquisa.setAlignment(
                Pos.CENTER_LEFT
        );

// ======================================================
// CARD PESQUISA
// ======================================================

        Label lblPesquisa =
                new Label("🔍 Pesquisa");

        lblPesquisa.getStyleClass()
                .add("card-title");

        VBox cardPesquisa =
                new VBox(
                        6,
                        lblPesquisa,
                        linhaPesquisa
                );

        cardPesquisa.getStyleClass()
                .add("card");

// ======================================================
// CARD AÇÕES
// ======================================================


        linhaBotoes.setAlignment(Pos.CENTER_LEFT);

        Button btnMenuDashboard =
                new Button("📊 Dashboard");

        btnMenuDashboard.getStyleClass()
                .add("menu-button");

        btnMenuDashboard.getStyleClass()
                .add("menu-button-active");


        Button btnMenuAtivos =
                new Button("📦 Ativos");

        btnMenuAtivos.getStyleClass()
                .add("menu-button");

        Button btnMenuEmpresas =
                new Button("🏢 Empresas");

        btnMenuEmpresas.getStyleClass()
                .add("menu-button");

        Button btnMenuLocalidades =
                new Button("📍 Localidades");

        btnMenuLocalidades.getStyleClass()
                .add("menu-button");


        Label lblLogo =
                new Label("📋 SGA");

        lblLogo.getStyleClass()
                .add("sidebar-logo");

        VBox menuLateral = new VBox(
                20,
                lblLogo,
                btnMenuDashboard,
                btnMenuAtivos,
                btnMenuEmpresas,
                btnMenuLocalidades
        );

        menuLateral.setPadding(
                new Insets(20)
        );

        menuLateral.setPrefWidth(220);

        menuLateral.getStyleClass()
                .add("sidebar");

        VBox layout = new VBox(10);
        layout.setFillWidth(true);

        HBox linhaFiltro = new HBox(
                20,
                lblFiltro,
                cbFiltroUnidade,
                lblTotal
        );
// ======================================================
// CARD INVENTÁRIO
// ======================================================

        Label lblInventario =
                new Label("📊 Inventário");

        lblInventario.getStyleClass()
                .add("card-title");

        VBox cardInventario =
                new VBox(
                        10,
                        lblInventario,
                        linhaFiltro,
                        tabela
                );

        cardInventario.getStyleClass()
                .add("card");

        VBox.setVgrow(
                cardInventario,
                Priority.ALWAYS
        );

        layout.setPadding(new Insets(20));

        VBox.setVgrow(
                tabela,
                Priority.ALWAYS
        );

        layout.getChildren().addAll(
                titulo,
                cardPesquisa,
                cardCadastro,
                cardInventario
        );

        BorderPane root = new BorderPane();

        StackPane areaConteudo =
                new StackPane();

        areaConteudo.getChildren()
                .add(layout);

        root.setLeft(menuLateral);

        root.setCenter(areaConteudo);


// ======================================
// MÉTRICAS DO DASHBOARD
// ======================================

        int totalAtivos = 0;
        int totalEmpresas = 0;
        int totalLocais = 0;
        int totalEstoque = 0;
        int totalManutencao = 0;

        try {

            Connection conn =
                    Database.connect();

            Statement stmt =
                    conn.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                            "SELECT COUNT(*) total FROM ativos"
                    );

            if (rs.next()) {
                totalAtivos = rs.getInt("total");
            }

            rs = stmt.executeQuery(
                    "SELECT COUNT(*) total FROM empresas"
            );

            if (rs.next()) {
                totalEmpresas = rs.getInt("total");
            }

            rs = stmt.executeQuery(
                    "SELECT COUNT(*) total FROM unidades"
            );

            if (rs.next()) {
                totalLocais = rs.getInt("total");
            }
            rs = stmt.executeQuery(
                    """
                    SELECT COUNT(*) total
                    FROM ativos
                    WHERE status = 'Estoque'
                    """
            );

            if (rs.next()) {
                totalEstoque = rs.getInt("total");
            }

            rs = stmt.executeQuery(
                    """
                    SELECT COUNT(*) total
                    FROM ativos
                    WHERE status = 'Manutenção'
                    """
            );

            if (rs.next()) {
                totalManutencao = rs.getInt("total");
            }

            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Label lblDashboard =
                new Label("📊 Dashboard");

        lblDashboard.getStyleClass()
                .add("card-title");
        Label lblSubtitulo =
                new Label(
                        "Visão geral do ambiente"
                );

        lblSubtitulo.setStyle(
                "-fx-text-fill:#666666;"
        );

// ======================
// Ativos
// ======================

        Label lblAtivos =
                new Label("📦 Ativos");

        lblAtivos.setStyle(
                "-fx-font-size:20px;" +
                        "-fx-font-weight:bold;"
        );

        Label lblTotalAtivos =
                new Label(String.valueOf(totalAtivos));

        lblTotalAtivos.setStyle(
                "-fx-font-size:25px;" +
                        "-fx-font-weight:bold;"
        );


// ======================
// Empresas
// ======================

        Label lblEmpresas =
                new Label("🏢 Empresas");

        lblEmpresas.setStyle(
                "-fx-font-size:22px;" +
                        "-fx-font-weight:bold;"
        );

        Label lblTotalEmpresas =
                new Label(String.valueOf(totalEmpresas));

        lblTotalEmpresas.setStyle(
                "-fx-font-size:25px;" +
                        "-fx-font-weight:bold;"
        );


// ======================
// Localidades
// ======================

        Label lblLocais =
                new Label("📍 Locais");

        lblLocais.setStyle(
                "-fx-font-size:22px;" +
                        "-fx-font-weight:bold;"
        );

        Label lblTotalLocais =
                new Label(String.valueOf(totalLocais));

        Label lblEstoque =
                new Label("📋 Estoque");

        lblEstoque.setStyle(
                "-fx-font-size:22px;" +
                        "-fx-font-weight:bold;"
        );

        Label lblTotalEstoque =
                new Label(String.valueOf(totalEstoque));

        lblTotalEstoque.setStyle(
                "-fx-font-size:25px;" +
                        "-fx-font-weight:bold;"
        );

        Label lblManutencao =
                new Label("🔧 Manutenção");

        lblManutencao.setStyle(
                "-fx-font-size:22px;" +
                        "-fx-font-weight:bold;"
        );

        Label lblTotalManutencao =
                new Label(String.valueOf(totalManutencao));

        lblTotalManutencao.setStyle(
                "-fx-font-size:30px;" +
                        "-fx-font-weight:bold;"
        );
        lblTotalLocais.setStyle(
                "-fx-font-size:25px;" +
                        "-fx-font-weight:bold;"
        );

        VBox cardAtivos =
                new VBox(
                        15,
                        lblAtivos,
                        lblTotalAtivos
                );

        VBox cardEmpresas =
                new VBox(
                        15,
                        lblEmpresas,
                        lblTotalEmpresas
                );

        VBox cardLocais =
                new VBox(
                        15,
                        lblLocais,
                        lblTotalLocais
                );

        VBox cardEstoque =
                new VBox(
                        15,
                        lblEstoque,
                        lblTotalEstoque
                );
        VBox cardManutencao =
                new VBox(
                        15,
                        lblManutencao,
                        lblTotalManutencao
                );
        cardAtivos.getStyleClass()
                .add("card");

        cardEmpresas.getStyleClass()
                .add("card");

        cardLocais.getStyleClass()
                .add("card");

        cardAtivos.getStyleClass()
                .add("dashboard-card");

        cardEstoque.getStyleClass()
                .add("card");

        cardEmpresas.getStyleClass()
                .add("dashboard-card");

        cardLocais.getStyleClass()
                .add("dashboard-card");

        cardEstoque.getStyleClass()
                .add("dashboard-card");

        cardEstoque.getStyleClass()
                .add("card-estoque");
        cardManutencao.getStyleClass()
                .add("card");

        cardManutencao.getStyleClass()
                .add("dashboard-card");

        cardManutencao.getStyleClass()
                .add("card-manutencao");


        HBox linhaCards =
                new HBox(
                        20,
                        cardAtivos,
                        cardEmpresas,
                        cardLocais,
                        cardEstoque,
                        cardManutencao
                );

        HBox.setHgrow(cardAtivos, Priority.ALWAYS);
        HBox.setHgrow(cardEmpresas, Priority.ALWAYS);
        HBox.setHgrow(cardLocais, Priority.ALWAYS);
        HBox.setHgrow(cardEstoque, Priority.ALWAYS);
        HBox.setHgrow(cardManutencao, Priority.ALWAYS);

        cardAtivos.setMaxWidth(Double.MAX_VALUE);
        cardEmpresas.setMaxWidth(Double.MAX_VALUE);
        cardLocais.setMaxWidth(Double.MAX_VALUE);
        cardEstoque.setMaxWidth(Double.MAX_VALUE);
        cardManutencao.setMaxWidth(Double.MAX_VALUE);

        VBox dashboardView =
                new VBox(20);

        dashboardView.setPadding(
                new Insets(20)
        );

        dashboardView.getChildren()
                .addAll(
                        lblDashboard,
                        lblSubtitulo,
                        linhaCards
                );


        EmpresasView empresasView =
                new EmpresasView();
        LocalidadesView localidadesView =
                new LocalidadesView();

// ======================================
// NAVEGAÇÃO
// ======================================

        btnMenuDashboard.setOnAction(e -> {

            ativarMenu(
                    btnMenuDashboard,
                    btnMenuDashboard,
                    btnMenuAtivos,
                    btnMenuEmpresas,
                    btnMenuLocalidades
            );

            areaConteudo.getChildren().clear();

            areaConteudo.getChildren()
                    .add(dashboardView);
        });

        btnMenuAtivos.setOnAction(e -> {

            ativarMenu(
                    btnMenuAtivos,
                    btnMenuDashboard,
                    btnMenuAtivos,
                    btnMenuEmpresas,
                    btnMenuLocalidades
            );

            areaConteudo.getChildren().clear();

            areaConteudo.getChildren()
                    .add(layout);
        });

        btnMenuEmpresas.setOnAction(e -> {

            ativarMenu(
                    btnMenuEmpresas,
                    btnMenuDashboard,
                    btnMenuAtivos,
                    btnMenuEmpresas,
                    btnMenuLocalidades
            );

            areaConteudo.getChildren().clear();

            areaConteudo.getChildren()
                    .add(
                            empresasView.getView()
                    );
        });

        btnMenuLocalidades.setOnAction(e -> {

            ativarMenu(
                    btnMenuLocalidades,
                    btnMenuDashboard,
                    btnMenuAtivos,
                    btnMenuEmpresas,
                    btnMenuLocalidades
            );

            areaConteudo.getChildren().clear();

            areaConteudo.getChildren()
                    .add(
                            localidadesView.getView()
                    );
        });

        Scene scene = new Scene(root, 1300, 750);

        scene.getStylesheets().add(
                getClass()
                        .getResource("/com/ativos/css/app.css")
                        .toExternalForm()
        );

        stage.setTitle("Controle de Ativos");

        stage.setScene(scene);
        stage.setWidth(1100);
        stage.setHeight(700);

        stage.centerOnScreen();
        stage.show();
    }

// =====================================
// MENU ATIVO
// =====================================

    private void ativarMenu(
            Button ativo,
            Button... botoes
    ) {

        for (Button botao : botoes) {

            botao.getStyleClass()
                    .remove("menu-button-active");
        }

        ativo.getStyleClass()
                .add("menu-button-active");
    }

// ======================================================
// EXPORTAÇÃO EXCEL
// ======================================================
    private void exportarExcel() {

        try {

            Workbook workbook =
                    new XSSFWorkbook();

            String dataHora =
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "dd/MM/yyyy HH:mm:ss"
                                    )
                            );
            Sheet abaAtivos =
                    workbook.createSheet(
                            "Ativos"
                    );
            Sheet abaResumo =
                    workbook.createSheet(
                            "Resumo"
                    );

            Row r0 =
                    abaResumo.createRow(0);

            r0.createCell(0)
                    .setCellValue(
                            "SISTEMA DE GESTÃO DE ATIVOS"
                    );

            Row r2 =
                    abaResumo.createRow(2);

            r2.createCell(0)
                    .setCellValue(
                            "Gerado em:"
                    );

            r2.createCell(1)
                    .setCellValue(
                            dataHora
                    );

            Row cabecalho =
                    abaAtivos.createRow(0);

            cabecalho.createCell(0)
                    .setCellValue("Empresa");

            cabecalho.createCell(1)
                    .setCellValue("CD");

            cabecalho.createCell(2)
                    .setCellValue("Equipamento");

            cabecalho.createCell(3)
                    .setCellValue("Marca");

            cabecalho.createCell(4)
                    .setCellValue("Modelo");

            cabecalho.createCell(5)
                    .setCellValue("Serial");

            cabecalho.createCell(6)
                    .setCellValue("Host");

            cabecalho.createCell(7)
                    .setCellValue("Patrimônio");

            cabecalho.createCell(8)
                    .setCellValue("Local");

            cabecalho.createCell(9)
                    .setCellValue("Status");

            cabecalho.createCell(10)
                    .setCellValue("Condição");

            cabecalho.createCell(11)
                    .setCellValue("Situação");

            cabecalho.createCell(12)
                    .setCellValue("Responsável");

            cabecalho.createCell(13)
                    .setCellValue("Observações");


            Connection conn = Database.connect();

            Statement resumoStmt =
                    conn.createStatement();


            Statement stmt =
                    conn.createStatement();

            ResultSet rsResumo =
                    resumoStmt.executeQuery(
                            "SELECT COUNT(*) total FROM ativos"
                    );

            if (rsResumo.next()) {

                Row r5 =
                        abaResumo.createRow(5);

                r5.createCell(0)
                        .setCellValue(
                                "Total de Ativos"
                        );

                r5.createCell(1)
                        .setCellValue(
                                rsResumo.getInt("total")
                        );
            }

            rsResumo =
                    resumoStmt.executeQuery(
                            "SELECT COUNT(*) total FROM empresas"
                    );

            if (rsResumo.next()) {

                Row r6 =
                        abaResumo.createRow(6);

                r6.createCell(0)
                        .setCellValue(
                                "Total de Empresas"
                        );

                r6.createCell(1)
                        .setCellValue(
                                rsResumo.getInt("total")
                        );
                rsResumo =
                        resumoStmt.executeQuery(
                                "SELECT COUNT(*) total FROM unidades"
                        );

                if (rsResumo.next()) {

                    Row r7 =
                            abaResumo.createRow(7);

                    r7.createCell(0)
                            .setCellValue(
                                    "Total de Localidades"
                            );

                    r7.createCell(1)
                            .setCellValue(
                                    rsResumo.getInt("total")
                            );
                }

            }

            ResultSet rs =
                    stmt.executeQuery(
                            "SELECT * FROM ativos"
                    );

            int linhaExcel = 1;

            while (rs.next()) {

                Row linha =
                        abaAtivos.createRow(
                                linhaExcel++
                        );

                linha.createCell(0)
                        .setCellValue(
                                rs.getString("empresa")
                        );

                linha.createCell(1)
                        .setCellValue(
                                rs.getString("cd")
                        );

                linha.createCell(2)
                        .setCellValue(
                                rs.getString("equipamento")
                        );

                linha.createCell(3)
                        .setCellValue(
                                rs.getString("marca")
                        );

                linha.createCell(4)
                        .setCellValue(
                                rs.getString("modelo")
                        );

                linha.createCell(5)
                        .setCellValue(
                                rs.getString("serial")
                        );

                linha.createCell(6)
                        .setCellValue(
                                rs.getString("host")
                        );

                linha.createCell(7)
                        .setCellValue(
                                rs.getString("patrimonio")
                        );

                linha.createCell(8)
                        .setCellValue(
                                rs.getString("local")
                        );

                linha.createCell(9)
                        .setCellValue(
                                rs.getString("status")
                        );

                linha.createCell(10)
                        .setCellValue(
                                rs.getString("condicao")
                        );

                linha.createCell(11)
                        .setCellValue(
                                rs.getString("situacao")
                        );

                linha.createCell(12)
                        .setCellValue(
                                rs.getString("responsavel")
                        );

                linha.createCell(13)
                        .setCellValue(
                                rs.getString("observacoes")
                        );
            }

            for (int i = 0; i < 14; i++) {

                abaAtivos.autoSizeColumn(i);
            }

            String nomeArquivo =
                    "Inventario_Ativos_"
                            + LocalDateTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "yyyy-MM-dd_HHmmss"
                                    )
                            )
                            + ".xlsx";

            FileOutputStream arquivo =
                    new FileOutputStream(
                            nomeArquivo
                    );

            abaResumo.autoSizeColumn(0);
            abaResumo.autoSizeColumn(1);

            workbook.write(arquivo);

            arquivo.close();

            workbook.close();

            conn.close();

            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Arquivo criado: " + nomeArquivo + " ✅"
            ).show();

        } catch (Exception ex) {

            ex.printStackTrace();

            new Alert(
                    Alert.AlertType.ERROR,
                    ex.getMessage()
            ).showAndWait();
        }
    }

// ======================================================
// CARREGAMENTO DE EMPRESAS
// ======================================================

    private void carregarEmpresas(
            ComboBox<String> cbEmpresa
    ) {

        cbEmpresa.getItems().clear();

        EmpresaDAO dao =
                new EmpresaDAO();

        cbEmpresa.getItems().addAll(
                dao.listar()
        );
    }

// ======================================================
// CARREGAMENTO DE LOCALIDADES
// ======================================================

    private void carregarLocalidades(
            ComboBox<String> cbUnidade
    ) {

        cbUnidade.getItems().clear();

        try {

            Connection conn =
                    Database.connect();

            Statement stmt =
                    conn.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                            """
                            SELECT nome
                            FROM unidades
                            ORDER BY nome
                            """
                    );

            while (rs.next()) {

                cbUnidade.getItems().add(
                        rs.getString("nome")
                );
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
// ======================================================
// INICIALIZAÇÃO DA APLICAÇÃO
// ======================================================

    public static void main(String[] args) {
        launch();
    }
}
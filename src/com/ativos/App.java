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

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

import com.ativos.view.DashboardView;
import com.ativos.view.EmpresaView;
import com.ativos.view.LocalidadeView;
import com.ativos.controller.AtivoController;

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
                new Label("Sistema de Gestão de Ativos");

        titulo.setStyle(
                "-fx-font-size:28px;" +
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
                "Selecione a Empresa"
        );

        cbEmpresa.setPrefWidth(300);

// ======================================================
// CARREGAMENTO DE LOCALIDADES
// ======================================================

        ComboBox<String> cbUnidade = new ComboBox<>();

        carregarLocalidades(cbUnidade);

        cbUnidade.setPromptText("Selecione o Local");

        cbEmpresa.setPrefWidth(350);
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

                System.out.println(
                        "Empresa selecionada: "
                                + cbEmpresa.getValue()
                );

                ResultSet rs =
                        ps.executeQuery();

                while (rs.next()) {

                    System.out.println(
                            "Empresa selecionada: "
                                    + cbEmpresa.getValue()
                    );

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
        Button btnLocalidades = new Button("Localidades");
        Button btnEmpresas = new Button("Empresas");
        Button btnDashboard = new Button("Dashboard");
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

        btnLocalidades.setStyle(
                "-fx-background-color:#F57C00;" +
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

        txtBusca.setPrefWidth(950);

        Button btnBuscar = new Button("Buscar");

        btnBuscar.setPrefWidth(120);
        btnBuscar.setPrefHeight(35);

        btnBuscar.setStyle(
                "-fx-background-color:#1976D2;" +
                        "-fx-text-fill:white;" +
                        "-fx-font-weight:bold;"
        );

        lblTotal.setStyle(
                "-fx-font-size:18px;" +
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
        tabela.setPrefHeight(500);
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

        btnLocalidades.setOnAction(
                e -> new LocalidadeView().show()
        );

        btnEmpresas.setOnAction(e -> {

            EmpresaView view =
                    new EmpresaView();

            view.show();

            view.getJanela().setOnHidden(event ->
                    carregarEmpresas(cbEmpresa)
            );
        });

        btnDashboard.setOnAction(
                e -> new DashboardView().show()
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
        cbEmpresa.setMaxWidth(Double.MAX_VALUE);
        cbUnidade.setMaxWidth(Double.MAX_VALUE);

        HBox linhaEmpresaUnidade = new HBox(
                10,
                cbEmpresa,
                cbUnidade
        );

        HBox.setHgrow(cbEmpresa, Priority.ALWAYS);
        HBox.setHgrow(cbUnidade, Priority.ALWAYS);
        linhaEmpresaUnidade.setAlignment(Pos.CENTER_LEFT);

        HBox linhaBusca = new HBox(
                10,
                txtBusca,
                btnBuscar
        );

        linhaBusca.setAlignment(Pos.CENTER);

        HBox linhaBotoes = new HBox(
                10,
                btnSalvar,
                btnEditar,
                btnExcluir,
                btnLocalidades,
                btnEmpresas,
                btnDashboard,
                btnExportar
        );
        linhaBotoes.setAlignment(Pos.CENTER_LEFT);

        VBox layout = new VBox(6);
        HBox linhaFiltro = new HBox(
                20,
                lblFiltro,
                cbFiltroUnidade,
                lblTotal
        );

        layout.setPadding(new Insets(20));

        layout.getChildren().addAll(

                titulo,

                linhaEmpresaUnidade,

                linhaBusca,

                linhaEquipamento,

                linhaSerial,

                linhaPatrimonio,

                linhaStatus,

                txtResponsavel,

                txtObs,

                linhaBotoes,

                linhaFiltro,

                tabela
        );

        Scene scene = new Scene(layout, 1000, 600);
        System.out.println(
                getClass().getResource("/css/app.css")
        );

        stage.setTitle("Controle de Ativos");

        stage.setScene(scene);

        stage.show();

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
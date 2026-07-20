package com.ativos;

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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App extends Application {

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

        carregarTabela(
                tabela,
                cbFiltroUnidade,
                lblTotal
        );

// ======================================================
// EVENTO BUSCAR
// ======================================================

        btnBuscar.setOnAction(
                e -> buscarAtivos(
                        tabela,
                        cbEmpresa,
                        cbUnidade,
                        txtBusca
                )
        );

        txtBusca.textProperty().addListener(
                (obs, antigo, novo) ->
                        buscarAtivos(
                                tabela,
                                cbEmpresa,
                                cbUnidade,
                                txtBusca
                        )
        );
        cbFiltroUnidade.setOnAction(e ->
                        carregarTabela(
                                tabela,
                                cbFiltroUnidade,
                                lblTotal

                        )
        );

// ======================================================
// EVENTO EDITAR
// ======================================================

        btnEditar.setOnAction(e -> {

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

            // índice 4 = patrimônio
            String patrimonioSelecionado =
                    ativo[4];

            AtivoDAO dao = new AtivoDAO();

            Ativo ativoSelecionado =
                    dao.buscarPorPatrimonio(
                            patrimonioSelecionado
                    );

            if (ativoSelecionado != null) {

                cbEmpresa.setValue(
                        ativoSelecionado.getEmpresa()
                );

                cbUnidade.setValue(
                        ativoSelecionado.getCd()
                );

                txtEquipamento.setText(
                        ativoSelecionado.getEquipamento()
                );

                txtMarca.setText(
                        ativoSelecionado.getMarca()
                );

                txtModelo.setText(
                        ativoSelecionado.getModelo()
                );

                txtSerial.setText(
                        ativoSelecionado.getSerial()
                );

                txtHost.setText(
                        ativoSelecionado.getHost()
                );

                txtPatrimonio.setText(
                        ativoSelecionado.getPatrimonio()
                );

                patrimonioOriginal[0] =
                        ativoSelecionado.getPatrimonio();

                txtLocal.setText(
                        ativoSelecionado.getLocal()
                );

                txtResponsavel.setText(
                        ativoSelecionado.getResponsavel()
                );

                txtObs.setText(
                        ativoSelecionado.getObservacoes()
                );

                cbStatus.setValue(
                        ativoSelecionado.getStatus()
                );

                cbCondicao.setValue(
                        ativoSelecionado.getCondicao()
                );

                cbSituacao.setValue(
                        ativoSelecionado.getSituacao()
                );

                modoEdicao[0] = true;
            }

        });

// ======================================================
// EVENTO EXCLUIR
// ======================================================

        btnExcluir.setOnAction(e -> {

            String[] ativo =
                    tabela.getSelectionModel()
                            .getSelectedItem();

            if (ativo == null) {

                new Alert(
                        Alert.AlertType.WARNING,
                        "Selecione um ativo para excluir."
                ).show();

                return;
            }

            // índice 4 = patrimônio
            String patrimonioSelecionado =
                    ativo[4];

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

                AtivoDAO dao = new AtivoDAO();
                dao.excluir(
                        patrimonioSelecionado
                );

                carregarTabela(
                        tabela,
                        cbFiltroUnidade,
                        lblTotal
                );

                new Alert(
                        Alert.AlertType.INFORMATION,
                        "Registro excluído com sucesso ✅"
                ).show();

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        btnLocalidades.setOnAction(
                e -> new LocalidadeView().show()
        );

        btnEmpresas.setOnAction(
                e -> new EmpresaView().show()
        );

        btnDashboard.setOnAction(
                e -> new DashboardView().show()
        );

// ======================================================
// EVENTO SALVAR
// ======================================================

        btnSalvar.setOnAction(e -> {

            try {

                Connection conn = Database.connect();

                if (cbUnidade.getValue() == null
                        || txtEquipamento.getText().trim().isEmpty()
                        || txtSerial.getText().trim().isEmpty()
                        || txtPatrimonio.getText().trim().isEmpty()) {

                    new Alert(
                            Alert.AlertType.WARNING,
                            "ENTROU NO IF DE VALIDACAO"
                    ).show();

                    return;
                }

                if (modoEdicao[0]) {

                    Ativo ativo = new Ativo(
                            cbEmpresa.getValue(),
                            cbUnidade.getValue(),
                            txtEquipamento.getText(),
                            txtMarca.getText(),
                            txtModelo.getText(),
                            txtSerial.getText(),
                            txtHost.getText(),
                            txtPatrimonio.getText(),
                            txtLocal.getText(),
                            cbStatus.getValue(),
                            cbCondicao.getValue(),
                            cbSituacao.getValue(),
                            txtResponsavel.getText(),
                            txtObs.getText()
                    );

                    AtivoDAO dao = new AtivoDAO();

                    dao.atualizar(
                            ativo,
                            patrimonioOriginal[0]
                    );

                    modoEdicao[0] = false;

                    carregarTabela(
                            tabela,
                            cbFiltroUnidade,
                            lblTotal
                    );

                    new Alert(
                            Alert.AlertType.INFORMATION,
                            "Registro atualizado com sucesso ✅"
                    ).show();

                    return;
                }

                String checkSql =
                        "SELECT COUNT(*) FROM ativos WHERE patrimonio = ? OR serial = ?";

                PreparedStatement checkStmt =
                        conn.prepareStatement(checkSql);

                checkStmt.setString(
                        1,
                        txtPatrimonio.getText().trim()
                );

                checkStmt.setString(
                        2,
                        txtSerial.getText().trim()
                );

                ResultSet rs =
                        checkStmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {

                    new Alert(
                            Alert.AlertType.ERROR,
                            "Serial ou patrimônio já cadastrado ❌"
                    ).show();

                    conn.close();

                    return;
                }

                conn.close();

                Ativo novoAtivo = new Ativo(
                        cbEmpresa.getValue(),
                        cbUnidade.getValue(),
                        txtEquipamento.getText(),
                        txtMarca.getText(),
                        txtModelo.getText(),
                        txtSerial.getText(),
                        txtHost.getText(),
                        txtPatrimonio.getText(),
                        txtLocal.getText(),
                        cbStatus.getValue(),
                        cbCondicao.getValue(),
                        cbSituacao.getValue(),
                        txtResponsavel.getText(),
                        txtObs.getText()
                );

                AtivoDAO dao = new AtivoDAO();
                dao.salvar(novoAtivo);

                carregarTabela(
                        tabela,
                        cbFiltroUnidade,
                        lblTotal
                );

                new Alert(
                        Alert.AlertType.INFORMATION,
                        "Ativo salvo com sucesso ✅"
                ).show();

                limparFormulario(
                        cbUnidade,
                        txtEquipamento,
                        txtMarca,
                        txtModelo,
                        txtSerial,
                        txtPatrimonio,
                        txtHost,
                        txtLocal,
                        txtResponsavel,
                        txtObs,
                        cbStatus,
                        cbCondicao,
                        cbSituacao
                );

            } catch (Exception ex) {

                ex.printStackTrace();

                new Alert(
                        Alert.AlertType.ERROR,
                        "Erro ao salvar ❌"
                ).show();
            }
        });

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

        stage.setTitle("Controle de Ativos");

        stage.setScene(scene);

        stage.show();


    }

    private void exportarExcel() {

        new Alert(
                Alert.AlertType.INFORMATION,
                "Método exportarExcel() funcionando ✅"
        ).show();
    }

    private void limparFormulario(
            ComboBox<String> cbUnidade,
            TextField txtEquipamento,
            TextField txtMarca,
            TextField txtModelo,
            TextField txtSerial,
            TextField txtPatrimonio,
            TextField txtHost,
            TextField txtLocal,
            TextField txtResponsavel,
            TextArea txtObs,
            ComboBox<String> cbStatus,
            ComboBox<String> cbCondicao,
            ComboBox<String> cbSituacao
    ) {

        cbUnidade.setValue(null);

        txtEquipamento.clear();
        txtMarca.clear();
        txtModelo.clear();
        txtSerial.clear();
        txtPatrimonio.clear();
        txtHost.clear();
        txtLocal.clear();
        txtResponsavel.clear();
        txtObs.clear();

        cbStatus.setValue(null);
        cbCondicao.setValue(null);
        cbSituacao.setValue(null);
    }

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

    private void carregarTabela(
            TableView<String[]> tabela,
            ComboBox<String> cbFiltroUnidade,
            Label lblTotal
    ) {

        tabela.getItems().clear();


        try {

            Connection conn = Database.connect();

            if (cbFiltroUnidade.getValue().equals("Todos")) {

                Statement stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery(
                        """
                        SELECT
                            empresa,
                            cd,
                            equipamento,
                            host,
                            patrimonio,
                            local,
                            responsavel
                        FROM ativos
                        ORDER BY cd
                        """
                );

                while (rs.next()) {

                    tabela.getItems().add(
                            new String[]{
                                    rs.getString("empresa"),
                                    rs.getString("cd"),
                                    rs.getString("equipamento"),
                                    rs.getString("host"),
                                    rs.getString("patrimonio"),
                                    rs.getString("local"),
                                    rs.getString("responsavel")
                            }
                    );
                }

            } else {

                PreparedStatement ps =
                        conn.prepareStatement(
                                """
                                SELECT
                                    empresa,
                                    cd,
                                    equipamento,
                                    host,
                                    patrimonio,
                                    local,
                                    responsavel
                                FROM ativos
                                WHERE cd = ?
                                ORDER BY cd
                                """
                        );

                ps.setString(
                        1,
                        cbFiltroUnidade.getValue()
                );

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    tabela.getItems().add(
                            new String[]{
                                    rs.getString("empresa"),
                                    rs.getString("cd"),
                                    rs.getString("equipamento"),
                                    rs.getString("host"),
                                    rs.getString("patrimonio"),
                                    rs.getString("local"),
                                    rs.getString("responsavel")
                            }
                    );
                }
            }

            lblTotal.setText(
                    "Total de Ativos: "
                            + tabela.getItems().size()
            );

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    private void buscarAtivos(
            TableView<String[]> tabela,
            ComboBox<String> cbEmpresa,
            ComboBox<String> cbUnidade,
            TextField txtBusca
    ) {

        tabela.getItems().clear();

        try {

            Connection conn =
                    Database.connect();

            PreparedStatement ps =
                    conn.prepareStatement(
                            """
                            SELECT
                                empresa,
                                cd,
                                equipamento,
                                host,
                                patrimonio,
                                local,
                                responsavel
                            FROM ativos
                            WHERE empresa = ?
                            AND cd = ?
                              AND (
                                    patrimonio LIKE ?
                                 OR host LIKE ?
                                 OR responsavel LIKE ?
                                 OR equipamento LIKE ?
                              )
                            ORDER BY cd
                            """
                    );

            String busca =
                    "%" + txtBusca.getText().trim() + "%";

            String empresa =
                    cbEmpresa.getValue();

            String local =
                    cbUnidade.getValue();

            ps.setString(1, empresa);
            ps.setString(2, local);

            ps.setString(3, busca);
            ps.setString(4, busca);
            ps.setString(5, busca);
            ps.setString(6, busca);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                tabela.getItems().add(
                        new String[]{
                                rs.getString("empresa"),
                                rs.getString("cd"),
                                rs.getString("equipamento"),
                                rs.getString("host"),
                                rs.getString("patrimonio"),
                                rs.getString("local"),
                                rs.getString("responsavel")
                        }
                );
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
     public static void main(String[] args) {
        launch();
    }
}
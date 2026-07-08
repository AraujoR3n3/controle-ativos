package com.ativos;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        Label titulo =
                new Label("Sistema de Gestão de Ativos");

        titulo.setStyle(
                "-fx-font-size:28px;" +
                        "-fx-font-weight:bold;"
        );
        titulo.setMaxWidth(Double.MAX_VALUE);
        titulo.setAlignment(Pos.CENTER);
        // Seleção de empresa
        ComboBox<String> cbEmpresa =
                new ComboBox<>();
        try {

            Connection conn = Database.connect();

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

        cbEmpresa.setPromptText(
                "Selecione a Empresa"
        );
        cbEmpresa.setPrefWidth(300);

        ComboBox<String> cbUnidade = new ComboBox<>();
        try {

            Connection conn = Database.connect();

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
        Button btnExportar = new Button("Exportar CSV");
        Button btnExcluir = new Button("Excluir");
        Button btnNovoLocal = new Button("Novo Local");
        final boolean[] modoEdicao = {false};
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

        btnNovoLocal.setStyle(
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

        TableView<String[]> tabela = new TableView<>();
        tabela.setPrefHeight(500);

        TableColumn<String[], String> colEmpresa =
                new TableColumn<>("Empresa");

        colEmpresa.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue()[0])
        );
        TableColumn<String[], String> colUnidade =
                new TableColumn<>("Localidade");

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

        TableColumn<String[], String> colLocal =
                new TableColumn<>("Local Físico");

        colLocal.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue()[4])
        );

        colUnidade.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue()[1])
        );

        TableColumn<String[], String> colPatrimonio =
                new TableColumn<>("Patrimônio");

        colPatrimonio.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue()[3])
        );

        TableColumn<String[], String> colSerial =
                new TableColumn<>("Serial");

        colSerial.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue()[2])
        );

        TableColumn<String[], String> colResponsavel =
                new TableColumn<>("Responsável");

        colResponsavel.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue()[5])
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

        Runnable carregarTabela = () -> {

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
                        ); }
                }
                else {

                    PreparedStatement ps = conn.prepareStatement(
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

                    ps.setString(1, cbFiltroUnidade.getValue());

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

                conn. close();

            }
                catch (Exception ex) {

                ex.printStackTrace();
            }
        };

        carregarTabela.run();
        btnBuscar.setOnAction(e -> {

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
                                           WHERE patrimonio LIKE ?
                                              OR host LIKE ?
                                              OR responsavel LIKE ?
                                              OR equipamento LIKE ?
                                           ORDER BY cd
                                """
                        );

                String busca =
                        "%" + txtBusca.getText().trim() + "%";

                ps.setString(1, busca);
                ps.setString(2, busca);
                ps.setString(3, busca);
                ps.setString(4, busca);

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

        });
        txtBusca.textProperty().addListener(
                (obs, antigo, novo) -> {

                    btnBuscar.fire();

                }
        );
        cbFiltroUnidade.setOnAction(
                e -> carregarTabela.run()
        );
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

            String patrimonioSelecionado =
                    ativo[3];

            try {

                Connection conn = Database.connect();

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

                    cbUnidade.setValue(rs.getString("cd"));
                    txtEquipamento.setText(rs.getString("equipamento"));
                    txtMarca.setText(rs.getString("marca"));
                    txtModelo.setText(rs.getString("modelo"));
                    txtSerial.setText(rs.getString("serial"));
                    txtHost.setText(rs.getString("host"));
                    txtPatrimonio.setText(rs.getString("patrimonio"));
                    txtLocal.setText(rs.getString("local"));
                    txtResponsavel.setText(rs.getString("responsavel"));
                    txtObs.setText(rs.getString("observacoes"));

                    cbStatus.setValue(rs.getString("status"));
                    cbCondicao.setValue(rs.getString("condicao"));
                    cbSituacao.setValue(rs.getString("situacao"));
                    modoEdicao[0] = true;
                }

                conn.close();

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

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

            String patrimonioSelecionado =
                    ativo[3];

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

                Connection conn = Database.connect();

                PreparedStatement ps =
                        conn.prepareStatement(
                                """
                                        DELETE FROM ativos
                                        WHERE patrimonio = ?
                                        """
                        );

                ps.setString(
                        1,
                        patrimonioSelecionado
                );

                ps.executeUpdate();

                conn.close();

                carregarTabela.run();

                new Alert(
                        Alert.AlertType.INFORMATION,
                        "Registro excluído com sucesso ✅"
                ).show();

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        btnNovoLocal.setOnAction(e -> {

            if (cbEmpresa.getValue() == null) {

                new Alert(
                        Alert.AlertType.WARNING,
                        "Selecione uma empresa primeiro."
                ).show();

                return;
            }

            TextInputDialog dialog =
                    new TextInputDialog();

            dialog.setTitle("Novo Local");

            dialog.setHeaderText(
                    "Cadastrar novo local"
            );

            dialog.setContentText(
                    "Nome da Unidade:"
            );

            dialog.showAndWait().ifPresent(nome -> {

                try {

                    Connection conn =
                            Database.connect();

                    PreparedStatement psEmpresa =
                            conn.prepareStatement(
                                    """
                                    SELECT id
                                    FROM empresas
                                    WHERE nome = ?
                                    """
                            );

                    psEmpresa.setString(
                            1,
                            cbEmpresa.getValue()
                    );

                    ResultSet rsEmpresa =
                            psEmpresa.executeQuery();

                    if (rsEmpresa.next()) {

                        int empresaId =
                                rsEmpresa.getInt("id");

                        PreparedStatement ps =
                                conn.prepareStatement(
                                        """
                                        INSERT INTO unidades
                                        (
                                            nome,
                                            empresa_id
                                        )
                                        VALUES (?, ?)
                                        """
                                );

                        ps.setString(
                                1,
                                nome.trim()
                        );

                        ps.setInt(
                                2,
                                empresaId
                        );

                        ps.executeUpdate();

                        cbUnidade.getItems().add(
                                nome.trim()
                        );
                    }

                    conn.close();

                    new Alert(
                            Alert.AlertType.INFORMATION,
                            "Unidade cadastrada com sucesso ✅"
                    ).show();

                } catch (Exception ex) {

                    new Alert(
                            Alert.AlertType.ERROR,
                            "Unidade já existe ou ocorreu um erro."
                    ).show();
                }
            });
        });

        btnSalvar.setOnAction(e -> {

            try {

                Connection conn = Database.connect();
                if (cbUnidade.getValue() == null ||
                        txtEquipamento.getText().trim().isEmpty() ||
                        txtSerial.getText().trim().isEmpty() ||
                        txtPatrimonio.getText().trim().isEmpty()) {

                    new Alert(
                            Alert.AlertType.WARNING,
                            """
                                    Preencha os campos obrigatórios:
                                    
                                    • CD
                                    • Equipamento
                                    • Serial
                                    • Patrimônio
                                    """
                    ).show();

                    return;
                }

                if (conn == null) {

                    new Alert(
                            Alert.AlertType.ERROR,
                            "Erro na conexão com banco ❌"
                    ).show();

                    return;
                }

                if (modoEdicao[0]) {

                    String sql =
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
                                        local = ?,
                                        status = ?,
                                        condicao = ?,
                                        situacao = ?,
                                        observacoes = ?,
                                        responsavel = ?
                                    WHERE patrimonio = ?
                                    """;

                    PreparedStatement stmt =
                            conn.prepareStatement(sql);

                    stmt.setString(1, cbUnidade.getValue());
                    stmt.setString(2, txtEquipamento.getText());
                    stmt.setString(3, txtMarca.getText());
                    stmt.setString(4, txtModelo.getText());
                    stmt.setString(5, txtSerial.getText());
                    stmt.setString(6, txtHost.getText());

                    stmt.setString(7, txtLocal.getText());

                    stmt.setString(8, cbStatus.getValue());
                    stmt.setString(9, cbCondicao.getValue());
                    stmt.setString(10, cbSituacao.getValue());

                    stmt.setString(11, txtObs.getText());
                    stmt.setString(12, txtResponsavel.getText());

                    stmt.setString(
                            13,
                            txtPatrimonio.getText()
                    );

                    stmt.executeUpdate();

                    modoEdicao[0] = false;

                    carregarTabela.run();

                    conn.close();

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

                checkStmt.setString(1, txtPatrimonio.getText().trim());
                checkStmt.setString(2, txtSerial.getText().trim());

                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {

                    new Alert(
                            Alert.AlertType.ERROR,
                            "Serial ou patrimônio já cadastrado ❌"
                    ).show();

                    conn.close();
                    return;
                }

                String sql =
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
                                                                                  observacoes,
                                                                                  responsavel
                                                                              )
                                                                              VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                                """;

                PreparedStatement stmt =
                        conn.prepareStatement(sql);

                stmt.setString(1, cbEmpresa.getValue());
                stmt.setString(2, cbUnidade.getValue());
                stmt.setString(3, txtEquipamento.getText());
                stmt.setString(4, txtMarca.getText());
                stmt.setString(5, txtModelo.getText());
                stmt.setString(6, txtSerial.getText());
                stmt.setString(7, txtHost.getText());
                stmt.setString(8, txtPatrimonio.getText());
                stmt.setString(9, txtLocal.getText());
                stmt.setString(10, cbStatus.getValue());
                stmt.setString(11, cbCondicao.getValue());
                stmt.setString(12, cbSituacao.getValue());
                stmt.setString(13, txtObs.getText());
                stmt.setString(14, txtResponsavel.getText());

                stmt.executeUpdate();
                carregarTabela.run();
                conn.close();

                new Alert(
                        Alert.AlertType.INFORMATION,
                        "Ativo salvo com sucesso ✅"
                ).show();

                cbUnidade.setValue(null);
                txtEquipamento.clear();
                txtMarca.clear();
                txtModelo.clear();
                txtSerial.clear();
                txtPatrimonio.clear();
                cbStatus.setValue(null);
                txtObs.clear();
                txtHost.clear();
                txtLocal.clear();
                txtResponsavel.clear();

                cbCondicao.setValue(null);
                cbSituacao.setValue(null);

            } catch (Exception ex) {

                ex.printStackTrace();

                new Alert(
                        Alert.AlertType.ERROR,
                        "Erro ao salvar ❌"
                ).show();
            }
        });

        btnExportar.setOnAction(e -> {

            try {

                FileWriter writer =
                        new FileWriter("ativos.csv");

                writer.append(
                        "CD;Equipamento;Marca;Modelo;Serial;Host;Patrimonio;Local;Status;Condicao;Situacao;Responsavel;Observacoes\n"
                );

                Connection conn = Database.connect();

                Statement stmt =
                        conn.createStatement();

                ResultSet rs =
                        stmt.executeQuery(
                                "SELECT * FROM ativos"
                        );

                while (rs.next()) {

                    writer.append(
                            rs.getString("cd") + ";"
                                    + rs.getString("equipamento") + ";"
                                    + rs.getString("marca") + ";"
                                    + rs.getString("modelo") + ";"
                                    + rs.getString("serial") + ";"
                                    + rs.getString("host") + ";"
                                    + rs.getString("patrimonio") + ";"
                                    + rs.getString("local") + ";"
                                    + rs.getString("status") + ";"
                                    + rs.getString("condicao") + ";"
                                    + rs.getString("situacao") + ";"
                                    + rs.getString("responsavel") + ";"
                                    + rs.getString("observacoes")
                                    + "\n"
                    );
                }

                writer.close();
                conn.close();

                new Alert(
                        Alert.AlertType.INFORMATION,
                        "Arquivo ativos.csv criado ✅"
                ).show();

            } catch (Exception ex) {

                ex.printStackTrace();

                new Alert(
                        Alert.AlertType.ERROR,
                        "Erro ao exportar ❌"
                ).show();
            }
        });

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
                btnNovoLocal,
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

    public static void main(String[] args) {
        launch();
    }
}
package com.ativos.controller;

import com.ativos.AtivoDAO;
import com.ativos.Database;
import com.ativos.util.FormularioUtil;

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
import java.sql.Statement;

public class AtivoController {

    // ======================================================
    // CONTROLLER DE ATIVOS
    // ======================================================



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

            // Atualiza tabela após exclusão

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

            // ======================================
            // UPDATE
            // ======================================

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
                            patrimonio = ?,
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

                stmt.setString(1, cbEmpresa.getValue());
                stmt.setString(2, cbUnidade.getValue());
                stmt.setString(3, txtEquipamento.getText());
                stmt.setString(4, txtMarca.getText());
                stmt.setString(5, txtModelo.getText());
                stmt.setString(6, txtSerial.getText());
                stmt.setString(7, txtHost.getText());
                stmt.setString(8, txtPatrimonio.getText());
                stmt.setString(9, txtLocal.getText());

                stmt.setString(
                        10,
                        cbStatus.getValue() == null
                                ? ""
                                : cbStatus.getValue()
                );

                stmt.setString(
                        11,
                        cbCondicao.getValue() == null
                                ? ""
                                : cbCondicao.getValue()
                );

                stmt.setString(
                        12,
                        cbSituacao.getValue() == null
                                ? ""
                                : cbSituacao.getValue()
                );

                stmt.setString(13, txtObs.getText());
                stmt.setString(14, txtResponsavel.getText());
                stmt.setString(15, patrimonioOriginal[0]);

                stmt.executeUpdate();

                modoEdicao[0] = false;

                conn.close();

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

            // ======================================
            // VALIDA DUPLICIDADE
            // ======================================

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

            // ======================================
            // INSERT
            // ======================================

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

            conn.close();

            carregarTabela(
                    tabela,
                    cbFiltroUnidade,
                    lblTotal
            );

            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Ativo salvo com sucesso ✅"
            ).show();

            FormularioUtil.limparFormulario(
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
    }

// ======================================================
// CARREGAR TABELA
// ======================================================

    public void carregarTabela(
            TableView<String[]> tabela,
            ComboBox<String> cbFiltroUnidade,
            Label lblTotal
    ) {

        // Limpa tabela antes de carregar

        tabela.getItems().clear();

        try {

            Connection conn =
                    Database.connect();

            // ======================================
            // EXIBE TODOS OS REGISTROS
            // ======================================

            if ("Todos".equals(
                    cbFiltroUnidade.getValue()
            )) {

                Statement stmt =
                        conn.createStatement();

                ResultSet rs =
                        stmt.executeQuery(
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

            }

            // ======================================
            // EXIBE APENAS O CD SELECIONADO
            // ======================================

            else {

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

                ResultSet rs =
                        ps.executeQuery();

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

            // Atualiza contador

            lblTotal.setText(
                    "Total de Ativos: "
                            + tabela.getItems().size()
            );

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
// ======================================================
// BUSCAR ATIVOS
// ======================================================

    public void buscarAtivos(
            TableView<String[]> tabela,
            ComboBox<String> cbEmpresa,
            ComboBox<String> cbUnidade,
            TextField txtBusca
    ) {

        // Limpa resultados anteriores

        if (cbEmpresa.getValue() == null ||
                cbUnidade.getValue() == null) {
            return;
        }

        tabela.getItems().clear();
        try {

            Connection conn =
                    Database.connect();

            System.out.println(
                    "Empresa: " + cbEmpresa.getValue()
            );

            System.out.println(
                    "Local: " + cbUnidade.getValue()
            );

            System.out.println(
                    "Busca: " + txtBusca.getText()
            );

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

            ResultSet rs =
                    ps.executeQuery();

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

            new Alert(
                    Alert.AlertType.ERROR,
                    "Erro ao pesquisar ativos ❌"
            ).show();
        }
    }
}

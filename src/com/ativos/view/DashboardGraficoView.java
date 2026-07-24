package com.ativos.view;

import com.ativos.Database;

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardGraficoView {

    // =====================================
    // Janela única
    // Evita múltiplos dashboards abertos
    // =====================================

    private static Stage janela;

    public void show() {

        // =====================================
        // Se já estiver aberta
        // apenas traz para frente
        // =====================================

        if (janela != null && janela.isShowing()) {

            janela.toFront();
            return;
        }

        // =====================================
        // Criação da janela
        // =====================================

        janela = new Stage();

        janela.setTitle("Dashboard");

        // =====================================
        // Indicadores
        // =====================================

        Label lblAtivos =
                new Label("Total de Ativos: 0");

        Label lblEmpresas =
                new Label("Total de Empresas: 0");

        Label lblLocalidades =
                new Label("Total de Localidades: 0");

        // =====================================
        // Gráfico de Ativos por Localidade
        // =====================================

        CategoryAxis eixoX =
                new CategoryAxis();

        NumberAxis eixoY =
                new NumberAxis();

        BarChart<String, Number> grafico =
                new BarChart<>(
                        eixoX,
                        eixoY
                );

        grafico.setTitle(
                "Ativos por Localidade"
        );

        XYChart.Series<String, Number> serie =
                new XYChart.Series<>();

        // =====================================
        // Gráfico de Ativos por Status
        // =====================================

        CategoryAxis eixoXStatus =
                new CategoryAxis();

        NumberAxis eixoYStatus =
                new NumberAxis();

        BarChart<String, Number> graficoStatus =
                new BarChart<>(
                        eixoXStatus,
                        eixoYStatus
                );

        graficoStatus.setTitle(
                "Ativos por Status"
        );

        XYChart.Series<String, Number> serieStatus =
                new XYChart.Series<>();

        serieStatus.setName(
                "Quantidade"
        );

        // =====================================
        // Gráfico de Ativos por Empresa
        // =====================================

        CategoryAxis eixoXEmpresa =
                new CategoryAxis();

        NumberAxis eixoYEmpresa =
                new NumberAxis();

        BarChart<String, Number> graficoEmpresa =
                new BarChart<>(
                        eixoXEmpresa,
                        eixoYEmpresa
                );

        graficoEmpresa.setTitle(
                "Ativos por Empresa"
        );

        XYChart.Series<String, Number> serieEmpresa =
                new XYChart.Series<>();

        serieEmpresa.setName(
                "Quantidade"
        );

        // =====================================
        // Carregar informações do banco
        // =====================================

        try {

            Connection conn =
                    Database.connect();

            Statement indicadores =
                    conn.createStatement();

            // =====================================
            // Total de Ativos
            // =====================================

            ResultSet rsIndicador =
                    indicadores.executeQuery(
                            "SELECT COUNT(*) total FROM ativos"
                    );

            if (rsIndicador.next()) {

                lblAtivos.setText(
                        "Total de Ativos: "
                                + rsIndicador.getInt("total")
                );
            }

            // =====================================
            // Total de Empresas
            // =====================================

            rsIndicador =
                    indicadores.executeQuery(
                            "SELECT COUNT(*) total FROM empresas"
                    );

            if (rsIndicador.next()) {

                lblEmpresas.setText(
                        "Total de Empresas: "
                                + rsIndicador.getInt("total")
                );
            }

            // =====================================
            // Total de Localidades
            // =====================================

            rsIndicador =
                    indicadores.executeQuery(
                            "SELECT COUNT(*) total FROM unidades"
                    );

            if (rsIndicador.next()) {

                lblLocalidades.setText(
                        "Total de Localidades: "
                                + rsIndicador.getInt("total")
                );
            }

            Statement stmt =
                    conn.createStatement();

            // =====================================
            // Ativos por Localidade
            // =====================================

            ResultSet rsLocal =
                    stmt.executeQuery(
                            """
                            SELECT
                                cd,
                                COUNT(*) total
                            FROM ativos
                            GROUP BY cd
                            ORDER BY total DESC
                            """
                    );

            while (rsLocal.next()) {

                serie.getData().add(
                        new XYChart.Data<>(
                                rsLocal.getString("cd"),
                                rsLocal.getInt("total")
                        )
                );
            }

            // =====================================
            // Ativos por Status
            // =====================================

            ResultSet rsStatus =
                    stmt.executeQuery(
                            """
                            SELECT
                                status,
                                COUNT(*) total
                            FROM ativos
                            GROUP BY status
                            ORDER BY total DESC
                            """
                    );

            while (rsStatus.next()) {

                serieStatus.getData().add(
                        new XYChart.Data<>(
                                rsStatus.getString("status"),
                                rsStatus.getInt("total")
                        )
                );
            }

            // =====================================
            // Ativos por Empresa
            // =====================================

            ResultSet rsEmpresa =
                    stmt.executeQuery(
                            """
                            SELECT
                                empresa,
                                COUNT(*) total
                            FROM ativos
                            GROUP BY empresa
                            ORDER BY total DESC
                            """
                    );

            while (rsEmpresa.next()) {

                serieEmpresa.getData().add(
                        new XYChart.Data<>(
                                rsEmpresa.getString("empresa"),
                                rsEmpresa.getInt("total")
                        )
                );
            }

            // =====================================
            // Fechar recursos
            // =====================================

            rsLocal.close();
            rsStatus.close();
            rsEmpresa.close();

            stmt.close();
            indicadores.close();

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        // =====================================
        // Adicionar séries aos gráficos
        // =====================================

        grafico.getData().add(
                serie
        );

        graficoStatus.getData().add(
                serieStatus
        );

        graficoEmpresa.getData().add(
                serieEmpresa
        );

        // =====================================
        // Layout
        // =====================================

        VBox layout =
                new VBox(
                        15,
                        lblAtivos,
                        lblEmpresas,
                        lblLocalidades,
                        grafico,
                        graficoStatus,
                        graficoEmpresa
                );

        // =====================================
        // Cena
        // =====================================

        janela.setScene(
                new Scene(
                        layout,
                        800,
                        600
                )
        );

        janela.show();
    }
}
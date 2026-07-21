package com.ativos.util;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FormularioUtil {

    // ======================================================
    // LIMPAR FORMULÁRIO
    // ======================================================

    public static void limparFormulario(
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

        // Limpa seleção da unidade

        cbUnidade.setValue(null);

        // Limpa campos de texto

        txtEquipamento.clear();
        txtMarca.clear();
        txtModelo.clear();

        txtSerial.clear();
        txtPatrimonio.clear();

        txtHost.clear();

        txtLocal.clear();

        txtResponsavel.clear();

        txtObs.clear();

        // Limpa ComboBoxes

        cbStatus.setValue(null);
        cbCondicao.setValue(null);
        cbSituacao.setValue(null);
    }
}
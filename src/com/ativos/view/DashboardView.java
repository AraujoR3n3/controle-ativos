package com.ativos.view;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardView {

    private VBox view;

    public DashboardView() {

        view = new VBox();

        view.getChildren().add(
                new Label("Dashboard funcionando ✅")
        );
    }

    public VBox getView() {
        return view;
    }
}
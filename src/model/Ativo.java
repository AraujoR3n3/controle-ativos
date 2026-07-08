package com.ativos.model;

public class Ativo {

    private int id;
    private String equipamento;
    private String marca;
    private String modelo;
    private String serial;
    private String patrimonio;
    private String status;

    public Ativo(int id, String equipamento, String marca, String modelo,
                 String serial, String patrimonio, String status) {

        this.id = id;
        this.equipamento = equipamento;
        this.marca = marca;
        this.modelo = modelo;
        this.serial = serial;
        this.patrimonio = patrimonio;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getSerial() {
        return serial;
    }

    public String getPatrimonio() {
        return patrimonio;
    }

    public String getStatus() {
        return status;
    }
}
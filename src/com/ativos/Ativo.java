package com.ativos;

public class Ativo {

    private String empresa;
    private String cd;
    private String equipamento;
    private String marca;
    private String modelo;
    private String serial;
    private String host;
    private String patrimonio;
    private String local;
    private String status;
    private String condicao;
    private String situacao;
    private String responsavel;
    private String observacoes;

    public Ativo(
            String empresa,
            String cd,
            String equipamento,
            String marca,
            String modelo,
            String serial,
            String host,
            String patrimonio,
            String local,
            String status,
            String condicao,
            String situacao,
            String responsavel,
            String observacoes
    ) {
        this.empresa = empresa;
        this.cd = cd;
        this.equipamento = equipamento;
        this.marca = marca;
        this.modelo = modelo;
        this.serial = serial;
        this.host = host;
        this.patrimonio = patrimonio;
        this.local = local;
        this.status = status;
        this.condicao = condicao;
        this.situacao = situacao;
        this.responsavel = responsavel;
        this.observacoes = observacoes;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getCd() {
        return cd;
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

    public String getHost() {
        return host;
    }

    public String getPatrimonio() {
        return patrimonio;
    }

    public String getLocal() {
        return local;
    }

    public String getStatus() {
        return status;
    }

    public String getCondicao() {
        return condicao;
    }

    public String getSituacao() {
        return situacao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public String getObservacoes() {
        return observacoes;
    }

    @Override
    public String toString() {
        return "Ativo{" +
                "patrimonio='" + patrimonio + '\'' +
                ", equipamento='" + equipamento + '\'' +
                ", empresa='" + empresa + '\'' +
                ", cd='" + cd + '\'' +
                '}';
    }
}
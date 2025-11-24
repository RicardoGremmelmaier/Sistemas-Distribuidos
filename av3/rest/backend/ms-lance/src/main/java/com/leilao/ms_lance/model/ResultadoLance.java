package com.leilao.ms_lance.model;

public class ResultadoLance {
    
    private boolean valido;
    private String motivo;

    public ResultadoLance() {}

    public ResultadoLance(boolean valido, String motivo) {
        this.valido = valido;
        this.motivo = motivo;
    }

    public boolean isValido() {return valido; }
    public void setValido(boolean valido) {this.valido = valido; }

    public String getMotivo() {return motivo; }
    public void setMotivo(String motivo) {this.motivo = motivo; }
}

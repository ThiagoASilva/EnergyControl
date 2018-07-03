package tcc.tavcompany.energycontrol.model;

public class DispositivoMedidor {
    String Nome;
    int ID, Icone, porcentagemMeta = 0;
    boolean ligado;

    public int getPorcentagemMeta() {
        return porcentagemMeta;
    }

    public void setPorcentagemMeta(int porcentagemMeta) {
        this.porcentagemMeta = porcentagemMeta;
    }

    public boolean isLigado() {
        return ligado;
    }

    public void setLigado(boolean ligado) {
        this.ligado = ligado;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public int getIcone() {
        return Icone;
    }

    public void setIcone(int icone) {
        Icone = icone;
    }

    public DispositivoMedidor obterPeloId(int ID){

            this.setNome("ESPTomada" + ID);
            this.setID(ID);

        return this;
    }
}

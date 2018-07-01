package tcc.tavcompany.energycontrol.model;

public class DispositivoMedidor {
    String Nome;
    int ID, Icone;

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
        if(ID == 1){
            this.setNome("ESPTomada1");
            this.setID(ID);
        }else if(ID == 2){
            this.setNome("ESPTomada2");
            this.setID(ID);
        }else if (ID == 3){
            this.setNome("ESPTomada3");
            this.setID(ID);
        }else{
            this.setNome("Desconecido ID:" + ID);
            this.setID(ID);
        }
        return this;
    }
}

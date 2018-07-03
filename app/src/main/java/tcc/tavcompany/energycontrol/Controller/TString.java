package tcc.tavcompany.energycontrol.Controller;

import java.util.ArrayList;

public class TString {

    public String obterassunto(String mensagem) {
        String linha1 = mensagem;
        if(mensagem.contains("'")){
            String[] vetorb = mensagem.split("'");
            linha1 = vetorb[1];
            linha1.replace("'","");
        }
        String[] vetor1 = linha1.split("/");
        return  vetor1[2];
    }

    public ArrayList<String> listaDispositivos(String mensagem){
        ArrayList<String> lista = new ArrayList<String>();
        String linha1 = mensagem;
        if(mensagem.contains("'")){
            String[] vetorb = mensagem.split("'");
            linha1 = vetorb[1];
            linha1.replace("'","");
        }

        String[] linha2 = linha1.split("/");
        String[] listaDisp = linha2[3].split("%");
        for(int i = 0; i < listaDisp.length; i++){
            if(!listaDisp[i].isEmpty()) {
                lista.add(i, listaDisp[i]);
            }
        }

        return lista;
    }
}

package tcc.tavcompany.energycontrol.Controller;

public class Calculos {

    double ICMS_Uso_Sist_Distr = 0.18074;
    double ICMS_Energia = 0.23887;
    double Adicional_Bandeira_Amarela = 0.20;
    double Adicional_Bandeira_Vermelha = 3.0;
    double PERCENT_PIS_PASEP = 0.58;
    double PERCENT_COFINS = 2.69;
    double COSIP_Residencial = 8.19;

    public void Calculos(){

    }

    public double obterKWHPorValorMonetario(double valor){
        double kwh = 0.0f;
        double vp;

        kwh = valor - COSIP_Residencial;
        vp = (kwh / 100.0) * (PERCENT_COFINS + PERCENT_PIS_PASEP);
        kwh = kwh - vp;
        kwh = kwh - (Adicional_Bandeira_Vermelha + Adicional_Bandeira_Amarela);
        kwh = kwh / (ICMS_Energia + ICMS_Uso_Sist_Distr);

        return kwh;
    }

    public double obterValorMonetarioTotalPorKWH(double KWH){
        double valorM = 0;

        valorM = KWH * (ICMS_Energia + ICMS_Uso_Sist_Distr);
        valorM = valorM + (Adicional_Bandeira_Amarela + Adicional_Bandeira_Vermelha);
        valorM = (valorM / 100) * (PERCENT_COFINS + PERCENT_PIS_PASEP);
        valorM = valorM + COSIP_Residencial;

        return valorM;
    }

    public double obterValorMonetarioIndividualPorKWH(double KWH){
        double valorM = 0;

        valorM = KWH * (ICMS_Energia + ICMS_Uso_Sist_Distr);

        return valorM;
    }
}

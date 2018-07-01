package tcc.tavcompany.energycontrol.Controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import tcc.tavcompany.energycontrol.Metas;
import tcc.tavcompany.energycontrol.model.DispositivoMedidor;

public class ComunicacaoMQTT {

    static String MQTTHOST = "tcp://192.168.0.18:1883";
    static String USERNAME = "systemem";
    static String PASSWORD = "kwhy123";
    String topicoComando = "TCCCOMANDO";
    String topicoMedicao = "MEDICAO";
    String clientID = "AplicacaoAndroid";
    MqttAndroidClient cliente;

    public ComunicacaoMQTT(final Context contexto){
        cliente = new MqttAndroidClient(contexto, MQTTHOST, clientID);

        MqttConnectOptions options = new MqttConnectOptions();

        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = cliente.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(contexto,"MQTT conectado!", Toast.LENGTH_LONG).show();                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(contexto, "MQTT Falhou ao conectar!", Toast.LENGTH_LONG).show();
                    Log.e("CONEXAO BROKER MQTT", exception.getMessage());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Log.e("Erro", e.getMessage());
        }
    }

    public void publicar(){
        String mensagem = clientID+"/comando_dispositivo/ESPTOMADA1%ligar-desligar";
        try {
            cliente.publish(topicoComando, mensagem.getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public DispositivoMedidor BuscarDispositivoMPorID(int IDdoDispositivo){
        DispositivoMedidor dispositivo = new DispositivoMedidor();

        dispositivo.setID(0);
        dispositivo.setIcone(0);
        dispositivo.setNome("");

        return dispositivo;
    }
}

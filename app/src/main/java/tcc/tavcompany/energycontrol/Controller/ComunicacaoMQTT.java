package tcc.tavcompany.energycontrol.Controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

import tcc.tavcompany.energycontrol.Metas;
import tcc.tavcompany.energycontrol.model.DispositivoMedidor;

public class ComunicacaoMQTT {

    static String MQTTHOST = "tcp://192.168.0.18:1883";
    static String USERNAME = "systemem";
    static String PASSWORD = "kwhy123";
    String topicoComando = "TCCCOMANDO";
    String topicoMedicao = "MEDICAO";
    String topicoStatus = "STATUS";
    String clientID = "AplicacaoAndroid";
    MqttAndroidClient cliente;
    public boolean listaRecebida = false;
    public ArrayList<String> lista = new ArrayList<>();
    TString tstring = new TString();
    public boolean Ready = false;

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

        cliente.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                subEscreverTopicoStatus();
                String mensagem = clientID+"/comando_base/obter-lista-de-dispositivos";
                publicar2(mensagem, topicoComando);
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.e("ComunicacaoMQTT", "Mensagem Recebida: " + message.toString());

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        Ready = true;
    }

    public void subEscreverTopicoStatus(){

        try {
            cliente.subscribe(topicoStatus, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.e("MENSAGEM MQTT", "Topico:" + topic + " " + message.toString());
                    if(topic.equalsIgnoreCase(topicoStatus)){
                        Log.e("MENSAGEM MQTT", "É o topico status!");
                        String assunto = tstring.obterassunto(message.toString());
                        Log.e("MENSAGEM MQTT", "O assunto é: " + assunto);
                        if(assunto.equalsIgnoreCase("lista-dispositivos")){
                            lista.addAll(tstring.listaDispositivos(message.toString()));
                            listaRecebida = true;
                            Log.e("Mensagem MQTT", "Lista recebida");
                        }
                    }
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
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

    public void publicar2(String mensagem, String topico){
        try {
            while(cliente.isConnected()==false) {
            Log.i("COMUNICAÇÂOMQTT", "Aguardando conexao...");
        }
            cliente.publish(topico, mensagem.getBytes(), 0, false);
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

    public ArrayList<String> obterListaDeDispositivos(){

        String mensagem = clientID+"/comando_base/obter-lista-de-dispositivos";
        publicar2(mensagem, topicoComando);

        while(listaRecebida == false){
            Log.e("Agardando lista", "....");
        }
        listaRecebida = false;

        return lista;
    }

    public void testeLista(){

        String mensagem = clientID+"/comando_base/obter-lista-de-dispositivos";
        publicar2(mensagem, topicoComando);

            Log.i("Agardando lista", "....");

    }

    public boolean conectado(){

        return cliente.isConnected();
    }
}

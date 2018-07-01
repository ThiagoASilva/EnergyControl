package tcc.tavcompany.energycontrol;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import tcc.tavcompany.energycontrol.Controller.Calculos;
import tcc.tavcompany.energycontrol.Controller.ComunicacaoMQTT;
import tcc.tavcompany.energycontrol.model.DispositivoMedidor;

public class Metas extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ComunicacaoMQTT CMQTT;
    private ArrayList<String> dados = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView lv = (ListView) findViewById(R.id.Lista_dispositivos);
        final TextView metaR = (TextView) findViewById(R.id.textViewMetaReal);
        final TextView metaKWH = (TextView) findViewById(R.id.textViewMetaKWH);
        final AlertDialog dialogo;
        final EditText caixaDeTexto;
        dados.add("ESPTomada1");
        dados.add("EspTomada2");
        dados.add("ESPTomada3");

        lv.setAdapter(new ListaDeDispositivosMAdapter(this, R.layout.item_da_lista, dados));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Colocar funções para o Energy Control", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        CMQTT = new ComunicacaoMQTT(this.getApplicationContext());
        dialogo = new AlertDialog.Builder(this).create();
        caixaDeTexto = new EditText(this);

        dialogo.setTitle("Definir meta em Reais");
        dialogo.setView(caixaDeTexto);
        final Calculos c = new Calculos();

        dialogo.setButton(DialogInterface.BUTTON_POSITIVE, "Salvar meta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String texto = caixaDeTexto.getText().toString();

                try {

                    Double valor = Double.parseDouble(caixaDeTexto.getText().toString());
                    int k = (int) c.obterKWHPorValorMonetario(valor);
                    metaR.setText("R$ " + valor);
                    metaKWH.setText("KWH " + k);
                    Toast.makeText(Metas.this, "Mata definida com sucesso!", Toast.LENGTH_SHORT).show();

                }catch (Exception e){

                    Toast.makeText(Metas.this,"Não foi possivel definir a meta devido o texto digitado não ser valor monetário.", Toast.LENGTH_LONG).show();

                }
            }
        });
        metaR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogo.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.metas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_metas) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class ListaDeDispositivosMAdapter extends ArrayAdapter<String>{
        private int layout;
        private ListaDeDispositivosMAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = getLayoutInflater().from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.ImagemItem = (ImageView) convertView.findViewById(R.id.imagem_item_da_lista);
                viewHolder.titulo = (TextView) convertView.findViewById(R.id.titulo_item);
                viewHolder.botao = (Switch) convertView.findViewById(R.id.botao_switch_da_lista);
                DispositivoMedidor dm = new DispositivoMedidor().obterPeloId(position);
                viewHolder.titulo.setText(dm.getNome());
                viewHolder.botao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(compoundButton.isChecked()){

                            Toast.makeText(Metas.this, "ESPTomada1 Ligado", Toast.LENGTH_SHORT).show();
                            CMQTT.publicar();
                        }else{

                            Toast.makeText(Metas.this, "ESPTomada1 Desligado", Toast.LENGTH_LONG).show();
                            CMQTT.publicar();
                        }
                    }
                });
                convertView.setTag(viewHolder);
            }else{
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.titulo.setText(getItem(position));
            }

            return convertView;
        }
    }

    public class ViewHolder{

        ImageView ImagemItem;
        TextView titulo;
        Switch botao;
    }
}

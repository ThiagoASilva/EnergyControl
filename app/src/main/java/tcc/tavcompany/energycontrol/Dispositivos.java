package tcc.tavcompany.energycontrol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tcc.tavcompany.energycontrol.Controller.ComunicacaoMQTT;
import tcc.tavcompany.energycontrol.model.DispositivoMedidor;

public class Dispositivos extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ComunicacaoMQTT CMQTT;
    private ListaDeDispositivosMAdapter adapterLista = null;
    private ListView lv;
    private ArrayList<String> dados = new ArrayList<String>();
    int vez =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.Lista_tela_dispositivos);
        CMQTT = new ComunicacaoMQTT(this.getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if(CMQTT.Ready){
                    gerarLista();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void gerarLista(){

        Log.e("Dispositivos", "runnable");
        if(CMQTT.listaRecebida) {
            Log.e("Dispositivos", "Incluindo no Adapter");
            dados = CMQTT.lista;
            adapterLista = new ListaDeDispositivosMAdapter(this, R.layout.lista_dispositivos, dados);
            lv.setAdapter(adapterLista);
        }
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
        getMenuInflater().inflate(R.menu.dispositivos, menu);
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
            Intent intent = new Intent(this, Metas.class);
            startActivity(intent);
        } else if (id == R.id.nav_dispositivos) {

            Intent intent = new Intent(this, Dispositivos.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private class ListaDeDispositivosMAdapter extends ArrayAdapter<String> {
        private int layout;
        private ListaDeDispositivosMAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            final DispositivoMedidor dm = new DispositivoMedidor();
            if(convertView == null){
                LayoutInflater inflater = getLayoutInflater().from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final Dispositivos.ViewHolder viewHolder = new Dispositivos.ViewHolder();
                viewHolder.ImagemDisp = (ImageView) convertView.findViewById(R.id.imagem_disp_da_lista);
                viewHolder.titulo = (TextView) convertView.findViewById(R.id.titulo_disp);
                viewHolder.botao = (Switch) convertView.findViewById(R.id.botao_switch_da_lista_disp);
                dm.obterPeloId(Integer.parseInt(dados.get(position)));
                viewHolder.titulo.setText(dm.getNome());
                viewHolder.botao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(compoundButton.isChecked()){

                            Toast.makeText(Dispositivos.this, "ESPTomada1 Ligado", Toast.LENGTH_SHORT).show();
                            CMQTT.publicar();
                        }else{

                            Toast.makeText(Dispositivos.this, "ESPTomada1 Desligado", Toast.LENGTH_LONG).show();
                            CMQTT.publicar();
                        }
                    }
                });
                convertView.setTag(viewHolder);
            }else{
                dm.obterPeloId(Integer.parseInt(dados.get(position)));
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.titulo.setText(dm.getNome());
            }

            return convertView;
        }
    }

    public class ViewHolder{

        ImageView ImagemDisp;
        TextView titulo;
        Switch botao;
    }
}

package pt.ubi.di.pdm.pediduchos;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PediduchosMain extends AppCompatActivity {

    MenuItem adminPanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pediduchos_main);
        populateListView();
        if(getSession(this) == 0)
            loginMethod(1);
        else Toast.makeText(getApplicationContext(), "Bem-vindo, "+ getNomeLogin(getSession(this))+"!", Toast.LENGTH_SHORT).show();
        final Intent intentVerPedido = new Intent(this, PedidoDetalhes.class);
        final ListView lista = (ListView) findViewById(R.id.listaPedidos);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Cursor cursor = (Cursor) lista.getItemAtPosition(position);
                intentVerPedido.putExtra("idPedido", cursor.getInt(0));
                startActivity(intentVerPedido);
            }
        });

    }

    public void loginMethod(int key) {
        Intent getIDSession = new Intent(this, PediduchosLogin.class);
        if (key == 0) getIDSession.putExtra("logout",1);   // Quer dizer que o logout foi feito. Serve para mostrar uma mensagem na outra atividade
        startActivity(getIDSession);
    }

    public String getNomeLogin(int id){
        CallDB datasource = new CallDB(this);
        datasource.open();
        String nome = datasource.getNomeLogin(id);
        datasource.close();
        return nome;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        adminPanel = menu.findItem(R.id.admin);
        if (getPrivilegio(this) != 1) adminPanel.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout: {
                logoutSession(this);
                invalidateOptionsMenu();
                loginMethod(0);
                break;
            }
            case R.id.admin:{
                Intent iAdminPanel = new Intent(this, PediduchosAdmin.class);
                startActivity(iAdminPanel);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        invalidateOptionsMenu();
        populateListView();

    }

    public void criarPedido(View v) {
        Intent iCriar = new Intent(this, PedidoCriar.class);
        iCriar.putExtra("idPedido", 0);
        startActivity(iCriar);
}

    public void populateListView(){
        CallDB datasource = new CallDB(this);
        datasource.open();

        Cursor cursor = datasource.getPedidos();
        String[] fromFields = new String[] {PediduchosDB.ID,PediduchosDB.PEDIDO,PediduchosDB.MESA,PediduchosDB.DATA};
        int[] toViews = new int[] {R.id.listViewID,R.id.listaPedido,R.id.listaMesa,R.id.listaData};
        SimpleCursorAdapter adapter;
        adapter= new SimpleCursorAdapter(getBaseContext(),R.layout.list_layout,cursor,fromFields,toViews,0);


        ListView lista = (ListView) findViewById(R.id.listaPedidos);
        lista.setAdapter(adapter);
        datasource.close();
    }

    static SharedPreferences getSharedPreferences(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c);
    }

    public static int getSession(Context c)
    {
        return getSharedPreferences(c).getInt("idSession", 0);
    }

    public static int getPrivilegio(Context c)
    {
        return getSharedPreferences(c).getInt("idPrivileges", 0);
    }

    public static void logoutSession(Context c)
    {
        SharedPreferences.Editor editor = getSharedPreferences(c).edit();
        editor.remove("idSession");
        editor.remove("idPrivileges");
        editor.commit();
    }
}

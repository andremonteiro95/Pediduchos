package pt.ubi.di.pdm.pediduchos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PedidoCriar extends AppCompatActivity {

    final CallDB datasource = new CallDB(this); // inicializar base de dados
    List<String> vProduto = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_pedido);
        Intent intent = getIntent();
        final int idPedido = intent.getIntExtra("idPedido", 0);
        datasource.open();
        Button saveButton;
        saveButton = (Button) findViewById(R.id.saveButton);                                      //BOTÃO GUARDAR
        if (idPedido != 0){
            saveButton.setText(getString(R.string.editarpedido_editar));
        }
        populateSpinner();
        if (idPedido == 0) setTitle(getString(R.string.editarpedido_criarpedido));
        else {
            setTitle(getString(R.string.editarpedido_editar) + ": " + idPedido);
            try {
                getDados(idPedido);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        final Spinner lista = (Spinner) findViewById(R.id.editDropdown);

        lista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                Cursor cursor = (Cursor) lista.getItemAtPosition(position);
                if (cursor.getInt(0) == 1) return;
                vProduto.add(cursor.getString(1));
                System.out.println(vProduto);
                lista.setSelection(0);
                populateListView();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        ListView editProdutos = (ListView) findViewById(R.id.editProdutos);
        editProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Cursor cursor = (Cursor) lista.getItemAtPosition(position);
                System.out.println("before"+vProduto);
                vProduto.remove(position);
                System.out.println("after" + vProduto);
                populateListView();
            }
        });

        final EditText editMesa = (EditText) findViewById(R.id.editMesa);
        final EditText editCliente = (EditText) findViewById(R.id.editCliente);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (vProduto.isEmpty()) return;
                if (idPedido == 0) {
                    String empregado = datasource.getNomeEmpregado(getSession(PedidoCriar.this));
                    datasource.criarPedido(vectorToString(), Integer.parseInt(editMesa.getText().toString()), editCliente.getText().toString(), empregado);
                } else
                    datasource.editarPedido(idPedido, vectorToString(), Integer.parseInt(editMesa.getText().toString()), editCliente.getText().toString());
                datasource.close();
                finish();
            }
        });
    }

    public void populateListView(){

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, vProduto);
        ListView editProdutos = (ListView) findViewById(R.id.editProdutos);
        editProdutos.setAdapter(adapter);
    }

    public String vectorToString(){
        String s=vProduto.get(0);
        for(int i=1; i<vProduto.size(); i++){
            s=s.concat(";"+vProduto.get(i));
        }
        return s;
    }

    public void populateSpinner(){
        Cursor cursor = datasource.getProdutos();
        String[] fromFields = new String[] {PediduchosDB.NOME};
        int[] toViews = new int[] {R.id.spinnerText};
        SimpleCursorAdapter adapter;
        adapter= new SimpleCursorAdapter(getBaseContext(),R.layout.spinner_layout,cursor,fromFields,toViews,0);

        Spinner lista = (Spinner) findViewById(R.id.editDropdown);
        lista.setAdapter(adapter);
    }

    public void getDados(int id) throws ParseException {
        final EditText editMesa = (EditText) findViewById(R.id.editMesa);
        final EditText editCliente = (EditText) findViewById(R.id.editCliente);
        Pedido pedido = datasource.getPedido(id);
        editMesa.setText(Integer.toString(pedido.getMesa()));
        editCliente.setText(pedido.getCliente());

        // Split da string dos produtos
        String s = pedido.getListaPedidos();
        String[] tokens = s.split(";");
        for(int i=0; i<tokens.length;i++){
            vProduto.add(tokens[i]);
        }
        populateListView();
    }

    static SharedPreferences getSharedPreferences(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c);
    }

    public static int getSession(Context c)
    {
        return getSharedPreferences(c).getInt("idSession",0);
    }
}

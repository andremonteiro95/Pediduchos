package pt.ubi.di.pdm.pediduchos;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class PediduchosAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pediduchos_admin);
        populateListView();
        setTitle(getString(R.string.admin_title));
        final Intent iEditar = new Intent(this, ProdutoCriar.class);
        final ListView lista = (ListView) findViewById(R.id.listaProdutos);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Cursor cursor = (Cursor) lista.getItemAtPosition(position);
                iEditar.putExtra("idProduto", cursor.getInt(0));
                startActivity(iEditar);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        populateListView();

    }

    public void populateListView(){
        CallDB datasource = new CallDB(this);
        datasource.open();
        Cursor cursor = datasource.getProdutosLV();
        String[] fromFields = new String[] {PediduchosDB.ID,PediduchosDB.NOME,PediduchosDB.PRECO};
        int[] toViews = new int[] {R.id.listaprodutosID,R.id.listaprodutosNome,R.id.listaprodutosPreco};
        SimpleCursorAdapter adapter;
        adapter= new SimpleCursorAdapter(getBaseContext(),R.layout.list_produtos_layout,cursor,fromFields,toViews,0);

        ListView lista = (ListView) findViewById(R.id.listaProdutos);
        lista.setAdapter(adapter);
        datasource.close();
    }

    public void criarProduto(View v) {
        Intent iCriar = new Intent(this, ProdutoCriar.class);
        iCriar.putExtra("idProduto", 0);
        startActivity(iCriar);
    }

}

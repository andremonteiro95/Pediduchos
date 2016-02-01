package pt.ubi.di.pdm.pediduchos;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProdutoCriar extends AppCompatActivity {

    CallDB datasource = new CallDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_criar);
        datasource.open();
        Intent intent = getIntent();
        Button saveButton = (Button)findViewById(R.id.produtoButton);
        Button deleteButton = (Button)findViewById(R.id.produtoEliminar);
        final int idProduto = intent.getIntExtra("idProduto", 0);
        if (idProduto == 0) {
            setTitle(getString(R.string.produtocriar_title));
            deleteButton.setVisibility(View.GONE);
        }
        else {
            setTitle(getString(R.string.produtocriar_title2));
            getDados(idProduto);
        }

        if (idProduto != 0) saveButton.setText("Editar produto");
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText produtoNome = (EditText)findViewById(R.id.produtoNome);
                EditText produtoPreco = (EditText)findViewById(R.id.produtoPreco);
                if (idProduto == 0) {
                    datasource.criarProduto(produtoNome.getText().toString(), Double.parseDouble(produtoPreco.getText().toString()));
                } else
                    datasource.editarProduto(idProduto, produtoNome.getText().toString(), Double.parseDouble(produtoPreco.getText().toString()));
                datasource.close();
                finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datasource.removeProduto(idProduto);
                datasource.close();
                finish();
            }
        });
    }

    public void getDados(int id){
        EditText produtoNome = (EditText)findViewById(R.id.produtoNome);
        EditText produtoPreco = (EditText)findViewById(R.id.produtoPreco);
        ContentValues bundle = new ContentValues();
        bundle = datasource.getProduto(id);
        produtoNome.setText(bundle.getAsString("nome"));
        produtoPreco.setText(bundle.getAsString("preco"));
    }
}

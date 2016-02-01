package pt.ubi.di.pdm.pediduchos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;

public class PedidoDetalhes extends AppCompatActivity {

    ArrayList<String> vProduto = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_detalhes);
        Intent intentRes = getIntent();
        final int idPedido=intentRes.getIntExtra("idPedido", 0);

        try {
            getData(idPedido);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setTitle(getString(R.string.pedidodetalhes_titulo) + idPedido);

        final Button buttonFechar = (Button)findViewById(R.id.dbuttonFechar);
        buttonFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallDB datasource = new CallDB(PedidoDetalhes.this);
                datasource.open();
                datasource.fecharPedido(idPedido);
                datasource.close();
                TextView textFechado = (TextView) findViewById(R.id.detalhesFechado);
                textFechado.setText("Sim");
                buttonFechar.setVisibility(View.GONE);
            }
        });

        final Button buttonEditar = (Button)findViewById(R.id.dbuttonEditar);
        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditar = new Intent(PedidoDetalhes.this, PedidoCriar.class);
                intentEditar.putExtra("idPedido", idPedido);
                startActivity(intentEditar);
                finish();
            }
        });
    }

    public void getData(int id) throws ParseException {
        CallDB datasource = new CallDB(this);
        datasource.open();
        Pedido current = datasource.getPedido(id);
        TextView textMesa = (TextView)findViewById(R.id.detalhesMesa);
        TextView textCliente = (TextView)findViewById(R.id.detalhesCliente);
        TextView textData = (TextView)findViewById(R.id.detalhesData);
        TextView textPedido = (TextView)findViewById(R.id.detalhesPedido);
        TextView textEmpregado = (TextView)findViewById(R.id.detalhesEmpregado);
        TextView textFechado = (TextView)findViewById(R.id.detalhesFechado);
        TextView textValor = (TextView)findViewById(R.id.detalhesValor);

        textMesa.setText(Integer.toString(current.getMesa()));
        textCliente.setText(current.getCliente());
        textData.setText(current.getData().toString());

        // Split da string dos produtos
        String s = current.getListaPedidos();
        String[] tokens = s.split(";");
        for(int i=0; i<tokens.length;i++){
            vProduto.add(tokens[i]);
            textPedido.setText(textPedido.getText() + tokens[i] + "\n");
        }
        textEmpregado.setText(current.getEmpregado());
        Button buttonFechar = (Button)findViewById(R.id.dbuttonFechar);
        if (current.getFechado()) {
            buttonFechar.setVisibility(View.GONE);
            textFechado.setText("Sim");
        }
        else textFechado.setText("Não");

        double valor = datasource.getValor(vProduto);
        textValor.setText(Double.toString(valor) +"€");
        datasource.close();
    }

}

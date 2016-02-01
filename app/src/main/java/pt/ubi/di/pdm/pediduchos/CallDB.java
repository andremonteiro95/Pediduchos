package pt.ubi.di.pdm.pediduchos;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CallDB {
    private SQLiteDatabase database;
    private PediduchosDB PediduchosDB;
    private String[] allColumns = { PediduchosDB.ID, PediduchosDB.MESA, PediduchosDB.PEDIDO,PediduchosDB.CLIENTE,PediduchosDB.DATA, PediduchosDB.TABLE_EMPREGADO,PediduchosDB.FECHADO};

    public CallDB(Context context) {
        PediduchosDB = new PediduchosDB(context);
    }

    public void open() throws SQLException {
        database = PediduchosDB.getWritableDatabase();
    }

    public void close() {
        PediduchosDB.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public int criarPedido(String pedido, int mesa, String cliente, String empregado) {
        ContentValues values = new ContentValues();
        values.put(PediduchosDB.MESA, mesa);
        values.put(PediduchosDB.PEDIDO, pedido);
        values.put(PediduchosDB.CLIENTE, cliente);
        values.put(PediduchosDB.DATA,getDateTime());
        values.put(PediduchosDB.TABLE_EMPREGADO,empregado);
        values.put(PediduchosDB.FECHADO,0);
        long insertId = database.insert(PediduchosDB.TABLE_PEDIDO, null, values);
        Cursor cursor = database.query(PediduchosDB.TABLE_PEDIDO, allColumns, PediduchosDB.ID + " = " +
                insertId, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public void editarPedido(int id, String pedido, int mesa, String cliente) {
        database.execSQL("update Pedido set Pedido = '" + pedido + "', Mesa = '" + mesa + "', Cliente = '" + cliente + "' where _id=" + id + ";");
    }

    public Cursor getPedidos(){
        Cursor cursor = database.rawQuery("select _id, Mesa, Pedido, Cliente, Data, Empregado, Fechado from Pedido order by _id desc", null);
        return cursor;
    }

    public Cursor getProdutos(){
        Cursor cursor = database.rawQuery("select _id, Nome, Preco from Produto", null);
        return cursor;
    }

    public Cursor getProdutosLV(){
        Cursor cursor = database.rawQuery("select _id, Nome, Preco from Produto where _id != 1", null);
        return cursor;
    }

    public Pedido getPedido (int id) throws ParseException {
        Cursor cursor = database.query(PediduchosDB.TABLE_PEDIDO, allColumns, PediduchosDB.ID + " = " + id, null,null, null, null);
        cursor.moveToFirst();
        boolean fechado = false;
        if (cursor.getInt(6) == 1) fechado = true;
        Pedido pedido = new Pedido(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3), cursor.getString(4), cursor.getString(5),fechado);
        return pedido;
    }

    public int getUtilizador(String email, String password) {
        Cursor cursor = database.rawQuery("select _id from Empregado where Email='"+email+"' and Password='"+password+"';",null);
        if (cursor.moveToFirst() && cursor != null)
            return cursor.getInt(0);
        return -1;
    }

    public int getPrivilegio(int id) {
        Cursor cursor = database.rawQuery("select Privilegio from Empregado where _id="+id+";",null);
        if (cursor.moveToFirst() && cursor != null)
            return cursor.getInt(0);
        return -1;
    }

    public String getNomeEmpregado(int id){

        Cursor cursor = database.rawQuery("select Nome from Empregado where _id ="+id+";",null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public void fecharPedido(int id)
    {
        database.execSQL("update Pedido set Fechado = 1 where _id=" + id + ";");
    }

    public double getValor(ArrayList<String> produtos){
        double valor = 0.0;
        Cursor cursor = null;
        for(int i=0; i<produtos.size(); i++)
        {
            cursor = database.rawQuery("select Preco from Produto where Nome = '"+produtos.get(i)+"';",null);
            cursor.moveToFirst();
            valor = valor + cursor.getDouble(0);
        }
        return valor;
    }

    public ContentValues getProduto(int id){
        Cursor cursor = database.rawQuery("select Nome, Preco from Produto where _id="+id+" ;",null);
        cursor.moveToFirst();
        ContentValues bundle = new ContentValues();
        bundle.put("nome",cursor.getString(0));
        bundle.put("preco",cursor.getDouble(1));
        return bundle;
    }

    public void criarProduto(String nome, double preco){
        ContentValues values = new ContentValues();
        values.put(PediduchosDB.NOME, nome);
        values.put(PediduchosDB.PRECO, preco);
        database.insert(PediduchosDB.TABLE_PRODUTO, null, values);
    }

    public void editarProduto(int id, String nome, double preco){
        database.execSQL("update Produto set Nome = '" + nome + "', Preco = '" + preco + "' where _id=" + id + ";");
    }

    public String getNomeLogin(int id){
        Cursor cursor = database.rawQuery("select Nome from Empregado where _id ="+id,null);
        cursor.moveToFirst();
        return(cursor.getString(0));
    }

    public void removeProduto(int id){
        database.execSQL("delete from Produto where _id="+id+" ;");
    }
}

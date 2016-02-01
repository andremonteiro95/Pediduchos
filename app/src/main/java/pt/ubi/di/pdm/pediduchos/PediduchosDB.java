package pt.ubi.di.pdm.pediduchos;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PediduchosDB extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "pediduchos.db";
    public static final String TABLE_PEDIDO = "Pedido";
    public static final String TABLE_EMPREGADO = "Empregado";
    public static final String TABLE_PRODUTO ="Produto";
    private static final int DATABASE_VERSION = 3;
    public static final String ID = "_id";
    public static final String PEDIDO = "Pedido";
    public static final String MESA = "Mesa";
    public static final String CLIENTE ="Cliente";
    public static final String DATA ="Data";
    public static final String FECHADO ="Fechado";   // 0 se pedido aberto, 1 se fechado
    public static final String NOME = "Nome";
    public static final String EMAIL = "Email";
    public static final String PASSWORD = "Password";
    public static final String PRIVILEGIO = "Privilegio"; // 1 se admin, 0 se normal
    public static final String PRECO = "Preco";
    private static final String CREATE_PEDIDOS = "create table "
            + TABLE_PEDIDO + "( " + ID
            + " integer primary key autoincrement, " + MESA
            + " integer not null, " + PEDIDO + " text not null, "
            + CLIENTE +" text not null, "+ DATA +" text not null, "
            + "Empregado text not null, "+ FECHADO +" tinyint default 0);";
    private static final String CREATE_EMPREGADOS = "create table "
            + TABLE_EMPREGADO + "( " + ID
            + " integer primary key autoincrement, " + NOME
            + " text not null, " + EMAIL + " text not null, "
            + PASSWORD +" text not null, " + PRIVILEGIO
            + " tinyint default 0);";
    private static final String CREATE_PRODUTOS = "create table "
            + TABLE_PRODUTO +"( " + ID
            + " integer primary key autoincrement, " + NOME
            + " text not null, " + PRECO + " real not null);";

    public PediduchosDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PEDIDOS);
        db.execSQL(CREATE_EMPREGADOS);
        db.execSQL(CREATE_PRODUTOS);
        ContentValues cv = new ContentValues();
        ContentValues produtoexemplo = new ContentValues();
        cv.put(NOME, "Admin");
        cv.put(EMAIL, "admin");
        cv.put(PASSWORD, "teste");
        cv.put(PRIVILEGIO, 1);
        db.insert(TABLE_EMPREGADO, null, cv);
        cv.clear();
        cv.put(NOME, "Empregado");
        cv.put(EMAIL, "empregado");
        cv.put(PASSWORD, "teste");
        cv.put(PRIVILEGIO, 0);
        db.insert(TABLE_EMPREGADO, null, cv);
        produtoexemplo.put(NOME, "");
        produtoexemplo.put(PRECO, 0.0);
        db.insert(TABLE_PRODUTO,null,produtoexemplo);
        produtoexemplo.put(NOME, "Folhado misto");
        produtoexemplo.put(PRECO, 1.0);
        db.insert(TABLE_PRODUTO,null,produtoexemplo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PediduchosDB.class.getName(), "Upgrading database from version " + oldVersion + "to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPREGADO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUTO);
        onCreate(db);
    }
}

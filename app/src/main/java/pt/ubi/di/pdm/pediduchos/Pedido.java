package pt.ubi.di.pdm.pediduchos;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Utilizador on 06-11-2015.
 */
public class Pedido {
    private int id;
    private String pedido;
    private String cliente;
    private int mesa;
    private Date data;
    private String empregado;
    private boolean fechado;

    public Pedido(int id, int mesa, String pedido, String cliente, String datastring, String empregado, boolean fechado) throws ParseException {
        this.id=id;
        this.pedido=pedido;
        this.cliente=cliente;
        this.mesa=mesa;
        this.empregado=empregado;
        this.fechado = fechado;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        data = (Date)formatter.parse(datastring);
    }

    public String getListaPedidos(){
        return pedido;
    }

    public int getID(){
        return id;
    }

    public String getCliente(){ return cliente;}

    public int getMesa(){return mesa;}

    public Date getData(){return data;}

    public String getEmpregado(){return empregado;}

    public boolean getFechado(){
        return fechado;
    }

}

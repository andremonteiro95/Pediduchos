package pt.ubi.di.pdm.pediduchos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class PediduchosLogin extends AppCompatActivity {

    public CallDB datasource = new CallDB(PediduchosLogin.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pediduchos_login);
        setTitle("Login");
        Intent intentRes = getIntent();
        if (intentRes.getIntExtra("logout", 0) == 1)
            Toast.makeText(getApplicationContext(), "Logout realizado com sucesso.", Toast.LENGTH_SHORT).show();

        final EditText email = (EditText)findViewById(R.id.loginEmail);
        final EditText password = (EditText)findViewById(R.id.loginPassword);
        Button login = (Button)findViewById(R.id.loginButton);
    }

    public void onClick(View v) {
        EditText email = (EditText)findViewById(R.id.loginEmail);
        EditText password = (EditText)findViewById(R.id.loginPassword);
        if (email.length() == 0 || password.length()==0) return;
        datasource.open();
        int idUtilizador = datasource.getUtilizador(email.getText().toString(), password.getText().toString());
        if (idUtilizador != -1) {
            int privilegio = datasource.getPrivilegio(idUtilizador);
            System.out.println("PRIVILEGIO"   +privilegio);
            setSession(this,idUtilizador, privilegio);
            datasource.close();
            Toast.makeText(getApplicationContext(), "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else Toast.makeText(getApplicationContext(), "Email e/ou PW errados.", Toast.LENGTH_SHORT).show();
    }

    public static void setSession(Context c, int id, int privilegio)
    {
        SharedPreferences.Editor editor = getSharedPreferences(c).edit();
        editor.putInt("idSession", id);
        editor.putInt("idPrivileges", privilegio);
        editor.commit();
    }

    static SharedPreferences getSharedPreferences(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c);
    }

    @Override
    public void onBackPressed() {
        System.exit(1);
    }
}
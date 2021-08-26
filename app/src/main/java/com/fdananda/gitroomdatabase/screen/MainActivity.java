package com.fdananda.gitroomdatabase.screen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.fdananda.gitroomdatabase.R;
import com.fdananda.gitroomdatabase.data.UserDatabase;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.delete) {
            deleteUser();
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteUser(){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setCancelable(true);
            alertDialog.setTitle("Excluir Todos os usuários");
            alertDialog.setMessage("Tem certeza de que deseja excluir TODOS os usuários?");

            alertDialog.setPositiveButton("SIM", new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DeleteAllUsersAsyncTask deleteAllUsersAsyncTask = new DeleteAllUsersAsyncTask();
                            deleteAllUsersAsyncTask.execute("");
                        }
                    });
            alertDialog.setNegativeButton("NÃO", new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            alertDialog.create();
            alertDialog.show();
    }

    public class DeleteAllUsersAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            UserDatabase userDatabase = UserDatabase.getInstance(MainActivity.this);
            userDatabase.userDao().deleteAllUsers();
            return "OK";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("OK")){
                Toast.makeText(MainActivity.this, "Usuários excluídos com sucesso", Toast.LENGTH_LONG).show();
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(MainActivity.this, "Ops! Algo deu muito errado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
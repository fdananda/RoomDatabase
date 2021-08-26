package com.fdananda.gitroomdatabase.screen.delete;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Delete;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fdananda.gitroomdatabase.R;
import com.fdananda.gitroomdatabase.data.UserDatabase;
import com.fdananda.gitroomdatabase.model.User;
import com.fdananda.gitroomdatabase.screen.update.UpdateFragment;

import java.lang.ref.WeakReference;

public class DeleteFragment extends Fragment {

    private EditText editTextNameDelete;
    private EditText editTextSurnameDelete;
    private EditText editTextAgeDelete;
    private TextView textViewErrorDelete;
    private Button buttonDelete;

    public DeleteFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete, container, false);

        buttonDelete            = view.findViewById(R.id.button_delete);
        editTextNameDelete      = view.findViewById(R.id.editText_name_delete);
        editTextSurnameDelete   = view.findViewById(R.id.editText_surname_delete);
        editTextAgeDelete       = view.findViewById(R.id.editText_age_delete);
        textViewErrorDelete     = view.findViewById(R.id.textViewError_delete);

        editTextNameDelete.setText(getArguments().getString("name"));
        editTextSurnameDelete.setText(getArguments().getString("surname"));
        editTextAgeDelete.setText(getArguments().getString("age"));

        editTextNameDelete.setEnabled(false);
        editTextSurnameDelete.setEnabled(false);
        editTextAgeDelete.setEnabled(false);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name     = editTextNameDelete.getText().toString();
                String surname  = editTextSurnameDelete.getText().toString();
                String age      = editTextAgeDelete.getText().toString();

                if (!name.isEmpty() && !name.equals("") &&
                        !surname.isEmpty() && !surname.equals("") &&
                        !age.isEmpty() && !age.equals("")){
                    deleteDatabase(getArguments().getString("id"), name, surname, age);

                }else {
                    textViewErrorDelete.setText("Campo obrigatório não preenchido!");
                }
            }
        });

        return view;
    }

    private void deleteDatabase(String id, String name, String surname, String age){

        String[] taskParams = {name, surname, age, id};
        DeleteUserAsyncTask deleteUserAsyncTask = new DeleteUserAsyncTask(getActivity().getApplicationContext());
        deleteUserAsyncTask.execute(taskParams);
    }

    public class DeleteUserAsyncTask extends AsyncTask<String, Void, String> {

        private final WeakReference<Context> weakReference;
        DeleteUserAsyncTask(Context context){
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            String name     = strings[0];
            String surname  = strings[1];
            int age         = Integer.parseInt(strings[2]);
            int id          = Integer.parseInt(strings[3]);

            User userDelete = new User();
            userDelete.setId(id);
            userDelete.setName(name);
            userDelete.setSurname(surname);
            userDelete.setAge(age);

            UserDatabase userDatabase = UserDatabase.getInstance(getActivity().getApplicationContext());
            userDatabase.userDao().deleteUser(userDelete);

            return "OK";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("OK")){
                Toast.makeText(getContext(), "Usuário excluído com sucesso", Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(DeleteFragment.this)
                        .navigate(R.id.action_deleteFragment_to_FirstFragment);
            }else {
                Toast.makeText(getContext(), "Ops! Algo deu muito errado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
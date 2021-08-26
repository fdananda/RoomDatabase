package com.fdananda.gitroomdatabase.screen.add;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.fdananda.gitroomdatabase.R;
import com.fdananda.gitroomdatabase.data.UserDatabase;
import com.fdananda.gitroomdatabase.model.User;
import java.lang.ref.WeakReference;

public class AddFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.add_fragment, container, false);

        Button button            = view.findViewById(R.id.button);
        EditText editTextName    = view.findViewById(R.id.editText_name);
        EditText editTextSurname = view.findViewById(R.id.editText_surname);
        EditText editTextAge     = view.findViewById(R.id.editText_age);
        TextView textViewError   = view.findViewById(R.id.textViewError);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editTextName.getText().toString();
                String surname = editTextSurname.getText().toString();
                String age = editTextAge.getText().toString();

                if (!name.isEmpty() && !name.equals("") &&
                    !surname.isEmpty() && !surname.equals("") &&
                    !age.isEmpty() && !age.equals("")){
                    insertIntoDatabase(name, surname, age);
                }else {
                    textViewError.setText("Campo obrigatório não preenchido!");
                }
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void insertIntoDatabase(String name, String surname, String age){

        String[] taskParams = {name, surname, age};
        InsertUserAsyncTask insertUserAsyncTask = new InsertUserAsyncTask(getActivity().getApplicationContext());
        insertUserAsyncTask.execute(taskParams);
    }

    public class InsertUserAsyncTask extends AsyncTask<String, Void, String> {
        
        private final WeakReference<Context> weakReference;
        
        InsertUserAsyncTask(Context context){
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            
            String name     = strings[0];
            String surname  = strings[1];
            String age      = strings[2];

            User user = new User(name, surname, Integer.parseInt(age));
            UserDatabase userDatabase = UserDatabase.getInstance(getActivity().getApplicationContext());
            userDatabase.userDao().insertUser(user);
            
            return "OK";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            
            if (s.equals("OK")){
                Toast.makeText(requireContext(), "Usuário adicionado com sucesso", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(AddFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }else {
                Toast.makeText(requireContext(), "Ops! Algo deu muito errado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
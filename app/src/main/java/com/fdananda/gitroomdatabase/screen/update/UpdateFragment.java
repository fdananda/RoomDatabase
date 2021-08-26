package com.fdananda.gitroomdatabase.screen.update;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
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
import java.lang.ref.WeakReference;

public class UpdateFragment extends Fragment {

    private EditText editTextNameUpdate;
    private EditText editTextSurnameUpdate;
    private EditText editTextAgeUpdate;
    private TextView textViewErrorUpdate;
    private Button   buttonUpdate;

    public UpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update, container, false);

        buttonUpdate            = view.findViewById(R.id.button_update);
        editTextNameUpdate      = view.findViewById(R.id.editText_name_update);
        editTextSurnameUpdate   = view.findViewById(R.id.editText_surname_update);
        editTextAgeUpdate       = view.findViewById(R.id.editText_age_update);
        textViewErrorUpdate     = view.findViewById(R.id.textViewError_update);

        editTextNameUpdate.setText(getArguments().getString("name"));
        editTextSurnameUpdate.setText(getArguments().getString("surname"));
        editTextAgeUpdate.setText(getArguments().getString("age"));

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name     = editTextNameUpdate.getText().toString();
                String surname  = editTextSurnameUpdate.getText().toString();
                String age      = editTextAgeUpdate.getText().toString();

                if (!name.isEmpty() && !name.equals("") &&
                        !surname.isEmpty() && !surname.equals("") &&
                        !age.isEmpty() && !age.equals("")){
                        updateIntoDatabase(getArguments().getString("id"), name, surname, age);

                }else {
                    textViewErrorUpdate.setText("Campo obrigatório não preenchido!");
                }
            }
        });

        return view;
    }

    private void updateIntoDatabase(String id, String name, String surname, String age){

        String[] taskParams = {name, surname, age, id};
        UpdateUserAsyncTask updateUserAsyncTask = new UpdateUserAsyncTask(getActivity().getApplicationContext());
        updateUserAsyncTask.execute(taskParams);
    }

    public class UpdateUserAsyncTask extends AsyncTask<String, Void, String> {

        private final WeakReference<Context> weakReference;
        UpdateUserAsyncTask(Context context){
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            String name     = strings[0];
            String surname  = strings[1];
            int age         = Integer.parseInt(strings[2]);
            int id          = Integer.parseInt(strings[3]);

            User userUpdate = new User();
            userUpdate.setId(id);
            userUpdate.setName(name);
            userUpdate.setSurname(surname);
            userUpdate.setAge(age);

            UserDatabase userDatabase = UserDatabase.getInstance(getActivity().getApplicationContext());
            userDatabase.userDao().updateUser(userUpdate);

            return "OK";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("OK")){
                NavHostFragment.findNavController(UpdateFragment.this)
                        .navigate(R.id.action_updateFragment_to_FirstFragment);
                Toast.makeText(requireContext(), "Usuário alterado com sucesso", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(requireContext(), "Ops! Algo deu muito errado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
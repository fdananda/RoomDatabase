package com.fdananda.gitroomdatabase.screen.list;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.fdananda.gitroomdatabase.R;
import com.fdananda.gitroomdatabase.data.UserDatabase;
import com.fdananda.gitroomdatabase.model.User;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    ListView listView;
    List<User> users;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        listView  = view.findViewById(R.id.listViewUsers);
        users = new ArrayList<User>();

        GetUsersAsyncTask getUsersAsyncTask = new GetUsersAsyncTask(getActivity().getApplicationContext());
        getUsersAsyncTask.execute();

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.fab2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ListFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    public class GetUsersAsyncTask extends AsyncTask<ArrayList<User>, Void, ArrayList<User>> {

        private final WeakReference<Context> weakReference;
        GetUsersAsyncTask(Context context){
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected ArrayList<User> doInBackground(ArrayList<User>... arrayLists) {

            UserDatabase userDatabase = UserDatabase.getInstance(getActivity().getApplicationContext());
            users = (ArrayList<User>) userDatabase.userDao().loadUsers();

            return (ArrayList<User>) users;
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);

            ArrayAdapter adapter = new AdapterUser(weakReference.get().getApplicationContext(), (ArrayList<User>) users);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", String.valueOf(users.get(position).getId()));
                    bundle.putString("name", users.get(position).getName());
                    bundle.putString("surname", users.get(position).getSurname());
                    bundle.putString("age", String.valueOf(users.get(position).getAge()));

                    NavHostFragment.findNavController(ListFragment.this)
                            .navigate(R.id.action_FirstFragment_to_updateFragment, bundle);
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    Bundle bundle = new Bundle();
                    bundle.putString("id", String.valueOf(users.get(position).getId()));
                    bundle.putString("name", users.get(position).getName());
                    bundle.putString("surname", users.get(position).getSurname());
                    bundle.putString("age", String.valueOf(users.get(position).getAge()));

                    NavHostFragment.findNavController(ListFragment.this)
                            .navigate(R.id.action_FirstFragment_to_deleteFragment, bundle);

                    return true;
                }
            });
        }
    }
}
package rudy.myplace;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AddActivity extends AppCompatActivity {

    private NotesDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Base de données */
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();

        // ButtonAddPlace
        Button buttonAddPlace =(Button)findViewById(R.id.buttonAddPlace);
        buttonAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNote();
            }
        });
    }

    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllNotes();
        startManagingCursor(c);

        String[] from = new String[] { NotesDbAdapter.KEY_TITLE };
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, c, from, to);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(notes);

    }

    private void createNote() {
        final EditText editText = (EditText) findViewById(R.id.new_task);
        String newTask = editText.getText().toString();

        /*if("".equals(newTask)){
            Snackbar.make(view, "La tâche de ne peut pas être vide", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }*/

        mDbHelper.createNote(newTask, "");
        editText.setText("");

        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), "Lieu ajouté", Snackbar.LENGTH_LONG)
                .setAction("VOIR LA LISTE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AddActivity.this, ListActivity.class);
                        startActivity(intent);
                    }
                });

        snackbar.show();
    }
}

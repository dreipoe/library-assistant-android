package localhost.libraryassistant;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {
    ListView readerList;
    DatabaseMaker databaseMaker;
    SQLiteDatabase db;
    Cursor cursor;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //позволяет запустить редактирование
        readerList = findViewById(R.id.list);
        readerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Edit.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        databaseMaker = new DatabaseMaker(getApplicationContext());
        databaseMaker.createDB();
    }

    @Override
    public void onResume() {
        super.onResume();

        db = databaseMaker.open();
        cursor = db.rawQuery("select * from " + databaseMaker.TABLE, null);
        String[] headers = new String[]{databaseMaker.SURNAME, databaseMaker.REG};
        adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, cursor, headers,
                new int[]{android.R.id.text1, android.R.id.text2}, 0);
        readerList.setAdapter(adapter);
    }

    public void add(View view) {
        Intent intent = new Intent(this, Edit.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        db.close();
        cursor.close();
    }
}

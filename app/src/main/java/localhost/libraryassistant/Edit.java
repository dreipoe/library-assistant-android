package localhost.libraryassistant;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Edit extends AppCompatActivity {
    Cursor cursor;

    EditText surname;
    EditText name;
    EditText patronym;
    EditText reg;
    EditText quit;

    DatabaseMaker maker;
    SQLiteDatabase db;

    Button delButton;
    Button saveButton;

    long readerId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        surname = findViewById(R.id.surname);
        name = findViewById(R.id.name);
        patronym = findViewById(R.id.patronym);
        reg = findViewById(R.id.reg);
        quit = findViewById(R.id.quit);
        delButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);

        maker = new DatabaseMaker(this);
        db = maker.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            readerId = extras.getLong("id");
        }

        if (readerId > 0) {
            cursor = db.rawQuery("select * from " + DatabaseMaker.TABLE + " where " +
                    DatabaseMaker.ID + "=" + String.valueOf(readerId), null);
            cursor.moveToFirst();
            surname.setText(cursor.getString(1));
            name.setText(String.valueOf(cursor.getString(2)));
            patronym.setText(String.valueOf(cursor.getString(3)));
            reg.setText(String.valueOf(cursor.getString(4)));
            quit.setText(String.valueOf(cursor.getString(5)));
            cursor.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        ContentValues cv = new ContentValues();

        cv.put(DatabaseMaker.SURNAME, surname.getText().toString());
        cv.put(DatabaseMaker.NAME, name.getText().toString());
        cv.put(DatabaseMaker.REG, reg.getText().toString());
        cv.put(DatabaseMaker.PATRONYM, patronym.getText().toString());
        cv.put(DatabaseMaker.QUIT, quit.getText().toString());

        try {
            if (readerId > 0) {
                db.update(DatabaseMaker.TABLE, cv, "_id=" + String.valueOf(readerId), null);
            } else {
                db.insert(DatabaseMaker.TABLE, null, cv);
            }
            goHome();
        } catch (SQLiteException ex) {
            Toast.makeText(this, "Заполните обязательные поля!", Toast.LENGTH_LONG).show();
        }
    }

    public void delete(View view){
        db.delete("Reader", "_id = ?", new String[]{String.valueOf(readerId)});
        goHome();
    }

    private void goHome(){
        db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}

package localhost.libraryassistant;

import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dreipoe on 08.01.2018.
 */

class DatabaseMaker extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static String DB_NAME = "libassist.db";
    private static final int V = 1;
    private Context context;

    static final String TABLE = "Reader";
    static final String ID = "_id";
    static final String SURNAME = "surname";
    static final String NAME = "name";
    static final String PATRONYM = "patronym";
    static final String REG = "reg";
    static final String QUIT = "quit";

    public DatabaseMaker(Context context) {
        super(context, null, null, V);
        this.context = context;
        DB_PATH =context.getFilesDir().getPath() + DB_NAME;
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
    }

    void createDB(){
        InputStream input = null;
        OutputStream output = null;
        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                this.getReadableDatabase();

                input = context.getAssets().open(DB_NAME);

                String outFileName = DB_PATH;
                output = new FileOutputStream(outFileName);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                output.flush();
                output.close();
                input.close();
            }
        }
        catch(IOException ex){
            Log.d("DatabaseHelper", ex.getMessage());
        }
    }

    public SQLiteDatabase open()throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}

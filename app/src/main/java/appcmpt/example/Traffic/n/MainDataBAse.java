package appcmpt.example.Traffic.n;

import java.util.ArrayList;  
import java.util.List;  
   
import android.content.ContentValues;  
import android.content.Context;  
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class MainDataBAse {

    private DbHelper myHelper;
    private final Context myContext;
    private SQLiteDatabase myDatabase;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager";
    public static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERID = "iddriver";
    private static final String KEY_PH_NO = "phone_number";

    public static class DbHelper extends SQLiteOpenHelper {

   
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
        //3rd argument to be passed is CursorFactory instance  
    }  
   
    // Creating Tables  
    @Override  
    public void onCreate(SQLiteDatabase db) {  
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("  
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERID + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";  
        db.execSQL(CREATE_CONTACTS_TABLE);  
    }  
   
    // Upgrading database  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        // Drop older table if existed  
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);  
   
        // Create tables again  
        onCreate(db);  
    }  }


    public MainDataBAse(Context c){
        myContext = c;
    }


    public MainDataBAse open() throws SQLException {
        myHelper = new DbHelper(myContext);
        myDatabase = myHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        myHelper.close();
    }
    public void deleteEntry() throws SQLException{
// TODO Auto-generated method stub
        myDatabase.delete(TABLE_CONTACTS, null,null);
    }

     // code to add the new contact  
     public void addContact(profile witem) {  

   
        ContentValues values = new ContentValues();
         values.put(KEY_USERID, witem.getUser_id());
        values.put(KEY_NAME, witem.getQcode()); // Contact Name
        values.put(KEY_PH_NO, witem.getType()); // Contact Phone
   
        // Inserting Row  
         myDatabase.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack  
         // Closing database connection
    }  
   
    // code to get the single contact  
     public profile getContact(int id) {  

   
        Cursor cursor = myDatabase.query(TABLE_CONTACTS, new String[] { KEY_ID,KEY_USERID,
                KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",  
                new String[] { String.valueOf(id) }, null, null, null, null);  
        if (cursor != null)  
            cursor.moveToFirst();  
   
//        profile witem = new profile(cursor.getString(1),cursor.getString(2),cursor.getString(3));
        // return contact  
        return null;
    }  
   
    // code to get all contacts in a list view  
    public List<profile> getAllContacts() {  
        List<profile> contactList = new ArrayList<profile>();  
        // Select All Query  
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;  
   

        Cursor cursor = myDatabase.rawQuery(selectQuery, null);
   
        // looping through all rows and adding to list  
        if (cursor.moveToFirst()) {  
            do {  
            	profile witem = new profile();  
                witem.setUser_id(cursor.getString(1));
                witem.setQcode(cursor.getString(2));
                witem.setType(cursor.getString(3));
                // Adding contact to list  
                contactList.add(witem);  
            } while (cursor.moveToNext());  
        }  
   
        // return contact list  
        return contactList;  
    }  
   
    // code to update the single contact  
   /* public int updateContact(profile witem) {

   
        ContentValues values = new ContentValues();  
        values.put(KEY_NAME, witem.getName());  
        values.put(KEY_PH_NO, witem.getQR());  
   
        // updating row  
        return myDatabase.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(witem.getName()) });
    } */
   
    // Deleting single contact  
    public void deleteContact(profile witem) {  

        myDatabase.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(witem.getUser_id()) });

    }  
   
    // Getting contacts Count  
    public int getContactsCount() {  
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;  

        Cursor cursor = myDatabase.rawQuery(countQuery, null);
        cursor.close();  
   
        // return count  
        return cursor.getCount();  
    }  
   
}
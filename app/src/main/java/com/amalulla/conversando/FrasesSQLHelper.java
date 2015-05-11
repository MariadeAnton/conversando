package com.amalulla.conversando;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FrasesSQLHelper extends SQLiteOpenHelper {


	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "frasesDB";
	private static final String KEY_PHRASE = "frase";
	private static final String KEY_CAT = "categoría";
	private static final String KEY_RW = "rw";
	private static final String KEY_KEYBOARD = "teclado";
	
	

	public FrasesSQLHelper (Context context) {
		super (context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	//Creando tabla

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + DATABASE_NAME + " (" + KEY_KEYBOARD + " INTEGER, " +
				KEY_CAT + " INTEGER, " + KEY_PHRASE + " TEXT, " + KEY_RW + " INTEGER)";
		db.execSQL(CREATE_TABLE);

	}
	
	//Actualizando tabla
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
		onCreate(db);
		}
	
	//Añadir frase
	
	public void addPhrase(String frase, int keyboard, int category, int readwrite) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PHRASE, frase);
		values.put(KEY_CAT, category);
		values.put(KEY_RW, readwrite);
		values.put(KEY_KEYBOARD, keyboard);
		
		// Inserting Row
		db.insert(DATABASE_NAME, null, values);
		db.close(); // Closing database connection
	}
		
	//Borrar frase
	// REVISAR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public void deletePhrase(String frase, int tab, int category) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(DATABASE_NAME, KEY_PHRASE + " = '" + frase + "' AND " + KEY_KEYBOARD + " = '" + tab + "' AND " + KEY_CAT + " = '" + category + "'", null);
		db.close();
	}

	
	
	//Buscar categorias
	
	public List<String> getAllCategories() {
		List<String> categoryList = new ArrayList<String>();
		HashSet<String> hs = new HashSet<String>(); //hashSet no admite info duplicada

		String selectQuery = "SELECT " + KEY_PHRASE + " FROM " + DATABASE_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				categoryList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}

		hs.addAll(categoryList);
		categoryList.clear();
		categoryList.addAll(hs);
		return categoryList;
	}
	
	
	//Buscar frases de una categoría
	public List<String> getAllPhrases(int category) {
		List<String> phraseList = new ArrayList<String>();
		
		String selectQuery = "SELECT " + KEY_PHRASE + " FROM " + DATABASE_NAME + 
				" WHERE " + KEY_CAT + "= '" + category + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		//nos aseguramos de que al menos hay un registro		
		if (cursor.moveToFirst()) {
			do {
					phraseList.add(cursor.getString(0));
				} while (cursor.moveToNext());
		}
		
		db.close();
		return phraseList;
	}
	//Devuelve un Cursor con todos los datos en la db
	public Cursor getAllData() {
		String selectQuery = "SELECT " + KEY_CAT + ", " + KEY_PHRASE + ", " + KEY_RW + " FROM " + DATABASE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
			} while (cursor.moveToNext());
		}
		return cursor;
	}
	
	public Cursor getWordsInTab(int keyboard) {
		String selectQuery = "SELECT " + KEY_PHRASE + ", " + KEY_CAT + " FROM " + DATABASE_NAME + " WHERE " + KEY_KEYBOARD + "= '" + keyboard + "'" ;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		return cursor;
	}

	public Cursor getWordsInTabForKeyboard(int keyboard, int category) {
		String selectQuery = "SELECT " + KEY_PHRASE + " FROM " + DATABASE_NAME + " WHERE " + KEY_KEYBOARD + "= '" + keyboard 
				+ "' AND " + KEY_CAT + " = '" + category + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		return cursor;
	}
	
		
	//Editando las frases, actualizándolas
	
	public void updatePhrase(String oldphrase, String newphrase, String category) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(DATABASE_NAME, KEY_PHRASE + " =?", new String[] {String.valueOf(oldphrase) });
		ContentValues values = new ContentValues();
	    values.put(KEY_CAT, category);
	    values.put(KEY_PHRASE, newphrase);
	 
	    // Inserting Row
	    db.insert(DATABASE_NAME, null, values);
		db.close();
	}
	
	//Editando las categorías, actualizándolas
	public void updateCategory(String oldcat, String newcat) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String selectQuery = "SELECT " + KEY_PHRASE + " FROM " + DATABASE_NAME + 
				" WHERE " + KEY_CAT + "= '" + oldcat + "'";
		Cursor cursor = db.rawQuery(selectQuery, null);
		ContentValues values = new ContentValues();	
		values.put(KEY_CAT, newcat);
		if (cursor.moveToFirst()) {
			do {
				values.put(KEY_PHRASE, cursor.getString(0));
			} while (cursor.moveToNext());
		}
	    
	    db.delete(DATABASE_NAME, KEY_CAT + " =?", new String[] {String.valueOf(oldcat) });
	    // Inserting Row
	    db.insert(DATABASE_NAME, null, values);
		db.close();
	}
	
	
	//Borrar DB
	public void borrarTabla() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
		db.close();
		
	}

}


package com.amalulla.conversando;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

public class WordLoader {
	
	public static TableLayout buildTable(Context context, int tab) {
		
		FrasesSQLHelper dbHelper = new FrasesSQLHelper(context);
		Cursor cursor = dbHelper.getWordsInTab(tab);
		
		TableLayout table = new TableLayout(context);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        table.setLayoutParams(params);    
        table.setStretchAllColumns(true); //it can expand in width to fit any extra space
        table.setShrinkAllColumns(true); //it can be shrunk to fit the table into its parent objet
        //it can be both, the column will change its size to always use up the available space, but never more.
        TableRow fila1 = new TableRow(context);
        TableRow fila2 = new TableRow(context);
        TableRow fila3 = new TableRow(context);
        TableRow fila4 = new TableRow(context);
        TableRow fila5 = new TableRow(context);
        TextView vacio = new TextView(context);
        vacio.setText("Aquí irán tus palabras guardadas");
        vacio.setTextSize(25);
        
        
  
        int i = 0;
        if (cursor.moveToFirst()) {
        	do {
        		vacio.setVisibility(View.GONE);
        		CustomButton cb = new CustomButton(context, cursor.getString(0), cursor.getInt(1));
        		if (i < 7) fila1.addView(cb);
            	if (i > 6 && i < 14) fila2.addView(cb);
            	if (i > 13 && i < 21) fila3.addView(cb);
            	if (i > 20 && i < 28) fila4.addView(cb);
            	if (i > 27 && i < 35) fila5.addView(cb);
            	i++;
        	} while (cursor.moveToNext());
        } 
        else {
        	fila1.addView(vacio);
     
        }
        
        table.addView(fila1);
        table.addView(fila2);
        table.addView(fila3);
        table.addView(fila4);
        table.addView(fila5);
        
      
		
		return table;
	}
	
	public static void preloadedWords(Context context) {
		
		FrasesSQLHelper dbHelper = new FrasesSQLHelper(context);
		
		// 1st Tab, 1st Row
		dbHelper.addPhrase("Que", Keyboards.FIXED1, Categories.OTROS, RW.WRITE);
		dbHelper.addPhrase("Pienso", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("Necesito", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("Quiero", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("Espero", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("Estoy", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("Tengo", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		// 1st Tab, 2nd Row
		dbHelper.addPhrase("Quien", Keyboards.FIXED1, Categories.OTROS, RW.WRITE);
		dbHelper.addPhrase("a mi", Keyboards.FIXED1, Categories.SOCIALES, RW.WRITE);
		dbHelper.addPhrase("tu", Keyboards.FIXED1, Categories.SOCIALES, RW.WRITE);
		dbHelper.addPhrase("todos", Keyboards.FIXED1, Categories.PERSONAS, RW.WRITE);
		dbHelper.addPhrase("nadie", Keyboards.FIXED1, Categories.PERSONAS, RW.WRITE);
		dbHelper.addPhrase("lo", Keyboards.FIXED1, Categories.PERSONAS, RW.WRITE);
		dbHelper.addPhrase("sí", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		// 1st Tab, 3rd Row
		dbHelper.addPhrase("Cuando", Keyboards.FIXED1, Categories.OTROS, RW.WRITE);
		dbHelper.addPhrase("es", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("el", Keyboards.FIXED1, Categories.PERSONAS, RW.WRITE);
		dbHelper.addPhrase("ella", Keyboards.FIXED1, Categories.PERSONAS, RW.WRITE);
		dbHelper.addPhrase("me", Keyboards.FIXED1, Categories.SOCIALES, RW.WRITE);
		dbHelper.addPhrase("duele", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("gusta", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		// 1st Tab, 4th Row
		dbHelper.addPhrase("Como", Keyboards.FIXED1, Categories.OTROS, RW.WRITE);
		dbHelper.addPhrase("pocos", Keyboards.FIXED1, Categories.DESCRIPTIVOS, RW.WRITE);
		dbHelper.addPhrase("muy", Keyboards.FIXED1, Categories.DESCRIPTIVOS, RW.WRITE);
		dbHelper.addPhrase("mucho", Keyboards.FIXED1, Categories.DESCRIPTIVOS, RW.WRITE);
		dbHelper.addPhrase("nada", Keyboards.FIXED1, Categories.DESCRIPTIVOS, RW.WRITE);
		dbHelper.addPhrase("mas", Keyboards.FIXED1, Categories.DESCRIPTIVOS, RW.WRITE);
		dbHelper.addPhrase("menos", Keyboards.FIXED1, Categories.DESCRIPTIVOS, RW.WRITE);
		// 1st Tab, 5th Row
		dbHelper.addPhrase("Donde", Keyboards.FIXED1, Categories.OTROS, RW.WRITE);
		dbHelper.addPhrase("aqui", Keyboards.FIXED1, Categories.PERSONAS, RW.WRITE);
		dbHelper.addPhrase("en", Keyboards.FIXED1, Categories.PERSONAS, RW.WRITE);
		dbHelper.addPhrase("traer", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("venir", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("ir", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("hacer", Keyboards.FIXED1, Categories.VERBOS, RW.WRITE);
		
	
		// 2nd Tab, 1st Row
		dbHelper.addPhrase("Como", Keyboards.FIXED2, Categories.OTROS, RW.WRITE);
		dbHelper.addPhrase("era", Keyboards.FIXED2, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("esta", Keyboards.FIXED2, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("frio", Keyboards.FIXED2, Categories.DESCRIPTIVOS, RW.WRITE);
		dbHelper.addPhrase("caliente", Keyboards.FIXED2, Categories.DESCRIPTIVOS, RW.WRITE);
		dbHelper.addPhrase("cine", Keyboards.FIXED2, Categories.NOMBRES, RW.WRITE);
		dbHelper.addPhrase("para", Keyboards.FIXED2, Categories.OTROS, RW.WRITE);
		// 2nd Tab, 2nd Row
		dbHelper.addPhrase("Por que", Keyboards.FIXED2, Categories.OTROS, RW.WRITE);
		dbHelper.addPhrase("hace", Keyboards.FIXED2, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("hay", Keyboards.FIXED2, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("en", Keyboards.FIXED2, Categories.PERSONAS, RW.WRITE);
		dbHelper.addPhrase("y", Keyboards.FIXED2, Categories.OTROS, RW.WRITE);
		dbHelper.addPhrase("tele", Keyboards.FIXED2, Categories.NOMBRES, RW.WRITE);
		dbHelper.addPhrase("con", Keyboards.FIXED2, Categories.OTROS, RW.WRITE);
		
		// 2nd Tab, 3rd Row
		dbHelper.addPhrase("Cual", Keyboards.FIXED2, Categories.OTROS, RW.WRITE);
		dbHelper.addPhrase("es", Keyboards.FIXED2, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("Tengo", Keyboards.FIXED2, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("sed", Keyboards.FIXED2, Categories.PERSONAS, RW.WRITE);
		dbHelper.addPhrase("hambre", Keyboards.FIXED2, Categories.OTROS, RW.WRITE);
		dbHelper.addPhrase("radio", Keyboards.FIXED2, Categories.NOMBRES, RW.WRITE);
		dbHelper.addPhrase("desde", Keyboards.FIXED2, Categories.OTROS, RW.WRITE);
		
		// 2nd Tab, 4th Row
		dbHelper.addPhrase("Por", Keyboards.FIXED2, Categories.OTROS, RW.WRITE);
		dbHelper.addPhrase("la", Keyboards.FIXED2, Categories.PERSONAS, RW.WRITE);
		dbHelper.addPhrase("mañana", Keyboards.FIXED2, Categories.DESCRIPTIVOS, RW.WRITE);
		dbHelper.addPhrase("tarde", Keyboards.FIXED2, Categories.DESCRIPTIVOS, RW.WRITE);
		dbHelper.addPhrase("noche", Keyboards.FIXED2, Categories.DESCRIPTIVOS, RW.WRITE);
		dbHelper.addPhrase("hoy", Keyboards.FIXED2, Categories.DESCRIPTIVOS, RW.WRITE);
		dbHelper.addPhrase("ayer", Keyboards.FIXED2, Categories.DESCRIPTIVOS, RW.WRITE);
		
		// 2nd Tab, 5th Row
		dbHelper.addPhrase("Vamos", Keyboards.FIXED2, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("a", Keyboards.FIXED2, Categories.PERSONAS, RW.WRITE);
		dbHelper.addPhrase("pasear", Keyboards.FIXED2, Categories.VERBOS, RW.WRITE);
		dbHelper.addPhrase("salón", Keyboards.FIXED2, Categories.NOMBRES, RW.WRITE);
		dbHelper.addPhrase("cocina", Keyboards.FIXED2, Categories.NOMBRES, RW.WRITE);
		dbHelper.addPhrase("baño", Keyboards.FIXED2, Categories.NOMBRES, RW.WRITE);
		dbHelper.addPhrase("habitación", Keyboards.FIXED2, Categories.NOMBRES, RW.WRITE);
		
		
	}

}

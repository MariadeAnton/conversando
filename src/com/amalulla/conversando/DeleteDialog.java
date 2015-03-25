package com.amalulla.conversando;

import java.util.ArrayList;
import java.util.List;

import maria.com.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.support.v4.app.NavUtils;

public class DeleteDialog extends Activity {
	
	private int category;
	private String phrase;
	private EditText sendText;
	private List<String> CATEGORIES;

	private Spinner spinnerCat;
	private Spinner spinnerKey;
	private Spinner spinnerPhrase;
	private RadioGroup radios;
	private RadioButton radioWord;
	private RadioButton radioPhrase;
	private TextView textView;
	private TextView textView1;
	private int tab;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setup main content view
		setContentView(R.layout.activity_delete_dialog);
		// Show the Up button in the action bar.
		setupActionBar();
		

	    textView = (TextView) findViewById(R.id.textView1);
	    textView1 = (TextView) findViewById(R.id.textView2);
	       
	    radios = (RadioGroup) findViewById(R.id.radioGroup1);
	    radioWord = (RadioButton) findViewById(R.id.radioWord);
	    radioPhrase = (RadioButton) findViewById(R.id.radioPhrase);
	 
	        
	    radios.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
	    	@Override
	    	public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(radioWord.isChecked()) {
					showWords();
				}
				if(radioPhrase.isChecked()) {
					showPhrase();
				}
					
			}
		});	        
	   
	                
	    //there are a lot of settings, for dialog, check them all out!
	 
	    FrasesSQLHelper db = new FrasesSQLHelper(this);
	    CATEGORIES = new ArrayList<String>(); 
	        
	    CATEGORIES.add("Verbo");
	    CATEGORIES.add("Sustantivo");
	    CATEGORIES.add("Persona");
	    CATEGORIES.add("Adjetivo");
	    CATEGORIES.add("Social");
	    CATEGORIES.add("Otros");
	        
			
		spinnerCat = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> adapterCat = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_dropdown_item, CATEGORIES);
		spinnerCat.setAdapter(adapterCat);
	        		
		spinnerCat.setOnItemSelectedListener(new OnItemSelectedListener() {
				
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	    		category = position;
	    		resetPhrases();
		    }
			@Override
			public void onNothingSelected(AdapterView<?> parent){
					
			}
		});
			
		List<String> KEYBOARDS = new ArrayList<String>();
		KEYBOARDS.add("Básico"); 
		KEYBOARDS.add("Extendido");
		KEYBOARDS.add("Frecuente");
		KEYBOARDS.add("Útil");
		KEYBOARDS.add("Varios");
		spinnerKey = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> adapterKey = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_dropdown_item, KEYBOARDS);
		spinnerKey.setAdapter(adapterKey);
	        		
		spinnerKey.setOnItemSelectedListener(new OnItemSelectedListener() {
				
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, 
					int position, long id) {
				switch(position) {
				case 0:
		    		tab = Keyboards.CUSTOM1;
		   			break;
		   		case 1:
		   			tab = Keyboards.CUSTOM2;
		   			break;
	    		case 2:
	    			tab = Keyboards.CUSTOM3;
	    			break;
		    	}
				resetPhrases();
		    
		    }
			@Override
			public void onNothingSelected(AdapterView<?> parent){
					
			}
		});
		
		radios.check(R.id.radioPhrase);
		showPhrase();
			
	}

	public void cancelDialog(View view){
		finish();
	}
		
	public void eraseDialog(View view){

		FrasesSQLHelper db = new FrasesSQLHelper(this);
		db.deletePhrase(phrase, tab, category);
		Toast toast= Toast.makeText(getApplicationContext(),"Frase Borrada", Toast.LENGTH_SHORT);
		toast.show();
		Intent refresh = new Intent(this, ConversandoActivity.class);
		startActivity(refresh);
	   	this.finish();
	}
		
	private void showWords() {
		textView.setVisibility(View.VISIBLE);
		textView1.setVisibility(View.VISIBLE);
		spinnerCat.setVisibility(View.VISIBLE);
		spinnerKey.setVisibility(View.VISIBLE);
	}
	
	private void showPhrase() {
		category = 6;
		tab = 5;
		resetPhrases();
		textView.setVisibility(View.GONE);
		textView1.setVisibility(View.GONE);
		spinnerKey.setVisibility(View.GONE);
		spinnerCat.setVisibility(View.GONE);
	}
	
	private void resetPhrases(){
		List<String> phraseList = new ArrayList<String>();
		FrasesSQLHelper db = new FrasesSQLHelper(this);
		Cursor cursor= db.getWordsInTabForKeyboard(tab, category);
		if (cursor.moveToFirst()) {
			do {
				phraseList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		
		
		spinnerPhrase = (Spinner) findViewById(R.id.spinner3);
		ArrayAdapter<String> adapterPhrase = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_dropdown_item, phraseList);
		spinnerPhrase.setAdapter(adapterPhrase);
	        		
		spinnerPhrase.setOnItemSelectedListener(new OnItemSelectedListener() {
				
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				phrase = (String) parent.getItemAtPosition(position);
		    
		    }
			@Override
			public void onNothingSelected(AdapterView<?> parent){
					
			}
		});
	}
		
		
	

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete_dialog, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

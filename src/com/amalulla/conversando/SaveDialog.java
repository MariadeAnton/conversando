package com.amalulla.conversando;

import java.util.ArrayList;
import java.util.List;

import maria.com.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;


public class SaveDialog extends Activity {
	
	private int category;
	private String phrase;
	private EditText sendText;
	private List<String> CATEGORIES;
	private RadioGroup radios;
	private RadioButton radioWord;
	private RadioButton radioPhrase;
	private Spinner spinnerCat;
	private Spinner spinnerKey;
	private TextView textView;
	private TextView textView1;
	private int tab;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set up main content view

        setContentView(R.layout.save_dialog);
        
        radios = (RadioGroup) findViewById(R.id.radioGroup1);
        radioWord = (RadioButton) findViewById(R.id.radioWord);
        radioPhrase = (RadioButton) findViewById(R.id.radioPhrase);
        textView = (TextView) findViewById(R.id.textView1);
        textView1 = (TextView) findViewById(R.id.textView2);
        
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

        
        Intent intent = getIntent();
 		Bundle param = intent.getExtras();
 		phrase = param.getString("phrase");
   
                
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
			public void onItemSelected(AdapterView<?> parent, View view, 
					int position, long id) {
	    		category = position;
	    
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
	    
	    	}
			@Override
			public void onNothingSelected(AdapterView<?> parent){
				
			}
		});
		
		radios.check(R.id.radioPhrase);
        showPhrase();
        		
		sendText = (EditText) findViewById(R.id.editPhrase2);
		sendText.setText(phrase);
		int stringLength = phrase.toString().length();
		sendText.setSelection(stringLength);
	}

	public void cancelDialog(View view){
		finish();
	}
	
	public void sendDialog(View view){
		sendText.setSelection(sendText.getText().length());
		String sendPhrase = sendText.getText().toString();
		
		if (sendPhrase.matches("")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.error_null_message)
			   .setIcon(android.R.drawable.ic_delete)
		       .setTitle(R.string.error_null_title)
		       .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		    	   public void onClick(DialogInterface dialog, int id) {
		    	   }
		    	})
			   .show();
		} else {
			FrasesSQLHelper db = new FrasesSQLHelper(this);
			db.addPhrase(sendPhrase, tab, category, 1);
			Toast toast= Toast.makeText(getApplicationContext(),"Frase Guardada", Toast.LENGTH_SHORT);
			toast.show();
			Intent refresh = new Intent(this, ConversandoActivity.class);
	    	startActivity(refresh);
	    	this.finish();
		}
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
		textView.setVisibility(View.GONE);
		textView1.setVisibility(View.GONE);
		spinnerKey.setVisibility(View.GONE);
		spinnerCat.setVisibility(View.GONE);
	}
}

	



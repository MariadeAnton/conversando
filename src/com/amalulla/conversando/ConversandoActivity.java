package com.amalulla.conversando;



import java.util.List;

import maria.com.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

 
public class ConversandoActivity extends FragmentActivity  implements OnInitListener {

	private int MY_DATA_CHECK_CODE = 0;
	static TextToSpeech tts;
	public static EditText inputText; 
	private Button deleteButton;
	private Button yesButton, noButton;
	private Button hiButton, talButton, thanksButton;
	private Button fineButton, pleaseButton,sorryButton;
	private Button okButton, maybeButton, careButton;
	public ViewPager_widget tabs;
  	private Button speakButton;
	private TableLayout tb;
	private static boolean firstTime = true;
    public static ConversandoActivity miActividad=null;

    public void onCreate(Bundle savedInstanceState) {
    		
    	super.onCreate(savedInstanceState);
        miActividad=this;
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.conversando_main); 
    
        inputText = (EditText) findViewById(R.id.input_text);
        deleteButton = (Button)findViewById(R.id.delete_button);
        speakButton = (Button) findViewById(R.id.speak_button);
   /**
  	 * This lines preload fixed keyboards
   	 */      
        FrasesSQLHelper dbHelper = new FrasesSQLHelper(this);
        Cursor checkCursor = dbHelper.getWordsInTab(Keyboards.FIXED1);
    	if (checkCursor.moveToFirst() == false) {
    		WordLoader.preloadedWords(this);
    	}

        Cursor checkCursor2 = dbHelper.getWordsInTab(Keyboards.FIXED2);
     	if (checkCursor2.moveToFirst() == false) {
     		WordLoader.preloadedWords(this);
     	}  
     	                    
        yesButton = (Button)findViewById(R.id.yes_button);
        TalkButton(yesButton);
        noButton = (Button)findViewById(R.id.no_button);
        TalkButton(noButton);
        hiButton = (Button)findViewById(R.id.hi_button);
        TalkButton(hiButton);
        talButton = (Button)findViewById(R.id.tal_button);
        TalkButton(talButton);
        thanksButton = (Button)findViewById(R.id.thanks_button);
        TalkButton(thanksButton); 
        fineButton = (Button)findViewById(R.id.fine_button);
        TalkButton(fineButton);
        pleaseButton = (Button)findViewById(R.id.please_button); 
        TalkButton(pleaseButton); 
        sorryButton = (Button)findViewById(R.id.sorry_button);
        TalkButton(sorryButton);
        okButton = (Button)findViewById(R.id.ok_button);
        TalkButton(okButton);
        maybeButton = (Button)findViewById(R.id.maybe_button);
        TalkButton(maybeButton);
        careButton = (Button)findViewById(R.id.care_button);
        TalkButton(careButton);
        tabs=(ViewPager_widget) findViewById(R.id.viewPagerTabs);   
        tabs.onCreate(this);
        buildTable();
        drawTabs();
	 
/*
 * These are special buttons they have a non ordinary behaviour
 */
		speakButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				String text = inputText.getText().toString();
				if (text!=null && text.length()>0) {
				  Toast.makeText(ConversandoActivity.this, "Diciendo: " + text, Toast.LENGTH_LONG).show();
				  tts.speak(text, TextToSpeech.QUEUE_ADD, null);
		  		}
		  	}
	  	});
		deleteButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v ) {
	  		  inputText.selectAll();
	  		  inputText.setText("");
	  		  inputText.setSelection(0);
			}
		});
  
	if (firstTime) {
			//Launching Text to Speech Engine
			Intent checkIntent = new Intent();
			checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
			startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
			firstTime = false;
		}
    }
	    

        

		
	//Launches Guarda Dialog for saving a phrase
		
	public void savePhrase (View view){
		
		String frasecica=inputText.getText().toString(); //frasecica is my inputText's name      	  

		Intent intent=new Intent(this,SaveDialog.class);
		intent.putExtra("phrase", frasecica);
		startActivity (intent);
	
    
    }
    
	/**
	 * This method is used for talking buttons
	 */
	public void TalkButton(Button buton){

		buton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {	
				Button buton = (Button) v;
				String phrase = buton.getText().toString();
			
				if (phrase!=null && phrase.length()>0) {
					Toast.makeText(ConversandoActivity.this, "Diciendo: " + phrase, Toast.LENGTH_LONG).show();
					tts.speak(phrase, TextToSpeech.QUEUE_ADD, null);
					}
			}
			
		});
		
	}

	/**
	 * This method is used for ordinary button
	 */
	public void ClickButton(Button boton){

		boton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				inputText.getText().insert(inputText.getSelectionStart(),((Button)v).getText()+ " ");

			}
		});
	}
	

		
	/*
	 * This method is for TTS to work
	 */

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				//creates TTS instance
				tts = new TextToSpeech(this, this);
			} 
			else {
				// missing information, install TTS
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	/**
	 * These methods are to start TTS service and to shut it down. 
	 */
	public void onInit(int status) {		
		if (status == TextToSpeech.SUCCESS) {
			Toast.makeText(ConversandoActivity.this, 
					"Text-To-Speech engine se ha inicializado", Toast.LENGTH_LONG).show();
		}
		else if (status == TextToSpeech.ERROR) {
			Toast.makeText(ConversandoActivity.this,
					"Error incicializando Text-To-Speech engine", Toast.LENGTH_LONG).show();
		}
	}

	public void onDestroy() {
		// Don't forget to shutdown - enable TTS for other apps
		if (tts != null) {
			tts.stop();
	  		tts.shutdown();
		}
		super.onDestroy();
	}        

    /*
	 * If user tries to leave the app, this Dialog confirms it
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Salir")
	        .setMessage("¿Desea salir de la aplicación?")
	        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
	        	@Override
	        	public void onClick(DialogInterface dialog, int which) {
	        		finish();    
	        	}

	        })
	        .setNegativeButton("No", null)
	        .show();
		}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case R.id.action_keyboard:
	    		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	    		imm.hideSoftInputFromWindow(inputText.getWindowToken(),0);
	    	case R.id.action_delete:
	            Intent delete = new Intent(this, DeleteDialog.class);
	            startActivity(delete);
	            return true;
	        case R.id.action_settings:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	/**
	 * These method is for drawing the tabs from database 
	 */
	private void drawTabs() {
	    TableLayout tab1 = WordLoader.buildTable(this, Keyboards.FIXED1);
	    TableLayout tab2 = WordLoader.buildTable(this, Keyboards.FIXED2);
	    TableLayout tab3 = WordLoader.buildTable(this, Keyboards.CUSTOM1);
	    TableLayout tab4 = WordLoader.buildTable(this, Keyboards.CUSTOM2);
	    TableLayout tab5 = WordLoader.buildTable(this, Keyboards.CUSTOM3);
	    
		tabs.addFragment(tab1);
		tabs.addFragment(tab2);
		tabs.addFragment(tab3);
		tabs.addFragment(tab4);
        tabs.addFragment(tab5);
        
        tabs.setAdapter();
	}
	
	/**
	 * These method is for drawing the phrases saved in the database
	 */
	
	private void buildTable() {
		
		tb = (TableLayout)findViewById(R.id.list);

        tb.setStretchAllColumns(true); //it can expand in width to fit any extra space
        tb.setShrinkAllColumns(true); //it can be shrunk to fit the table into its parent objet
        //it can be both, the column will change its size to always use up the available space, but never more.
        
		FrasesSQLHelper dbHelper = new FrasesSQLHelper(this);
		Cursor cursor = dbHelper.getWordsInTabForKeyboard(5, 6);
		TextView empty = new TextView(this);
        empty.setText("Aqui van tus frases guardadas");
        empty.setTextSize(25);
        
        
        TableRow row = new TableRow(this);
        
        int i = 0;

        if (cursor.moveToFirst()) {
        	do {
        		if (i == 0) tb.addView(row);
        		empty.setVisibility(View.GONE);
        		CustomButton cb = new CustomButton(this, cursor.getString(0), Categories.FRASES);
        		row.addView(cb);
        		if(i > 0 && i%2 != 0) {
        			row = new TableRow(this);
        			tb.addView(row);
        		}
        		i++;
        	} while (cursor.moveToNext());
        } 
        else {
        	row.addView(empty);
     
        }
		
	}
}
    
    
    
package com.amalulla.conversando;

import maria.com.R;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class CustomButton extends Button {
	
	private Context context;

	public CustomButton(Context context) {
		super(context);
		setBackgroundResource(R.drawable.borders);
		setWidth(0);
	}
	
	public CustomButton (Context context, AttributeSet attrs) {
	    super(context, attrs);
	    setBackgroundResource(R.drawable.borders);
	    // TODO Auto-generated constructor stub
	}

	public CustomButton (Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    setBackgroundResource(R.drawable.borders);
	    // TODO Auto-generated constructor stub
	}
	
	public CustomButton (Context context, String text) {
		super(context);
		final String phrase = text;
		setText(text);

		
		setMinHeight(90);
		setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				ConversandoActivity.inputText.getText().insert(ConversandoActivity.inputText.getSelectionStart(),phrase + " ");
			}
		});
	}
	public CustomButton (Context context, String text, int category) {
		super(context);
		final String phrase = text;
//		int resColor = getResources().getColor(color);
		setText(text);
//		setBackgroundColor(resColor);
//		setBackgroundResource(R.drawable.borders);
		setMinHeight(90);
//		setWidth(0);
		setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				ConversandoActivity.inputText.getText().insert(ConversandoActivity.inputText.getSelectionStart(),phrase + " ");
			}
		});
		
		switch(category) {
		case Categories.VERBOS: 
			setBackgroundResource(R.color.green_button);
			break;
		case Categories.NOMBRES:
			setBackgroundResource(R.color.orange_button);
			break;
		case Categories.PERSONAS:
			setBackgroundResource(R.color.yellow_button);
			break;
		case Categories.DESCRIPTIVOS:
			setBackgroundResource(R.color.blue_button);
			break;
		case Categories.SOCIALES:
			setBackgroundResource(R.color.purple_button);
			break;
		case Categories.OTROS:
			setBackgroundResource(R.color.grey_button);
			break;
		case Categories.FRASES:
			setBackgroundResource(R.color.frases_button);
			break;
		
		}	
	
	}	
	
}
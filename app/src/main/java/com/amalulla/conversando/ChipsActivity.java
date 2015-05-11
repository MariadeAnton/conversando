package com.amalulla.conversando;




import maria.com.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ChipsActivity extends EditText {
        
        
        public boolean isTextAdditionInProgress = false, isTextDeletedFromTouch = false;
        private int beforeChangeIndex = 0, afterChangeIndex = 0;
        public boolean textAdded = false; 
        public int stringLength = 0;
        public int myPosition = 0;
        private String changeString = "";
        /* Constructor */
        public ChipsActivity(Context context) {
                super(context);
                init(context);
        }
        /* Constructor */
        public ChipsActivity(Context context, AttributeSet attrs) {
                super(context, attrs);
                init(context);
        }
        /* Constructor */
        public ChipsActivity(Context context, AttributeSet attrs, int defStyle) {
                super(context, attrs, defStyle);
                init(context);
        }
        /* set listeners for item click and text change */
        public void init(Context context){

                addTextChangedListener(textWather);
        }
        /*TextWatcher, If user type any word and presses space then following code will regenerate chips */
        private TextWatcher textWather = new TextWatcher() {
                
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                       
                        String texto = s.toString();
                        if((texto.length()==count) && textAdded ==false) return;
                        
                        textAdded = true;
                        if(count-before<0)return;
                        if((count-before ==1 )&&(( s.charAt(start) == ' ')||( s.charAt(start) == ',')||( s.charAt(start) == '.')||( s.charAt(start) == '!')||( s.charAt(start) == '?')))setChips();
                        if(count-before >1 )setChips();
                

                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,int after) {

                	String currentText = getText().toString();
	                if(currentText.length() ==0) textAdded=true;
                        
                }
                @Override
                public void afterTextChanged(Editable s) {
	        			
        			
        		}
        	};
        /*This function has whole logic for chips generate*/
        public void setChips(){
                SpannableStringBuilder ssb = new SpannableStringBuilder(getText());
                String mytext = getText().toString();
                
                String chips[] = mytext.trim().split(" ");
                LayoutInflater lf = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                int x=0;
                for(String c: chips){
                        int sum=0;
                        if((c.length()>1)&&(c.endsWith(","))){c=c.substring(0, c.length()-1);
                                                                sum=1;
                                                }
                        
                        TextView textView = (TextView) lf.inflate(R.layout.textview, null);
                        textView.setText(c); // set text
                        
                        // converts TextView to drawable Bitmap.
                        int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                        textView.measure(spec, spec);
                        textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
                        Bitmap b = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(),Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(b);
                        canvas.translate(-textView.getScrollX(), -textView.getScrollY());
                        textView.draw(canvas);
                        textView.setDrawingCacheEnabled(true);
                        Bitmap cacheBmp = textView.getDrawingCache();
                        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
                        textView.destroyDrawingCache(); // destroy drawable
                        // create bitmap drawable for imagespan
                        BitmapDrawable bmpDrawable = new BitmapDrawable(this.getResources(),viewBmp);
                        bmpDrawable.setBounds(0, 0,bmpDrawable.getIntrinsicWidth(),bmpDrawable.getIntrinsicHeight());
                        // create and set imagespan
                        ssb.setSpan(new ImageSpan(bmpDrawable),x ,x + c.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                       
                        
                        this.setMovementMethod(LinkMovementMethod.getInstance());
                
                        ClickableSpan clickSpan = new ClickableSpan() {

	                        @Override
	                        public void onClick(View view) {
	                        		deleteString();
	                        }
                		
                        };
                
                        ssb.setSpan(clickSpan, x, x + c.length()+sum,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        x = x+ c.length() +1+sum;
                }
                // set chips span
                
                
                if(mytext.endsWith(" "))setText(ssb);
                else {
                        setText(mytext+" ");
                        setChips();
                }
                // move cursor to last
                setSelection(getText().length());
        }
       
        
        private void deleteString(){
            int[] startEnd = getSelectionStartAndEnd();
            int i = startEnd[0];
            int j = startEnd[1];
            isTextDeletedFromTouch = true;
            textAdded = true;
            
         
            
        final SpannableStringBuilder sb = new SpannableStringBuilder(this.getText());

                    
        final String deletedSubString = sb.subSequence(Math.min(i, j),
                                    Math.max(i, j)).toString();
            
        myPosition = Math.min(i, j);
            
        boolean hasSpaceAtLast = true;
        try {
              sb.subSequence(Math.min(i, j + 1), Math.max(i, j + 1))
                                    .toString();
            } catch (Exception e) {
                    hasSpaceAtLast = false;
            	}
    
        sb.replace(Math.min(i, hasSpaceAtLast ? j + 1 : j),
                            Math.max(i, hasSpaceAtLast ? j + 1 : j), "");
            new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            ChipsActivity.this.setText(sb);
                            ChipsActivity.this.setSelection(myPosition); //my edittext cursor remains where i deleted!
                            sb.removeSpan(deletedSubString);
                            new Handler().postDelayed(new Runnable() {
                                    
                                    @Override
                                    public void run() {
                                            // TODO Auto-generated method stub
                                            
                                            textAdded= false;
                                            stringLength = ChipsActivity.this.getText().toString().length();
                                            if (stringLength == 0){
                                            	ChipsActivity.this.setSelection(0);
                                            }
                                            isTextDeletedFromTouch = false;                                                                                    
                                            //ChipsActivity.this.setSelection(stringLength);                                          
                                            //Log.i("I am replacing text","I am replacing text 4");
                                           ChipsActivity.this.setMovementMethod(LinkMovementMethod.getInstance());
                                    }
                            },50);
                            
                     }  
            }, 10);
            
            
}

            
/**
     * @return int[]
     * method which simply gets the getSelectionStart and getSelectionEnd
     * of this TextView
     */
    
    private int[] getSelectionStartAndEnd(){
            int[] startEnd = new int[2];
            startEnd[0] = this.getSelectionStart()<0 ? 0 : this.getSelectionStart();
            startEnd[1] = this.getSelectionEnd()<0 ? 0 : this.getSelectionEnd();
            return startEnd;
    }
        
        
                
}                
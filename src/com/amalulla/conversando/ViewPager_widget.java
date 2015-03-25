package com.amalulla.conversando;
import maria.com.R;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import ch.bretscherhochstrasser.android.stickyviewpager.StickyViewPager;


public class ViewPager_widget extends FrameLayout{

	private LayoutInflater inflater;
	private LinearLayout layout;
	private Button tab1, tab2, tab3, tab4, tab5;
	private StickyViewPager pager;
	private ViewPageAdapter adapterList;
	
	public ViewPager_widget(Context context) {
		super(context);
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// TODO Auto-generated constructor stub
	}

	public ViewPager_widget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// TODO Auto-generated constructor stub
	}

	public ViewPager_widget(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// TODO Auto-generated constructor stub
	}
	
	public void onCreate(FragmentActivity act)
	{
		layout =(LinearLayout)inflater.inflate(R.layout.swypetabs,null);
	    tab1 = (Button)layout.findViewById(R.id.tab1);
	    tab1.setOnClickListener(new TabsMovement(0));
	    tab2 = (Button)layout.findViewById(R.id.tab2);
	    tab2.setOnClickListener(new TabsMovement(1));
	    tab3 = (Button)layout.findViewById(R.id.tab3);
	    tab3.setOnClickListener(new TabsMovement(2));;
	    tab4 = (Button)layout.findViewById(R.id.tab4);
	    tab4.setOnClickListener(new TabsMovement(3));
	    tab5 = (Button)layout.findViewById(R.id.tab5);
	    tab5.setOnClickListener(new TabsMovement(4));
		pager= (StickyViewPager)layout.findViewById(R.id.viewPager);
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
			switch(arg0) {
			case 0:
				tab1.setBackgroundResource(R.color.tabs_button);
				tab2.setBackgroundResource(R.color.white_button);
				tab3.setBackgroundResource(R.color.white_button);
				tab4.setBackgroundResource(R.color.white_button);
				tab5.setBackgroundResource(R.color.white_button);
				break;
			case 1:
				tab1.setBackgroundResource(R.color.white_button);
				tab2.setBackgroundResource(R.color.tabs_button);
				tab3.setBackgroundResource(R.color.white_button);
				tab4.setBackgroundResource(R.color.white_button);
				tab5.setBackgroundResource(R.color.white_button);
				break;
			case 2:
				tab1.setBackgroundResource(R.color.white_button);
				tab2.setBackgroundResource(R.color.white_button);
				tab3.setBackgroundResource(R.color.tabs_button);
				tab4.setBackgroundResource(R.color.white_button);
				tab5.setBackgroundResource(R.color.white_button);
				break;
			case 3:
				tab1.setBackgroundResource(R.color.white_button);
				tab2.setBackgroundResource(R.color.white_button);
				tab3.setBackgroundResource(R.color.white_button);
				tab4.setBackgroundResource(R.color.tabs_button);
				tab5.setBackgroundResource(R.color.white_button);
				break;
			case 4:
				tab1.setBackgroundResource(R.color.white_button);
				tab2.setBackgroundResource(R.color.white_button);
				tab3.setBackgroundResource(R.color.white_button);
				tab4.setBackgroundResource(R.color.white_button);
				tab5.setBackgroundResource(R.color.tabs_button);
				break;
			default:
				tab1.setBackgroundResource(R.color.white_button);
				tab2.setBackgroundResource(R.color.white_button);
				tab3.setBackgroundResource(R.color.white_button);
				tab4.setBackgroundResource(R.color.white_button);
				tab5.setBackgroundResource(R.color.white_button);
				break;
			}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			}
			});


		
		adapterList= new ViewPageAdapter (act.getSupportFragmentManager ());
		addView(layout);
		
		
	}
	
	public void addFragment(View view){
		
		
		adapterList.addFragment(new FragmentForPager(view));
		
	}
	
	public void setAdapter(){
		
		pager.setAdapter(adapterList);
		pager.setAnimationDuration(100);
		pager.setAnimationEffect(StickyViewPager.DEPTH_EFFECT);
		
	}
	
	public class TabsMovement implements OnClickListener {

		private int index=-1;
		
		
		public TabsMovement(int index) {
			super();
			this.index = index;
			
		}


		@Override
		public void onClick(View v) {
			
				pager.setCurrentItem(index);
	
			}
			
		}
		

	}




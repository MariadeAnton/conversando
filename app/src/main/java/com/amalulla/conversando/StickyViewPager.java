/*
 * Copyright (C) 2013 Martin Hochstrasser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amalulla.conversando;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;




import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import maria.com.R;

/**
 * Simple extension to {@link ViewPager} that allows the protection of paged
 * views against {@link ViewPager}'s default horizontal swipe detection and
 * gesture handling by declaring them "sticky".
 * <p>
 * The {@link StickyViewPager} allows for placing of any views that require
 * touch input(e.g. map fragment) within its paged views and selectively
 * overriding {@link ViewPager}'s default swipe detection behavior by declaring
 * the view's position as sticky. The positions that should be treated sticky
 * can be set through {@link #addStickyPosition(int)} or declared through XML.
 * <p>
 * Swipe gestures for navigation on sticky positions are only accepted when
 * initiated within a certain margin on the left and right of the pager's inner
 * edge. The with of the margin can either be set through
 * {@link #setSwipeMarginWidth(int)} or declared as XML attribute. The default
 * margin width is 25dip.
 * <p>
 * Other view pager positions, not declared as sticky, are handled by
 * {@link ViewPager}'s default swipe detection.*
 *
 * @attr ref R.styleable.StickyViewPager_stickyPositions
 * @attr ref R.styleable.StickyViewPager_swipeMarginWidth
 *
 * @author Martin Hochstrasser
 *
 */
public class StickyViewPager extends ViewPager {

	/** Default swipe margin width: 40dip */
	public static final int DEPTH_EFFECT_LEFT = 0;
	public static final int DEPTH_EFFECT_RIGHT=1;
	public static final int ZOOM_OUT_EFFECT= 2;

	private static final int DEFAULT_SWIPE_MARGIN_WIDTH_DIP = 40;

	private static final String TAG = "ScrollProtectionViewPager";
	private boolean isPagingEnabled=true;
	private FixedSpeedScroller scroller;
	private OnPageChangeListener onPageChangeListener;
	private int currentPosition;
	private final Set<Integer> stickyPositions = new HashSet<Integer>();
	private int swipeMarginWidth;

	/**
	 * Constructor without XML attributes.
	 *
	 * @param context
	 *            the activity context
	 */
	public StickyViewPager(final Context context) {
		super(context);
		setDefaultSwipeMargin(context);
		wireUpInternalListener();


	}

	/**
	 * Constructor with XML attributes.
	 *
	 * @param context
	 *            the activity context
	 * @param attrs
	 *            the attributes to set
	 */
	public StickyViewPager(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		setDefaultSwipeMargin(context);
		wireUpInternalListener();
		readAttributes(context, attrs);
		
	}

	private void setDefaultSwipeMargin(final Context context) {
		swipeMarginWidth = (int) (DEFAULT_SWIPE_MARGIN_WIDTH_DIP * context
				.getResources().getDisplayMetrics().density);
	}

	private void wireUpInternalListener() {
		// The subclass must be the parent ViewPager's only
		// OnPageChangeListener, but this class can have one listener
		// too, to also delegate all incoming listener calls to. This allows for
		// the same listener functionality as the original ViewPager.
		super.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(final int position) {
				currentPosition = position;
				if (onPageChangeListener != null) {
					onPageChangeListener.onPageSelected(position);
				}
			}

			@Override
			public void onPageScrolled(final int position,
					final float positionOffset, final int positionOffsetPixels) {
				if (onPageChangeListener != null) {
					onPageChangeListener.onPageScrolled(position,
							positionOffset, positionOffsetPixels);
				}
			}

			@Override
			public void onPageScrollStateChanged(final int state) {
				if (onPageChangeListener != null) {
					onPageChangeListener.onPageScrollStateChanged(state);
				}
			}
		});

	}

	private void readAttributes(final Context context, final AttributeSet attrs) {
		final TypedArray styledAttributes = context.obtainStyledAttributes(
				attrs, R.styleable.StickyViewPager);

		for (int i = 0; i < styledAttributes.getIndexCount(); ++i) {
			final int attr = styledAttributes.getIndex(i);
			switch (attr) {
			case R.styleable.StickyViewPager_swipeMarginWidth:
				swipeMarginWidth = styledAttributes.getDimensionPixelSize(attr,
						swipeMarginWidth);
				break;
			case R.styleable.StickyViewPager_stickyPositions:
				addStickyPositions(styledAttributes.getString(attr));
				break;
			}
		}
		styledAttributes.recycle();
	}

	private void addStickyPositions(final String positionList) {
		if ((positionList != null) && (positionList.length() != 0)) {
			final String[] rawPositions = positionList.split(",");
			for (final String rawPosition : rawPositions) {
				try {
					addStickyPosition(Integer.parseInt(rawPosition.trim()));
				} catch (final NumberFormatException e) {
					Log.w(TAG, String.format(
                            "Cannot parse sticky position value from '%s'",
                            rawPosition));
				}
			}
		}
	}

	
	
	
	
	@Override
	protected boolean canScroll(final View v, final boolean checkV,
			final int dx, final int x, final int y) {
		// handle here if current position is declared sticky and must be
		// protected, otherwise let ViewPager handle it normally.
		if (stickyPositions.contains(currentPosition)) {
			return !isAllowedSwipe(x, dx);
		}
		return super.canScroll(v, checkV, dx, x, y);
	}

	/**
	 * Determines if the pointer movement event at x and moved pixels is
	 * considered an allowed swipe movement overriding the inner horizontal
	 * scroll content protection.
	 * 
	 * @param x
	 *            X coordinate of the active touch point
	 * @param dx
	 *            Delta scrolled in pixels
	 * @return true if the movement should start a page swipe
	 */
	protected boolean isAllowedSwipe(final float x, final float dx) {
		return ((x < swipeMarginWidth) && (dx > 0))
				|| ((x > (getWidth() - swipeMarginWidth)) && (dx < 0));
	}

	@Override
	public void setOnPageChangeListener(final OnPageChangeListener listener) {
		this.onPageChangeListener = listener;
	}
	
	public void setAnimationEffect(int animationEffect)
	{
		switch(animationEffect)
		{
			case DEPTH_EFFECT_LEFT:
				setPageTransformer(true,new DepthPageTransformer(true));
				break;
				
			case DEPTH_EFFECT_RIGHT:
				setPageTransformer(true,new DepthPageTransformer(false));
				break;
				
			case ZOOM_OUT_EFFECT:
				setPageTransformer(true,new ZoomOutPageTransformer());
				break;
		}
	}

	public void setAnimationDuration(int speed)
	{
		
			
			Interpolator sInterpolator = new AccelerateInterpolator();
		    try {
		        Field mScroller;
		        mScroller = ViewPager.class.getDeclaredField("mScroller");
		        mScroller.setAccessible(true); 
		        scroller = new FixedSpeedScroller(this.getContext(), sInterpolator);
		        // scroller.setFixedDuration(5000);
		        mScroller.set(this, scroller);
		        if(speed!=0)scroller.setmDuration(speed);
		   
		    } catch (NoSuchFieldException e) {
		    } catch (IllegalArgumentException e) {
		    } catch (IllegalAccessException e) {
		    }
		    
		    
	}
	/**
	 * Returns the width of the left and right margin that allows swipe input.
	 * 
	 * @return the width in pixels
	 */
	public int getSwipeMarginWidth() {
		return swipeMarginWidth;
	}

	/**
	 * Sets the width of the left and right margin that allows swipe input.
	 * 
	 * @param width
	 *            the width in pixels
	 */
	public void setSwipeMarginWidth(final int width) {
		swipeMarginWidth = width;
	}

	/**
	 * Adds the position to the set of sticky positions.
	 * 
	 * @param position
	 *            the position to be sticky
	 */
	public void addStickyPosition(final int position) {
		stickyPositions.add(position);
	}

	/**
	 * Removes the position from the set of sticky positions.
	 * 
	 * @param position
	 *            the position to no longer be sticky
	 */
	public void removeStickyPosition(final int position) {
		stickyPositions.remove(position);
	}
	
	
	  @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        if (this.isPagingEnabled) {
	            return super.onTouchEvent(event);
	        }

	        return false;
	    }

	    @Override
	    public boolean onInterceptTouchEvent(MotionEvent event) {
	        if (this.isPagingEnabled) {
	            return super.onInterceptTouchEvent(event);
	        }

	        return false;
	    }

	    public void setPagingEnabled(boolean b) {
	        this.isPagingEnabled = b;
	    }
	
	@Override
		public boolean shouldDelayChildPressedState() {
			// TODO Auto-generated method stub
			return false;
		
		}

	public class FixedSpeedScroller extends Scroller {

	    private int mDuration =0;

	    public FixedSpeedScroller(Context context) {
	        super(context);
	    }

	    public FixedSpeedScroller(Context context, Interpolator interpolator) {
	        super(context, interpolator);
	    }

	    public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
	        super(context, interpolator, flywheel);
	    }


	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
	        // Ignore received duration, use fixed one instead
	        super.startScroll(startX, startY, dx, dy, mDuration);
	    }

	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy) {
	        // Ignore received duration, use fixed one instead
	        super.startScroll(startX, startY, dx, dy, mDuration);
	    }

		public void setmDuration(int mDuration) {
			this.mDuration = mDuration;
		}
	
	}
	
	public class DepthPageTransformer implements ViewPager.PageTransformer {
	    private static final float MIN_SCALE = 0.75f;
	    private boolean leftDir=true;
	    
	    
	    
	    public DepthPageTransformer(boolean dir) {
			// TODO Auto-generated constructor 
	    	leftDir=dir;
		}



		public void transformPage(View view, float position) {
	        int pageWidth = view.getWidth();

	        if(leftDir)
	        {
	        
	        if (position < -1) { // [-Infinity,-1)
	            // This page is way off-screen to the left.
	            view.setAlpha(0);

	        } else if (position <= 0) { // [-1,0]
	            // Use the default slide transition when moving to the left page
	            view.setAlpha(1);
	            view.setTranslationX(0);
	            view.setScaleX(1);
	            view.setScaleY(1);

	        } else if (position <= 1) { // (0,1]
	            // Fade the page out.
	            view.setAlpha(1 - position);

	            // Counteract the default slide transition
	            view.setTranslationX(pageWidth * -position);

	            // Scale the page down (between MIN_SCALE and 1)
	            float scaleFactor = MIN_SCALE
	                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
	            view.setScaleX(scaleFactor);
	            view.setScaleY(scaleFactor);

	        } else { // (1,+Infinity]
	            // This page is way off-screen to the right.
	            view.setAlpha(0);
	        }
	    }
	    else
	    {
	    	 if (position >1) { //(1,+Infinity] 
		            // This page is way off-screen to the left.
		            view.setAlpha(0);

		        } else if (position >= 0) { // (0,1]
		            // Use the default slide transition when moving to the left page
		            
		        	view.setAlpha(1-position);
		            view.setTranslationX(pageWidth * -position);
		            float scaleFactor = MIN_SCALE
		                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
		            view.setScaleX(scaleFactor);
		            view.setScaleY(scaleFactor);

		        } else if (position >= -1) { // [-1,0]
		            // Fade the page out.
		            view.setAlpha(1);

		            // Counteract the default slide transition
		            view.setTranslationX(2*(pageWidth * -position));
		            view.setScaleX(1);
		            view.setScaleY(1);
		            // Scale the page down (between MIN_SCALE and 1)
		            

		        } else { // [-Infinity,-1)
		            // This page is way off-screen to the right.
		            view.setAlpha(0);
		        }
	    }
	}
	}
	
	public class ZoomOutPageTransformer implements PageTransformer {
	    private static final float MIN_SCALE = 0.85f;
	    private static final float MIN_ALPHA = 0.5f;

	    public void transformPage(View view, float position) {
	        int pageWidth = view.getWidth();
	        int pageHeight = view.getHeight();

	        if (position < -1) { // [-Infinity,-1)
	            // This page is way off-screen to the left.
	            view.setAlpha(0);

	        } else if (position <= 1) { // [-1,1]
	            // Modify the default slide transition to shrink the page as well
	            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
	            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
	            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
	            if (position < 0) {
	                view.setTranslationX(horzMargin - vertMargin / 2);
	            } else {
	                view.setTranslationX(-horzMargin + vertMargin / 2);
	            }

	            // Scale the page down (between MIN_SCALE and 1)
	            view.setScaleX(scaleFactor);
	            view.setScaleY(scaleFactor);

	            // Fade the page relative to its size.
	            view.setAlpha(MIN_ALPHA +
	                    (scaleFactor - MIN_SCALE) /
	                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

	        } else { // (1,+Infinity]
	            // This page is way off-screen to the right.
	            view.setAlpha(0);
	        }
	    }
	}
	
	
	
	
}

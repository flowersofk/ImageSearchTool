package com.flowersofk.imagesearchtool.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.flowersofk.imagesearchtool.R;

/**
 * Clear 버튼 추가 에디트 텍스트
 */
public class ClearEditTextView extends androidx.appcompat.widget.AppCompatEditText {

    private Drawable imgCloseButton;

    public ClearEditTextView(Context context) {
        super(context);
        init();
    }
 
    public ClearEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
 
    public ClearEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray types = getContext().obtainStyledAttributes(attrs, R.styleable.ClearEditTextView);
        if (types != null) {
            int cnt = types.getIndexCount();
            for (int i = 0; i < cnt; i++) {
                int attr = types.getIndex(i);

                switch (attr) {
                    case R.styleable.ClearEditTextView_button_clear:
                        imgCloseButton = types.getDrawable(attr);
                        break;
                }
            }
        }

        init();

    }
     
    void init() {     
    	
        // Set bounds of the Clear button so it will look ok
        //imgCloseButton.setBounds(0, 0, imgCloseButton.getIntrinsicWidth(), imgCloseButton.getIntrinsicHeight());
    	
    	float dps = 30;
    	float pxs = dps * getResources().getDisplayMetrics().density;
    	
        imgCloseButton.setBounds(0, 0, (int)pxs, (int)pxs);
    	
        // There may be initial text in the field, so we may need to display the  button
        //handleClearButton();
 
        //if the Close image is displayed and the user remove his finger from the button, clear it. Otherwise do nothing
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	
            	ClearEditTextView et = ClearEditTextView.this;
 
                if (et.getCompoundDrawables()[2] == null)
                    return false;
                 
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                 
                if (event.getX() > et.getWidth() - et.getPaddingRight() - imgCloseButton.getIntrinsicWidth()) {
                    et.setText("");
                    ClearEditTextView.this.handleClearButton();
                }
                return false;
            }
        });
 
        //if text changes, take care of the button
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	ClearEditTextView.this.handleClearButton();
            }
 
            @Override
            public void afterTextChanged(Editable arg0) {
            }
 
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        
        this.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				handleClearButton();			
			}    	
        });
    }
     
    //intercept Typeface change and set it with our custom font
    /*public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Vegur-B 0.602.otf"));
        } else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Vegur-R 0.602.otf"));
        }
    }*/
     
    void handleClearButton() {   
    	
        if (this.getText().toString().equals(""))
        {      	
            // remove clear button
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], null, this.getCompoundDrawables()[3]);
        }
        else
        {
        	if(!this.isFocused()){       		
        		 // remove clear button
                this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], null, this.getCompoundDrawables()[3]);
        	}else{
	            // add clear button
	            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], imgCloseButton, this.getCompoundDrawables()[3]);
        	}
    	}
    }
}
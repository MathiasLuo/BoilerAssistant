package me.mathiasluo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import me.mathiasluo.APP;
import me.mathiasluo.R;
import weekselection.com.library.CheckedTextView;
import weekselection.com.library.util.StringUtil;
import weekselection.com.library.util.ThemeUtil;

/**
 * Created by mathiasluo on 16-4-6.
 */
public class WeekPickLayout extends LinearLayout implements View.OnClickListener {

    private int mAnimationDuration=300;//点击后动画时长默认300毫秒
    private final String SPLIT = "、";
    private final String TAG = "WeekPickLayout";

    private final String[] WEEKS = APP.getInstance().getResources().getStringArray(R.array.weeks);
    private boolean[] mIsCheckeds = new boolean[]{false, false, false, false, false, false, false};

    private int[][] states = new int[][]{
            new int[]{-android.R.attr.state_checked},
            new int[]{android.R.attr.state_checked},
    };
    private int[] colors;
    private int mTextColor=0XFFFFFF;
    private int mTextSize = -1;
    private int mCheckedColor;
    private CheckedTextView mCircleCTV_1;
    private CheckedTextView mCircleCTV_2;
    private CheckedTextView mCircleCTV_3;
    private CheckedTextView mCircleCTV_4;
    private CheckedTextView mCircleCTV_5;
    private CheckedTextView mCircleCTV_6;
    private CheckedTextView mCircleCTV_7;


    public void setVaules(String vaule) {
        if (!StringUtil.isEmpty(vaule)) {
            String values[] = vaule.split(SPLIT);
            int len = values.length;
            for (int i = 0; i < len; i++) {
                int index = StringUtil.getIndex(values[i], WEEKS);
                mIsCheckeds[index] = true;
            }
            setChecked();
        }

    }

    public String getValues() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            if (mIsCheckeds[i]) sb.append(WEEKS[i]).append(SPLIT);
        }
        if (sb.length() == 0) return "";
        else return sb.deleteCharAt(sb.length() - 1).toString();
    }

    public void setChecked() {
        mCircleCTV_1.setChecked(mIsCheckeds[0]);
        mCircleCTV_2.setChecked(mIsCheckeds[1]);
        mCircleCTV_3.setChecked(mIsCheckeds[2]);
        mCircleCTV_4.setChecked(mIsCheckeds[3]);
        mCircleCTV_5.setChecked(mIsCheckeds[4]);
        mCircleCTV_6.setChecked(mIsCheckeds[5]);
        mCircleCTV_7.setChecked(mIsCheckeds[6]);
    }

    public WeekPickLayout(Context context) {
        super(context);
        iniColor( context);
        init(context);
    }

    public WeekPickLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniColor( context);
        applyStyle(context,attrs,0,0);
        init(context);
    }

    public WeekPickLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniColor( context);
        applyStyle(context,attrs,defStyleAttr,0);
        init(context);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WeekPickLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        iniColor( context);
        applyStyle(context,attrs,defStyleAttr,defStyleRes);
        init(context);
    }

    protected void init(Context context) {

        setOrientation(HORIZONTAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;

        mCircleCTV_1 = new CheckedTextView(getContext());
        mCircleCTV_1.setLayoutParams(params);
        intiStyle(mCircleCTV_1, 1);

        addView(mCircleCTV_1);

        mCircleCTV_2 = new CheckedTextView(getContext());
        mCircleCTV_2.setLayoutParams(params);
        intiStyle(mCircleCTV_2, 2);
        addView(mCircleCTV_2);

        mCircleCTV_3 = new CheckedTextView(getContext());
        mCircleCTV_3.setLayoutParams(params);
        intiStyle(mCircleCTV_3, 3);
        addView(mCircleCTV_3);

        mCircleCTV_4 = new CheckedTextView(getContext());
        mCircleCTV_4.setLayoutParams(params);
        intiStyle(mCircleCTV_4, 4);
        addView(mCircleCTV_4);

        mCircleCTV_5 = new CheckedTextView(getContext());
        mCircleCTV_5.setLayoutParams(params);
        intiStyle(mCircleCTV_5, 5);
        addView(mCircleCTV_5);

        mCircleCTV_6 = new CheckedTextView(getContext());
        mCircleCTV_6.setLayoutParams(params);
        intiStyle(mCircleCTV_6, 6);
        addView(mCircleCTV_6);

        mCircleCTV_7 = new CheckedTextView(getContext());
        mCircleCTV_7.setLayoutParams(params);
        intiStyle(mCircleCTV_7, 7);
        addView(mCircleCTV_7);
        setListener();
    }

    protected void iniColor(Context context){
        int mPrimaryColor = ThemeUtil.colorPrimary(context, 0xFF000000);
        mCheckedColor = ThemeUtil.colorControlActivated(context, 0xFF000000);
        colors=new int[]{mPrimaryColor, Color.WHITE};
    }
    protected void setListener() {
        mCircleCTV_1.setOnClickListener(this);
        mCircleCTV_2.setOnClickListener(this);
        mCircleCTV_3.setOnClickListener(this);
        mCircleCTV_4.setOnClickListener(this);
        mCircleCTV_5.setOnClickListener(this);
        mCircleCTV_6.setOnClickListener(this);
        mCircleCTV_7.setOnClickListener(this);
    }

    protected void setClickState(View view) {
        int index = 0;
        if (view == mCircleCTV_1) {
            index = 0;
        } else if (view == mCircleCTV_2) {
            index = 1;
        } else if (view == mCircleCTV_3) {
            index = 2;
        } else if (view == mCircleCTV_4) {
            index = 3;
        } else if (view == mCircleCTV_5) {
            index = 4;
        } else if (view == mCircleCTV_6) {
            index = 5;
        } else if (view == mCircleCTV_7) {
            index = 6;
        }
        mIsCheckeds[index] = !mIsCheckeds[index];
        ((CheckedTextView) view).setChecked(mIsCheckeds[index]);
    }

    protected void intiStyle(CheckedTextView CheckedTextView, int index) {
        CheckedTextView.setAnimDuration(mAnimationDuration);
        CheckedTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        CheckedTextView.setTextColor(new ColorStateList(states, colors));
        CheckedTextView.setBackgroundColor(mCheckedColor);
        CheckedTextView.setText(WEEKS[index - 1]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            CheckedTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        }
        CheckedTextView.setGravity(Gravity.CENTER);
    }
    protected void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){

        TypedArray a = context.obtainStyledAttributes(attrs, weekselection.com.library.R.styleable.WeekPickLayout, defStyleAttr, defStyleRes);
        for(int i = 0, count = a.getIndexCount(); i < count; i++){
            int attr = a.getIndex(i);
            if(attr== weekselection.com.library.R.styleable.WeekPickLayout_wpl_TextSize){
                mTextSize = a.getDimensionPixelSize(attr, 0);
            }else if(attr== weekselection.com.library.R.styleable.WeekPickLayout_wpl_AnimDuration){
                mAnimationDuration= a.getInteger(attr, 0);
            }else if(attr== weekselection.com.library.R.styleable.WeekPickLayout_wpl_CheckedBgColor){
                mCheckedColor=a.getColor(attr,0);
            }else if(attr== weekselection.com.library.R.styleable.WeekPickLayout_wpl_TextColor){
                colors[0]=a.getColor(attr,0);
            }
        }
    }
    @Override
    public void onClick(View v) {
        setClickState(v);
    }
}

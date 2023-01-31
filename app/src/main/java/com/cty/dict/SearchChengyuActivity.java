package com.cty.dict;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.cty.dict.db.DBManager;
import java.util.ArrayList;
import java.util.List;
public class SearchChengyuActivity extends AppCompatActivity {
    EditText cyEt;
    GridView cyGv;
    List<String>mDatas;
    LinearLayout root;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_chengyu);
        cyEt = findViewById(R.id.searchcy_et);
        cyGv = findViewById(R.id.searchcy_gv);
        root = findViewById(R.id.chengyu_root);

        root.setFocusable(true);
        root.setFocusableInTouchMode(true);
        root.requestFocus();

        mDatas = new ArrayList<>();
//        创建适配器对象
        adapter = new ArrayAdapter<>(this, R.layout.item_searchcy_gv, R.id.item_searchcy_tv, mDatas);
        cyGv.setAdapter(adapter);
//        设置GridView的点击1事件
        setGVListener();

        root.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId() != R.id.main_et){
                    root.setFocusable(true);
                    root.setFocusableInTouchMode(true);
                    root.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(cyEt.getWindowToken(), 0);
                }
                return false;
            }
        });
    }
   /* GridView每一个Item点击事件的方法*/
    private void setGVListener() {
        cyGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = mDatas.get(position);
                startPage(msg);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cyEt.setText("");
        initDatas();
    }
    /**
     * 初始化GridView显示的历史记录数据
     * */
    private void initDatas() {
        mDatas.clear();
        List<String> list = DBManager.queryAllCyFromCyutb();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.main_et:
                cyEt.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(cyEt, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.searchcy_iv_back:
                finish();
                break;
            case R.id.searchcy_iv_search:
                String text = cyEt.getText().toString();
                if (TextUtils.isEmpty(text) && text.length() != 4) {
                    return;
                }
                //跳转到成语详情页面，将输入内容传递过去
                startPage(text);
                break;
        }
    }
    /* 携带成语跳转到下一个页面*/
    private void startPage(String text) {
        Intent intent = new Intent(this, ChengyuInfoActivity.class);
        intent.putExtra("chengyu",text);
        startActivity(intent);
    }
}

package com.cuong.rss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
 import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


    public class AddActivity extends AppCompatActivity implements View.OnClickListener {

        private EditText edtName, edtPhone, edtLink;


        private Button btnAdd, btnCancel;
        private DataBaseAcess db = new DataBaseAcess(this);
        int type= 0;
        RSS rss;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add);
            getSupportActionBar().setTitle("RSS detail");
            init();
            Intent intent = getIntent();
            String action = intent.getAction();
            if (action.equals("detail")){
                type =1;
                rss =(RSS) intent.getSerializableExtra("rss");
                setData(rss);
            } else
            if (action.equals("add")){
                type =0;
                rss = new RSS();
            }

        }

        private void init(){
            edtName = (EditText) findViewById(R.id.edtName);
            edtLink = (EditText) findViewById(R.id.edtLink);
            btnAdd = (Button) findViewById(R.id.btnAdd);
            btnCancel = (Button) findViewById(R.id.btnCancel);

            btnAdd.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
        }
        private  void setData(RSS rss){
            getSupportActionBar().setTitle("Detail");
            btnAdd.setText("Update");
            edtName.setText(rss.getmTitle());
            edtLink.setText(rss.getmLink());
        }
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.btnAdd:
                    add();
                    break;
                case R.id.btnCancel:
                    finish();
                    break;
            }
        }

        private void add(){
            if (edtName.getText().toString().isEmpty()) {
                Toast.makeText(this,"Nhập tên Rss!",Toast.LENGTH_LONG).show();
                return;
            }
            if (edtLink.getText().toString().isEmpty()) {
                Toast.makeText(this,"Nhập link Rss!",Toast.LENGTH_LONG).show();
                return;
            }
            rss.setmTitle(edtName.getText().toString());
            rss.setmLink(edtLink.getText().toString());

            if (type==1){
                db.updateRss(rss);


            } else {
                db.addRss(rss);

            }

            db.close();
            Intent i = new Intent();
            setResult(RESULT_OK,i);
            finish();
        }

        @Override
        public void onBackPressed() {
            setResult(RESULT_OK);
            finish();
        }
    }


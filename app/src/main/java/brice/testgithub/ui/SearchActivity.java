/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import brice.testgithub.R;

public class SearchActivity extends AppCompatActivity {

    private Button btnTest;
    private TextView textView;
    private String searchValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        searchValue = intent.getStringExtra(String.valueOf(R.string.search_value));

        btnTest = (Button) findViewById(R.id.btnTab3);
        textView = (TextView) findViewById(R.id.textTab3);
        textView.setText(searchValue);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Testing Button click 3", Toast.LENGTH_SHORT).show();
            }
        });


    }
}

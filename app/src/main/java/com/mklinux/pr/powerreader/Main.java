package com.mklinux.pr.powerreader;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mklinux.pr.powerreader.com.mklinux.pr.powerreader.util.ConnectionUtil;
import com.mklinux.pr.powerreader.com.mklinux.pr.powerreader.util.Info;

import java.util.ArrayList;
import java.util.List;


public class Main extends ActionBarActivity {
    private static String TAG = Main.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edit = (EditText)findViewById(R.id.editText);
        edit.setText("1002435");
        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            @SuppressWarnings("unchecked")
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                List<String> param = new ArrayList<String>();
                param.add("http://api.money.126.net/data/feed/");
                param.add("money.api");
                EditText et = (EditText)findViewById(R.id.editText);

                String num = et.getText().toString();
                param.add(num);

                ConnectionUtil cu = new ConnectionUtil(getApplicationContext());
                cu.addCallback(new NetConnectionCallback());

                cu.execute(param);
            }});
 
    }

    private class NetConnectionCallback implements ConnectionUtil.ConnectionCallback {

        @Override
        public void ConnectionComplete(List<Info> list) {
            // TODO Auto-generated method stub
            for(Info info: list){
                String result = info.id +" , "+ info.current +" , "+ info.high + " , " + info.low;
                Log.v(TAG, result);
            }
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

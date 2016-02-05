package com.mklinux.pr.powerreader.com.mklinux.pr.powerreader.util;

/**
 * Created by Administrator on 2016/1/30.
 */
        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.List;

        import org.json.JSONObject;

        import android.content.Context;
        import android.os.AsyncTask;
        import android.util.Log;

public class ConnectionUtil extends AsyncTask<List<String>, Void, String>{
    private String TAG = ConnectionUtil.class.getSimpleName();

    private String mRequestData = "";

    private List<String> paramList;

    private List<Info> infoList;

    private ConnectionCallback mCallback = null;

    private static final int CONNECTION_TIMEOUT = 10000;

    private static final int READ_TIMEOUT = 10000;

    private Context mContext;

    public interface ConnectionCallback{

        public void ConnectionComplete(List<Info> list);
    }

    public ConnectionUtil(Context context){
        mContext = context;
    }

    public ConnectionUtil addCallback(ConnectionCallback cb){
        mCallback = cb;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")

    protected String doInBackground(List<String>... arg) {
        // TODO Auto-generated method stubd
        String content = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        Log.d(TAG, "do in background.");
        //OutputStreamWriter wr = null;//post method
        try{
            String prefix = arg[0].get(0);//address.
            String surfix = arg[0].get(1);// additional param.
            StringBuilder urlsb = new StringBuilder();
            urlsb.append(prefix);

            paramList = arg[0].subList(2, arg[0].size());
            for(String param: paramList){
                urlsb.append(param);
                urlsb.append(",");//param seperate by comma
            }
            urlsb.append(surfix);
            URL url = new URL(urlsb.toString());
            Log.d(TAG, urlsb.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("GET");
            //conn.setRequestMethod("POST"); //post method
            conn.setDoInput(true);

            //mRequestData = arg[1];//post method
            //wr = new OutputStreamWriter(conn.getOutputStream());//post method
            //wr.write(mRequestData);//post method
            //wr.flush();//post method

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line= reader.readLine())!=null){
                sb.append(line+"\n");
            }

            content = sb.toString();

            reader.close();
            //wr.close();//post method

        }catch (Exception e){
            e.printStackTrace();
            if(reader != null){
                try{
                    reader.close();
                }catch(Exception e1){
                    e1.printStackTrace();
                }

            }
            //if(wr != null){ //post method
            //	try{
            //		wr.close();
            //	}catch(Exception e2){
            //		e2.printStackTrace();
            //	}
            //}
        }finally{
            if(conn != null){
                conn.disconnect();
            }
        }
        Log.d(TAG, content);
        return content;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        String jString = result.substring(result.indexOf('(')+1);
        jString = jString.substring(0, jString.indexOf(')'));
        Log.d(TAG, jString);
        extractJsonData(jString);
        mCallback.ConnectionComplete(infoList);
    }

    private void extractJsonData(String in){
        String result = null;
        infoList = new ArrayList<Info>();
        try{
            JSONObject jObject = new JSONObject(in);
            for(String param: paramList){
                JSONObject obj = jObject.optJSONObject(param);
                Info info = new Info();
                info.id = obj.getString("code");
                info.current = obj.getString("price");
                info.high = obj.getString("high");
                info.low = obj.getString("low");
                infoList.add(info);

            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }


}

package com.example.chethan.jsonwithsqlitedb;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.chethan.jsonwithsqlitedb.DataBase.DBClass;
import com.example.chethan.jsonwithsqlitedb.DataBase.DBHelper;
import com.example.chethan.jsonwithsqlitedb.DataBase.DBPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    Adapter adapter;


    List<String> NameLst = new ArrayList<>();
    List<String> IdLst = new ArrayList<>();
    List<String> YearLst = new ArrayList<>();
    List<String> ColorLst = new ArrayList<>();
    List<String> ValueLst = new ArrayList<>();

    DBHelper dbHelper;
    DBPojo dbPojo;

    List<DBPojo> dbPojoList = new ArrayList<>();

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(MainActivity.this);
        dbPojo = new DBPojo();

        recyclerView = findViewById(R.id.rcv_id);
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);


        new  FetchDataFromJSON().execute();

    }


    public class FetchDataFromJSON extends AsyncTask<String,Integer,Boolean>{

        Boolean asdf = false;

        @Override
        protected void onPreExecute() {
//            super.onPreExecute();

            DBHelper helper = new DBHelper(MainActivity.this);

            helper.DeleteAll();

            progressDialog = ProgressDialog.show(MainActivity.this,"Loading","Please Wait",true);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            HttpURLConnection connection = null;
            StringBuilder builder = null;

            try {
                URL url = new URL("https://reqres.in/api/unknown/2");
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                builder = new StringBuilder();
                String line = "";

                while ((line = reader.readLine())!=null){
                    builder.append(line);
                    System.out.println("Line:"+line);
                }

                inputStream.close();

                String jsonStr = builder.toString();

                if (jsonStr!=null){

                    JSONObject jsonObject = new JSONObject(jsonStr);

                    JSONObject object = jsonObject.getJSONObject("data");


//                    NameLst.add(object.getString("name"));
//                    IdLst.add(object.getString("id"));
//                    YearLst.add(object.getString("year"));
//                    ColorLst.add(object.getString("color"));
//                    ValueLst.add(object.getString("pantone_value"));


                    dbPojo.setName(object.getString("name"));
                    dbPojo.setId(object.getString("id"));
                    dbPojo.setYear(object.getString("year"));
                    dbPojo.setColor(object.getString("color"));
                    dbPojo.setValue(object.getString("pantone_value"));

                    dbHelper.ADDIntoDb(dbPojo);


                }





            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return asdf;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);

            progressDialog.dismiss();

            dbPojoList = dbHelper.Fetch();

            for (DBPojo pojo:dbPojoList){

                NameLst.add(pojo.getName());
                IdLst.add(pojo.getId());
                YearLst.add(pojo.getYear());
                ColorLst.add(pojo.getColor());
                ValueLst.add(pojo.getValue());

                System.out.println("pojoName:"+pojo.getName());

            }





            adapter.notifyDataSetChanged();

        }

    }



    public class Adapter extends RecyclerView.Adapter<Adapter.JSONHolder>{


        @NonNull
        @Override
        public JSONHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.recycler_layout,viewGroup,false);
            JSONHolder holder = new JSONHolder(view);
            return holder;

        }

        @Override
        public void onBindViewHolder(@NonNull JSONHolder jsonHolder, int i) {

            jsonHolder.name.setText(NameLst.get(i));
            jsonHolder.id.setText(IdLst.get(i));
            jsonHolder.year.setText(YearLst.get(i));
            jsonHolder.color.setText(ColorLst.get(i));
            jsonHolder.value.setText(ValueLst.get(i));



        }

        @Override
        public int getItemCount() {
            return NameLst.size();
        }

        public class JSONHolder extends RecyclerView.ViewHolder{

            private TextView name,id,year,color,value;


            public JSONHolder(@NonNull View v) {
                super(v);

                name = v.findViewById(R.id.disp_name);
                id = v.findViewById(R.id.disp_id);
                year = v.findViewById(R.id.disp_year);
                color = v.findViewById(R.id.disp_color);
                value = v.findViewById(R.id.disp_value);
            }
        }
    }
}

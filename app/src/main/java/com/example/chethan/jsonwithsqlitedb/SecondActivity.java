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
import android.widget.TextView;

import org.json.JSONArray;
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

public class SecondActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SecAdapter secAdapter;

    ProgressDialog progressDialog;

    List<String> PageLst = new ArrayList<>();
    List<String> Per_PageLst = new ArrayList<>();
    List<String> TotalLst = new ArrayList<>();
    List<String> Total_page_Lst = new ArrayList<>();


    List<String> IDLst = new ArrayList<>();
    List<String> NameLst = new ArrayList<>();
    List<String> YearLst = new ArrayList<>();
    List<String> ColorLst = new ArrayList<>();
    List<String> ValueLst = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        recyclerView = findViewById(R.id.sec_rcv_id);

        recyclerView.setHasFixedSize(true);
        secAdapter = new SecAdapter();
        recyclerView.setAdapter(secAdapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);



        new GetJsonArrayAndObject().execute();


    }

    public class GetJsonArrayAndObject extends AsyncTask<String,Integer,Boolean>{

        Boolean asdf = false;

        @Override
        protected void onPreExecute() {
//            super.onPreExecute();
            progressDialog = ProgressDialog.show(SecondActivity.this,"Loading JsonData","Please Wait",true);
        }



        @Override
        protected Boolean doInBackground(String... strings) {

            HttpURLConnection connection = null;
            StringBuilder builder = null;

            try {
                URL url = new URL(ServerLinkPojo.getSecLink());
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                builder = new StringBuilder();
                String line ="";

                while ((line = reader.readLine())!=null){
                    builder.append(line);
                    System.out.println("Line:"+line);

                }

                inputStream.close();

                String jsonString = builder.toString();

                if (jsonString!=null){


                    JSONObject object = new JSONObject(jsonString);


                 PageLst.add(object.getString("page"));
                 Per_PageLst.add(object.getString("per_page"));
                 TotalLst.add(object.getString("total"));
                 Total_page_Lst.add(object.getString("total_pages"));

                    System.out.println("PageSize:"+PageLst.size());
                    System.out.println("Page:"+PageLst);

//                    JSONObject objectJSONObject = object.getJSONObject("data");


                    JSONArray jsonArray = object.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        IDLst.add(jsonObject.getString("id"));
                        NameLst.add(jsonObject.getString("name"));
                        YearLst.add(jsonObject.getString("year"));
                        ColorLst.add(jsonObject.getString("color"));
                        ValueLst.add(jsonObject.getString("pantone_value"));

                        System.out.println("ID:"+IDLst);
                        System.out.println("Name:"+NameLst);

                    }





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

            secAdapter.notifyDataSetChanged();
        }
    }



    public class SecAdapter extends RecyclerView.Adapter<SecAdapter.SecHolder>{

        @NonNull
        @Override
        public SecHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(SecondActivity.this).inflate(R.layout.sec_rcv_lay,viewGroup,false);
            SecHolder holder = new SecHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull SecHolder secHolder, int i) {

            secHolder.page.setText(PageLst.get(i));
            secHolder.per_page.setText(Per_PageLst.get(i));
            secHolder.total.setText(TotalLst.get(i));
            secHolder.total_pages.setText(Total_page_Lst.get(i));

            secHolder.id.setText(IDLst.get(i));
            secHolder.name.setText(NameLst.get(i));
            secHolder.year.setText(YearLst.get(i));
            secHolder.color.setText(ColorLst.get(i));
            secHolder.value.setText(ValueLst.get(i));


        }

        @Override
        public int getItemCount() {
            return PageLst.size();
        }

        public class SecHolder extends RecyclerView.ViewHolder{

           private TextView page,per_page,total,total_pages,id,name,year,color,value;

            public SecHolder(@NonNull View view) {
                super(view);

                page = view.findViewById(R.id.page_id);
                per_page = view.findViewById(R.id.per_page_id);
                total = view.findViewById(R.id.total_id);
                total_pages = view.findViewById(R.id.total_pages_id);

                id = view.findViewById(R.id.data_id);
                name = view.findViewById(R.id.data_name);
                year = view.findViewById(R.id.data_year);
                color = view.findViewById(R.id.data_color);
                value = view.findViewById(R.id.data_value);



            }
        }
    }



}

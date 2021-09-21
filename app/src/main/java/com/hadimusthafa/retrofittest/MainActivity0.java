package com.hadimusthafa.retrofittest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity0 extends AppCompatActivity {
    private RecyclerView recyclerView;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        recyclerView=findViewById(R.id.recyclerView);
        getWhatWeNeedMethod();
    }

    private void getWhatWeNeedMethod() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        Call<String> call = api.getWhatWeNeed("9544","AreYouOk");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.e("call-->", "-->"+call.request());
                Log.e("response-->", "-->"+response.body());
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        try {

                            JSONArray jsonArray = new JSONArray(response.body());

                            Log.e("response-->", "----Hadi----->"+jsonArray);

                            ArrayList<MyModel> modelRecyclerArrayList = new ArrayList<>();
                            try {
                                for (int i=0;i<jsonArray.length();i++){

                                    MyModel modelRecycler = new MyModel();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    modelRecycler.setImage(jsonObject.getJSONObject("image").getString("original"));
                                    modelRecycler.setName(jsonObject.getString("name"));
                                    modelRecyclerArrayList.add(modelRecycler);
                                }
                                if (modelRecyclerArrayList.size()>0){
                                    myAdapter = new MyAdapter(getApplicationContext() , modelRecyclerArrayList);
                                    recyclerView.setAdapter(myAdapter);
                                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                                }else {
                                    recyclerView.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                Log.e("e-->","populateRecycler-->"+e.toString()+"");
                            }


                        }catch (Exception e){
                            Log.e("ee-->",e.toString());
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("t-->","-->"+t.toString());
            }
        });
    }
}
package com.liu.handbeauty.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.liu.handbeauty.R;
import com.liu.handbeauty.bean.Classify;
import com.liu.handbeauty.system.SystemBean;
import com.liu.handbeauty.system.SystemPath;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Liu on 2016-05-25.
 */
public class SplashActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getClassify();

    }

    public interface ClassifyService {
        @GET("classify")
        Call<Classify> listRepos();
    }

    //    http://www.tngou.net/tnfs/api/classify
    public void getClassify() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SystemPath.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ClassifyService service = retrofit.create(ClassifyService.class);
        Call<Classify> repos = service.listRepos();
        repos.enqueue(new Callback<Classify>() {
            @Override
            public void onResponse(Call<Classify> call, Response<Classify> response) {
                Log.d("he",response.body().toString());
                SystemBean.testbean=response.body();

                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<Classify> call, Throwable t) {
                Log.d("he",t.getMessage());
            }
        });
    }
}

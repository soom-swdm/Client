package io.github.swdm.pos;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ApiService apiService;

    private static String USER_ID = "";

    private boolean userFlag = false;
    private String todayDate = "2019-06-27T08:25:10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.listview);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter();
        recyclerView.setAdapter(mAdapter);

        apiService = ((ApiReposApplication) getApplication()).getApiService();
        todayDate = new Date().toString();
        Toast.makeText(getApplicationContext(),"오늘 : " + todayDate,Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.user_menu:
                userBtnClick();
                break;
            case R.id.code_menu:
                codeBtnClick();
                break;
            case R.id.clear_menu:
                clearBtnClick();
                break;
            case R.id.charge_menu:
                chargeBtnClick();
                break;
            case R.id.buy_menu:
                buyBtnClick();
                break;
        }
        return false;
    }

    public void userBtnClick() {
        userFlag = true;
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.initiateScan();
    }

    public void clearBtnClick() {
        mAdapter.clearData();
    }

    public void chargeBtnClick() {
//        ArrayList<Product> items = (ArrayList<Product>) mAdapter.getItems();
////        for (Product item : items) {
////            apiService.pay(USER_ID, item.ID, todayDate,
////                    0, item.price, 1).enqueue(new Callback<ResponseBody>() {
////                @Override
////                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
////                    Toast.makeText(getApplicationContext(), "완료!", Toast.LENGTH_SHORT).show();
////                }
////
////                @Override
////                public void onFailure(Call<ResponseBody> call, Throwable t) {
////
////                }
////            });
////        }
        apiService.pay(USER_ID, "987654321", todayDate,
                    0, 30000, 1).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(getApplicationContext(), "완료!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
    }

    public void buyBtnClick() {
        int totall = 0;
        int percents = 0;
        // 0 : 포인트 차감, accum : 적립
        ArrayList<Product> items = (ArrayList<Product>) mAdapter.getItems();
        for (Product item : items) {
            //적립중
            totall += item.price;
            percents += (int)(item.price * (item.persent/100.f));
            apiService.pay(USER_ID, item.ID, todayDate,
                    0, item.price, 1).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(getApplicationContext(), "완료!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
            //소비
            apiService.pay(USER_ID, item.ID, todayDate,
                    1, item.price, 1).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(getApplicationContext(), "완료!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
        Toast.makeText(getApplicationContext(),percents + "숨 적립 | " +  "구매 금액 : " + totall,Toast.LENGTH_LONG).show();


    }

    public void codeBtnClick() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.initiateScan();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String re = scanResult.getContents();
            String message = re;
            if (userFlag == true) {
                USER_ID = message;
                userFlag = false;
                Toast.makeText(getApplicationContext(), USER_ID + "님 안녕하세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("onActivityResult", "onActivityResult: ." + re);
            Toast.makeText(this, re, Toast.LENGTH_LONG).show();
            apiService.getItem(re).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Product product = response.body();
                    Log.d("TLATPDYD", "성공 : " + response.message());
                    if (product != null) {
                        Log.d("TLATPDYD", "성공 2: ");
                        ((MyAdapter) mAdapter).addData(product);

                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {

                }
            });

        }
    }
}

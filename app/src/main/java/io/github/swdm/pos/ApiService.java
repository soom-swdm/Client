package io.github.swdm.pos;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST("SeeItem")
    Call<Product> getItem(@Field("ID") String ID);

    @FormUrlEncoded
    @POST("Pay")
    Call<ResponseBody> pay(
            @Field("user") String user,
            @Field("item") String item,
            @Field("date") String date, //"2019-05-27T08:25:10"
            @Field("flag") int flag, //1,돈쓰기, 0: 적립
            @Field("amount") int amount, //가격
            @Field("number") int number //갯수
            );
}
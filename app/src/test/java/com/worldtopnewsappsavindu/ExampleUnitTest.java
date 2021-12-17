package com.worldtopnewsappsavindu;

import org.junit.Test;

import static org.junit.Assert.*;

import com.Model.Headlines;
import com.Retrofit_Rest_Api.ApiClient;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void APIIsWorking(){
        final String API_KEY = "209ef3dd92b94868bb9f733db7df01c2";
        final String[] val = {"hai","b"};
        Call<Headlines> call;
            call= ApiClient.getInstance().getApi().getHeadlines("us",API_KEY);
        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                val[0] = response.toString();
                if (response.isSuccessful() && response.body().getArticles() != null){
                    val[0] = response.toString();
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                val[0] = t.getMessage();
            }
        });

        assertEquals("data", Arrays.toString(val));
    }
}
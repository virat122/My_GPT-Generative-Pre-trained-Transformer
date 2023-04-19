package com.example.mygpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText massageEditText;
    ImageButton sendButton;
    List<Massage> massageList;
    MassageAdopter massageAdopter;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        massageList=new ArrayList<>();

        recyclerView=findViewById(R.id.recycler_view);
        welcomeTextView=findViewById(R.id.welcome_text);
        massageEditText=findViewById(R.id.massage_edit_text);
        sendButton=findViewById(R.id.send_btn);

        massageAdopter=new MassageAdopter(massageList);
        recyclerView.setAdapter(massageAdopter);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);


        sendButton.setOnClickListener((v)->{
            String question=(String)massageEditText.getText().toString().trim();
            addToChat(question,Massage.SENT_BY_ME);
            massageEditText.setText("");
            callAPI(question);
            welcomeTextView.setVisibility(View.GONE);
          //  Toast.makeText(this, question, Toast.LENGTH_SHORT).show();
        });
    }

    void addToChat(String massage,String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                massageList.add(new Massage(massage,sentBy));
                massageAdopter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(massageAdopter.getItemCount());
            }
        });
    }

    void addResponse(String response){

        addToChat(response,Massage.SENT_BY_bot);
    }
    void callAPI(String question){
        //okhttp set up
        JSONObject jsonBody=new JSONObject();
        try {
            jsonBody.put("model","text-davinci-003");
            jsonBody.put("prompt",question);
            jsonBody.put("temperature",0);
            jsonBody.put("max_tokens",100);
            jsonBody.put("top_p",1);
            jsonBody.put("frequency_penalty",0.0);
            jsonBody.put("presence_penalty",0.0);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body=RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer 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")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("failed to load response due to  : "+e.getMessage());

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        String jsonData = response.body().string();


                        JSONObject jsonObject=new JSONObject(jsonData);
                        JSONArray jsonArray=jsonObject.getJSONArray("choices");
                       String result=jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }else {
                    addResponse("failed to load response due to json : "+response.body().toString());
                }

            }
        });


    }
}
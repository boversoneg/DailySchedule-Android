package com.example.studiaharmonogram;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recycler_view;
    DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recycler_view = findViewById(R.id.recycler_view);

        setRecyclerView();
    }

    private void setRecyclerView() {
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DataAdapter(this, new ArrayList<>());
        recycler_view.setAdapter(adapter);

        getList(dataList -> adapter.updateData(dataList));
    }

    public interface DataCallback {
        void onDataReceived(List<DataModel> dataList);
    }

    public void getList(DataCallback callback) {
        TextView apiStatus_textView = (TextView) findViewById(R.id.api_status);
        List<DataModel> data_list = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String Api = "https://api.example.com/lessons"; // Replace with your actual API URL

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api,
                response -> {
                    apiStatus_textView.setText("Status API: Połączono");
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray lessonsArray = jsonObject.getJSONArray("lessons");

                        // Get current date
                        Calendar now = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String today = dateFormat.format(now.getTime());

                        for (int i = 0; i < lessonsArray.length(); i++) {
                            JSONObject lesson = lessonsArray.getJSONObject(i);

                            // Parse lesson date
                            String lessonDate = lesson.getString("dataOd").substring(0, 10); // Get YYYY-MM-DD part

                            // Only add if lesson is today
                            if (lessonDate.equals(today)) {
                                String przedmiot = lesson.getString("przedmiot");
                                String sala = lesson.getString("nazwaSali");
                                String godziny = lesson.getString("godzinaOd") + "-" +
                                        lesson.getString("godzinaDo");

                                data_list.add(new DataModel(przedmiot, sala, godziny));
                            }
                        }

                        // Sort by start time
                        Collections.sort(data_list, (a, b) -> {
                            String timeA = a.getGodziny().split("-")[0];
                            String timeB = b.getGodziny().split("-")[0];
                            return timeA.compareTo(timeB);
                        });

                        runOnUiThread(() -> {
                            adapter.updateData(data_list);
                            adapter.notifyDataSetChanged();
                        });

                        callback.onDataReceived(data_list);
                    } catch (Exception e) {
                        e.printStackTrace();
                        apiStatus_textView.setText("Status API: Błąd parsowania danych");
                    }
                }, error -> {
            apiStatus_textView.setText("Status API: Błąd\n" + error);
            callback.onDataReceived(new ArrayList<>());
        });

        queue.add(stringRequest);
    }
}
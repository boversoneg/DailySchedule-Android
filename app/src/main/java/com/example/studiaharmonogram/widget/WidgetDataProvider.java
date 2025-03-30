package com.example.studiaharmonogram.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.studiaharmonogram.R;
import com.example.studiaharmonogram.DataModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<DataModel> data = new ArrayList<>();

    public WidgetDataProvider(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        // Initialize if needed
    }

    @Override
    public void onDataSetChanged() {
        data.clear();

        // Get today's date in "dd-MM-yyyy" format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String today = dateFormat.format(new Date());

        RequestQueue queue = Volley.newRequestQueue(context);
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.GET,
                "https://schoolapi.socialproject.pl/harmonogram", future, future);

        queue.add(request);

        try {
            String response = future.get(30, TimeUnit.SECONDS);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray lessonsArray = jsonObject.getJSONArray("lessons");

            for (int i = 0; i < lessonsArray.length(); i++) {
                JSONObject lesson = lessonsArray.getJSONObject(i);

                // Check if lesson is today
                String lessonDate = lesson.getString("dataZajec");
                if (!lessonDate.equals(today)) {
                    continue;
                }

                String przedmiot = lesson.getString("przedmiot");
                String sala = lesson.getString("nazwaSali");
                String godziny = lesson.getString("godzinaOd") + "-" +
                        lesson.getString("godzinaDo");

                data.add(new DataModel(przedmiot, sala, godziny));
            }

            // Sort by start time
            Collections.sort(data, (a, b) -> {
                String timeA = a.getGodziny().split("-")[0];
                String timeB = b.getGodziny().split("-")[0];
                return timeA.compareTo(timeB);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == -1 || position >= data.size()) return null;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        DataModel item = data.get(position);

        views.setTextViewText(R.id.widget_przedmiot, item.getPrzedmiot());
        views.setTextViewText(R.id.widget_sala, item.getSala());
        views.setTextViewText(R.id.widget_godziny, item.getGodziny());

        return views;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDestroy() {
        data.clear();
    }
}
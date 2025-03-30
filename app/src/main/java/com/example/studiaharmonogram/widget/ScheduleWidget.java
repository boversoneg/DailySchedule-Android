package com.example.studiaharmonogram.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.example.studiaharmonogram.R;

public class ScheduleWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            // Set up intent for updating widget data
            Intent intent = new Intent(context, WidgetService.class);
            views.setRemoteAdapter(R.id.widget_list_view, intent);

            // Set up empty view
            views.setEmptyView(R.id.widget_list_view, R.id.empty_view);

            // Refresh button intent
            Intent refreshIntent = new Intent(context, ScheduleWidget.class);
            refreshIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    refreshIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            views.setOnClickPendingIntent(R.id.refresh_button, refreshPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, ScheduleWidget.class);
            manager.notifyAppWidgetViewDataChanged(
                    manager.getAppWidgetIds(componentName),
                    R.id.widget_list_view
            );
        }
    }
}
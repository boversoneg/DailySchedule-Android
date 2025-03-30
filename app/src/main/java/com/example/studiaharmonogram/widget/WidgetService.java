package com.example.studiaharmonogram.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(getApplicationContext());
    }
}
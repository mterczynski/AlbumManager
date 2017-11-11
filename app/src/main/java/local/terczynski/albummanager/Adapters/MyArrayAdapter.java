package local.terczynski.albummanager.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter {
    private ArrayList<Object> objects;
    private Context context;
    private int resource;

    public MyArrayAdapter(Context context, int resource, ArrayList<Object> objects) {
        super(context, resource, objects);

        this.context = context;
        this.objects = objects;
        this.resource = resource;
    }
}
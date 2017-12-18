package local.terczynski.albummanager.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import local.terczynski.albummanager.Helpers.Note;
import local.terczynski.albummanager.R;

/**
 * Created by HP on 18.12.2017.
 */

public class DrawerArrayAdapter extends ArrayAdapter {
    private List<String> objects;
    private Context context;
    private int resource;

    public DrawerArrayAdapter(@NonNull Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // inflater - klasa konwertująca xml na kod javy
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.drawer_item_row_layout, null);

        LinearLayout drawer_item_linear = (LinearLayout) convertView.findViewById(R.id.drawer_item_linear);
        ImageView textView_imageView = (ImageView) convertView.findViewById(R.id.drawer_item_image);
        TextView textView_textView = (TextView) convertView.findViewById(R.id.drawer_item_text);

        if(position != 0){
            textView_textView.setText("item " + (position+1));
            textView_imageView.setImageResource(R.drawable.folder);
        }

        // fonts:
        if(position == 0){
            drawer_item_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("click", "hello drawer");
                }
            });
        }

        return convertView;
    }
}

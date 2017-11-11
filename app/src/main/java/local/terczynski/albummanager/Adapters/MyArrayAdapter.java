package local.terczynski.albummanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import local.terczynski.albummanager.R;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //inflater - klasa konwertująca xml na kod javy
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.note_row_layout, null);

        //convertView = inflater.inflate(_resource, null);
        //szukamy każdego TextView w layoucie

        TextView textView_titleValue = (TextView) convertView.findViewById(R.id.note_row_title_value);
        TextView textView_textValue = (TextView) convertView.findViewById(R.id.note_row_text_value);
        TextView textView_idValue = (TextView) convertView.findViewById(R.id.note_row_id_value);

        textView_titleValue.setText("title pobrany z listy, getterem");
        textView_textValue.setText("tekst pobrany z listy, getterem");
        textView_idValue.setText("id pobrany z listy, getterem");

        //gdybyśmy chcieli klikać Imageview wewnątrz wiersza:
//        ImageView iv1 = (ImageView) convertView.findViewById(R.id.iv2);
//        iv1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // klik w obrazek
//            }
//        });

        return convertView;
    }
}
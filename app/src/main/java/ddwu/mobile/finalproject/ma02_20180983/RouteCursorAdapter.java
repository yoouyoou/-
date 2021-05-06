package ddwu.mobile.finalproject.ma02_20180983;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class RouteCursorAdapter extends CursorAdapter {
    LayoutInflater inflater;
    int layout;

    public RouteCursorAdapter(Context context, int layout, Cursor c){
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View listItemLayout = inflater.inflate(layout, parent, false);
        return listItemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvId = view.findViewById(R.id.tv_route_id);
        TextView tvName = view.findViewById(R.id.tv_route_name);
        tvId.setText(cursor.getString(cursor.getColumnIndex("_id")));
        tvName.setText(cursor.getString(cursor.getColumnIndex("address")));
    }
}

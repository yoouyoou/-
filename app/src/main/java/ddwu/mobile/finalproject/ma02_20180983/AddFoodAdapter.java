package ddwu.mobile.finalproject.ma02_20180983;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class AddFoodAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<food> list;

    public AddFoodAdapter(Context context, int resource, ArrayList<food> list){
        this.context = context;
        this.layout = resource;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return list.size(); }

    @Override
    public Object getItem(int position) { return list.get(position); }

    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if(view == null){
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = view.findViewById(R.id.tv_name);
            viewHolder.tvMaker = view.findViewById(R.id.tv_maker);
            viewHolder.tvVol = view.findViewById(R.id.tv_vol);
            viewHolder.tvKcal = view.findViewById(R.id.tv_kcal);
            view.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder)view.getTag();
        }
        food dto = list.get(position);
        if(dto.getName() != null)
            viewHolder.tvName.setText(dto.getName());
        if(dto.getMaker() != null)
            viewHolder.tvMaker.setText("( " + dto.getMaker() + " )");
        viewHolder.tvVol.setText(String.valueOf(dto.getVolume()));
        viewHolder.tvKcal.setText(String.valueOf(dto.getKcal()));

        return view;
    }

    public void setList(ArrayList<food> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    static class ViewHolder{
        public TextView tvName = null;
        public TextView tvVol = null;
        public TextView tvKcal = null;
        public TextView tvMaker = null;
    }
}

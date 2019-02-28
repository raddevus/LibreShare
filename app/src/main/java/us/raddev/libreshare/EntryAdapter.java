package us.raddev.libreshare;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder>{
    public List<Entry> allEntries;
    public EntryAdapter() {

    }
    public EntryAdapter(List<Entry> entryList) {
        allEntries = entryList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView entryTextView, league, yearEstablished;

        public ViewHolder(View itemView) {
            super(itemView);
            entryTextView = (TextView) itemView.findViewById(R.id.entryIdTextView);
            //league = (TextView) itemView.findViewById(R.id.tvLeague);
            //yearEstablished = (TextView) itemView.findViewById(R.id.tvYear);
        }
    }

    public EntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EntryAdapter.ViewHolder holder, int position) {
        holder.entryTextView.setText(allEntries.get(position).get_id());
        //holder.league.setText(allEntries.get(position).getLeague());
        //holder.yearEstablished.setText(String.valueOf(allEntries.get(position).getYearEstablished()));
    }

    @Override
    public int getItemCount() {
        return allEntries.size();
    }
}

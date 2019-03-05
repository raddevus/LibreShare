package us.raddev.libreshare;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.view.HapticFeedbackConstants.LONG_PRESS;
import static us.raddev.libreshare.MainActivity.mConfig;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder>{
    public List<Entry> allEntries;
    static SpannableString spannableStyle;
    static SpannableString previousStyle;
    static String previousText;
    static BackgroundColorSpan textStyle;

    public EntryAdapter() {

    }
    public EntryAdapter(List<Entry> entryList) {
        allEntries = entryList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView entryTextView, league, yearEstablished, currentUserId;

        public ViewHolder(View itemView) {
            super(itemView);
            entryTextView = (TextView) itemView.findViewById(R.id.entryIdTextView);

            entryTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.d("MainActivity", "entryTextView LongClick! : "
                            + entryTextView.getText());
                    Log.d("MainActivity", "got focus : " + String.valueOf(view.getId()));
                    Log.d("MainActivity", "haptic is enabled : " + entryTextView.isHapticFeedbackEnabled ());
                    entryTextView.performHapticFeedback(LONG_PRESS);

                    String initialText = entryTextView.getText().toString();

                    spannableStyle = new SpannableString(initialText);
                    textStyle =  new BackgroundColorSpan(Color.YELLOW);

                    spannableStyle.setSpan(textStyle, 0, spannableStyle.length(), 0);
                    entryTextView.setText(spannableStyle);

                    Log.d("MainActivity", "copying to clipboard");
                    ClipboardManager clipboard = (ClipboardManager)view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.  newPlainText(mConfig.getUserId() ,
                            mConfig.getUserId()+":"+entryTextView.getTag());
                    clipboard.setPrimaryClip(clip);
                    previousStyle = spannableStyle;
                    MainActivity activity = (MainActivity) entryTextView.getContext();
                    activity.openTab(1,mConfig.getUserId()+":"+entryTextView.getTag().toString());

                    return true;
                }
            });

            entryTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {

                        previousText = entryTextView.getText().toString();
                        Log.d("MainActivity", "lost focus : " + String.valueOf(v.getId()) + " : " + previousText);
                        previousStyle = new SpannableString(previousText);
                        previousStyle.removeSpan(previousStyle);
                        entryTextView.setText(previousStyle);
                    }
                }
            });
            //league = (TextView) itemView.findViewById(R.id.tvLeague);
            //yearEstablished = (TextView) itemView.findViewById(R.id.tvYear);
        }
    }

    @Override
    public EntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EntryAdapter.ViewHolder holder, int position) {
        if (allEntries.get(position).title != null && !allEntries.get(position).title.equals("")){
            holder.entryTextView.setText(allEntries.get(position).title);
        }
        else {
            holder.entryTextView.setText(allEntries.get(position).get_id());
        }
        holder.entryTextView.setTag(allEntries.get(position).get_id());
        //holder.league.setText(allEntries.get(position).getLeague());
        //holder.yearEstablished.setText(String.valueOf(allEntries.get(position).getYearEstablished()));
    }

    @Override
    public int getItemCount() {
        return allEntries.size();
    }
}

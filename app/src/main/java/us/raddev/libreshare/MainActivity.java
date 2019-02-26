package us.raddev.libreshare;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public static Config mConfig;

    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mConfig = new Config(getApplicationContext());

        database = FirebaseDatabase.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rand = new Random();
                int n = rand.nextInt(50);
                String outVal = String.valueOf(Config.generateId());
                outVal += ":" + String.valueOf(rand.nextInt(50)) + ":" + String.valueOf(rand.nextInt(50));
                DatabaseReference myRef = database.getReference("message");
                myRef.setValue(outVal);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Random rand = new Random();
            int n = rand.nextInt(50);
            String outVal = String.valueOf(n);
            outVal += ":" + String.valueOf(rand.nextInt(50)) + ":" + String.valueOf(rand.nextInt(50));
            DatabaseReference myRef = database.getReference("message");
            myRef.setValue(outVal);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private TextView currentUserId;
        private Button addNewEntryButton;
        private Button saveMessageButton;
        private EditText newItemEditText;
        private EditText entryIdEditText;
        FirebaseDatabase database;
        ValueEventListener postListener;
        Entry entry;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int section = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = null;
            switch (section){
                case 1:{
                    rootView = inflater.inflate(R.layout.fragment_first, container, false);
                    currentUserId = (TextView)rootView.findViewById(R.id.currentUserIdTextView);
                    currentUserId.setText(mConfig.getUserId());
                    //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                    //textView.setText(getString(R.string.section_format, section));

                    saveMessageButton = (Button)rootView.findViewById(R.id.saveMessageButton);
                    addNewEntryButton = (Button) rootView.findViewById(R.id.addNewEntryButton);
                    newItemEditText = (EditText)  rootView.findViewById(R.id.newItemEditText);

                    addNewEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            writeNewEntry();
                        }
                    });

                    saveMessageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveEntryMessages();
                        }
                    });

                    FirebaseApp.initializeApp(rootView.getContext());
                    database = FirebaseDatabase.getInstance();

                    break;
                }
                case 2:
                {
                    database = FirebaseDatabase.getInstance();
                    rootView = inflater.inflate(R.layout.fragment_entry_list, container, false);
                    entryIdEditText = (EditText)  rootView.findViewById(R.id.entryId);
                    //registerWatcher();
                    break;
                }
                default:{
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                    textView.setText(getString(R.string.section_format, section));
                }
            }

            return rootView;
        }

        private void registerWatcher(){
            postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    Entry entry = dataSnapshot.getValue(Entry.class);
                    entryIdEditText.setText(entry.get_allMessages().get(0));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("MainActivity", "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };
            Entry entry = new Entry();
            //DatabaseReference dbf = database.getReference();
            database.getReference().addValueEventListener(postListener);
        }

        public void writeNewEntry() {
            entry = new Entry();
            //DatabaseReference dbf = database.getReference(mConfig.getUserId());
//            if (dbf == null){
//                database.getReference().setValue(mConfig.getUserId());
//            }
            DatabaseReference dbf = database.getReference(mConfig.getUserId()).child(entry._id);
            dbf.setValue(entry);
//            if (dbf == null){
//                database.getReference(mConfig.getUserId()).setValue(entry._id);
//            }
        }

        public void saveEntryMessages(){
            DatabaseReference dbf = null;
            if (entry == null){
                entry = new Entry();
                dbf = database.getReference(mConfig.getUserId()).child(entry._id);
                if (entry.get_allMessages() != null && !entry.get_allMessages().isEmpty()) {
                    dbf.setValue(entry);
                }
                else{
                    dbf.push();
                }
                dbf = database.getReference(mConfig.getUserId()).child(entry._id);
                if (entry.get_allMessages() != null && !entry.get_allMessages().isEmpty()) {
                    dbf.setValue(entry);
                }
                else{
                    dbf.push();
                }
            }

            if (!newItemEditText.getText().equals("")){
                entry.get_allMessages().add(newItemEditText.getText().toString());
                dbf = database.getReference(mConfig.getUserId()).child(entry._id);
                if (entry.get_allMessages() != null && !entry.get_allMessages().isEmpty()) {
                    dbf.setValue(entry.get_allMessages());
                }

            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }
}

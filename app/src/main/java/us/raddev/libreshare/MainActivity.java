package us.raddev.libreshare;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
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
    public static String ownerId;

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
        private EditText outputEditText;
        private Button deleteItemButton;
        private Button pullEntryButton;
        private Button addMessageButton;

        FirebaseDatabase database;
        ValueEventListener entryListener;
        ValueEventListener listener;
        Entry entry;
        private static String currentValue;
        private static Entry currentEntry;

        EntryAdapter adapter;
        List<Entry> entryList = new ArrayList<>();

        private LinearLayout checkBoxLayout;

        FirebaseDatabase fdb;
        private Button getCurrentEntryButton;

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
            MainActivity.ownerId = mConfig.getUserId();
            switch (section){
                case 1:{
                    rootView = inflater.inflate(R.layout.fragment_first, container, false);
                    currentUserId = (TextView)rootView.findViewById(R.id.currentUserIdTextView);
                    currentUserId.setText(mConfig.getUserId());
                    //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                    //textView.setText(getString(R.string.section_format, section));

                    saveMessageButton = (Button)rootView.findViewById(R.id.saveMessageButton);
                    addNewEntryButton = (Button) rootView.findViewById(R.id.addNewEntryButton);
                    deleteItemButton = (Button)rootView.findViewById(R.id.deleteItemButton);
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
                            //saveEntryMessages();
                            Entry x = new Entry("First One");
                            fdb.getReference().child(mConfig.getUserId()).child(x.get_id()).setValue(x);
                            currentEntry = x;
                            newItemEditText.setText(currentEntry.get_id());
                        }
                    });

                    deleteItemButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            registerWatcherWithValue();
                        }
                    });

                    FirebaseApp.initializeApp(rootView.getContext());
                    database = FirebaseDatabase.getInstance();
                    fdb = FirebaseDatabase.getInstance();
                    currentValue = "garbage";

                    RecyclerView rvEntries = (RecyclerView) rootView.findViewById(R.id.entryRecyclerView);
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(rootView.getContext());
                    rvEntries.setLayoutManager(manager);
                    adapter = new EntryAdapter(entryList);
                    getAllEntries(rvEntries);
                    //setEntryData();
                    break;

                }
                case 2:
                {
                    database = FirebaseDatabase.getInstance();
                    rootView = inflater.inflate(R.layout.fragment_entry_list, container, false);
                    entryIdEditText = (EditText)  rootView.findViewById(R.id.entryId);
                    outputEditText = (EditText)  rootView.findViewById(R.id.outputEditText);
                    getCurrentEntryButton = (Button) rootView.findViewById(R.id.getCurrentEntry);
                    pullEntryButton = (Button) rootView.findViewById(R.id.pullEntry);
                    addMessageButton = (Button)rootView.findViewById(R.id.addMessageButton);
                    checkBoxLayout = (LinearLayout) rootView.findViewById(R.id.check_add_layout);

                    entryIdEditText.setText("test");
                    if (currentValue != null){
                        entryIdEditText.setText(currentValue);
                    }
                    fdb = FirebaseDatabase.getInstance();
                    entryIdEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        public void afterTextChanged(Editable s) {
                            registerWatcherWithValue();
                        }
                    });

                    getCurrentEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("MainActivity", "In getCurrentEntryButton");
                            Log.d("MainActivity", "currentValue - entrybuttonclick : " + currentValue);
                            if (currentValue != null){
                                outputEditText.setText(currentValue);
                                addCheckBoxes(view);
                            }
                        }
                    });

                    pullEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("MainActivity", "In pullEntry");
                            registerWatcherWithValue();
                        }
                    });

                    addMessageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("MainActivity", "In pullEntry");
                            String newMsgTxt = outputEditText.getText().toString();
                            if (currentEntry != null && !newMsgTxt.isEmpty()) {
                                currentEntry.get_allMessages().add(new Message(newMsgTxt));
                                fdb.getReference().child(MainActivity.ownerId).
                                        child(currentEntry.get_id())
                                        .setValue(currentEntry);
                                outputEditText.setText("");
                            }
                        }
                    });

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

        private void getAllEntries(final RecyclerView rvEntries){
            fdb.getReference().child(MainActivity.ownerId)
                .addValueEventListener(new ValueEventListener() {
                    //changed to addValueEventListener from the
                    // .addListenerForSingleValueEvent() since I want the list to update
                    // when the user adds a new one.

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // clear it first so when one is added the list isn't doubled.
                        adapter.allEntries.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Entry localEntry = snapshot.getValue(Entry.class);
                            adapter.allEntries.add(localEntry);
                            Log.d("MainActivity", ".addListenerForSingleValueEvent : " + localEntry.get_id());
                            rvEntries.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        }

        private void addCheckBoxes(View view){
            checkBoxLayout.removeAllViews();
            for (Message m : currentEntry.get_allMessages()) {
                CheckBox checkBox = new CheckBox(view.getContext());
                checkBox.setId(new Random().nextInt());
                checkBox.setText(m.Note);
                checkBox.setChecked(m.isComplete);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("MainActivity", "checkBox clicked!");
                        CheckBox localCheckBox = (CheckBox)view;
                        Log.d("MainActivity", "I am checked : " + localCheckBox.isChecked());
                        String noteText = localCheckBox.getText().toString();
                        for (Message msg : currentEntry.get_allMessages()){
                            if (msg.Note.equals(noteText)){
                                msg.isComplete = localCheckBox.isChecked();
                                fdb.getReference().child(MainActivity.ownerId).
                                        child(currentEntry.get_id())
                                        .setValue(currentEntry);
                                return;
                            }

                        }
                    }
                });
                checkBoxLayout.addView(checkBox);
            }
        }

        private void registerWatcherWithValue(){
            listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("MainActivity", "onDataChange()...");
                    // Get Post object and use the values to update the UI
                    Entry entry = dataSnapshot.getValue(Entry.class);
                    //set latest value -
                    if (currentValue != null && entry.get_allMessages().size() > 0) {
                        currentValue = entry.get_allMessages().get(0).toString();
                        currentEntry = entry;
                    }
                    if (checkBoxLayout != null) {
                        addCheckBoxes(checkBoxLayout.getRootView());
                    }
                    Log.d("MainActivity", "currentValue : " + currentValue);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("MainActivity", "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };
            Log.d("MainActivity", "registerWatcherWithValue...");
            String userId = null;
            String entryId = null;
            if (entryIdEditText != null) {
                Log.d("MainActivity", "entryIdEditText not NULL!");
                String [] allResults = entryIdEditText.getText().toString().split(":");
                MainActivity.ownerId = userId = allResults[0];
                entryId = allResults[1];
                Log.d("MainActivity", userId + "--" + entryId);
                fdb.getReference().child(userId).child(entryId).addValueEventListener(listener);
            }
            else {
                Log.d("MainActivity", "entryIdEditText IS--NULL!");
                entryId = newItemEditText.getText().toString();
                Log.d("MainActivity", entryId);
                fdb.getReference().child(mConfig.getUserId()).child(entryId).addValueEventListener(listener);
                Log.d("MainActivity", "uid : " + mConfig.getUserId().toString() + "entryId : " + entryId);
            }
        }

        public void writeNewEntry() {
            entry = new Entry("test");
            //DatabaseReference dbf = database.getReference(mConfig.getUserId());
//            if (dbf == null){
//                database.getReference().setValue(mConfig.getUserId());
//            }
            DatabaseReference dbf = database.getReference(mConfig.getUserId()).child(entry.get_id());
            dbf.setValue(entry);
//            if (dbf == null){
//                database.getReference(mConfig.getUserId()).setValue(entry._id);
//            }
        }

        public void saveEntryMessages(){
            DatabaseReference dbf = null;
            if (entry == null){
                entry = new Entry("First");
                dbf = database.getReference(mConfig.getUserId()).child(entry.get_id());
                if (entry.get_allMessages() != null && !entry.get_allMessages().isEmpty()) {
                    dbf.setValue(entry);
                }
                else{
                    dbf.push();
                }
                dbf = database.getReference(mConfig.getUserId()).child(entry.get_id());
                if (entry.get_allMessages() != null && !entry.get_allMessages().isEmpty()) {
                    dbf.setValue(entry);
                }
                else{
                    dbf.push();
                }
            }

            if (!newItemEditText.getText().equals("")){
                entry.get_allMessages().add(new Message(newItemEditText.getText().toString()));
                dbf = database.getReference(mConfig.getUserId()).child(entry.get_id());
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

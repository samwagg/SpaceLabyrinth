/*
 * This file is a heavily-modified version of an Android Open Source Project file (NetworkActivity.java)
 * Below is the copyright notice provided with the original file.
 *
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.samwagg.gravity.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.homebrewedapps.unmfinder.UnmBuildingsXmlParser.Entry;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying a clickable menu of UNM locations.
 * Clicking expands selection to display info and give the
 * chance to locate on google maps.
 *
 * Some code borrowed from android network connections tutorial
 */
public class LevelAquisitionActivity extends Activity {

    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String URL =
            "http://datastore.unm.edu/locations/abqbuildings.xml";

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;

    // The user's current network preference setting.
    public static String sPref = null;

    // The BroadcastReceiver that tracks network connectivity changes.
    private NetworkReceiver receiver = new NetworkReceiver();

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        registerReceiver(receiver, filter);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_location_selection, container, false);
//    }

    @Override
    public void onStart() {
        super.onStart();

        // Gets the user's network preference settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        sPref = sharedPrefs.getString("listPref", "Any");

        updateConnectedFlags();

        if (refreshDisplay) {
            loadPage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }

    // Checks the network connection and sets the wifiConnected and mobileConnected
    // variables accordingly.
    private void updateConnectedFlags() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }

   void loadPage() {
        if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected))
                || ((sPref.equals(WIFI)) && (wifiConnected))) {
            // AsyncTask subclass
            DownloadXmlTask task = new DownloadXmlTask();
            task.setActivityContext(getActivity(), this);
            task.execute(URL);
        } else {
            // TODO put this back in when it's defined
            //showErrorPage();
        }
    }


    // Implementation of AsyncTask used to download XML
    // feed from "http://datastore.unm.edu/locations/abqbuildings.xml"
    private class DownloadXmlTask extends AsyncTask<String, Void, List<Entry>> {

        private Context activityContext;
        private LocationSelectionFragment fragment;

        @Override
        protected List<Entry> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                Toast.makeText(activityContext, R.string.connection_error, Toast.LENGTH_SHORT).show();
                return null;
            } catch (XmlPullParserException e) {
                Toast.makeText(activityContext, R.string.xml_error, Toast.LENGTH_SHORT).show();
                return null;

            }
        }

        @Override
        protected void onPostExecute(final List<Entry> entryList) {

            if (entryList != null) {

                final ListView locListView = (ListView) getActivity().findViewById(R.id.loc_listview);
                final CustomArrayAdapter adapter = new CustomArrayAdapter(activityContext, android.R.layout.simple_list_item_1, entryList, fragment);
                locListView.setAdapter(adapter);

                CustomTextWatcher watcher = new CustomTextWatcher(adapter);
                EditText searchView = (EditText) getActivity().findViewById(R.id.search_box);
                searchView.addTextChangedListener(watcher);

                // From http://www.vogella.com/tutorials/AndroidListView/article.html
                locListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {

                        View descView = view.findViewById(R.id.building_desc_view);
                        View buttonView = view.findViewById(R.id.navigate_button);

                        if (descView.getVisibility() == View.GONE) {
                            descView.setVisibility(View.VISIBLE);
                            buttonView.setVisibility(View.VISIBLE);
                            adapter.addExpandedItem(view);
                        } else {
                            descView.setVisibility(View.GONE);
                            buttonView.setVisibility(View.GONE);
                            adapter.removeExpandedItem(view);
                        }
                    }

                });
            }
        }

        private void setActivityContext(Context activityContext, LocationSelectionFragment fragment) {
            this.activityContext = activityContext;
            this.fragment = fragment;
        }


    }

    // Uploads XML from stackoverflow.com, parses it, and combines it with
    // HTML markup. Returns HTML string.
    private List<Entry> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        UnmBuildingsXmlParser unmBuildingsXmlParser = new UnmBuildingsXmlParser();
        List<Entry> entries = null;

        try {
            stream = downloadUrl(urlString);
            entries = unmBuildingsXmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return entries;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new java.net.URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    /**
     * This BroadcastReceiver intercepts the android.net.ConnectivityManager.CONNECTIVITY_ACTION,
     * which indicates a connection change. It checks whether the type is TYPE_WIFI.
     * If it is, it checks whether Wi-Fi is connected and sets the wifiConnected flag in the
     * main activity accordingly.
     */
    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


            if (WIFI.equals(sPref) && networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                refreshDisplay = true;

                Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_SHORT).show();

            } else if (ANY.equals(sPref) && networkInfo != null) {
                refreshDisplay = true;

            } else {
                refreshDisplay = false;
                Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Code from http://stackoverflow.com/questions/8166497/custom-adapter-for-list-view
    public class CustomArrayAdapter extends ArrayAdapter<Entry> implements Filterable {

        private List<View> expandedItems = new ArrayList<View>();
        private LocationSelectionFragment creatingFragment;

        public CustomArrayAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public CustomArrayAdapter(Context context, int resource, List<Entry> items, LocationSelectionFragment creatingFragment) {
            super(context, resource, items);
            this.creatingFragment = creatingFragment;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {

                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                convertView = vi.inflate(R.layout.loc_listview, null);

            }

            final Entry entry = getItem(position);

            if (entry != null) {

                TextView buildingNameView = (TextView) convertView.findViewById(R.id.building_name_view);
                TextView buildingDescView = (TextView) convertView.findViewById(R.id.building_desc_view);
                Button navButton = (Button) convertView.findViewById(R.id.navigate_button);

                if (buildingNameView != null && entry.title != null) {
                    buildingNameView.setText(entry.title);
                }
                if (buildingDescView != null && entry.description != null) {
                    buildingDescView.setText(Html.fromHtml(entry.description));
                }
                System.out.println(entry + " on the outside");

                navButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println(entry + "on the inside");
                        String title = entry.title;
                        String desc  = entry.description;
                        System.out.println(entry.latitude + " " + entry.longitude);
                        LatLng dest = new LatLng(Float.parseFloat(entry.latitude), Float.parseFloat(entry.longitude));
                        creatingFragment.mListener.onNavigateClicked(dest, title, desc);
                    }
                });

            }

            return convertView;

        }

        public void addExpandedItem(View view) {
            this.expandedItems.add(view);
        }

        public void removeExpandedItem(View view) {
            this.expandedItems.remove(view);
        }

        public void collapseAll() {

            for (View view : expandedItems) {
                view.findViewById(R.id.building_desc_view).setVisibility(View.GONE);
                view.findViewById(R.id.navigate_button).setVisibility(View.GONE);
            }
        }


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onNavigateClicked(LatLng coords, String name, String description);
    }


    /**
     * CustomTextWatcher for search bar. Watches bar and filters
     * the adapter accordingly
     */
    private class CustomTextWatcher implements TextWatcher {

        CustomArrayAdapter adapter;

        /**
         * @param adapter the adapter for the data being displayed
         *                in a UI component that needs to be filtered
         *                (e.g. listview)
         */
        public CustomTextWatcher(CustomArrayAdapter adapter) {
            super();
            this.adapter = adapter;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            adapter.collapseAll();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            adapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
package com.rolodex;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.rolodex.adapters.CardAdapter;
import com.rolodex.models.Robot;
import com.rolodex.rolodex.R;
import com.rolodex.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RolodexActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private List<Robot> listRolodex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rolodex);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialization of views
        init();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void init(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RolodexActivity.this, LinearLayoutManager.HORIZONTAL, false));
        listRolodex = new ArrayList<>();
        cardAdapter = new CardAdapter(this, listRolodex);
        recyclerView.setAdapter(cardAdapter);
        // load data from here
        new MyHttpRequestTask().execute(Constants.URL);
    }

    private void populateCardView(){
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rolodex, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyHttpRequestTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {
            String my_url = params[0];
            String response = "";
            try {
                URL url = new URL(my_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                try{

                    httpURLConnection.setDoOutput(true);

                    httpURLConnection.setChunkedStreamingMode(0);

                    InputStream in = new URL(my_url).openStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line).append('\n');
                    }
                    response = total.toString();
                    Log.d("Response",""+response);

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    httpURLConnection.disconnect();
                }


            }catch (Exception e){
                e.printStackTrace();
            }

            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.length() > 0){
                listRolodex.clear();
                try{
                    /**Parsing JSON Object from the result
                     * */
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject robotJObj = null;
                    for(int i=0;i<jsonArray.length();i++){
                        robotJObj = jsonArray.optJSONObject(i);
                        /**Store the data into 'Robot' model
                         * */
                        Robot robot = new Robot();

                        robot.setFirstName(robotJObj.optString(Constants.FIRST_NAME));
                        robot.setLastName(robotJObj.optString(Constants.LAST_NAME));
                        robot.setCompany(robotJObj.optString(Constants.COMPANY_NAME));
                        robot.setEmail(robotJObj.optString(Constants.EMAIL));
                        robot.setBio(robotJObj.optString(Constants.BIO));
                        robot.setStartDate(robotJObj.optString(Constants.START_DATE));
                        robot.setAvatar(robotJObj.optString(Constants.AVATAR));

                        /**add model into list
                         * */
                        listRolodex.add(robot);
                    }
                } catch(JSONException je){
                    je.printStackTrace();
                }

                // Notify Adapter for data changes
               populateCardView();

            }

        }
    }

}

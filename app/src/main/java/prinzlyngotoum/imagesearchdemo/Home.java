package prinzlyngotoum.imagesearchdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import prinzlyngotoum.imagesearchdemo.adapter.CustomAdapter;


public class Home extends Activity {

    /** Called when the activity is first created. */

    private GridView mGridview;
    private EditText mTxtSearchText;

    private CustomAdapter mAdapter;
    private ArrayList<String> mListImagesUrl;
    private Activity mActivity;

    // The image name
    String mStrSearch = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mActivity = this;
        mGridview = (GridView) findViewById(R.id.gViewImages);
        mTxtSearchText = (EditText) findViewById(R.id.txtViewSearch);

        mTxtSearchText.setOnKeyListener(keyListener);

        if(savedInstanceState!=null){
            if(savedInstanceState.getSerializable("listeOfImages")!=null){
                SetGridViewAdapter((ArrayList<String>)savedInstanceState.getSerializable("listeOfImages"));
            }
        }
    }

    //Event listener for the keyboard.
    View.OnKeyListener keyListener=new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                mStrSearch = mTxtSearchText.getText().toString();
                if (!mStrSearch.isEmpty()) {//If the user entered a value
                    mStrSearch = Uri.encode(mStrSearch);

                    // Call the asynctask method to get the images
                    new getImagesTask().execute();

                }
                else{
                    Toast.makeText(getApplicationContext(), "Please enter a value", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mAdapter!=null){
            outState.putSerializable("listeOfImages",mAdapter.getListImages());
        }
    }


    /*
    Get the list of images via an asyncTask.
     */
    public class getImagesTask extends AsyncTask<Void, Void, Void>
    {
        JSONObject json;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            dialog = ProgressDialog.show(Home.this, "", "Searching...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            URL url;
            try {
                url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
                        "v=1.0&q="+mStrSearch+"&rsz=8");

                URLConnection connection = url.openConnection();
                connection.addRequestProperty("Referer", "http://www.ngotoum.com");

                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                json = new JSONObject(builder.toString());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if(dialog.isShowing())
            {
                dialog.dismiss();
            }

            try {
                JSONObject responseObject = json.getJSONObject("responseData");
                JSONArray resultArray = responseObject.getJSONArray("results");

                mListImagesUrl = getImageList(resultArray);
                SetGridViewAdapter(mListImagesUrl);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    /*
    Get list of Image url using a Json Array
     */
    public ArrayList<String> getImageList(JSONArray resultArray)
    {
        ArrayList<String> listImages = new ArrayList<String>();

        try
        {
            for(int i=0; i<resultArray.length(); i++)
            {
                JSONObject obj;
                obj = resultArray.getJSONObject(i);

                listImages.add(obj.getString("tbUrl"));

            }
            return listImages;
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public void SetGridViewAdapter(ArrayList<String> images)
    {
        mAdapter = new CustomAdapter(mActivity, images);
        mGridview.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

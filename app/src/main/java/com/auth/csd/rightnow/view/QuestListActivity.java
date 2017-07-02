package com.auth.csd.rightnow.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.auth.csd.rightnow.MyApplication;
import com.auth.csd.rightnow.model.Quest;
import com.auth.csd.rightnow.R;
import com.auth.csd.rightnow.utils.ConnectionProperties;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestListActivity extends AppCompatActivity {

    private ListView viewQuestList;
    private ProgressBar viewProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_list);

        getSupportActionBar().setTitle("Quest Log");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewQuestList = (ListView) findViewById(R.id.quest_list);
        viewProgressBar = (ProgressBar) findViewById(R.id.quest_list_progress);

        fillListWithContent();
    }

    private void fillListWithContent() {
        StringRequest request = new StringRequest(Request.Method.GET,
                ConnectionProperties.getUserQuestTakenUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
                        QuestListActivity.this.viewProgressBar.setVisibility(View.INVISIBLE);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.getBoolean("error")) {
                                QuestListActivity.this.alert("Error", jsonResponse.getString("response_msg"));
                                return;
                            }


                        } catch (JSONException e) {
                            QuestListActivity.this.alert("Error", "Invalid response from server");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VOLLEY", "ERROR " + error.getMessage());
                QuestListActivity.this.viewProgressBar.setVisibility(View.INVISIBLE);

                QuestListActivity.this.alert("Error", "Server responsed with error");
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postData = new HashMap<String, String>();
                postData.put("sid", "random_string"); // TODO
                return postData;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);

        // dumb content
        ArrayList<Quest> quests = new ArrayList<>();
        for (int i=0; i<20; i++)
            quests.add(Quest.make("Quest name #" + i));

        QuestAdapter adapter = new QuestAdapter(this, quests);
        viewQuestList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    public void alert(String title, String text) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }

    public class QuestAdapter extends ArrayAdapter<Quest> {
        public QuestAdapter(Context context, ArrayList<Quest> quests) {
            super(context, 0, quests);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Quest quest = getItem(position);

            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.quest_item, parent, false);

            ImageView questIcon = (ImageView) convertView.findViewById(R.id.quest_icon);
            TextView questName = (TextView) convertView.findViewById(R.id.quest_name);

            Drawable icon = quest.isCompleted() ? ContextCompat.getDrawable(getApplicationContext(), R.drawable.quest_completed): ContextCompat.getDrawable(getApplicationContext(), R.drawable.quest_failed);

            questIcon.setImageDrawable(icon);
            questName.setText(quest.getName());

            return convertView;
        }

    }
}

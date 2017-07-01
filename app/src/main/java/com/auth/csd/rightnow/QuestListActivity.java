package com.auth.csd.rightnow;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class QuestListActivity extends AppCompatActivity {

    private ListView viewQuestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_list);

        getSupportActionBar().setTitle("Quest Log");

        viewQuestList = (ListView) findViewById(R.id.quest_list);

        ArrayList<Quest> quests = new ArrayList<>();
        for (int i=0; i<20; i++)
            quests.add(Quest.make("Quest name #" + i));

        QuestAdapter adapter = new QuestAdapter(this, quests);
        viewQuestList.setAdapter(adapter);
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

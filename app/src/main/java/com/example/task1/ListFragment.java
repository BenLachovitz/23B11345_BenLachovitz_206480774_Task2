package com.example.task1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.task1.Intarfaces.CallBack_sendClick;
import com.example.task1.Models.Record;
import com.example.task1.Models.RecordList;
import com.example.task1.Utillities.SaveAndReadJson;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private CallBack_sendClick callBack_sendClick;
    private ListView list_FRAGMENT_list;
    private RecordList recordList;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list,container,false);
        findViews(view);
        initViews();
        recordList = SaveAndReadJson.getInstance().getRecordList();
        makeList();
        return view;
    }

    private void sendClicked(Record record)
    {
        if(callBack_sendClick != null)
            callBack_sendClick.userNameChosen(record);
    }
    private void findViews(View view)
    {
        list_FRAGMENT_list = (ListView) view.findViewById(R.id.list_FRAGMENT_list);
    }

    private void initViews()
    {
        list_FRAGMENT_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendClicked(recordList.getRecords().get(position));
            }
        });
    }

    private void makeList() {
        if (recordList != null){
            ArrayList<String> temp = new ArrayList<>();
            for(int i=0;i<recordList.getRecords().size();i++)
            {
                temp.add((i+1)+") Score: "+recordList.getRecords().get(i).getScore()+
                        " Location: "+recordList.getRecords().get(i).getCity()+
                        ", "+recordList.getRecords().get(i).getCountry()+
                        " Mode: "+recordList.getRecords().get(i).getDifficulty()+
                        " Type: "+recordList.getRecords().get(i).getType());
            }
            ArrayAdapter<String> item = new ArrayAdapter<String>(view.getContext(), R.layout.score_det, temp);
            list_FRAGMENT_list.setAdapter(item);
        }
    }

    public void setCallback(CallBack_sendClick callBack_sendClick)
    {
        this.callBack_sendClick = callBack_sendClick;
    }
}
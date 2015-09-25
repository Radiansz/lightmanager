package com.lightsoft.microwave.lightmanager.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.lightsoft.microwave.lightmanager.DBHelper;
import com.lightsoft.microwave.lightmanager.NewPurchase;
import com.lightsoft.microwave.lightmanager.R;
import com.lightsoft.microwave.lightmanager.RuleEditorActivity;
import com.lightsoft.microwave.lightmanager.Type;
import com.lightsoft.microwave.lightmanager.dbworks.TypeReplaceRule;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RuleViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RuleViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RuleViewerFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener, OnTouchListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int DELETE_ELEM = 0;
    private static final int EDIT_ELEM = 1;

    DBHelper dbh;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView lv;
    CursorAdapter adapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RuleViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RuleViewerFragment newInstance(String param1, String param2) {
        RuleViewerFragment fragment = new RuleViewerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    public RuleViewerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbh = new DBHelper(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        String from[];
        String to[];
        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, null, new String[]{TypeReplaceRule.TYPE, TypeReplaceRule.BRAND_RAW},
                new int[]{android.R.id.text1, android.R.id.text2}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER ); // TODO: Заменить элемент списка на более подходящий

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View content = inflater.inflate(R.layout.fragment_rule_viewer, container, false);
        lv = (ListView) content.findViewById(R.id.rulelist);
        registerForContextMenu(lv);
        lv.setAdapter(adapter);
        lv.setOnTouchListener(this);
        refreshList();
        return content;
    }

    private void refreshList(){
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor tempCur = db.query(TypeReplaceRule.TABLE, new String[]{TypeReplaceRule.ID ,TypeReplaceRule.BRAND_RAW, TypeReplaceRule.TYPE}, null, null,null,null, TypeReplaceRule.ID);
        if(tempCur.moveToFirst())
            do{
                Log.i("myinfo", tempCur.getString(tempCur.getColumnIndex(TypeReplaceRule.BRAND_RAW)));
            }while (tempCur.moveToNext());
        if(tempCur != null)
            adapter.swapCursor(tempCur);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo ac = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch(item.getItemId()){
            case EDIT_ELEM:

                break;
            case DELETE_ELEM:
                TypeReplaceRule rule = new TypeReplaceRule();
                rule.setId((int) ac.id);
                rule.deleteMatches(dbh.getWritableDatabase());
                refreshList();
                break;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,EDIT_ELEM,0,R.string.edit);
        menu.add(0, DELETE_ELEM, 0, R.string.delete);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        RuleEditorActivity activ = (RuleEditorActivity)getActivity();
        return activ.getSlide().getGestureDetector().onTouchEvent(event);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

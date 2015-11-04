package com.lightsoft.microwave.lightmanager.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.lightsoft.microwave.lightmanager.DBHelper;
import com.lightsoft.microwave.lightmanager.InputAssister;
import com.lightsoft.microwave.lightmanager.R;
import com.lightsoft.microwave.lightmanager.dbworks.Purchase;
import com.lightsoft.microwave.lightmanager.dbworks.TypeReplaceRule;
import com.lightsoft.microwave.lightmanager.dbworks.TypesProvider;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RuleMakerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RuleMakerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RuleMakerFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView lv;
    private EditText purchaseEdit;
    private EditText rawPlaceEdit;
    private EditText placeEdit;
    private Button createBut;
    private CursorAdapter adapter;
    private DBHelper dbh;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RuleMakerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RuleMakerFragment newInstance(String param1, String param2) {
        RuleMakerFragment fragment = new RuleMakerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RuleMakerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, new String[]{"place","count"},
                new int[]{android.R.id.text1, android.R.id.text2}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbh = new DBHelper(getActivity());
        SQLiteDatabase db = dbh.getWritableDatabase();
        // Inflate the layout for this fragment
        View content = inflater.inflate(R.layout.fragment_rule_maker, container, false);

        purchaseEdit = (EditText) content.findViewById(R.id.purchaseName_edit);
        TypesProvider tp = new TypesProvider(dbh, "purchasetypes");
        purchaseEdit.addTextChangedListener(new InputAssister(purchaseEdit,  tp.gainTypes("purchasetypes")));
        rawPlaceEdit = (EditText) content.findViewById(R.id.placeName_edit);
        placeEdit = (EditText) content.findViewById(R.id.place2Name_edit);
        createBut = (Button) content.findViewById(R.id.create_button);
        createBut.setOnClickListener(this);
        lv = (ListView) content.findViewById(R.id.listView);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        //Extracting data
        updateList();
        return content;
    }

    private void updateList(){
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor c = db.query("purchases", new String[]{"_id", "place", "place as count"}, "purchasename = 'Card parsed'", null, "place", null, null);
        Log.i("myinfo", "RuleMaker: number of strings " + c.getCount());
        if(c!= null)
            adapter.swapCursor(c);
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
        TextView tv = (TextView)view.findViewById(android.R.id.text1);
        String data = tv.getText().toString();
        rawPlaceEdit.setText(data);

    }

    @Override
    public void onClick(View v) {
        TypesProvider tp = new TypesProvider(dbh, "purchasetypes");
        SQLiteDatabase db = dbh.getWritableDatabase();
        TypeReplaceRule rule = new TypeReplaceRule();
        rule.setType(purchaseEdit.getText().toString());
        rule.setBrandRaw(rawPlaceEdit.getText().toString());
        if(!placeEdit.getText().toString().equals(""))
            rule.setBrand(placeEdit.getText().toString());
        if(rule.insert(db) != -1){
            Purchase pchase = new Purchase();
            pchase.setPurchasename(rule.getType());
            pchase.setPlace(rule.getBrand());
            Purchase pattern = new Purchase();
            pattern.setPlace(rule.getBrandRaw());
            long res = pchase.updateMatches(db, pattern);
            Log.i("myinfo", String.valueOf(res));
            if(res>0)
                tp.incrType(rule.getType(),(int)res);


        }
        updateList();
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

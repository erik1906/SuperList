package com.company.erde.superlist.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.company.erde.superlist.Activities.CreateUpdateProductActivity;
import com.company.erde.superlist.Activities.MainActivity;
import com.company.erde.superlist.Activities.ProductDetailsActivity;
import com.company.erde.superlist.Adapters.ProductRecyclerViewAdapter;
import com.company.erde.superlist.R;
import com.company.erde.superlist.Realm.ProductCRUD;
import com.company.erde.superlist.RealModels.Product;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductFragments.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductFragments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragments extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private  Realm realm;
    private ProductRecyclerViewAdapter adapter;



    private OnFragmentInteractionListener mListener;

    public ProductFragments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProductFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragments newInstance(String name) {
        ProductFragments fragment = new ProductFragments();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;

        try {
            position = adapter.getPosition();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        Product product = (Product) adapter.getData().get(position);
        switch (item.getItemId()) {
            case 0:
                realm = Realm.getDefaultInstance();
                ProductCRUD.delete(realm, product.getId());
                //Toast.makeText(getContext(),position+" Delete",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Intent i = new Intent(getContext(), CreateUpdateProductActivity.class);
                i.putExtra("id", product.getId());
                startActivity(i);
                //Toast.makeText(getContext(), "id:"+product.getId() ,Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        realm = Realm.getDefaultInstance();

        recyclerView = view.findViewById(R.id.rvProduct);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        registerForContextMenu(recyclerView);
        final OrderedRealmCollection productData = ProductCRUD.orderedRealmCollection(realm);
        adapter= new ProductRecyclerViewAdapter(productData, true, new ProductRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                adapter.setSelect(false);
                Product p = (Product) productData.get(position);
                Intent i = new Intent(getContext(), ProductDetailsActivity.class);
                i.putExtra("id", p.getId());
                startActivity(i);
                //Toast.makeText(getContext(),"Seleccion "+ position,Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);


        // Inflate the layout for this fragment

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


}

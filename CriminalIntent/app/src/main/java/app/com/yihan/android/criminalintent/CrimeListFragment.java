package app.com.yihan.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import app.com.yihan.android.criminalintent.model.Crime;
import app.com.yihan.android.criminalintent.modelHelper.CrimeLab;

/**
 * Created by HanYi on 9/22/15.
 *
 * ToDo: Could implement the "Getting Results with Fragments" in Chapter 10 to update only one row.
 * ToDo: Try use notifyItemChanged(int) instead of notifyDataSetChanged()
 *
 */
public class CrimeListFragment extends Fragment {

    private RecyclerView mRecyclerViewCrime;
    private CrimeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mRecyclerViewCrime = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        // RecyclerView requires a LayoutManager to work.
        // If you forget to give it one, it will crash.
        mRecyclerViewCrime.setLayoutManager(new LinearLayoutManager(getActivity()));

        // connect Adapter to RecyclerView
        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mRecyclerViewCrime.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This ViewHolder maintains a reference to a single view: the title TextView.
     * This code expects for the itemView to be a TextView,
     * and will crash if it is not.
     */
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private CheckBox mCheckBoxSolved;

        private Crime mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mTextViewDate = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mCheckBoxSolved = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
            itemView.setOnClickListener(this);

        }

        /**
         * Helper method to bind View with the Model
         *
         * @param crime
         */
        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTextViewTitle.setText(mCrime.getTitle());
            mTextViewDate.setText(mCrime.getFormattedDate());
            mCheckBoxSolved.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }

    /**
     * ViewHolder needs to be created or connected with a Crime object.
     * The RecyclerView itself will not know anything about hte Crime object,
     * but the Adapter will know all of Crime's intimate and personal details.
     */
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        /**
         * onCreateViewHolder is called by the RecyclerView when it needs a new View to display an item.
         * in this method, you create the View and wrap it in a ViewHolder.
         * The RecyclerView does not expect that you will hook it up to any data yet.
         *
         * @param viewGroup
         * @param i
         * @return
         */
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, viewGroup, false);
            return new CrimeHolder(view);
        }

        /**
         * This method will bind a ViewHolder's View to your model object.
         * It receives the ViewHolder and a position in your data set.
         * To bind your View, you use that position to find the right model data.
         * Then you update the View to reflect that model data.
         *
         * @param crimeHolder
         * @param i
         */
        @Override
        public void onBindViewHolder(CrimeHolder crimeHolder, int i) {
            Crime crime = mCrimes.get(i);
            crimeHolder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

}

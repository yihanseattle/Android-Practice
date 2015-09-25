package app.com.yihan.android.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

import app.com.yihan.android.criminalintent.model.Crime;
import app.com.yihan.android.criminalintent.modelHelper.CrimeLab;
import app.com.yihan.android.criminalintent.others.Constants;


/**
 * Created by HanYi on 9/22/15.
 */
public class CrimeFragment extends Fragment {

    private Crime mCrime;

    private EditText mEditTextTitleField;
    private Button mButtonDate;
    private CheckBox mCheckBoxSolved;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimId = (UUID) getArguments().getSerializable(Constants.ARG_CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mEditTextTitleField = (EditText) v.findViewById(R.id.etCrimeTitle);
        mEditTextTitleField.setText(mCrime.getTitle());
        mEditTextTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This space intentionally left blank
            }
        });

        mButtonDate = (Button) v.findViewById(R.id.bCrimeDate);
        mButtonDate.setText(mCrime.getFormattedDate());
        mButtonDate.setEnabled(false);

        mCheckBoxSolved = (CheckBox) v.findViewById(R.id.cbCrimeSolved);
        mCheckBoxSolved.setChecked(mCrime.isSolved());
        mCheckBoxSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the crime's solved property
                mCrime.setSolved(isChecked);
            }
        });


        return v;
    }
}

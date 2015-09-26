package app.com.yihan.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

import app.com.yihan.android.criminalintent.model.Crime;
import app.com.yihan.android.criminalintent.modelHelper.CrimeLab;
import app.com.yihan.android.criminalintent.others.Constants;
import app.com.yihan.android.criminalintent.others.DatePickerFragment;


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
        updateDate();
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, Constants.REQUEST_DATE);
                dialog.show(manager, Constants.DIALOG_DATE);
            }
        });

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

    /**
     *
     * Returning data from DatePickerFragment
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == Constants.REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(Constants.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mButtonDate.setText(mCrime.getFormattedDate());
    }
}

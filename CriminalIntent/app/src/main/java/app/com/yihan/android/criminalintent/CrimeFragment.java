package app.com.yihan.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import app.com.yihan.android.criminalintent.model.Crime;
import app.com.yihan.android.criminalintent.modelHelper.CrimeLab;
import app.com.yihan.android.criminalintent.others.Constants;
import app.com.yihan.android.criminalintent.others.DatePickerFragment;
import app.com.yihan.android.criminalintent.others.PictureUtils;


/**
 * Created by HanYi on 9/22/15.
 */
public class CrimeFragment extends Fragment {

    private Crime mCrime;

    private EditText mEditTextTitleField;
    private Button mButtonDate;
    private CheckBox mCheckBoxSolved;
    private Button mButtonReport;
    private Button mButtonSuspect;
    private ImageButton mButtonPhoto;
    private ImageView mViewPhoto;
    private File mPhotoFile;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        // 下面等他被start以后，那到的东西通过这个serializable存在了bundle里
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
        mPhotoFile = CrimeLab.getInstance(getActivity()).getPhotoFile(mCrime);
    }

    @Override
    public void onPause() {
        super.onPause();

        // save crime when user exit this fragment
        CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
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

        mButtonReport = (Button) v.findViewById(R.id.crime_report);
        mButtonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                // 强行显示chooser for implicit intent
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mButtonSuspect = (Button) v.findViewById(R.id.crime_suspect);
        mButtonSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, Constants.REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mButtonSuspect.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mButtonSuspect.setEnabled(false);
        }

        // take photo
        mButtonPhoto = (ImageButton) v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mButtonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, Constants.REQUEST_PHOTO);
            }
        });

        mViewPhoto = (ImageView) v.findViewById(R.id.crime_photo);
        updatePhotoView();

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
        } else if (requestCode == Constants.REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            // Specify which fields you want your query to return values for.
            String[] queryFields = new String[] {ContactsContract.Contacts.DISPLAY_NAME};
            // Perform your query - the contactUri is like a "where" clause here
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            try {
                // Double-check that you actually got results

                if (c.getCount() == 0) {
                    return;
                }

                // Pull out the first column of the first row of data -
                // that is your suspect's name.
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mButtonSuspect.setText(suspect);
            } finally {
                c.close();
            }

        } else if (requestCode == Constants.REQUEST_PHOTO) {
            updatePhotoView();
        }
    }

    private void updateDate() {
        mButtonDate.setText(mCrime.getFormattedDate());
    }

    private String getCrimeReport() {

        String solvedString = null;

        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mViewPhoto.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mViewPhoto.setImageBitmap(bitmap);
        }
    }

}

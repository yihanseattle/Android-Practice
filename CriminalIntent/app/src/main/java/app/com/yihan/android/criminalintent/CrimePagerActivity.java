package app.com.yihan.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

import app.com.yihan.android.criminalintent.model.Crime;
import app.com.yihan.android.criminalintent.modelHelper.CrimeLab;
import app.com.yihan.android.criminalintent.others.Constants;

/**
 * Created by HanYi on 9/24/15.
 */
public class CrimePagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(Constants.EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes = CrimeLab.getInstance(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();

        // get UUID from recyclerView
        UUID crimeId = (UUID) getIntent().getSerializableExtra(Constants.EXTRA_CRIME_ID);

        /**
         *
         * Remember that FragmentStatePagerAdapter is your agent managing the conversation with ViewPager.
         * For you agent to do its job with the fragments that getItem(int) returns,
         * it needs to be able to add them to your activity.
         * That is why it needs your FragmentManager.
         *
         **/
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            /**
             *
             * This is where the magic happens.
             * It fetches the Crime instance for the given position in the data set.
             * It then uses that Crime's ID to create and return a properly configured CrimeFragment.
             *
             * @param position
             * @return
             */
            @Override
            public Fragment getItem(int position) {

                // Get your data set from CrimeLab - the List of crimes.
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            /**
             * Returns the number of items in the array list.
             * @return
             */
            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        // By default, the ViewPager show the first item in its PagerAdapter.
        // locate the crime and let the ViewPager display that crime
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }



}

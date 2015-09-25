package app.com.yihan.android.criminalintent;

import android.support.v4.app.Fragment;

import app.com.yihan.android.criminalintent.others.SingleFragmentActivity;

/**
 * Created by HanYi on 9/22/15.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}

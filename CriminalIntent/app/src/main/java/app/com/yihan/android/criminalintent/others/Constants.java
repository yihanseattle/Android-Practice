package app.com.yihan.android.criminalintent.others;

/**
 * Created by HanYi on 9/23/15.
 */
public class Constants {

    // Constants for CrimeListFragment to start a CrimeActivty(contains a CrimeFragment)
    // This is also for CrimePagerActivity
    public static final String EXTRA_CRIME_ID = "app.com.yihan.android.criminalIntent.crime_id";
    public static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    // Constants for CrimeFragment
    public static final String ARG_CRIME_ID = "crime_id";
    public static final String DIALOG_DATE = "DialogDate";
    public static final int REQUEST_DATE = 0;
    public static final int REQUEST_CONTACT = 1;
    public static final int REQUEST_PHOTO = 2;

    // Constants for DatePickerFragment
    public static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "app.com.yihan.android.criminalIntent.date";

}

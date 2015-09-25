package app.com.yihan.android.criminalintent.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by HanYi on 9/21/15.
 */
public class Crime {

    private UUID mId;
    private String mTitle;


    private Date mDate;
    private boolean mSolved;

    public Crime() {
        // Generate unique idnetifier
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getFormattedDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); //08/06/2014 15:59:48
        return dateFormat.format(this.getDate());
    }
}

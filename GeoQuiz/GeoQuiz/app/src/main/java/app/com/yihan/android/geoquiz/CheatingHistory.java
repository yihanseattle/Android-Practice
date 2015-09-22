package app.com.yihan.android.geoquiz;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HanYi on 9/21/15.
 */
public class CheatingHistory implements Parcelable{

    private boolean[] mCheatingHistory;

    public CheatingHistory(int numberOfQuestions) {
        mCheatingHistory = new boolean[numberOfQuestions];
    }

    protected CheatingHistory(Parcel in) {
        mCheatingHistory = in.createBooleanArray();
    }

    public static final Creator<CheatingHistory> CREATOR = new Creator<CheatingHistory>() {
        @Override
        public CheatingHistory createFromParcel(Parcel in) {
            return new CheatingHistory(in);
        }

        @Override
        public CheatingHistory[] newArray(int size) {
            return new CheatingHistory[size];
        }
    };

    public boolean[] getCheatingHistory() {
        return mCheatingHistory;
    }

    public void setCheatingHistory(boolean[] cheatingHistory) {
        mCheatingHistory = cheatingHistory;
    }

    public void setCheatAt(int index) {
        mCheatingHistory[index] = true;
    }

    public boolean isCheatAt(int index) {
        return mCheatingHistory[index];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBooleanArray(mCheatingHistory);
    }
}

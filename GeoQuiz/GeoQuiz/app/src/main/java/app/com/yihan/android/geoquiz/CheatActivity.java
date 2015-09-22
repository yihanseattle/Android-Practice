package app.com.yihan.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    // answer
    private boolean mAnswer;

    // Views
    private TextView mTextViewAnswer;
    private Button mButtonShowAnswer;

    // boolean records that if the user has cheated or not
    private boolean mHasUserCheated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        // Retrieve Intent Extra
        mAnswer = getIntent().getBooleanExtra(Constants.EXTRA_ANSWER_IS_TRUE, false);

        mTextViewAnswer = (TextView) findViewById(R.id.tvAnswer);

        mButtonShowAnswer = (Button) findViewById(R.id.bShowAnswer);
        mButtonShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // give the user the answer
                setAnswerToTextView(mAnswer);

                // record this action
                mHasUserCheated = true;

                // send the result back to parent activity (QuizActivty)
                setAnswerShownResult(true);
            }
        });

        // get the user action history after rotation
        if (savedInstanceState != null) {
            mHasUserCheated = savedInstanceState.getBoolean(Constants.HAS_USER_CHEATED, false);
            setAnswerShownResult(true);
            if (mHasUserCheated) {
                mAnswer = savedInstanceState.getBoolean(Constants.ANSWER, false);
                setAnswerToTextView(mAnswer);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.ANSWER, mAnswer);
        outState.putBoolean(Constants.HAS_USER_CHEATED, mHasUserCheated);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cheat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Return an Intent reference to start this CheatActivty.
     *
     * @param packageContext
     * @param answerIsTrue
     * @return
     */
    public static Intent newIntet(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(Constants.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    /**
     * If the user chose to see the answer, we send this action back to parent activty.
     *
     * @param isAnswerShown
     */
    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(Constants.EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    /**
     * Will return false if the user didn't click the "show answer" button
     *
     * @param result
     * @return true: when user has clicked the "show answer" button
     * false: when user didn't click the "show answer" button
     */
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(Constants.EXTRA_ANSWER_SHOWN, false);
    }

    /**
     * Set the answer to the TextView.
     *
     * @param answer
     */
    private void setAnswerToTextView(boolean answer) {
        int answerId;
        if (answer) {
            answerId = R.string.true_button;
        } else {
            answerId = R.string.false_button;
        }
        mTextViewAnswer.setText(answerId);
    }

}

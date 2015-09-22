package app.com.yihan.android.geoquiz;

import android.media.Image;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private TextView mTextViewQuestion;
    private Button mButtonTrue;
    private Button mButtonFlase;
    private ImageButton mImageButtonPrev;
    private ImageButton mImageButtonNext;

    // store questions
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // retrieve saved data
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(Constants.KEY_INDEX, 0);
        }

        // Initialize question TextView
        mTextViewQuestion = (TextView) findViewById(R.id.tvQuestion);
        mTextViewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTheNextQuestion();
            }
        });
        updateQuestion();

        // Setup buttons and listeners
        mButtonFlase = (Button) findViewById(R.id.bFalse);
        mButtonFlase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mButtonTrue = (Button) findViewById(R.id.bTrue);
        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mImageButtonPrev = (ImageButton) findViewById(R.id.bPrev);
        mImageButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the previous question
                goToThePreviousQuestion();
            }
        });

        mImageButtonNext = (ImageButton) findViewById(R.id.bNext);
        mImageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the next Question
                goToTheNextQuestion();
            }
        });

    }

    /**
     * Make sure that the question index is saved when screen rotation happens.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.KEY_INDEX, mCurrentIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
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
     * Update the Question TextView based on the updated mCurrentIndex.
     */
    private void updateQuestion() {
        int questionId = mQuestionBank[mCurrentIndex].getTextResId();
        mTextViewQuestion.setText(questionId);
    }

    /**
     * Check for correct result based on User input.
     *
     * @param userInputAnswer : User Input
     */
    private void checkAnswer(boolean userInputAnswer) {

        boolean correctAnswer = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int outputMessageId = 0;
        if (userInputAnswer == correctAnswer) {
            outputMessageId = R.string.toast_correct;
        } else {
            outputMessageId = R.string.toast_incorrect;
        }

        Toast.makeText(getApplicationContext(), outputMessageId, Toast.LENGTH_SHORT).show();
    }

    /**
     * Go to the previous question helper method.
     */
    private void goToThePreviousQuestion() {
        mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
        updateQuestion();
    }

    /**
     * Go to the next question helper method.
     */
    private void goToTheNextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        updateQuestion();
    }
}

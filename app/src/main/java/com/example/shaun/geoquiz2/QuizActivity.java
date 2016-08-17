/**
 * Author: Shaun Alberts 05-August-2016
 */

package com.example.shaun.geoquiz2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    //create button objects
    private Button mTrueButton;
    private Button mfalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    // cheat button
    private Button mCheatButton;
    //hear back from CheatActivity aftet starting it
    private static final int REQUEST_CODE_CHEAT = 0;
    private boolean mIsCheater;


    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;

    private void updateQuestion() {
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.judgement_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //link button objects to widgets
        mTrueButton = (Button) findViewById(R.id.true_button);
        mfalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = (Button) findViewById(R.id.next_button);
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        //cheat button
        mCheatButton = (Button) findViewById(R.id.cheat_button);


        //set text of TextView
//        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
        this.updateQuestion();

        //give buttons listeners
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                boolean questionAnswer = mQuestionBank[mCurrentIndex].isAnswerTrue();
//                if (questionAnswer) {
//                    Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
//                }
                checkAnswer(true);
            }
        });

        mfalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                boolean questionAnswer = mQuestionBank[mCurrentIndex].isAnswerTrue();
//                if (!questionAnswer) {
//                    Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
//                }
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
//                mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
                mIsCheater = false;
                updateQuestion();
            }
        });

        //Cheat Button
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                //startActivity(i);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

}

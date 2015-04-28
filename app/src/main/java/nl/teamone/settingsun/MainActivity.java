package nl.teamone.settingsun;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import nl.teamone.settingsun.game.GameBoardView;
import nl.teamone.settingsun.utils.ScoreListener;


public class MainActivity extends AppCompatActivity implements ScoreListener {

    TextView mScoreText, mHighScoreText;
    GameBoardView mGameBoardView;
    SharedPreferences mPrefs;
    int mCurrentHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = getSharedPreferences("TeamOne.SettingSunApp", Context.MODE_PRIVATE);
        mCurrentHighScore = mPrefs.getInt("highScore", 0);

        mScoreText = (TextView) findViewById(R.id.textScore);
        mHighScoreText = (TextView) findViewById(R.id.textHighScore);

        mHighScoreText.setText(Integer.toString(mCurrentHighScore));

        mGameBoardView = (GameBoardView) findViewById(R.id.GameBoardView);
        mGameBoardView.addListener(this);

    }

    public void updateHighScore (int newScore) {
        if (mCurrentHighScore > newScore || mCurrentHighScore == 0) {
            SharedPreferences.Editor e = mPrefs.edit();
            e.putInt("highScore", newScore);
            e.commit();
            mCurrentHighScore = newScore;
            mHighScoreText.setText(Integer.toString(newScore));
        }
    }

    public void updateMoves (int score) {
        mScoreText.setText(Integer.toString(score));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_back:
                mScoreText.setText(Integer.toString(mGameBoardView.undoMove()));
                return true;
            case R.id.action_reset:
                mScoreText.setText(Integer.toString(mGameBoardView.resetBoard()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

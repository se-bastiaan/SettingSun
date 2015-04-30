package nl.teamone.settingsun;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import nl.teamone.settingsun.game.BoardListener;
import nl.teamone.settingsun.game.GameBoardView;
import nl.teamone.settingsun.utils.PrefUtils;

/**
 * Main and only activity in the app
 */
public class MainActivity extends AppCompatActivity implements BoardListener {

    private TextView mScoreText, mHighScoreText;
    private GameBoardView mGameBoardView;
    private int mCurrentHighScore;
    boolean mIsFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrentHighScore = PrefUtils.get(this, "highscore", 0);
        mIsFirstRun = PrefUtils.get(this, "isfirstrun", true);

        if (mIsFirstRun) {
            AlertDialog greeting = new AlertDialog.Builder(MainActivity.this).create();
            greeting.setTitle(getString(R.string.greeting_title));
            greeting.setMessage(getString(R.string.greeting_text));
            greeting.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.greeting_dismiss), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            greeting.show();

            mIsFirstRun = false;
            PrefUtils.save(this, "isfirstrun", mIsFirstRun);
        }

        mScoreText = (TextView) findViewById(R.id.textScore);
        mHighScoreText = (TextView) findViewById(R.id.textHighScore);

        if (mCurrentHighScore > 0) {
            mHighScoreText.setText(Integer.toString(mCurrentHighScore));
        } else {
            mHighScoreText.setText("-"); // No High Score yet!
        }

        mGameBoardView = (GameBoardView) findViewById(R.id.GameBoardView);
        mGameBoardView.addListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGameBoardView.removeListener(this);
    }

    /**
     * Call made by board when the game goal is reached
     * @param newScore End score
     */
    public void gameFinished(int newScore) {
        if (mCurrentHighScore > newScore || mCurrentHighScore == 0) {
            PrefUtils.save(this, "highscore", newScore);
            mCurrentHighScore = newScore;
            mHighScoreText.setText(Integer.toString(newScore));
        }

        new AlertDialog.Builder(this)
            .setTitle(R.string.game_finished_title)
            .setMessage(String.format(getString(R.string.game_finished), newScore))
            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            })
            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mGameBoardView.resetBoard();
                }
            })
            .create().show();
    }

    /**
     * Move was made on the board
     * @param score New score
     */
    public void didMove(int score) {
        mScoreText.setText(Integer.toString(score));
    }

    /**
     * Move was undone on the board
     * @param score New score
     */
    public void undidMove(int score) {
        mScoreText.setText(Integer.toString(score));
    }

    @Override
    public void boardReset() {
        mScoreText.setText("0");
    }

    /**
     * Inflate actionbar menu
     * @param menu {@link android.view.Menu}
     * @return Always {@code true}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Process selections in the ActionBar menu
     * @param item {@link android.view.MenuItem}
     * @return {@code true} when processed, unknown when not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_back:
                mGameBoardView.undoMove();
                return true;
            case R.id.action_reset:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.sure_title)
                        .setMessage(R.string.sure)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mGameBoardView.resetBoard();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

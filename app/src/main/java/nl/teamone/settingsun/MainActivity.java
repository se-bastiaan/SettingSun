package nl.teamone.settingsun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import nl.teamone.settingsun.game.GameBoardView;


public class MainActivity extends AppCompatActivity {

    TextView mScoreText, mHighScoreText;
    GameBoardView mGameBoardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScoreText = (TextView) findViewById(R.id.textScore);
        mHighScoreText = (TextView) findViewById(R.id.textHighScore);
        mGameBoardView = (GameBoardView) findViewById(R.id.GameBoardView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_back)
            mScoreText.setText(Integer.toString(mGameBoardView.undoMove()));

        if (id == R.id.action_reset)
            mScoreText.setText(Integer.toString(mGameBoardView.resetBoard()));

        return super.onOptionsItemSelected(item);
    }

}

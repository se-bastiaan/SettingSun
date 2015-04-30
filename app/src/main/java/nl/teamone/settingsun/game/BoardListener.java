package nl.teamone.settingsun.game;

public interface BoardListener {
    public void gameFinished(int i);
    public void didMove(int i);
    public void undidMove(int i);
    public void boardReset();
}

package SharedSystem;

public interface IGMListener {
    public void updateResult(int result, String name1, String name2);
    public void updatePlayerTurn(String name);
    public void updateInvalidMove(String name);
    public void updateLostConnection();
}

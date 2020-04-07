public class Player {
    // CONSTANTS
    public final int RIGHT = 0;
    public final int UP = 1;
    public final int LEFT = 2;
    public final int DOWN = 3;

    private int x, y;
    private int[] items = new int[16];
    private int gender;
    private int direction = DOWN;

    public Player(int x, int y, int gender) {
        this.x = x;
        this.y = y;
        this.gender = gender;
    }
}

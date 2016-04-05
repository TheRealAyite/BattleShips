/**
 * Created by mbp on 2/28/16.
 */
public class Tile {

    public int tile_row;
    public int tile_columns;
    public int ship;

    public boolean hasShip;
    public boolean hasMine;
    public boolean notHit;
    public boolean mineHit;

    public Tile()
    {
        hasShip = false;
        hasMine = false;
        notHit = true;
        mineHit = false;
    }
    public boolean hasMine()
    {
        return hasMine;
    }

}

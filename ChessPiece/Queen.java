// Class file for Queen chess pieces.
package ChessPiece;

import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class Queen extends PieceAbstract
{
    public BufferedImage getImage()
        throws IOException
    {
        BufferedImage image;
        image = ImageIO.read(new File("ChessPiece/images/"+player+"Queen.png"));
        return image;
    }
    public void move(int x, int y)
        throws InvalidMoveException
    {
        //will use InvalidMoveException to prevent illegal moves in the future
        this.x = x;
        this.y = y;
    }
}
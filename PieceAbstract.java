//abstract class file providing default method implementations.
import java.io.*;
import java.awt.image.*;
public abstract class PieceAbstract
{
    //variables common to all classes extending PieceAbstract
    int x, y;
    int moveType = HistoryPanel.NORMAL;
    char player;
    boolean captured;

    //counters for captured pieces
    private static int wPawnCap = 0, wRookCap = 0, wKnightCap = 0, wBishopCap = 0, wQueenCap = 0;
    private static int bPawnCap = 0, bRookCap = 0, bKnightCap = 0, bBishopCap = 0, bQueenCap = 0;

    //reference to the MainFrame
    MainFrame mainFrame;
    
    //constructor taking an initial position and player value
    public PieceAbstract(int x, int y, char player, MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
        this.player = player;
        this.x = x;
        this.y = y;
        captured = false;
    }
    //used to retrieve count of captured pieces
    public static int getCapturedCount(int pieceType)
    {
        //all white piece types
        if(pieceType == MainFrame.W_PAWN_MIN)
            return wPawnCap;
        else if(pieceType == MainFrame.W_ROOK1)
            return wRookCap;
        else if(pieceType == MainFrame.W_KNIGHT1)
            return wKnightCap;
        else if(pieceType == MainFrame.W_BISHOP1)
            return wBishopCap;
        else if(pieceType == MainFrame.W_QUEEN)
            return wQueenCap;
        
        //all black piece types
        if(pieceType == MainFrame.B_PAWN_MIN)
            return bPawnCap;
        else if(pieceType == MainFrame.B_ROOK1)
            return bRookCap;
        else if(pieceType == MainFrame.B_KNIGHT1)
            return bKnightCap;
        else if(pieceType == MainFrame.B_BISHOP1)
            return bBishopCap;
        else if(pieceType == MainFrame.B_QUEEN)
            return bQueenCap;

        //if not valid request
        else
            return -1;
    }
    //used to tell if the piece has been captured
    public boolean isCaptured()
    {
        return captured;
    }
    //used to set this piece as captured and update captured counter
    public void setCaptured(boolean captured)
    {
        this.captured = captured;

        //increment appropriate counter
        if(this instanceof Pawn)
        {
            if(player == 'W')
                wPawnCap++;
            else
                bPawnCap++;
        }
        else if(this instanceof Rook)
        {
            if(player == 'W')
                wRookCap++;
            else
                bRookCap++;
        }
        else if(this instanceof Knight)
        {
            if(player == 'W')
                wKnightCap++;
            else
                bKnightCap++;
        }
        else if(this instanceof Bishop)
        {
            if(player == 'W')
                wBishopCap++;
            else
                bBishopCap++;
        }
        else if(this instanceof Queen)
        {
            if(player == 'W')
                wQueenCap++;
            else
                bQueenCap++;
        }
    }
    //used to capture a piece
    public void capturePiece(int occupyingPiece)
    {
        //sets the captured piece offscreen and sets it as captured
        mainFrame.pieces[occupyingPiece].setX(-100);
        mainFrame.pieces[occupyingPiece].setY(-100);
        mainFrame.pieces[occupyingPiece].setCaptured(true);
    }
    //used to tell who owns a piece
    public char getPlayer()
    {
        return player;
    }
    //used to get the image associated with this piece
    public abstract BufferedImage getImage()
        throws IOException;

    //used to get the x position of this piece
    public int getX()
    {
        return x;
    }
    //used to get the y position of this piece
    public int getY()
    {
        return y;
    }
    //used to set the x position of this piece
    public void setX(int x)
    {
        this.x = x;
    }
    //used to set the y position of this piece
    public void setY(int y)
    {
        this.y = y;
    }
    //used to move this piece according to it's movement rules
    public abstract void move(int x, int y, boolean check)
        throws InvalidMoveException;

    //used to return this piece to it's starting position if this player's king is in check
    protected void kingCheckLogic(int oldX, int oldY)
        throws InvalidMoveException
    {
        //check if the player's King is in check
        if(player == 'W' && ((King)mainFrame.pieces[MainFrame.W_KING]).check())
        {
            //return piece to starting position if King is in check
            this.x = oldX;
            this.y = oldY;
            throw new InvalidMoveException("The white king was in check after the move.");
        }
        else if(player == 'B' && ((King)mainFrame.pieces[MainFrame.B_KING]).check())
        {
            //return piece to starting position if King is in check
            this.x = oldX;
            this.y = oldY;
            throw new InvalidMoveException("The black king was in check after the move.");
        }
    }
    //used to move the Bishop, located here so that Queens can also use it
    public void bishopMove(int x, int y, boolean performingCheck)
        throws InvalidMoveException
    {
        boolean notBetween = true;
        int occupyingPiece = -1, oldX = this.x, oldY = this.y;
        //if in the diagonal 
        if(Math.abs(x - this.x) == Math.abs(y - this.y))
        {
            //iterating through each piece
            for (int i = 0; i < mainFrame.pieces.length; i++)
            {
                //if moving up and to the right 
                if(x - this.x > 0 && y - this.y < 0)
                {
                    if ((Math.abs(mainFrame.pieces[i].getX() - this.x) == Math.abs(mainFrame.pieces[i].getY() - this.y))&&
                        (mainFrame.pieces[i].getX() < x && mainFrame.pieces[i].getX() > this.x) &&
                        (mainFrame.pieces[i].getY() > y && mainFrame.pieces[i].getY() < this.y))
                    {
                        notBetween = false;
                    }
                }
                //if moving down and to the right
                else if(x - this.x > 0 && y - this.y > 0)
                {
                    if ((Math.abs(mainFrame.pieces[i].getX() - this.x) == Math.abs(mainFrame.pieces[i].getY() - this.y))&&
                        (mainFrame.pieces[i].getX() < x && mainFrame.pieces[i].getX() > this.x) &&
                        (mainFrame.pieces[i].getY() < y && mainFrame.pieces[i].getY() > this.y))
                    {
                        notBetween = false;
                    }
                }
                //if moving up and to the left
                else if(x - this.x < 0 && y - this.y < 0)
                {
                    if ((Math.abs(mainFrame.pieces[i].getX() - this.x) == Math.abs(mainFrame.pieces[i].getY() - this.y))&&
                        (mainFrame.pieces[i].getX() > x && mainFrame.pieces[i].getX() < this.x) &&
                        (mainFrame.pieces[i].getY() > y && mainFrame.pieces[i].getY() < this.y))
                    {
                        notBetween = false;
                    }
                }
                //if moving down and to the left
                else if(x - this.x < 0 && y - this.y > 0)
                {  
                    if ((Math.abs(mainFrame.pieces[i].getX() - this.x) == Math.abs(mainFrame.pieces[i].getY() - this.y))&&
                        (mainFrame.pieces[i].getX() > x && mainFrame.pieces[i].getX() < this.x) &&
                        (mainFrame.pieces[i].getY() < y && mainFrame.pieces[i].getY() > this.y))
                    {
                        notBetween = false;
                    }
                }
                //if space is occupied, store that piece's index
                if(mainFrame.pieces[i].getX() == x && mainFrame.pieces[i].getY() == y)
                    occupyingPiece = i;
            }
        }
        //if nothing between and no other of your piece are in the square and in the correct diagonal
        if(notBetween && occupyingPiece < 0 && (Math.abs(x - this.x) == Math.abs(y - this.y)))
        {
            //only perform move if not checking check status of other player's king
            if(!performingCheck)
            {
                this.x = x;
                this.y = y;
            }
        }
        else if (occupyingPiece >= 0 && notBetween && mainFrame.pieces[occupyingPiece].getPlayer() != player)
        {
            //only perform move if not checking check status of other player's king
            if(!performingCheck)
            {
                this.x = x;
                this.y = y;
                capturePiece(occupyingPiece);
                moveType = HistoryPanel.CAPTURE;
            }
        }
        else
            throw new InvalidMoveException("Bishops move along the diagonal, "
                +"and can't pass through other mainFrame.pieces.");

        //if not checking for check status of other player's king
        if(!performingCheck)
        {
            //check if this player's King is in check after performing their move
            kingCheckLogic(oldX, oldY);

            //update the move history (starting x not used in this case)
            mainFrame.updateHistory(this, moveType, -1, false);
        }
    }
    //used to move Rooks, located here so that Queens can also use it
    public void rookMove(int x, int y, boolean performingCheck)
        throws InvalidMoveException
    {
        boolean notBetween = true;
        int occupyingPiece = -1, oldX = this.x, oldY = this.y;

        //check vertically and horizontally for mainFrame.pieces between
        if((this.x == x && this.y != y) || (this.x != x && this.y == y))
        {
            for(int i = 0; i < mainFrame.pieces.length; i++)
            {
                //check vertically for pieces between
                if(mainFrame.pieces[i].getX() == x)
                {
                    if((mainFrame.pieces[i].getY() < this.y && mainFrame.pieces[i].getY() > y) ||
                        mainFrame.pieces[i].getY() < y && mainFrame.pieces[i].getY() > this.y)
                    {
                        notBetween = false;
                    }
                }
                //check horizontally for pieces between
                else if(mainFrame.pieces[i].getY() == y)
                {
                    if((mainFrame.pieces[i].getX() < this.x && mainFrame.pieces[i].getX() > x) ||
                        mainFrame.pieces[i].getX() < x && mainFrame.pieces[i].getX() > this.x)
                    {
                        notBetween = false;
                    }
                }
                //check if player is attempting to move ontop of any piece
                if(mainFrame.pieces[i].getX() == x && mainFrame.pieces[i].getY() == y)
                    occupyingPiece = i;
            }
        }
        if(notBetween && occupyingPiece < 0 && (this.x == x || this.y == y))
        {
            //only perform move if not checking check status of other player's king
            if(!performingCheck)
            {
                this.x = x;
                this.y = y;
            }
        }
        else if (occupyingPiece >= 0 && notBetween && mainFrame.pieces[occupyingPiece].getPlayer() != player)
        {
            //only perform move if not checking check status of other player's king
            if(!performingCheck)
            {
                this.x = x;
                this.y = y;
                capturePiece(occupyingPiece);
                moveType = HistoryPanel.CAPTURE;
            }
        }
        else
            throw new InvalidMoveException("Rooks move horizontally and vertically, "
                +"and can't pass through other mainFrame.pieces.");

        //if not checking for check status of other player's king
        if(!performingCheck)
        {
            //check if this player's King is in check after performing their move
            kingCheckLogic(oldX, oldY);

            //update the move history (starting x not used in this case)
            mainFrame.updateHistory(this, moveType, -1, false);
        }
    }    
}

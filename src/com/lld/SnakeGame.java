package com.lld;

/* NOTES
GS (getters and setters for all properties)
Cell : row,col,celltype
enum CellType
Snake : snakeBody(linkedlist), head(Cell), checkCrash(), grow(), move(nextCell)
Board : cells (Cell[][]),rowcount, colcount, generateFood()
SnakeGame : mainMethod, gameover, direction, snake, board, getNextCell(), update()
*/

import java.util.LinkedList;

class Snake{
    private LinkedList<Cell> snakeBody=new LinkedList();
    private Cell head;

    public Snake(Cell headPos)
    {
        head=headPos;
        head.setCellType(CellType.SNAKE);
        snakeBody.add(head);
    }

    public void grow()
    {
        snakeBody.add(head);
    }

    public void move(Cell nextCell){
        System.out.println("Snake is going to : "+
                nextCell.getRow()+" , "+nextCell.getCol());
        Cell tail=snakeBody.removeLast();
        tail.setCellType(CellType.EMPTY);

        head=nextCell;
        head.setCellType(CellType.SNAKE);
        snakeBody.addFirst(head);
    }

    public boolean checkCrash(Cell nextCell)
    {
        // System.out.println("Checking for crash");
        for(Cell cell:snakeBody)
        {
            if(cell.equals(nextCell))
                return true;
        }
        return false;
    }

    public void setSnake(LinkedList<Cell> snakeNew)
    {
        this.snakeBody=snakeNew;
    }

    public Cell getHead(){
        return head;
    }

    public void setHead(Cell newHead){
        this.head=newHead;
    }
}

class Cell{
    private final int row,col;
    private CellType celltype;

    public Cell(int x, int y)
    {
        this.row=x;
        this.col=y;
    }

    public CellType getCellType(){ return this.celltype;}

    public void setCellType(CellType type){
        celltype=type;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

}

enum CellType{
    EMPTY, FOOD, SNAKE;
}

class Board{
    final int ROW_COUNT, COL_COUNT;
    private Cell[][] cells;

    public Board(int rowCount, int colCount)
    {
        ROW_COUNT=rowCount;
        COL_COUNT=colCount;
        cells=new Cell[ROW_COUNT][COL_COUNT];
        for(int i=0;i<ROW_COUNT;i++)
        {
            for(int j=0;j<COL_COUNT;j++)
            {
                cells[i][j]=new Cell(i,j);
            }
        }
    }

    public Cell[][] getCells(){
        return cells;
    }

    public void setCells(Cell[][] cells)
    {
        this.cells=cells;
    }

    public void generateFood(){
        System.out.println("Generating Food: ");
        int row,col;
        while(true)
        {
            row=(int)(Math.random()*ROW_COUNT);
            col=(int)(Math.random()*COL_COUNT);
            if(cells[row][col].getCellType()!=CellType.SNAKE)
                break;
        }
        cells[row][col].setCellType(CellType.FOOD);
        System.out.println("Food is generated at row : "+row+" and col : "+col);
    }
}

public class SnakeGame{
    private Board board;
    private Snake snake;
    private int direction;
    private boolean gameOver;
    public static final int DIRECTION_NONE=0, DIRECTION_LEFT=1, DIRECTION_RIGHT=-1,
            DIRECTION_UP=2, DIRECTION_DOWN=-2;

    public SnakeGame(Snake snake, Board board)
    {
        this.board=board;
        this.snake=snake;
    }

    public Snake getSnake(){return this.snake;}
    public void setSnake(Snake snake){
        this.snake=snake;
    }

    public Board getBoard(){return this.board;}
    public void setBoard(Board board){
        this.board=board;
    }

    public boolean getGameOver(){return this.gameOver;}
    public void setGameOver(boolean gameOver){
        this.gameOver=gameOver;
    }

    public int getDirection(){return this.direction;}
    public void setDirection(int direction){
        this.direction=direction;
    }

    public void update(){
        // System.out.println("updating snake move");
        if(!gameOver)
        {
            if(direction!=DIRECTION_NONE)
            {
                Cell nextCell=getNextCell(snake.getHead());

                if(nextCell==null || snake.checkCrash(nextCell))
                {
                    System.out.println("gameover");
                    setDirection(DIRECTION_NONE);
                    gameOver=true;
                }
                else
                {
                    snake.move(nextCell);
                    if(nextCell.getCellType()==CellType.FOOD)
                    {
                        snake.grow();
                        board.generateFood();
                    }
                }
            }

        }
    }

    public Cell getNextCell(Cell cell){
        // System.out.println("getting next cell");
        int row=cell.getRow();
        int col=cell.getCol();
        int size=board.getCells().length;

        if(direction==DIRECTION_RIGHT) col++;
        else if(direction==DIRECTION_LEFT) col--;
        else if(direction==DIRECTION_UP) row--;
        else if(direction==DIRECTION_DOWN) row++;

        /*if(row<0) row=size-1;
        if(row>=size) row=0;
        if(col<0) col=size-1;
        if(col>=size) col=0;*/
        if(row<0 || col<0 || row >=size || col>=size) {
            /*System.out.println("gameover");
            setDirection(DIRECTION_NONE);
            gameOver=true;*/
            return null;
        }
        Cell nextCell=board.getCells()[row][col];
        return nextCell;
    }
    public static void main(String[] args) {
        System.out.println("Starting game");

        Cell initPos=new Cell(0,0);
        Snake initSnake=new Snake(initPos);
        Board initBoard=new Board(10,10);
        SnakeGame newgame=new SnakeGame(initSnake,initBoard);
        newgame.gameOver=false;
        newgame.direction=DIRECTION_RIGHT;

        for(int i=0;i<10;i++)
        {
            if(i==1)newgame.board.generateFood();
            newgame.update();
            if(newgame.gameOver==true)
                break;
            //if(i==0) continue;
            /*if(i%5==0)newgame.direction=DIRECTION_DOWN;
            if(i%6==0)newgame.direction=DIRECTION_LEFT;
            if(i%8==0)newgame.direction=DIRECTION_UP;
            if(i%12==0)newgame.direction=DIRECTION_RIGHT;*/

        }
    }
}


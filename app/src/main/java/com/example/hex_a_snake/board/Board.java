package com.example.hex_a_snake.board;

import com.example.hex_a_snake.model.HexXYZ;

public class Board {
    private final int[] array = new int[19];
    private final static int[] offset = {2, 5, 9, 13, 16};

    public Board(){
        /**
        for(int i = 0; i < array.length; i++){
            array[i] = i;
        }
        /**/
    }

    private Board(int score, int max){
        this.score = score;
        this.max   = max;
    }

    public final int get(int x, int y){
        return array[offset[x+2] + y];
    }

    public int score = 0;
    public int max = 5;
    private void set(int x, int y, int idx){
        array[offset[x+2] + y] = idx;
    }

    void addScore(int add){
        max = Math.max(max, add);
        score += add;
    }
    public void addRandom(final int total, EffectManager effects){
        int open = 0;
        for(int b : array){
            if (b == 0)
                open++;
        }

        int t = Math.min(total, open);
        while(t -- >= 0){
            int loc = (int)(Math.random() * open);
            int num = (int)(Math.random() * (max - 3) + 1);
            int eSum = 0;
            for(int b = 0; b < array.length; b++){
                if (array[b] == 0 && eSum++ == loc) {
                    array[b] = num;
                    //offset[x + 2] + y = b; {2, 5, 9, 13, 16}
                    int[] dist = {
                            -2, -2, -2,
                            -1, -1, -1, -1,
                            0, 0, 0, 0, 0,
                            1, 1, 1, 1,
                            2, 2, 2,
                    };

                    int x = dist[b];
                    int y = b - offset[x + 2];
                    effects.addRandom(x, y,num);
                }
            }
        }
    }

    public static boolean inHex(float x, float y){
        return Math.abs(x) <= 2 && Math.abs(y) <= 2 && Math.abs(x-y) <= 2;
    }

    public final Board move(HexXYZ hex, EffectManager effects){
        if(hex.getY() == 0 && hex.getX() == 0)
            return this;

        if(effects != null)
            effects.reset();
        final Board nextBoard = new Board(score, max);
        final int x = (int)hex.getX();
        final int y = (int)hex.getY();
        if(x != 0 || y != 0){
            for(int i = -2; i < 3; i++){
                int sum = Math.min(i + 2, 2);
                int openx = -y * i + x * sum;
                int openy =  x * i + y * (sum - i);
                int atOpen = get(openx, openy);
                if(atOpen != 0) {
                    nextBoard.set(openx, openy, atOpen);
                    if(effects != null)
                        effects.addStationary(openx, openy, atOpen);
                }

                for(int dt = 1; dt < 5 - Math.abs(i); dt++){
                    int locx =-y * i + x * (sum - dt);
                    int locy = x * i + y * (sum - dt - i);
                    int atLoc = get(locx, locy);

                    if(atLoc != 0){
                        if(atOpen == 0){
                            //position is empty shift over
                            nextBoard.set(openx, openy, atLoc);
                            atOpen = atLoc;
                            if(effects != null)
                                effects.addShift(locx, locy, openx, openy, atLoc);
                        } else if (atLoc == atOpen){
                            //numbers are the same (combine)
                            nextBoard.set(openx, openy, ++atOpen);
                            nextBoard.addScore(atOpen);
                            //shift to the next (empty) space
                            if(effects != null)
                                effects.addIncrease(locx, locy, openx, openy, atLoc);
                            openx -= x;
                            openy -= y;

                            atOpen = 0;
                        } else { //atLoc != atOpen
                            //place at next open space
                            openx -= x;
                            openy -= y;

                            //assign next value
                            nextBoard.set(openx, openy, atLoc);
                            if(effects != null)
                                effects.addShift(locx, locy, openx, openy, atLoc);
                            atOpen = atLoc;
                        }
                    }
                }

            }
        }
        return nextBoard;
    }

    public final boolean isEqual(Board board){
        for(int i = 0; i < array.length; i++){
            if(array[i] != board.array[i])
                return false;
        }
        return true;
    }

    public final boolean checkEndgame(){
        for(int i = 0; i < array.length; i++){
            if(array[i] == 0) return false;
        }

        HexXYZ direction = new HexXYZ();
        return
            move(direction.set(1,0,0), null).isEqual(this) &&
            move(direction.set(0,1,0), null).isEqual(this) &&
            move(direction.set(0,0,1), null).isEqual(this);
    }
}

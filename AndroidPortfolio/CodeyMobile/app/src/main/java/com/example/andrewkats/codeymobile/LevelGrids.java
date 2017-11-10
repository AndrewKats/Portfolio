package com.example.andrewkats.codeymobile;

/**
 * Created by AndrewKats on 12/16/2016.
 */
public class LevelGrids
{
    // 1=ground, 2=flag, 3=cond
    int[][] getLevel(int levelNumber)
    {
        if(levelNumber == 1)
        {
            int[][] level = new int[15][7];

            //ground
            for(int i=0; i<15; i++)
            {
                level[i][0] = 1;
            }

            //flag
            level[5][1] = 2;
            level[5][2] = 2;
            level[5][3] = 2;

            return level;
        }
        if(levelNumber == 2)
        {
            int[][] level = new int[15][7];

            //ground
            for(int i=0; i<9; i++)
            {
                if(i!=4)
                {
                    level[i][0] = 1;
                }
            }

            //floating ground
            level[9][2] = 1;
            level[10][2] = 1;
            level[11][2] = 1;
            level[12][2] = 1;

            //flag
            level[12][3] = 2;
            level[12][4] = 2;
            level[12][5] = 2;

            return level;
        }
        if(levelNumber == 3)
        {
            int[][] level = new int[15][7];
            //row 1
            level[0][0]=1;
            level[2][0]=1;
            level[3][0]=1;
            level[4][0]=1;
            level[5][0]=1;
            level[7][0]=1;
            level[8][0]=1;
            level[10][0]=1;
            level[11][0]=1;
            level[12][0]=1;
            level[13][0]=1;

            // row 2
            level[5][1]=1;

            // row 3
            level[5][2]=1;
            level[13][2]=1;

            //flag
            level[13][3]=2;
            level[13][4]=2;
            level[13][5]=2;

            return level;
        }
        if(levelNumber == 4)
        {
            int[][] level = new int[15][7];

            // ground
            for(int i=0; i<15; i++)
            {
                level[i][0] = 1;
            }

            // flag
            level[13][1] = 2;
            level[13][2] = 2;
            level[13][3] = 2;

            return level;
        }
        if(levelNumber == 5)
        {
            int[][] level = new int[15][7];

            // ground
            level[0][0] = 1;
            level[2][0] = 1;
            level[4][0] = 1;
            level[6][0] = 1;
            level[8][0] = 1;
            level[10][0] = 1;
            level[12][0] = 1;

            // floating
            level[1][2] = 1;
            level[3][2] = 1;
            level[5][2] = 1;
            level[7][2] = 1;
            level[9][2] = 1;
            level[11][2] = 1;
            level[13][2] = 1;

            // flag
            level[13][3] = 2;
            level[13][4] = 2;
            level[13][5] = 2;

            return level;
        }
        if(levelNumber == 6)
        {
            int[][] level = new int[15][7];

            // ground
            for(int i=0; i<8; i++)
            {
                if(i!=4)
                {
                    level[i][0] = 1;
                }
            }

            // floating
            level[4][2] = 1;
            level[8][2] = 1;
            level[10][2] = 1;
            level[12][2] = 1;
            level[14][2] = 1;

            // flag
            level[14][3] = 2;
            level[14][4] = 2;
            level[14][5] = 2;

            return level;
        }
        if(levelNumber == 7)
        {
            int[][] level = new int[15][7];

            //ground
            for(int i=0; i<15; i++)
            {
                if(i!=3 && i!=7)
                {
                    level[i][0] = 1;
                }
            }

            //cond
            level[3][1] = 3;
            level[3][2] = 3;
            level[3][3] = 3;

            level[7][1] = 3;
            level[7][2] = 3;
            level[7][3] = 3;

            //flag
            level[11][1] = 2;
            level[11][2] = 2;
            level[11][3] = 2;


            return level;
        }
        if(levelNumber == 8)
        {
            int[][] level = new int[15][7];

            //ground
            for(int i=0; i<7; i++)
            {
                if(i!=4)
                {
                    level[i][0] = 1;
                }
            }
            level[9][0] = 1;
            level[14][0] = 1;

            //floating
            level[4][2] = 1;
            level[7][2] = 1;
            level[10][2] = 1;
            level[12][2] = 1;

            //cond
            level[11][2] = 3;
            level[11][3] = 3;
            level[11][4] = 3;

            //flag
            level[14][1] = 2;
            level[14][2] = 2;
            level[14][3] = 2;

            return level;
        }
        return null;
    }

}

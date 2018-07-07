package oldbro.fight;

class Goblin extends Monster
{
    static String monsterGif;

    static int monsterWidth;

    static int monsterHeight;

    static int upLeftOffsetX;

    static int upLeftOffsetY;

    static int gifCycleDistance;

    static int gifOriginalDirection;

    static {
        monsterGif = "resource/goblin.gif";
        monsterWidth = 120;
        monsterHeight = 90;
        upLeftOffsetX = 60;
        upLeftOffsetY = 40;
        gifCycleDistance = 40;
        gifOriginalDirection = -1;
    }

    public Goblin(int centerX, int centerY, int speed, boolean isFree)
    {
        super(monsterGif, monsterWidth, monsterHeight, upLeftOffsetX, upLeftOffsetY, gifCycleDistance, gifOriginalDirection, centerX, centerY, speed, isFree);
    }
}

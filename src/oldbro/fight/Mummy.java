package oldbro.fight;

class Mummy extends Monster
{
    static String monsterGif;

    static int monsterWidth;

    static int monsterHeight;

    static int upLeftOffsetX;

    static int upLeftOffsetY;

    static int gifCycleDistance;

    static int gifOriginalDirection;

    static {
        monsterGif = "resource/mummy.gif";
        monsterWidth = 120;
        monsterHeight = 120;
        upLeftOffsetX = 60;
        upLeftOffsetY = 60;
        gifCycleDistance = 80;
        gifOriginalDirection = 1;
    }

    public Mummy(int centerX, int centerY, int speed, boolean isFree)
    {
        super(monsterGif, monsterWidth, monsterHeight, upLeftOffsetX, upLeftOffsetY, gifCycleDistance, gifOriginalDirection, centerX, centerY, speed, isFree);
    }
}
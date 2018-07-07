package oldbro;

public class Statistics
{
    public static int monsterKillCount;

    public static String monsterKillCountName;

    public static int yangYanCount;

    public static String yangYanCountName;

    public static int daXueCount;

    public static String daXueCountName;

    public static int fangZhenCount;

    public static String fangZhenCountName;

    public static int yanFanCount;

    public static String yanFanCountName;

    public static int score;

    static {
        monsterKillCountName = "灭";
        yangYanCountName = "炎";
        daXueCountName = "雪";
        fangZhenCountName = "阵";
        yanFanCountName = "燕";
        init();
    }

    public static void clear()
    {
        init();
    }

    public static void init()
    {
        monsterKillCount = 0;
        yangYanCount = 0;
        daXueCount = 0;
        fangZhenCount = 0;
        yanFanCount = 0;
        score = 0;
    }

    public static void print()
    {
        System.out.println("monsterKill: " + monsterKillCount);
        System.out.println("yangYanCount: " + yangYanCount);
        System.out.println("daXueCount: " + daXueCount);
        System.out.println("fangZhenCount: " + fangZhenCount);
        System.out.println("yanFanCount: " + yanFanCount);
    }

    public static void calculate()
    {
        score = 11 * monsterKillCount - 3 * yangYanCount - 97 * daXueCount - 76 * fangZhenCount - 135 * yanFanCount;
    }
}

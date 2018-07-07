package oldbro;

public class Player implements Comparable<Player>
{
    public String name;

    public int score;

    public int rank;

    public static String youName = "YOU";

    public static String oldbroName;

    public static String gatesName;

    public static String tysonName;

    public static String luhanName;

    static {
        youName = "YOU";
        oldbroName = "老哥";
        gatesName = "盖茨";
        tysonName = "泰森";
        luhanName = "鹿晗";
    }

    public Player(String name, int score)
    {
        this.name = name;
        this.score = score;
        rank = 0;
    }

    @Override
    public int compareTo(Player other)
    {
        if (score < other.score) {
            return 1;
        } else if (score > other.score) {
            return -1;
        } else {
            return 0;
        }
    }
}

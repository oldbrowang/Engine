package oldbro.fight;

import javafx.animation.Timeline;

import java.util.LinkedList;
import java.util.List;

class MonsterDispatcher extends FightComponent
{
    static List<Monster> monsters;

    static int completedCycleCount;

    static int maxCycleCount;

    static Timeline delayedDispatchTimer;

    static boolean dispatchDone;


    static {
        //completedCycleCount = 0;
        //maxCycleCount = 4;
    }


    static void load()
    {
        completedCycleCount = 0;
        maxCycleCount = 3;
        monsters = new LinkedList<>();
        DeploymentParser.load();
        dispatchDone = false;
    }

    static void dispatch()
    {
        //System.out.println(123);
        if (dispatchDone) {
            return;
        }
        while (true) {
            if (Swordman.inCombo) {
                delayDispatch(5000);
                break;
            }
            Deployment deployment = DeploymentParser.getDeployment();
            if (deployment instanceof MonsterDeployment) {
                Monster monster = createMonster((MonsterDeployment) deployment);
                monsters.add(monster);
                addToRoot(monster.monsterView);
                if (isFightStopped) {
                    monster.stopMoving();
                } else {
                    monster.go();
                }
            } else if (deployment instanceof DelayDeployment) {
                delayDispatch(((DelayDeployment) deployment).duration);
                break;
            } else {
                completedCycleCount++;
                //System.out.println(completedCycleCount);
                if (completedCycleCount < maxCycleCount) {
                    delayDispatch(15000);
                } else {
                    dispatchDone = true;
                }
                break;
            }
        }
    }

    static void delayDispatch(double duration)
    {
         delayedDispatchTimer = setTimeout(duration, e -> dispatch());
    }

    static Monster createMonster(MonsterDeployment monsterDeployment)
    {
        Monster monster = null;
        try {
            Class<?> monsterClass = Class.forName(MonsterDispatcher.class.getPackage().getName() + "."
                    + monsterDeployment.monsterClassName);
            monster = (Monster) monsterClass.getConstructor(int.class, int.class, int.class, boolean.class)
                    .newInstance(monsterDeployment.startX, monsterDeployment.startY, (int) (monsterDeployment.speed * Math.pow(1.4, completedCycleCount)), monsterDeployment.isFree);
        } catch (Exception e) {
            System.out.println(e);
        }
        return monster;
    }

    static void remove(Monster monster)
    {
        removeFromRoot(monster.monsterView);
        monsters.remove(monster);
        checkAndCloseFight();
    }

    static void stopAllMonsters()
    {
        for (Monster monster : monsters) {
            if (monster.state == Monster.State.Moving) {
                monster.stopMoving();
            }
            /*
            else if (monster.state == Monster.State.Flying) {
                monster.stopFlying();
            }
            */
        }
    }

    static void makeAllMonstersMove()
    {
        for (Monster monster : monsters) {
            if (monster.state == Monster.State.Stopped) {
                monster.go();
            }
        }
    }

    static void stopDispatcher()
    {
        if (delayedDispatchTimer != null) {
            delayedDispatchTimer.stop();
        }
    }

    static void clearMonsters()
    {
        for (Monster monster : monsters) {
            monster.fadeThenRemove();
        }
    }

    static int getCurrentMonsterNumber()
    {
        return monsters.size();
    }
}
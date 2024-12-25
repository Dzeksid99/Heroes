package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        //проходим по всем юнитам, а потом начинаем заново, и так m раундов
        //сложность метода O (n * m) где m - число раундов, а n - число юнитов. Однако если мы предположим
        // (это предположение нужно еще проверить, но похоже на правду), что число раундов - функция от числа юнитов, то получаем
        // Сложность можно выразить и как O (n^2), что лучше предложенного (O(n^2*log n))
        List<Unit> playerUnits = playerArmy.getUnits();
        List<Unit> computerUnits = computerArmy.getUnits();

        //эти сортировки O(n log n)
        playerUnits
                .sort(Comparator.comparingInt(Unit::getBaseAttack)
                        .reversed());
        computerUnits
                .sort(Comparator.comparingInt(Unit::getBaseAttack)
                        .reversed());

        int playerIndex = 0;
        int computerIndex = 0;

        // Пока обе армии имеют живых юнитов
        while (hasAliveUnits(playerUnits) && hasAliveUnits(computerUnits)) {
            // Ход игрока
            if (playerIndex < playerUnits.size()) {
                Unit playerUnit = playerUnits.get(playerIndex);
                if (playerUnit.isAlive()) {
                    Unit target = playerUnit.getProgram().attack(); //условились, что O(1)
                    if (target != null) {
                        performAttack(playerUnit, target);
                        printBattleLog.printBattleLog(playerUnit, target);
                    }
                }
                playerIndex++;
            }

            // Ход компьютера
            if (computerIndex < computerUnits.size()) {
                Unit computerUnit = computerUnits.get(computerIndex);
                if (computerUnit.isAlive()) {
                    Unit target = computerUnit.getProgram().attack();
                    if (target != null) {
                        performAttack(computerUnit, target);
                        printBattleLog.printBattleLog(computerUnit, target);
                    }
                }
                computerIndex++;
            }

            // Если все юниты сходили, начинаем новый раунд
            if (playerIndex >= playerUnits.size() && computerIndex >= computerUnits.size()) {
                playerIndex = 0;
                computerIndex = 0;
            }
        }
    }

    /**
     * Выполняет атаку юнита на цель. O(1)
     */
    private void performAttack(Unit attacker, Unit target) {
        int damage = attacker.getBaseAttack();
        target.setHealth(target.getHealth() - damage);
        if (target.getHealth() <= 0) {
            target.setAlive(false);
        }
    }

    /**
     * Проверяет, есть ли живые юниты в списке. O(n)
     */
    private boolean hasAliveUnits(List<Unit> units) {
        for (Unit unit : units) {
            if (unit.isAlive()) {
                return true;
            }
        }
        return false;
    }
}
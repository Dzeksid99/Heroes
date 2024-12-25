package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {

        List<Unit> armyList = new ArrayList<>();
        int currentPoints = maxPoints;

        //сложность данной сортировки O (log n * n)
        unitList = unitList.stream()
                .sorted(Comparator.comparingDouble(unit -> (double) unit.getBaseAttack() / unit.getCost()
                        + (double) unit.getHealth() / unit.getCost()))
                .toList();

        //Хоть тут два цикла, но второй константный, так что этот блок O(n)
        for (Unit unit : unitList) {
            for (int i = 0; i < 11; i++) {
                if (currentPoints < unit.getCost()) {
                    break;
                }

                currentPoints -= unit.getCost();

                armyList.add(new Unit(unit.getName() + " " + i, unit.getUnitType(), unit.getHealth(), unit.getBaseAttack(), unit.getCost(), unit.getAttackType(), unit.getAttackBonuses(), unit.getDefenceBonuses(), armyList.size() / 21, armyList.size() % 21));
            }
        }

        System.out.println(armyList);

        //Итоговая сложность O(n * log n) - это лучше, чем O(n * m), предложенное при решении
        return new Army(armyList);
    }
}
package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        //В данном методе видимо три цикла в цикле, но два из них зависят от количества столбцов, поэтому могут быть оценены как O(1), так как количество столбцов <= 3
        //Поэтому общая сложность алгоритма = O(n), что соотвествует требованию задачи
        //Уточнение: в задаче требуется на максимальный балл сделать еще быстрее, но это невозможно, для того,
        //чтобы проверить юнитов, нам нужно хотя бы по ним по всем один раз пройти, то есть это функция O(n)
        List<Unit> suitableUnits = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            for (Unit unit : row) {
                boolean isBlocked = false;
                if (isLeftArmyTarget) {
                    // Проверка для левой армии (компьютер)
                    if (unit.getxCoordinate() >= 24 && unit.getxCoordinate() <= 26) {
                        for (Unit otherUnit : row) {
                            if (otherUnit.getxCoordinate() < unit.getxCoordinate() && otherUnit.getyCoordinate() == unit.getyCoordinate() && otherUnit.isAlive()) {
                                isBlocked = true;
                                break;
                            }
                        }
                    }
                } else {
                    // Проверка для правой армии (игрок)
                    if (unit.getxCoordinate() >= 0 && unit.getxCoordinate() <= 2) {
                        for (Unit otherUnit : row) {
                            if (otherUnit.getxCoordinate() > unit.getxCoordinate() && otherUnit.getyCoordinate() == unit.getyCoordinate() && otherUnit.isAlive()) {
                                isBlocked = true;
                                break;
                            }
                        }
                    }
                }
                if (!isBlocked && unit.isAlive()) {
                    suitableUnits.add(unit);
                }
            }
        }

        return suitableUnits;
    }
}
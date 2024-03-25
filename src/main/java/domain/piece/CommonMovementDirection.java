package domain.piece;

import domain.board.Position;

import java.util.Arrays;
import java.util.Set;
import java.util.function.BiPredicate;

public enum CommonMovementDirection implements MovementDirection {
    UP(-1, 0, (rowDistance, columnDistance) -> rowDistance < 0 && columnDistance == 0),
    DOWN(1, 0, (rowDistance, columnDistance) -> rowDistance > 0 && columnDistance == 0),
    RIGHT(0, 1, (rowDistance, columnDistance) -> rowDistance == 0 && columnDistance > 0),
    LEFT(0, -1, (rowDistance, columnDistance) -> rowDistance == 0 && columnDistance < 0),
    UP_RIGHT(-1, 1, (rowDistance, columnDistance) -> rowDistance < 0 && columnDistance > 0),
    UP_LEFT(-1, -1, (rowDistance, columnDistance) -> rowDistance < 0 && columnDistance < 0),
    DOWN_RIGHT(1, 1, (rowDistance, columnDistance) -> rowDistance > 0 && columnDistance > 0),
    DOWN_LEFT(1, -1, (rowDistance, columnDistance) -> rowDistance > 0 && columnDistance < 0);

    private final int rowDistance;
    private final int columnDistance;
    private final BiPredicate<Integer, Integer> condition;

    CommonMovementDirection(final int rowDistance, final int columnDistance, final BiPredicate<Integer, Integer> condition) {
        this.rowDistance = rowDistance;
        this.columnDistance = columnDistance;
        this.condition = condition;
    }

    public static CommonMovementDirection calculateDirection(final Position source, final Position destination) {
        final int rowDifference = destination.rowIndex() - source.rowIndex();
        final int columnDifference = destination.columnIndex() - source.columnIndex();

        validateDistance(rowDifference, columnDifference);

        return Arrays.stream(CommonMovementDirection.values())
                .filter(unitVector -> unitVector.condition.test(rowDifference, columnDifference))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("상/하/좌/우 혹은 대각선으로 이동할 수 없는 칸입니다."));
    }

    private static void validateDistance(final int rowDistance, final int columnDistance) {
        if (rowDistance == 0 && columnDistance == 0
                || (!(Math.abs(rowDistance) == Math.abs(columnDistance))
                && !(rowDistance == 0 || columnDistance == 0))) {
            throw new IllegalArgumentException(("상/하/좌/우 혹은 대각선으로 이동할 수 없는 칸입니다."));
        }
    }

    public static Set<CommonMovementDirection> orthogonalVectors() {
        return Set.of(UP, RIGHT, DOWN, LEFT);
    }

    public static Set<CommonMovementDirection> diagonalVectors() {
        return Set.of(UP_RIGHT, DOWN_RIGHT, DOWN_LEFT, UP_LEFT);
    }

    public static Set<CommonMovementDirection> omnidirectionalVectors() {
        return Set.of(UP, RIGHT, DOWN, LEFT, UP_RIGHT, DOWN_RIGHT, DOWN_LEFT, UP_LEFT);
    }

    @Override
    public int getRowDistance() {
        return rowDistance;
    }

    @Override
    public int getColumnDistance() {
        return columnDistance;
    }
}
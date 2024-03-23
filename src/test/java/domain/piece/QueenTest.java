package domain.piece;

import domain.position.File;
import domain.position.Position;
import domain.position.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QueenTest {

    @DisplayName("주어진 출발지 -> 도착지를 Rook이 이동할 수 있는지 검증한다.")
    @MethodSource("checkMovableTestCase")
    @ParameterizedTest
    void checkMovableTest(final Position source, final Position destination) {
        // Given
        Queen queen = new Queen(PieceColor.WHITE);
        Map<Position, Piece> piecePositions = Map.of(position(File.D, Rank.TWO), new Rook(PieceColor.BLACK));

        // When & Then
        assertThatCode(() -> queen.checkMovable(source, destination, piecePositions))
                .doesNotThrowAnyException();
    }

    private static Stream<Arguments> checkMovableTestCase() {
        return Stream.of(
                Arguments.of(position(File.B, Rank.TWO), position(File.B, Rank.SIX)),
                Arguments.of(position(File.B, Rank.TWO), position(File.B, Rank.ONE)),
                Arguments.of(position(File.B, Rank.TWO), position(File.A, Rank.TWO)),
                Arguments.of(position(File.B, Rank.TWO), position(File.D, Rank.TWO)),
                Arguments.of(position(File.B, Rank.TWO), position(File.E, Rank.FIVE)),
                Arguments.of(position(File.B, Rank.TWO), position(File.A, Rank.THREE)),
                Arguments.of(position(File.B, Rank.TWO), position(File.A, Rank.ONE)),
                Arguments.of(position(File.B, Rank.TWO), position(File.C, Rank.ONE))
        );
    }

    @DisplayName("이동 경로에 기물이 존재하면 예외를 발생시킨다.")
    @MethodSource("throwExceptionWhenPathsHasPieceTestCase")
    @ParameterizedTest
    void throwExceptionWhenPathsHasPieceTest(final Position source, final Position destination) {
        // Given
        Queen queen = new Queen(PieceColor.WHITE);
        Map<Position, Piece> piecePositions = Map.of(
                position(File.B, Rank.FOUR), new Bishop(PieceColor.BLACK),
                position(File.D, Rank.TWO), new Rook(PieceColor.WHITE)
        );

        // When & Then
        assertThatThrownBy(() -> queen.checkMovable(source, destination, piecePositions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("목적지 경로에 기물이 존재하여 이동할 수 없습니다.");
    }

    private static Stream<Arguments> throwExceptionWhenPathsHasPieceTestCase() {
        return Stream.of(
                Arguments.of(position(File.B, Rank.TWO), position(File.B, Rank.SIX)),
                Arguments.of(position(File.B, Rank.TWO), position(File.G, Rank.TWO))
        );
    }

    @DisplayName("도착지에 아군 기물이 존재하면 예외를 발생시킨다.")
    @Test
    void throwExceptionWhenDestinationHasTeamPieceTest() {
        // Given
        Position source = position(File.B, Rank.TWO);
        Position destination = position(File.B, Rank.SIX);
        Queen queen = new Queen(PieceColor.WHITE);
        Map<Position, Piece> piecePositions = Map.of(destination, new Rook(PieceColor.WHITE));

        // When & Then
        assertThatThrownBy(() -> queen.checkMovable(source, destination, piecePositions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아군 기물이 위치한 칸으로는 이동할 수 없습니다.");
    }

    private static Position position(final File file, final Rank rank) {
        return new Position(file, rank);
    }
}
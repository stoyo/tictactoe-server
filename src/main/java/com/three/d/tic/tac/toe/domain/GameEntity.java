package com.three.d.tic.tac.toe.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.three.d.tic.tac.toe.dto.Game;
import com.vladmihalcea.hibernate.type.array.IntArrayType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDefs({
        @TypeDef(
                name = "int-array",
                typeClass = IntArrayType.class
        )
})
@Entity
@Table(name = "game_boards")
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Integer n;

    @NotNull
    @Column(nullable = false)
    private String board;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;

    @Enumerated(EnumType.STRING)
    private GamePlayer winner;

    @Column(name = "next_turn")
    @Enumerated(EnumType.STRING)
    private GamePlayer nextTurn;

    @NotNull
    @Type(type = "int-array")
    @Column(
            name = "blank_tiles_indexes",
            nullable = false,
            columnDefinition = "integer[]"
    )
    private Integer[] blankTilesIndexes;

    @Type(type = "int-array")
    @Column(
            name = "winning_tiles_indexes",
            columnDefinition = "integer[]"
    )
    private Integer[] winningTilesIndexes;

    @Version
    private Integer version;

    public static Game toDto(GameEntity ge) {
        return Game.builder()
                .id(ge.getId())
                .n(ge.getN())
                .board(ge.getBoard())
                .winner(ge.getWinner())
                .nextTurn(ge.getNextTurn())
                .blankTilesIndexes(ge.getBlankTilesIndexes())
                .winningTilesIndexes(ge.getWinningTilesIndexes())
                .build();
    }

    public static GameEntity fromDto(Game game) {
        return GameEntity.builder()
                .id(game.getId())
                .n(game.getN())
                .board(game.getBoard())
                .winner(game.getWinner())
                .nextTurn(game.getNextTurn())
                .blankTilesIndexes(game.getBlankTilesIndexes())
                .winningTilesIndexes(game.getWinningTilesIndexes())
                .build();
    }
}

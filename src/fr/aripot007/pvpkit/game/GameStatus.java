package fr.aripot007.pvpkit.game;

/**
 * The status of a {@link Game}
 * @author Aristide
 *
 */
public enum GameStatus {
	/** Players can join the game */
	OPEN,
	/** Players cannot join the game */
	CLOSED,
	/** The game is under maintenance, it is displayed but players can't join it */
	MAINTENANCE,
	/** The game is under configuration or is not valid, it is not displayed and players can't join it*/
	CONFIG;
}

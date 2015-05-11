package RS2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import RS2.clan.ClanChatHandler;
import RS2.fileserver.impl.FileServer;
import RS2.model.item.ItemHandler;
import RS2.model.npc.NPCHandler;
import RS2.model.object.ObjectHandler;
import RS2.model.object.ObjectManager;
import RS2.model.player.Player;
import RS2.model.player.PlayerHandler;
import RS2.model.player.packets.PlayerManager;
import RS2.model.shop.ShopHandler;
import RS2.net.PipelineFactory;
import RS2.task.Task;
import RS2.task.TaskScheduler;
import RS2.util.log.Logger;
import RS2.world.StillGraphicsManager;
 
@SuppressWarnings("all")
public class GameEngine {

	/**
	 * Calls to manage the players on the server.
	 */
	public static PlayerManager playerManager = null;
	private static StillGraphicsManager stillGraphicsManager = null;

	/**
	 * Sleep mode of the server.
	 */
	public static boolean sleeping;

	/**
	 * Calls the rate in which an event cycles.
	 */
	public static final int cycleRate;

	/**
	 * Server updating.
	 */
	public static boolean UpdateServer = false;

	/**
	 * Calls in which the server was last saved.
	 */
	public static long lastMassSave = System.currentTimeMillis();

	/**
	 * Calls the usage of CycledEvents.
	 */
	private static long cycleTime, cycles, totalCycleTime, sleepTime;

	/**
	 * Used for debugging the server.
	 */
	private static DecimalFormat debugPercentFormat;

	/**
	 * Forced shutdowns.
	 */
	public static boolean shutdownServer = false, shutdownClientHandler;

	/**
	 * Used to identify the server port.
	 */
	public static int serverlistenerPort;

	/**
	 * Calls the usage of player items.
	 */
	public static ItemHandler itemHandler = new ItemHandler();

	/**
	 * Handles logged in players.
	 */
	public static PlayerHandler playerHandler = new PlayerHandler();

	/**
	 * Handles global NPCs.
	 */
	public static NPCHandler npcHandler = new NPCHandler();

	/**
	 * Handles global shops.
	 */
	public static ShopHandler shopHandler = new ShopHandler();

	/**
	 * Handles global objects.
	 */
	public static ObjectHandler objectHandler = new ObjectHandler();
	public static ObjectManager objectManager = new ObjectManager();

	/**
	 * Handles the clan chat.
	 */
	public static ClanChatHandler clanChat = new ClanChatHandler();

	/**
	 * Handles the task scheduler.
	 */
	private static final TaskScheduler scheduler = new TaskScheduler();

	/**
	 * Gets the task scheduler.
	 */
	public static TaskScheduler getTaskScheduler() {
		return scheduler;
	}

	static {
		serverlistenerPort = 43594;
		cycleRate = 600;
		shutdownServer = false;
		sleepTime = 0;
		debugPercentFormat = new DecimalFormat("0.0#%");
	}

	/**
	 * Starts the server.
	 */
	public static void main(java.lang.String args[])
			throws NullPointerException, IOException {

		long startTime = System.currentTimeMillis();
		System.setOut(new Logger(System.out));
		System.setErr(new Logger(System.err));
		System.out.println("Launching fileserver..");
		try {
			new FileServer().start();
			new RS2.fileserver.FileServer().bind();
		} catch (Exception e) {
			e.printStackTrace();
		}

		bind();

		playerManager = PlayerManager.getSingleton();
		playerManager.setupRegionPlayers();
		stillGraphicsManager = new StillGraphicsManager();

		/**
		 * Successfully loaded the server.
		 */
		long endTime = System.currentTimeMillis();
		long elapsed = endTime - startTime;
		System.out.println("Took " + elapsed + " milliseconds to launch.");

		/**
		 * Main server tick.
		 */
		scheduler.schedule(new Task() {
			@Override
                    protected void execute() {
                        itemHandler.process();
                        playerHandler.process();	
                        npcHandler.process();
                        shopHandler.process();
                        objectManager.process();
                        }
                });
	}
	private Player p;
	/**
	 * Logging execution.
	 */
	public static boolean playerExecuted = false;

	/**
	 * Gets the sleep mode timer and puts the server into sleep mode.
	 */
	public static long getSleepTimer() {
		return sleepTime;
	}

	/**
	 * Gets the Graphics manager.
	 */
	public static StillGraphicsManager getStillGraphicsManager() {
		return stillGraphicsManager;
	}

	/**
	 * Gets the Player manager.
	 */
	public static PlayerManager getPlayerManager() {
		return playerManager;
	}

	/**
	 * Gets the Object manager.
	 */
	public static ObjectManager getObjectManager() {
		return objectManager;
	}

	/**
	 * Java connection. Ports.
	 */
	private static void bind() {
		ServerBootstrap serverBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		serverBootstrap.setPipelineFactory(new PipelineFactory(
				new HashedWheelTimer()));
		serverBootstrap.bind(new InetSocketAddress(serverlistenerPort));
	}
}
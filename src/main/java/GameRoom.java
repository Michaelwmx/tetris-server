import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class GameRoom implements Runnable {

    public static GameRoom getRoom(String id) {
        return rooms.getOrDefault(id, null);
    }

    public int getStatus() {
        return status.get();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void join(Player player) {
        if (getStatus() != 2) {
            players.add(player);
        }
    }

    public abstract boolean operate(int op, Player player);

    public void close() {
        status.set(0);
        rooms.remove(id);
    }

    public synchronized boolean threadAlive() {
        return roomThread.isAlive();
    }

    public synchronized Thread getThread() {
        return roomThread;
    }

    private final Thread roomThread;
    protected static final Hashtable<String, GameRoom> rooms = new Hashtable<>();
    protected final AtomicInteger status;//0 offline, 1 waiting, 2 running
    private final String id;
    private final List<Player> players;


    protected GameRoom(String id) {
        this.id = id;
        this.status = new AtomicInteger(1);
        this.players = Collections.synchronizedList(new ArrayList<>());
        this.roomThread = new Thread(this);
        rooms.put(id, this);
    }


}

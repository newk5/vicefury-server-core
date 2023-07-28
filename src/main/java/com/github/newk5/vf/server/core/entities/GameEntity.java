package com.github.newk5.vf.server.core.entities;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.entities.zone.Zone;
import com.github.newk5.vf.server.core.exceptions.InvalidThreadException;
import com.github.newk5.vf.server.core.utils.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GameEntity {

    private Map<String, Object> data = new ConcurrentHashMap<>();
    private long threadId;
    public GameEntityType type;
    protected int id;
    private GameData gameData;
    private List<String> tags;

    public GameEntity() {
        this.threadId = Thread.currentThread().getId();
    }

    protected boolean isOnMainThread() {
        return (this.threadId == Thread.currentThread().getId());
    }

    public List<String> getTags() {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags;
    }

    public GameEntity setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public boolean hasTag(String tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags.contains(tag);
    }

    public boolean hasAnyTag(String... tagsVar) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        for (String tag : Arrays.asList(tagsVar)) {
            if (tags.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    public GameEntity addTag(String tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
        return this;
    }

    public GameEntity removeTag(String tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.remove(tag);
        return this;
    }

    public boolean isInsideZone(Zone z) {
        if (z == null) {
            return false;
        }
        return z.isEntityInside(this);
    }
    
     public boolean isInsideSphereZone(Zone z) {
        if (z == null) {
            return false;
        }
        return z.isEntityInsideSphere(this);
    }

    protected boolean threadIsValid() {
        if (this.threadId != Thread.currentThread().getId()) {
            InvalidThreadException e = new InvalidThreadException("You cannot use the server API from outside the main server thread");
            Log.exception(e);
            return false;
        } else {
            return true;
        }
    }

    public boolean isValid() {
        return InternalServerEvents.isValid(this);
    }

    public Player asPlayer() {
        return (Player) this;
    }

    public Vehicle asVehicle() {
        return (Vehicle) this;
    }

    public NPC asNpc() {
        return (NPC) this;
    }

    public Vector getPosition() {
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.getPosition();
        } else if (this instanceof Vehicle) {
            Vehicle p = (Vehicle) this;
            return p.getPosition();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.getPosition();
        } else if (this instanceof GameObject) {
            GameObject obj = (GameObject) this;
            return obj.getPosition();
        } else if (this instanceof Zone) {
            Zone obj = (Zone) this;
            return obj.getPosition();
        }
        return null;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public int getId() {
        return id;
    }

    public GameEntity putData(String key, Object v) {
        data.put(key, v);
        return this;
    }

    public void clearData() {
        data.clear();
    }

    public <T> T getData(String key) {
        return (T) data.get(key);
    }

    public <T> T getCastedGameData() {
        return (T) gameData;
    }

    public GameEntity removeData(String key) {
        data.remove(key);
        return this;
    }

    public boolean hasData(String key) {
        return data.containsKey(key);
    }

    public boolean hasNonNullData(String key) {
        Object v = getData(key);
        return v != null;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "GameEntity{" + "type=" + type + ", id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.type);
        hash = 37 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameEntity other = (GameEntity) obj;
        if (this.id != other.id) {
            return false;
        }
        return this.type == other.type;
    }
}
